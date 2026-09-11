# Skybook Service

Сервис бронирования авиабилетов на Spring Boot c JWT аутентификацией и Stripe оплатой.

## Технологии

- **Java 21** + **Spring Boot 3.4.1**
- **Spring Security** + **JWT**
- **Spring Data JPA** + **JdbcTemplate**
- **PostgreSQL** + **Flyway**
- **Stripe API** (оплата картой, QR, наличными)
- **Docker** + **Docker Compose**
- **GitHub Actions** (CI/CD)
- **Swagger / OpenAPI**

## Возможности

- Регистрация и авторизация пользователей (JWT)
- Поиск рейсов по маршруту и дате
- Бронирование бтлетов с проверкой мест
- Отмена бронирования
- Оплата через Stripe (CARD, QR / CASH)
- Административная панель (управление рейсами, просмотр броней)
- Роли: USER и ADMIN

## Запуск через Docker
### Требования 
- Docker Desktop

### Шаги
1. Клонирование репозиторий:
```bash
git clone https://github.com/username/skybook-service.git
cd skybook-service
```
2. Создание `.env` файл в корне проекта:
DB_URL=jdbc:postgresql://db:5432/skybook
DB_USERNAME=postgres
DB_PASSWORD=<password>
JWT_SECRET=<jwt_secret_key_min_32_characters>
STRIPE_SECRET_KEY=<sk_test_stripe_key>

3. Запуск:
```bash
docker-compose up --build
```

4. Открыть Swagger UI: http://localhost:8080/swagger-ui/index.html

## Локальный запуск
### Требования
- Java 21
- Maven
- PostgreSQL

### Шаги
1. Создать БД `skybook_db` в PostgreSQL
2. Создать `src/main/resources/application-secret.properties`:
```properties
stripe.secret-key=stripe_key
```
3. Запустить приложение через Intellij IDEA или:
```bash
./mvnw spring-boot:run
```

## API Эндпоинты
### Публичные
| Метод| URL                | Описание             |
|------|--------------------|----------------------|
| POST | /api/auth/register | Регистрация          |
| POST | /api/auth/login    | Вход, возвращает JWT 

### USER
| Метод  | URL                                     | Описание              |
|--------|-----------------------------------------|-----------------------|
| GET    | /api/flights/search                     | Поиск рейсов          |
| POST   | /api/bookings                           | Создать бронирование  |
| GET    | /api/bookings/my                        | Мои бронирования      |
| DELETE | /api/bookings/{id}                      | Отменить бронирование |
| POST   | /api/payments/create-intent/{bookingId} | Создать платёж        |
| POST   | /api/payments/confirm/{bookingId}       | Подтвердить платёж    |

### ADMIN
| Метод  | URL                     | Описание         |
|--------|-------------------------|------------------|
| POST   | /api/admin/flights      | Добавить рейс    |
| PATCH  | /api/admin/flights/{id} | Обновить рейс    |
| GET    | /api/admin/bookings     | Все бронирования |

## Структура БД
- `users` - пользователи
- `flights` - рейсы
- `bookings` - бронирования
- `payments` - платежи

## Аутентификация в Swagger
1.Зарегистрироваться или войти через `/api/auth/login`
2.Скопировать JWT токен из ответа
3. Нажать **Authorize** в Swagger UI
4. Ввести токен (без слова Bearer)