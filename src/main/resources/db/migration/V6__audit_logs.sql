CREATE TABLE audit_logs (
    id          UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id     UUID         REFERENCES users(id),
    action      VARCHAR(50)  NOT NULL,
    entity_type VARCHAR(50)  NOT NULL,
    entity_id   UUID,
    details     TEXT,
    created_at  TIMESTAMPTZ  NOT NULL DEFAULT now()
);

CREATE INDEX idx_audit_logs_user      ON audit_logs(user_id);
CREATE INDEX idx_audit_logs_entity    ON audit_logs(entity_type, entity_id);
CREATE INDEX idx_audit_logs_created   ON audit_logs(created_at DESC);
