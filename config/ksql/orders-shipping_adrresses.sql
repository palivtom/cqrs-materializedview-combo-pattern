SET 'auto.offset.reset' = 'earliest';

CREATE STREAM "orders_stream" WITH (KAFKA_TOPIC='order-service.public.orders', VALUE_FORMAT='JSON_SR');
CREATE STREAM "shipping_addresses_stream" WITH (KAFKA_TOPIC='order-service.public.shipping_addresses', VALUE_FORMAT='JSON_SR');
CREATE STREAM "orders_shipping_addresses_creation" WITH (KAFKA_TOPIC='orders_shipping_addresses_creation', VALUE_FORMAT='JSON_SR', PARTITIONS=1) as SELECT o.after->id as id, o.after->user_id, sa.after->city, sa.after->country, sa.after->street, sa.after->zip_code FROM "shipping_addresses_stream" sa LEFT JOIN "orders_stream" o WITHIN 5 SECONDS ON o.after->id = sa.after->order_id WHERE o.op = 'c' AND sa.op = 'c' PARTITION BY o.after->id;

-- Create a table with the orders and shipping addresses (not working for a remove);
-- CREATE STREAM "orders_stream" WITH (KAFKA_TOPIC='order-service.public.orders', VALUE_FORMAT='JSON_SR');
-- CREATE STREAM "orders_stream_rekey" WITH (KAFKA_TOPIC='orders_rekey', VALUE_FORMAT='JSON_SR', PARTITIONS=1) as SELECT after->id, after->user_id, after->shipping_address_id FROM "orders_stream" PARTITION BY after->id;
-- CREATE TABLE "orders" (id BIGINT PRIMARY KEY, user_id BIGINT, shipping_address_id BIGINT) WITH (KAFKA_TOPIC='orders_rekey', VALUE_FORMAT='JSON_SR');
--
-- CREATE STREAM "shipping_addresses_stream" WITH (KAFKA_TOPIC='order-service.public.shipping_addresses', VALUE_FORMAT='JSON_SR');
-- CREATE STREAM "shipping_addresses_stream_rekey" WITH (KAFKA_TOPIC='shipping_addresses_stream_rekey', VALUE_FORMAT='JSON_SR', PARTITIONS=1) as SELECT after->id, after->order_id, after->city, after->country, after->street, after->zip_code FROM "shipping_addresses_stream" PARTITION BY after->id;
-- CREATE TABLE "shipping_addresses" (id BIGINT PRIMARY KEY, order_id BIGINT, city VARCHAR, country VARCHAR, street VARCHAR, zip_code VARCHAR) WITH (KAFKA_TOPIC='shipping_addresses_stream_rekey', VALUE_FORMAT='JSON_SR');
--
-- CREATE STREAM "orders_shipping_addresses" WITH (KAFKA_TOPIC='orders_shipping_addresses', VALUE_FORMAT='JSON_SR', KEY_FORMAT='JSON_SR', PARTITIONS=1) as SELECT o.user_id, o.id as order_id, sa.city, sa.country, sa.street, sa.zip_code FROM "shipping_addresses" sa LEFT JOIN "orders" o ON o.id = sa.order_id emit changes;