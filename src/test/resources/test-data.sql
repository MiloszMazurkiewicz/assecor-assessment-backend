-- Insert colors first
INSERT INTO colors (name) VALUES 
('blau'),
('grün'),
('violett'),
('rot'),
('gelb'),
('türkis'),
('weiß'),
('schwarz');

-- Insert persons with color references
INSERT INTO persons (name, lastname, zipcode, city, color_id) VALUES 
('Hans', 'Müller', '67742', 'Lauterecken', 1),
('Peter', 'Petersen', '18439', 'Stralsund', 2),
('Johnny', 'Johnson', '88888', 'made up', 3),
('Milly', 'Millenium', '77777', 'made up too', 4),
('Jonas', 'Müller', '32323', 'Hansstadt', 5);