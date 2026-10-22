CREATE TABLE IF NOT EXISTS maquinas (
    id         UUID                     PRIMARY KEY,
    nome       VARCHAR(50)              NOT NULL,
    riscos     VARCHAR(255)             NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX IF NOT EXISTS ux_maquinas_nome ON maquinas (nome);
