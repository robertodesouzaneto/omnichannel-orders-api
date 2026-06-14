CREATE TABLE payments (
    id           UUID           PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id     UUID           NOT NULL REFERENCES orders(id),
    status       VARCHAR(20)    NOT NULL
                     CHECK (status IN ('REQUESTED','APPROVED','REJECTED')),
    amount       NUMERIC(10, 2) NOT NULL CHECK (amount > 0),
    requested_at TIMESTAMPTZ    NOT NULL DEFAULT now(),
    processed_at TIMESTAMPTZ
);

-- Idempotência (decisão I-6): apenas um pagamento ativo por pedido
CREATE UNIQUE INDEX uq_payment_order_active
    ON payments(order_id)
    WHERE status IN ('REQUESTED', 'APPROVED');
