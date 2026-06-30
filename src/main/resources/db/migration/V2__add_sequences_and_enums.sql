-- switching from BIGSERIAL to explicit SEQUENCE for users and flights
-- for users
alter table users alter column id drop default;
drop sequence if exists users_id_seq;

create sequence users_id_seq start with 1 increment by 1 owned by users.id;
alter table users alter column id set default nextval ('users_id_seq');

-- for flights
alter table flights alter column id drop default;
drop sequence if exists flights_id_seq;

create sequence flights_id_seq start with 1 increment by 1 owned by flights.id;
alter table flights alter column id set default nexval ('flights_id_seq')

-- Limitation of acceptable values vis CHECK (imitation of enum at the database level)
alter table users add constraint chk_users_role CHECK ( role in ('USER', 'ADMIN') );

