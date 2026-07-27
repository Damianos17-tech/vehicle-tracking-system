-- =========================
-- COUNTRY
-- =========================
CREATE TABLE country (
    id SERIAL PRIMARY KEY,
    country_name TEXT
);

-- =========================
-- CITY
-- =========================
CREATE TABLE city (
    id SERIAL PRIMARY KEY,
    country_id INT REFERENCES country(id),
    city_name TEXT,
    postal_code TEXT
);

-- =========================
-- CUSTOMER
-- =========================
CREATE TABLE customer (
    id SERIAL PRIMARY KEY,
    city_id INT REFERENCES city(id),
    customer_name TEXT,
    customer_address TEXT,
    contact_person TEXT,
    email TEXT,
    phone TEXT,
    is_active BOOLEAN
);

-- =========================
-- USER ACCOUNT
-- =========================
CREATE TABLE user_account (
    id SERIAL PRIMARY KEY,
    first_name TEXT,
    last_name TEXT,
    user_name TEXT,
    password TEXT,
    email TEXT,
    phone TEXT,
    is_active BOOLEAN
);

-- =========================
-- INVOICE
-- =========================
CREATE TABLE invoice (
    id SERIAL PRIMARY KEY,
    invoice_number TEXT,
    customer_id INT REFERENCES customer(id),
    user_account_id INT REFERENCES user_account(id),
    total_price NUMERIC
);

-- =========================
-- CONTACT
-- =========================
CREATE TABLE contact (
    id SERIAL PRIMARY KEY,
    user_account_id INT REFERENCES user_account(id),
    customer_id INT REFERENCES customer(id),
    contact_type_id INT,
    customer_outcome_id INT,
    additional_comment TEXT,
    initiated_by_customer BOOLEAN,
    initiated_by_user BOOLEAN
);
