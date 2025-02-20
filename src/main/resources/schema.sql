CREATE TABLE IF NOT EXISTS trade (
    external_order_id VARCHAR(24) NOT NULL UNIQUE,
    order_id BIGINT NOT NULL PRIMARY KEY,
    trade_date VARCHAR(8) NOT NULL,
    ticker VARCHAR(24) NOT NULL,
    order_type VARCHAR(24) NOT NULL,
    quantity VARCHAR(24) NOT NULL,
    price VARCHAR(24) NOT NULL,
    exchange VARCHAR(24) NOT NULL,
    currency VARCHAR(3) NOT NULL
);