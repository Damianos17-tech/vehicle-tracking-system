-- =========================
-- COUNTRY
-- =========================
INSERT INTO country (country_name) VALUES
('Poland'),
('Germany'),
('United Kingdom'),
('United States'),
('Japan');

-- =========================
-- CITY
-- =========================
INSERT INTO city (country_id, city_name, postal_code) VALUES
(1, 'Warsaw', '00-001'),
(1, 'Krakow', '30-001'),
(2, 'Berlin', '10115'),
(2, 'Munich', '80331'),
(3, 'London', 'EC1A'),
(4, 'New York', '10001'),
(4, 'San Francisco', '94105'),
(5, 'Tokyo', '100-0001');

-- =========================
-- USER ACCOUNTS (Polish names)
-- =========================
INSERT INTO user_account (first_name, last_name, user_name, password, email, phone, is_active) VALUES
('Jan', 'Kowalski', 'jkowalski', 'pass123', 'jan.kowalski@mail.com', '500100200', TRUE),
('Anna', 'Nowak', 'anowak', 'pass123', 'anna.nowak@mail.com', '500100201', TRUE),
('Piotr', 'Zielinski', 'pzi', 'pass123', 'piotr.z@mail.com', '500100202', TRUE),
('Katarzyna', 'Wojcik', 'kwojcik', 'pass123', 'kasia.wojcik@mail.com', '500100203', TRUE);

-- =========================
-- CUSTOMERS (big companies)
-- =========================
INSERT INTO customer (city_id, customer_name, customer_address, contact_person, email, phone, is_active) VALUES
(6, 'Apple', '1 Infinite Loop, Cupertino', 'Tim Cook Team', 'contact@apple.com', '111111111', TRUE),
(7, 'Google', '1600 Amphitheatre Pkwy', 'Sundar Pichai Office', 'contact@google.com', '222222222', TRUE),
(6, 'Microsoft', 'Redmond Campus', 'Satya Nadella Office', 'contact@microsoft.com', '333333333', TRUE),
(5, 'BMW', 'Munich HQ', 'BMW Sales Dept', 'contact@bmw.com', '444444444', TRUE),
(1, 'mBank', 'Warsaw HQ', 'Banking Dept', 'contact@mbank.pl', '555555555', TRUE),
(2, 'Samsung', 'Seoul Branch EU', 'Samsung Sales', 'contact@samsung.com', '666666666', TRUE),
(4, 'PwC', 'Frankfurt Office', 'Consulting Team', 'contact@pwc.com', '777777777', TRUE);

-- =========================
-- INVOICE (some activity)
-- =========================
INSERT INTO invoice (invoice_number, customer_id, user_account_id, total_price) VALUES
('INV-001', 1, 1, 1200.50),
('INV-002', 1, 2, 800.00),
('INV-003', 2, 1, 1500.00),
('INV-004', 3, 3, 2200.00),
('INV-005', 4, 2, 3100.00),
('INV-006', 5, 1, 500.00),
('INV-007', 6, 4, 999.99),
('INV-008', 7, 3, 4500.00);

-- =========================
-- CONTACT (KEY PART - repeated relationships)
-- =========================
INSERT INTO contact (
    user_account_id,
    customer_id,
    contact_type_id,
    customer_outcome_id,
    additional_comment,
    initiated_by_customer,
    initiated_by_user
) VALUES

-- Apple ↔ Jan (many contacts)
(1, 1, 1, 1, 'Initial meeting', FALSE, TRUE),
(1, 1, 2, 2, 'Follow-up call', FALSE, TRUE),
(1, 1, 1, 1, 'Negotiation', FALSE, TRUE),

-- Google ↔ Anna (many contacts)
(2, 2, 1, 1, 'Intro call', TRUE, FALSE),
(2, 2, 2, 1, 'Technical discussion', FALSE, TRUE),
(2, 2, 2, 2, 'Offer sent', FALSE, TRUE),
(2, 2, 1, 1, 'Reminder call', FALSE, TRUE),

-- BMW ↔ Piotr
(3, 4, 1, 1, 'Business inquiry', FALSE, TRUE),
(3, 4, 2, 2, 'Pricing discussion', FALSE, TRUE),
(3, 4, 2, 1, 'Contract review', FALSE, TRUE),

-- mBank ↔ Jan (low activity)
(1, 5, 1, 2, 'Support call', TRUE, FALSE),

-- Samsung ↔ Kasia (many repeated)
(4, 6, 1, 1, 'Initial contact', FALSE, TRUE),
(4, 6, 2, 1, 'Offer presentation', FALSE, TRUE),
(4, 6, 2, 1, 'Follow-up', FALSE, TRUE),
(4, 6, 2, 2, 'Negotiation', FALSE, TRUE),

-- PwC ↔ Anna (medium activity)
(2, 7, 1, 1, 'Consulting request', FALSE, TRUE),
(2, 7, 2, 1, 'Workshop', FALSE, TRUE);
