CREATE TABLE shipping_addresses
(
    id       BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    street   VARCHAR(255)                            NOT NULL,
    city     VARCHAR(255)                            NOT NULL,
    zip_code VARCHAR(255)                            NOT NULL,
    country  VARCHAR(255)                            NOT NULL,
    CONSTRAINT pk_shipping_addresses PRIMARY KEY (id)
);

CREATE TABLE orders
(
    id                  BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    user_id             BIGINT                                  NOT NULL,
    shipping_address_id BIGINT,

    CONSTRAINT pk_orders PRIMARY KEY (id),
    CONSTRAINT FK_ORDERS_ON_SHIPPINGADDRESS FOREIGN KEY (shipping_address_id) REFERENCES shipping_addresses (id)
);

CREATE TABLE order_items
(
    id             BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    product_id     BIGINT                                  NOT NULL,
    quantity       INTEGER                                 NOT NULL,
    order_id       BIGINT,

    CONSTRAINT pk_order_items PRIMARY KEY (id),
    CONSTRAINT FK_ORDER_ITEMS_ON_ORDER FOREIGN KEY (order_id) REFERENCES orders (id)
);