-- Database seed file for property_db

-- Use database property_db (create if not exists)
CREATE DATABASE IF NOT EXISTS property_db;
USE property_db;

-- 1. Insert default Admin user if not exists
-- Username/Email: admin@property.com, Password: Admin@123
INSERT INTO accounts (id, name, email, phone, password, role, isactive, verify_status, created_at)
VALUES (1, 'Admin System', 'admin@property.com', '+911234567890', '$2a$10$WqU2j14KoxQ5h4Lsn9UjbeHkZ37bB11q.s0y4tDkpxDpeB2Q5pS26', 1, 1, 2, 1719410000000)
ON DUPLICATE KEY UPDATE name = 'Admin System';

-- 2. Insert default Categories
INSERT INTO categories (id, name, isactive) VALUES
(1, 'Residential', 1),
(2, 'Commercial', 1),
(3, 'Land/Plot', 1)
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- 3. Insert default Subcategories
INSERT INTO subcategories (id, name, category_id, isactive) VALUES
(1, 'Apartment', 1, 1),
(2, 'Independent House/Villa', 1, 1),
(3, 'Office Space', 2, 1),
(4, 'Shop/Showroom', 2, 1),
(5, 'Agricultural Land', 3, 1),
(6, 'Residential Plot', 3, 1)
ON DUPLICATE KEY UPDATE name = VALUES(name), category_id = VALUES(category_id);
