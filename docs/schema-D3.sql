CREATE TABLE IF NOT EXISTS knowledge_base (
    id              BIGINT PRIMARY KEY,
    tenant_id       BIGINT NOT NULL,
    name            VARCHAR(100) NOT NULL,
    description     VARCHAR(500),
    embedding_model VARCHAR(100),
    chunk_size      INT NOT NULL DEFAULT 600,
    chunk_overlap   INT NOT NULL DEFAULT 80,
    status          SMALLINT NOT NULL DEFAULT 1,
    create_time     TIMESTAMP NOT NULL DEFAULT now(),
    update_time     TIMESTAMP NOT NULL DEFAULT now(),
    deleted         SMALLINT NOT NULL DEFAULT 0
    );

CREATE TABLE IF NOT EXISTS kb_document (
    id           BIGINT PRIMARY KEY,
    tenant_id    BIGINT NOT NULL,
    kb_id        BIGINT NOT NULL,
    name         VARCHAR(200) NOT NULL,
    file_type    VARCHAR(20),
    file_size    BIGINT,
    storage_path VARCHAR(500),
    file_hash    VARCHAR(64),
    status       VARCHAR(20) NOT NULL DEFAULT 'PARSING',
    chunk_count  INT NOT NULL DEFAULT 0,
    error_msg    VARCHAR(1000),
    create_time  TIMESTAMP NOT NULL DEFAULT now(),
    update_time  TIMESTAMP NOT NULL DEFAULT now(),
    deleted      SMALLINT NOT NULL DEFAULT 0
    );

CREATE INDEX IF NOT EXISTS idx_kb_tenant ON knowledge_base (tenant_id);
CREATE INDEX IF NOT EXISTS idx_doc_kb ON kb_document (kb_id);