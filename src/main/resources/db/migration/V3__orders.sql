CREATE TABLE units (
    id         UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
    name       VARCHAR(100) NOT NULL UNIQUE,
    address    VARCHAR(255) NOT NULL,
    active     BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ  NOT NULL DEFAULT now()
);

CREATE TABLE stock (
    id         UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
    unit_id    UUID        NOT NULL REFERENCES units(id),
    product_id UUID        NOT NULL REFERENCES products(id),
    quantity   INTEGER     NOT NULL DEFAULT 0 CHECK (quantity >= 0),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT uq_stock_unit_product UNIQUE (unit_id, product_id)
);

CREATE TABLE orders (
    id          UUID           PRIMARY KEY DEFAULT gen_random_uuid(),
    customer_id UUID           NOT NULL REFERENCES users(id),
    unit_id     UUID           NOT NULL REFERENCES units(id),
    channel     VARCHAR(20)    NOT NULL
                    CHECK (channel IN ('APP','TOTEM','COUNTER','PICKUP','WEB')),
    status      VARCHAR(30)    NOT NULL
                    CHECK (status IN ('PENDING','AWAITING_PAYMENT','IN_PREPARATION','READY','DELIVERED','CANCELED')),
    total       NUMERIC(10, 2) NOT NULL DEFAULT 0,
    created_at  TIMESTAMPTZ    NOT NULL DEFAULT now(),
    updated_at  TIMESTAMPTZ
);

CREATE TABLE order_items (
    id         UUID           PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id   UUID           NOT NULL REFERENCES orders(id),
    product_id UUID           NOT NULL REFERENCES products(id),
    quantity   INTEGER        NOT NULL CHECK (quantity > 0),
    unit_price NUMERIC(10, 2) NOT NULL CHECK (unit_price >= 0)
);

CREATE INDEX idx_orders_customer  ON orders(customer_id);
CREATE INDEX idx_orders_unit      ON orders(unit_id);
CREATE INDEX idx_order_items_order ON order_items(order_id);

-- Seed: first unit so tests work out of the box
INSERT INTO units (name, address)
VALUES ('Unidade Centro', 'Rua do Nordeste, 100 – Centro');
