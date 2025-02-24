CREATE DATABASE easyshopdb_catalog;

\c easyshopdb_catalog;
CREATE TABLE IF NOT EXISTS products (
    product_id SERIAL PRIMARY KEY,
    code VARCHAR UNIQUE NOT NULL,
    name VARCHAR,
    price BIGINT,
    brand VARCHAR,
    category VARCHAR
);

