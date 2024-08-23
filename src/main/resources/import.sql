-- Insert multiple facilities
INSERT INTO facility (id, name, phone, email)
VALUES 
(1, 'Main Facility', '123-456-7890', 'contact@mainfacility.com'),
(2, 'Secondary Facility', '987-654-3210', 'contact@secondaryfacility.com');

-- Insert multiple buildings
INSERT INTO building (id, name, facility_id, address_country, address_state, address_city, address_postalcode, address_street, address_number)
VALUES 
(1, 'Building A', 1, 'USA', 'NY', 'New York', '10001', '5th Avenue', '1'),
(2, 'Building B', 2, 'USA', 'CA', 'Los Angeles', '90001', 'Sunset Blvd', '100');

-- Insert multiple floors
INSERT INTO floor (id, number, building_id)
VALUES 
(1, 1, 1),
(2, 2, 1),
(3, 1, 2);

-- Insert multiple rooms
INSERT INTO room (id, name, floor_id)
VALUES 
(1, 'Room 101', 1),
(2, 'Room 102', 2),
(3, 'Room 201', 3);

-- Insert multiple workstations
INSERT INTO workstation (id, workstation_id, workstation_type, room_id)
VALUES 
(1, 'WS-101', 'PC_WORKSTATION', 1),
(2, 'WS-102', 'SEATING_AREA', 2),
(3, 'WS-201', 'MEETING_ROOM', 3);

-- Insert multiple office hours for a building
INSERT INTO office_hours (id, day, starting_time, ending_time, building_id)
VALUES 
(1, 'MONDAY', '09:00', '18:00', 1),
(2, 'TUESDAY', '09:00', '18:00', 1),
(3, 'WEDNESDAY', '09:00', '18:00', 2);

-- Insert multiple bookings
INSERT INTO booking (id, day, starting_time, ending_time, workstation_id, user_id)
VALUES 
(1, '2024-09-01', '09:00', '12:00', 1, 1),
(2, '2024-09-02', '13:00', '16:00', 2, 2),
(3, '2024-09-03', '10:00', '12:00', 3, 3);
