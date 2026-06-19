CREATE TABLE consents (
    id         UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id    UUID        NOT NULL REFERENCES users(id),
    action     VARCHAR(10) NOT NULL CHECK (action IN ('GRANTED', 'REVOKED')),
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_consents_user ON consents(user_id);
