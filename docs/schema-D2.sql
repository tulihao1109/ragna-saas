CREATE TABLE IF NOT EXISTS tenant (
    id          BIGINT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    plan        VARCHAR(20)  NOT NULL DEFAULT 'FREE',
    status      SMALLINT     NOT NULL DEFAULT 1,
    api_key     VARCHAR(80)  NOT NULL,
    expire_at   TIMESTAMP,
    create_time TIMESTAMP    NOT NULL DEFAULT now(),
    update_time TIMESTAMP    NOT NULL DEFAULT now(),
    deleted     SMALLINT     NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS sys_user (
    id          BIGINT PRIMARY KEY,
    tenant_id   BIGINT NOT NULL,
    username    VARCHAR(50) NOT NULL,
    password    VARCHAR(100) NOT NULL,
    nickname    VARCHAR(50),
    role        VARCHAR(20) NOT NULL DEFAULT 'ADMIN',
    status      SMALLINT    NOT NULL DEFAULT 1,
    create_time TIMESTAMP   NOT NULL DEFAULT now(),
    update_time TIMESTAMP   NOT NULL DEFAULT now(),
    deleted     SMALLINT    NOT NULL DEFAULT 0
);

CREATE UNIQUE INDEX IF NOT EXISTS uk_tenant_apikey ON tenant (api_key);
CREATE UNIQUE INDEX IF NOT EXISTS uk_user_username ON sys_user (username);
CREATE INDEX IF NOT EXISTS idx_user_tenant ON sys_user (tenant_id);