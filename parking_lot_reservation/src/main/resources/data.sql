INSERT IGNORE INTO vehicle_types (type, hourly_rate, description, created_at, updated_at) VALUES
('2 wheeler', 20.0, 'Motorcycles, scooters', NOW(), NOW()),
('4 wheeler', 30.0, 'Cars, SUVs, trucks', NOW(), NOW()),
('Electric', 25.0, 'Electric vehicles', NOW(), NOW()),
('Truck', 50.0, 'Large trucks and buses', NOW(), NOW());

INSERT IGNORE INTO floors (floor_number, name, total_slots, created_at, updated_at) VALUES
(1, 'Ground Floor', 20, NOW(), NOW()),
(2, 'First Floor', 15, NOW(), NOW()),
(3, 'Second Floor', 10, NOW(), NOW()),
(-1, 'Basement', 25, NOW(), NOW());

INSERT IGNORE INTO slots (slot_number, floor_id, vehicle_type_id, is_available, created_at, updated_at) VALUES
('G-2W-01', 1, 1, true, NOW(), NOW()),
('G-2W-02', 1, 1, true, NOW(), NOW()),
('G-2W-03', 1, 1, true, NOW(), NOW()),

('G-4W-01', 1, 2, true, NOW(), NOW()),
('G-4W-02', 1, 2, true, NOW(), NOW()),
('G-4W-03', 1, 2, true, NOW(), NOW()),

('F1-4W-01', 2, 2, true, NOW(), NOW()),
('F1-4W-02', 2, 2, true, NOW(), NOW()),
('F1-4W-03', 2, 2, true, NOW(), NOW()),

('F1-EV-01', 2, 3, true, NOW(), NOW()),
('F1-EV-02', 2, 3, true, NOW(), NOW()),

('F2-TR-01', 3, 4, true, NOW(), NOW()),
('F2-TR-02', 3, 4, true, NOW(), NOW()),

('B-4W-01', 4, 2, true, NOW(), NOW()),
('B-4W-02', 4, 2, true, NOW(), NOW());