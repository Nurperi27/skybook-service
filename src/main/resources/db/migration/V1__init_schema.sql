create table users(
    id bigserial primary key,
    name varchar(255) not null,
    email varchar(255) not null,
    password varchar(255) not null,
    role varchar(50) not null,
    is_active boolean not null default true,
    created_at timestamp not null default now()
);

create table flights(
    id bigserial primary key,
    flight_number varchar(20) not null,
    origin varchar(100) not null,
    destination varchar(100) not null,
    departure_at timestamp not null,
    arrival_at timestamp not null,
    price numeric(10, 2) not null,
    total_seats int not null,
    available_seats int not null,
    status varchar(20) not null default 'SCHEDULED',
    created_at timestamp not null default now()
);

create table bookings(
       id bigserial primary key,
       user_id bigint not null references users(id),
       flight_id bigint not null references flights(id),
       passenger_name varchar(255) not null,
       seat_number varchar(10) not null,
       status varchar(20) not null default 'PENDING',
       created_at timestamp not null default now(),
       updated_at timestamp not null default now()
);

create table payments(
    id bigserial primary key,
    booking_id bigint not null references bookings(id),
    amount numeric(10, 2) not null,
    currency varchar(3) not null default 'USD',
    stripe_payment_id varchar(255),
    status varchar(20) not null default 'PENDING',
    paid_at timestamp
);

create unique index idx_bookings_flight_seat on bookings(flight_id, seat_number);
create index idx_bookings_user on bookings(user_id);
create index idx_flights_search on flights(origin, destination, departure_at);