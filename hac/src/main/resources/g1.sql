-- Insert dummy data for 'countries'
INSERT INTO countries (name_ar, name_en)
SELECT 'Country_AR_' || i, 'Country_EN_' || i
FROM generate_series(1, 100) AS i;

-- Insert dummy data for 'positions'
INSERT INTO positions (name_ar, name_en)
SELECT 'Position_AR_' || i, 'Position_EN_' || i
FROM generate_series(1, 50) AS i;

-- Insert dummy data for 'brands'
INSERT INTO brands (created_at, is_active, updated_at, code, main_brand_id, name_ar, name_en)
SELECT NOW(), true, NOW(), 'Code_' || i, NULL, 'Brand_AR_' || i, 'Brand_EN_' || i
FROM generate_series(1, 100) AS i;

-- Insert dummy data for 'machine_parts'
INSERT INTO machine_parts (created_at, is_active, updated_at, name_ar, name_en)
SELECT NOW(), true, NOW(), 'Part_AR_' || i, 'Part_EN_' || i
FROM generate_series(1, 50) AS i;

-- Insert dummy data for 'machinery_type'
INSERT INTO machinery_type (created_at, is_active, updated_at, name_ar, name_en)
SELECT NOW(), true, NOW(), 'Type_AR_' || i, 'Type_EN_' || i
FROM generate_series(1, 50) AS i;

-- Insert dummy data for 'machinery_model'
INSERT INTO machinery_model (created_at, is_active, updated_at, name_ar, name_en, brand_id, machinery_type_id)
SELECT NOW(), true, NOW(), 'Model_AR_' || i, 'Model_EN_' || i, (i % 100) + 1, (i % 50) + 1
FROM generate_series(1, 200) AS i;

-- Insert dummy data for 'employees'
INSERT INTO employees (name_ar, name_en, national_id, country_id, position_id)
SELECT 'Employee_AR_' || i, 'Employee_EN_' || i, 'NID_' || i, (i % 100) + 1, (i % 50) + 1
FROM generate_series(1, 500) AS i;

-- Insert dummy data for 'products'
INSERT INTO products (created_at, is_active, updated_at, buy_individual, buy_quantity, description_ar, description_en, image, is_original, min_quantity, part_image, product_number, sell_individual, sell_quantity, unit, country_id, machine_part_id, machinery_model_id, machinery_type_id, main_brand_id, sub_brand_id, price)
SELECT NOW(), true, NOW(), i, i*2, 'Description_AR_' || i, 'Description_EN_' || i, 'image_' || i || '.png', true, i, 'part_image_' || i || '.png', 'PN_' || i, i, i*2, 'PIECE', (i % 100) + 1, (i % 50) + 1, (i % 200) + 1, (i % 50) + 1, (i % 100) + 1, (i % 100) + 1, i*10.0
FROM generate_series(1, 1000) AS i;

-- Insert dummy data for 'stores'
INSERT INTO stores (created_at, is_active, updated_at, city_ar, city_en, name_ar, name_en)
SELECT NOW(), true, NOW(), 'City_AR_' || i, 'City_EN_' || i, 'Store_AR_' || i, 'Store_EN_' || i
FROM generate_series(1, 50) AS i;

-- Insert dummy data for 'store_locations'
INSERT INTO store_locations (created_at, is_active, updated_at, name_ar, name_en, store_id)
SELECT NOW(), true, NOW(), 'Location_AR_' || i, 'Location_EN_' || i, (i % 50) + 1
FROM generate_series(1, 200) AS i;

-- Insert dummy data for 'inventory'
INSERT INTO inventory (created_at, is_active, updated_at, quantity, location_id, product_id, store_id)
SELECT NOW(), true, NOW(), i*10, (i % 200) + 1, (i % 1000) + 1, (i % 50) + 1
FROM generate_series(1, 1000) AS i;

-- Insert dummy data for 'inventory_transactions'
INSERT INTO inventory_transactions (created_at, is_active, updated_at, quantity, transaction_date, transaction_type, location_id, product_id, store_id, des_location_id, des_store_id)
SELECT NOW(), true, NOW(), i*10, NOW(), 'TRANSFER', (i % 200) + 1, (i % 1000) + 1, (i % 50) + 1, (i % 200) + 1, (i % 50) + 1
FROM generate_series(1, 500) AS i;

-- Insert dummy data for 'suppliers'
INSERT INTO suppliers (created_at, is_active, updated_at, address, email, name_ar, name_en, phone)
SELECT NOW(), true, NOW(), 'Address_' || i, 'email_' || i || '@example.com', 'Supplier_AR_' || i, 'Supplier_EN_' || i, 'Phone_' || i
FROM generate_series(1, 100) AS i;

