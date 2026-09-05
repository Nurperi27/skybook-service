package com.skybook.skybookservice.service.impl;

import com.skybook.skybookservice.dto.request.PaymentRequest;
import com.skybook.skybookservice.dto.response.PaymentIntentResponse;
import com.skybook.skybookservice.dto.response.PaymentResponse;
import com.skybook.skybookservice.entity.User;
import com.skybook.skybookservice.enums.BookingStatus;
import com.skybook.skybookservice.enums.PaymentOption;
import com.skybook.skybookservice.enums.PaymentStatus;
import com.skybook.skybookservice.exceptions.PaymentException;
import com.skybook.skybookservice.exceptions.ResourceNotFoundException;
import com.skybook.skybookservice.models.Booking;
import com.skybook.skybookservice.models.Payment;
import com.skybook.skybookservice.repository.FlightRepository;
import com.skybook.skybookservice.repository.UserRepository;
import com.skybook.skybookservice.repository.template.BookingRepository;
import com.skybook.skybookservice.repository.template.PaymentRepository;
import com.skybook.skybookservice.service.PaymentService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {
    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final FlightRepository flightRepository;
    private final UserRepository userRepository;

    @Value("${stripe.secret-key}")
    private String stripeSecretKey;

    @PostConstruct
    public void init(){
        Stripe.apiKey =  stripeSecretKey;
    }

    @Override
    public PaymentIntentResponse createPaymentIntent(Long bookingId, String email, PaymentOption paymentOption) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        if(!booking.getUserId().equals(user.getId())){
            throw new ResourceNotFoundException("У вас нет брони");
        }
        if(booking.getStatus() != BookingStatus.PENDING){
            throw new PaymentException("Бронирование уже оплачено или отменено");
        }
        BigDecimal amount = flightRepository.findById(booking.getFlightId()).orElseThrow(() -> new ResourceNotFoundException("рейс не найден")).getPrice();
        if(paymentOption == PaymentOption.CARD){
            return handleCardPayment(bookingId, amount, paymentOption);
        }else {
            return handleNonCardPayment(bookingId, amount, paymentOption);
        }
    }

    private PaymentIntentResponse handleNonCardPayment(Long bookingId, BigDecimal amount, PaymentOption paymentOption) {
        Payment payment = Payment.builder().bookingId(bookingId).amount(amount).currency("USD").stripePaymentId(null).paymentOption(paymentOption).build();
        Payment savedPayment = paymentRepository.save(payment);
        paymentRepository.confirmPayment(savedPayment.getId(), null);
        bookingRepository.updateStatus(bookingId, BookingStatus.CONFIRMED);
        return PaymentIntentResponse.builder().clientSecret(null).PaymentIntentId(null).amount(amount).currency("USD").build();
    }

    private PaymentIntentResponse handleCardPayment(Long bookingId, BigDecimal amount, PaymentOption paymentOption) {
        try{
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(amount.multiply(BigDecimal.valueOf(100)).longValue()).setCurrency("usd")
                    .setDescription("Skybook: брониирование #" + bookingId).setConfirm(true).setPaymentMethod("pm_card_visa") //тестовая карта Stripe
                    .setReturnUrl("http://localhost:8080/api/payments/success").build();

            PaymentIntent paymentIntent = PaymentIntent.create(params);

            //сохраняем платеж со статусом PENDING
            Payment payment = Payment.builder().bookingId(bookingId).amount(amount).currency("USD").stripePaymentId(paymentIntent.getId()).paymentOption(paymentOption).build();

            Payment savedPayment = paymentRepository.save(payment);
            paymentRepository.confirmPayment(savedPayment.getId(), paymentIntent.getId());
            bookingRepository.updateStatus(bookingId, BookingStatus.CONFIRMED);

            //возвращаем client_secret фронту
            return PaymentIntentResponse.builder().clientSecret(paymentIntent.getClientSecret()).PaymentIntentId(paymentIntent.getId()).amount(BigDecimal.valueOf(paymentIntent.getAmount())).currency("USD").build();
        } catch (StripeException e) {
            throw new PaymentException("Ошибка при создании платежа");
        }
    }

    @Override
    public PaymentResponse confirmPayment(Long bookingId, String paymentIntentId, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        if(!booking.getUserId().equals(user.getId())){
            throw new ResourceNotFoundException("У вас нет бронирований");
        }
        try{
            //проверка статуса PaymentIntent в Stripe
            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
            if(!"succeeded".equals(paymentIntent.getStatus())){
                throw new PaymentException("Платеж не был подтвержден");
            }

            //находим платеж в БД
            Payment payment = paymentRepository.findByBookingId(booking.getId()).orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

            //обновляем статус платежа
            paymentRepository.confirmPayment(payment.getId(), paymentIntentId);

            //обновляем статус бронирования
            bookingRepository.updateStatus(bookingId, BookingStatus.CONFIRMED);
            return PaymentResponse.builder().id(payment.getId()).bookingId(bookingId).amount(payment.getAmount()).currency("USD").stripePaymentId(paymentIntentId).status(PaymentStatus.CONFIRMED).paymentOption(payment.getPaymentOption()).paidAt(LocalDateTime.now()).build();
        } catch (StripeException e) {
            throw new PaymentException("Ошибка при подтверждении платежа");
        }
    }
}
