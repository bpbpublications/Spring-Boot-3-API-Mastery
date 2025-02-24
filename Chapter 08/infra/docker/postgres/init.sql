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
INSERT INTO products(code, name, price, brand, category)
    VALUES ('LAP-ABCD', 'ALaptop', 30000,'FirstBrand', 'LAPTOP');
INSERT INTO products(code, name, price, brand, category)
    VALUES ('LAP-EFGH', 'SuperLaptop', 90000,'SecondBrand', 'LAPTOP');
INSERT INTO products(code, name, price, brand, category)
    VALUES ('SMA-XCV', 'MegaSmartphone', 60000,'FirstBrand', 'SMARTPHONE');

CREATE DATABASE easyshopdb_order;
\c easyshopdb_order;
CREATE TABLE IF NOT EXISTS orders (
    order_id SERIAL PRIMARY KEY,
    order_code VARCHAR UNIQUE NOT NULL,
    status VARCHAR,
    total_price BIGINT,
    created_at TIMESTAMPTZ,
    updated_at TIMESTAMPTZ
);
CREATE TABLE IF NOT EXISTS order_items (
    order_id INTEGER,
    product_code VARCHAR,
    quantity INTEGER,
    PRIMARY KEY(order_id, product_code),
    CONSTRAINT fk_purchase_orders
        FOREIGN KEY(order_id) REFERENCES orders(order_id)
);

CREATE DATABASE easyshopdb_shipping;
\c easyshopdb_shipping;
CREATE TABLE IF NOT EXISTS shipments (
      shipment_id SERIAL PRIMARY KEY,
      tracking_code VARCHAR UNIQUE NOT NULL,
      order_code VARCHAR NOT NULL,
      status VARCHAR NOT NULL,
      shipping_date TIMESTAMPTZ,
      delivery_date TIMESTAMPTZ
);