-- Insert dummy data for 'users'
INSERT INTO users (created_at, is_active, updated_at, password, username)
SELECT NOW(), true, NOW(), 'password_' || i, 'username_' || i
FROM generate_series(1, 100) AS i;

-- Insert dummy data for 'roles'
INSERT INTO roles (created_at, is_active, updated_at, name)
SELECT NOW(), true, NOW(), 'Role_' || i
FROM generate_series(1, 20) AS i;

-- Insert dummy data for 'privileges'
INSERT INTO privileges (created_at, is_active, updated_at, name)
SELECT NOW(), true, NOW(), 'Privilege_' || i
FROM generate_series(1, 50) AS i;

-- Insert dummy data for 'roles_privileges'
INSERT INTO roles_privileges (role_id, privilege_id)
SELECT (i % 20) + 1, (i % 50) + 1
FROM generate_series(1, 200) AS i;

-- Insert dummy data for 'users_roles'
INSERT INTO users_roles (user_id, role_id)
SELECT (i % 100) + 1, (i % 20) + 1
FROM generate_series(1, 200) AS i;

-- Insert dummy data for 'internal_refs'
INSERT INTO internal_refs (current_phase)
SELECT 'MATERIAL_REQUEST'
FROM generate_series(1, 50) AS i;

-- Insert dummy data for 'request_purchase_quotations'
INSERT INTO request_purchase_quotations (created_at, is_active, updated_at, date, notes, number, status, internal_ref_id, store_id, supplier_id, user_id)
SELECT NOW(), true, NOW(), NOW()::date, 'Notes_' || i, 'Number_' || i, 'DRAFT', (i % 50) + 1, (i % 50) + 1, (i % 100) + 1, (i % 100) + 1
FROM generate_series(1, 200) AS i;

-- Insert dummy data for 'purchase_orders'
INSERT INTO purchase_orders (created_at, is_active, updated_at, date, notes, number, status, sub_total, supplier_ref, total, vat, internal_ref_id, store_id, supplier_id, user_id)
SELECT NOW(), true, NOW(), NOW()::date, 'Notes_' || i, 'Number_' || i, 'DRAFT', i*100.0, 'Supplier_Ref_' || i, i*110.0, i*10.0, (i % 50) + 1, (i % 50) + 1, (i % 100) + 1, (i % 100) + 1
FROM generate_series(1, 200) AS i;

-- Insert dummy data for 'purchase_order_lines'
INSERT INTO purchase_order_lines (created_at, is_active, updated_at, notes, price, quantity, total, vat, product_id, purchase_order_id)
SELECT NOW(), true, NOW(), 'Notes_' || i, i*10.0, i*5, i*50.0, i*5.0, (i % 1000) + 1, (i % 200) + 1
FROM generate_series(1, 1000) AS i;

-- Insert dummy data for 'purchase_invoices'
INSERT INTO purchase_invoices (created_at, is_active, updated_at, date, notes, number, status, sub_total, supplier_ref, total, vat, internal_ref_id, store_id, supplier_id, user_id, discount, total_expenses)
SELECT NOW(), true, NOW(), NOW()::date, 'Notes_' || i, 'Number_' || i, 'DRAFT', i*100.0, 'Supplier_Ref_' || i, i*110.0, i*10.0, (i % 50) + 1, (i % 50) + 1, (i % 100) + 1, (i % 100) + 1, i*10.0, i*15.0
FROM generate_series(1, 200) AS i;

-- Insert dummy data for 'purchase_invoice_lines'
INSERT INTO purchase_invoice_lines (created_at, is_active, updated_at, notes, price, quantity, total, vat, product_id, purchase_invoice_id)
SELECT NOW(), true, NOW(), 'Notes_' || i, i*10.0, i*5, i*50.0, i*5.0, (i % 1000) + 1, (i % 200) + 1
FROM generate_series(1, 1000) AS i;

-- Insert dummy data for 'material_requests'
INSERT INTO material_requests (created_at, is_active, updated_at, date, notes, number, status, internal_ref_id, source_location_id, source_store_id, user_id, destination_store_id, destination_location_id)
SELECT NOW(), true, NOW(), NOW()::date, 'Notes_' || i, 'Number_' || i, 'PENDING', (i % 50) + 1, (i % 200) + 1, (i % 50) + 1, (i % 100) + 1, (i % 50) + 1, (i % 200) + 1
FROM generate_series(1, 200) AS i;

-- Insert dummy data for 'material_request_lines'
INSERT INTO material_request_lines (created_at, is_active, updated_at, quantity, notes, product_id, material_request_id)
SELECT NOW(), true, NOW(), i*10, 'Notes_' || i, (i % 1000) + 1, (i % 200) + 1
FROM generate_series(1, 1000) AS i;
