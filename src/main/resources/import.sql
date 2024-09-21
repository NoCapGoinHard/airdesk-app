-- Insert multiple facilities
INSERT INTO facility (id, name, phone, email) VALUES (1, 'newyork offices', '123-456-7890', 'contact@mainfacility.com');
INSERT INTO facility (id, name, phone, email) VALUES (2, 'cali offices', '987-654-3210', 'contact@secondaryfacility.com');

-- Insert multiple buildings
INSERT INTO building (id, name, facility_id, country, state, city, postalcode, street, number) VALUES (1, 'Building A ny', 1, 'USA', 'NY', 'New York', '10001', '5th Avenue', '1');
INSERT INTO building (id, name, facility_id, country, state, city, postalcode, street, number) VALUES (2, 'Building B cali', 2, 'USA', 'CA', 'Los Angeles', '90001', 'Sunset Blvd', '100');

-- Insert multiple office hours for a building
INSERT INTO office_hours (id, day, starting_time, ending_time, building_id) VALUES (1, 'MONDAY', '09:00', '18:00', 1);
INSERT INTO office_hours (id, day, starting_time, ending_time, building_id) VALUES (2, 'TUESDAY', '09:00', '18:00', 1);
INSERT INTO office_hours (id, day, starting_time, ending_time, building_id) VALUES (3, 'WEDNESDAY', '09:00', '18:00', 2);

-- Insert multiple floors
INSERT INTO floor (id, number, building_id) VALUES (1, 1, 1);
INSERT INTO floor (id, number, building_id) VALUES (2, 2, 1);
INSERT INTO floor (id, number, building_id) VALUES (3, 1, 2);

-- Insert multiple rooms
INSERT INTO room (id, name, floor_id) VALUES (1, 'Room 101', 1);
INSERT INTO room (id, name, floor_id) VALUES (2, 'Room 102', 2);
INSERT INTO room (id, name, floor_id) VALUES (3, 'Room 201', 3);

-- Insert multiple workstations
INSERT INTO workstation (id, workstation_id, workstation_type, room_id) VALUES (1, 'WS-101', 'PC_WORKSTATION', 1);
INSERT INTO workstation (id, workstation_id, workstation_type, room_id) VALUES (2, 'WS-102', 'SEATING_AREA', 2);
INSERT INTO workstation (id, workstation_id, workstation_type, room_id) VALUES (3, 'WS-201', 'MEETING_ROOM', 3);

-- Insert office hours for rooms (matching the opening hours of their buildings)
-- For rooms in Building A (New York)
INSERT INTO office_hours (id, day, starting_time, ending_time, room_id) VALUES (4, 'MONDAY', '09:00', '18:00', 1);  -- Room 101 (Floor 1)
INSERT INTO office_hours (id, day, starting_time, ending_time, room_id) VALUES (5, 'TUESDAY', '09:00', '18:00', 1);  -- Room 101 (Floor 1)

INSERT INTO office_hours (id, day, starting_time, ending_time, room_id) VALUES (6, 'MONDAY', '09:00', '18:00', 2);  -- Room 102 (Floor 2)
INSERT INTO office_hours (id, day, starting_time, ending_time, room_id) VALUES (7, 'TUESDAY', '09:00', '18:00', 2);  -- Room 102 (Floor 2)

-- For rooms in Building B (Cali)
INSERT INTO office_hours (id, day, starting_time, ending_time, room_id) VALUES (8, 'WEDNESDAY', '09:00', '18:00', 3);  -- Room 201 (Floor 1)


