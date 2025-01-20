-- Dodaj klientów
insert into customer (id, email) values (NEXTVAL('customer_seq'), 'customer1@wavestone.com');
insert into customer (id, email) values (NEXTVAL('customer_seq'), 'customer2@wavestone.com');
insert into customer (id, email) values (NEXTVAL('customer_seq'), 'customer3@wavestone.com');

insert into product (id, name, description) values (NEXTVAL('product_seq'), 'Book', 'Spring in Action');
insert into product (id, name, description) values (NEXTVAL('product_seq'), 'Game', 'Tetris 2025');
insert into product (id, name, description) values (NEXTVAL('product_seq'), 'Audiobook', 'Spring in Action Audio');