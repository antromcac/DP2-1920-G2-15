-- One admin user, named admin1 with passwor 4dm1n and authority admin
INSERT INTO users(username,password,enabled) VALUES ('admin1','4dm1n',TRUE);
INSERT INTO authorities VALUES ('admin1','admin');
-- One owner user, named owner1 with passwor 0wn3r
INSERT INTO users(username,password,enabled) VALUES ('owner1','0wn3r',TRUE);
INSERT INTO authorities VALUES ('owner1','owner');
INSERT INTO users(username,password,enabled) VALUES ('owner2','0wn3r',TRUE);
INSERT INTO authorities VALUES ('owner2','owner');
-- One vet user, named vet1 with passwor v3t
INSERT INTO users(username,password,enabled) VALUES ('vet1','v3t',TRUE);
INSERT INTO authorities VALUES ('vet1','veterinarian');

INSERT INTO vets VALUES (1, 'James', 'Carter');
INSERT INTO vets VALUES (2, 'Helen', 'Leary');
INSERT INTO vets VALUES (3, 'Linda', 'Douglas');
INSERT INTO vets VALUES (4, 'Rafael', 'Ortega');
INSERT INTO vets VALUES (5, 'Henry', 'Stevens');
INSERT INTO vets VALUES (6, 'Sharon', 'Jenkins');

INSERT INTO specialties VALUES (1, 'radiology');
INSERT INTO specialties VALUES (2, 'surgery');
INSERT INTO specialties VALUES (3, 'dentistry');

INSERT INTO vet_specialties VALUES (2, 1);
INSERT INTO vet_specialties VALUES (3, 2);
INSERT INTO vet_specialties VALUES (3, 3);
INSERT INTO vet_specialties VALUES (4, 2);
INSERT INTO vet_specialties VALUES (5, 1);

INSERT INTO types VALUES (1, 'cat');
INSERT INTO types VALUES (2, 'dog');
INSERT INTO types VALUES (3, 'lizard');
INSERT INTO types VALUES (4, 'snake');
INSERT INTO types VALUES (5, 'bird');
INSERT INTO types VALUES (6, 'hamster');

INSERT INTO owners VALUES (1, 'George', 'Franklin', '110 W. Liberty St.', 'Madison', '6085551023', 'owner1');
INSERT INTO owners VALUES (2, 'Betty', 'Davis', '638 Cardinal Ave.', 'Sun Prairie', '6085551749', 'owner1');
INSERT INTO owners VALUES (3, 'Eduardo', 'Rodriquez', '2693 Commerce St.', 'McFarland', '6085558763', 'owner1');
INSERT INTO owners VALUES (4, 'Harold', 'Davis', '563 Friendly St.', 'Windsor', '6085553198', 'owner1');
INSERT INTO owners VALUES (5, 'Peter', 'McTavish', '2387 S. Fair Way', 'Madison', '6085552765', 'owner1');
INSERT INTO owners VALUES (6, 'Jean', 'Coleman', '105 N. Lake St.', 'Monona', '6085552654', 'owner1');
INSERT INTO owners VALUES (7, 'Jeff', 'Black', '1450 Oak Blvd.', 'Monona', '6085555387', 'owner1');
INSERT INTO owners VALUES (8, 'Maria', 'Escobito', '345 Maple St.', 'Madison', '6085557683', 'owner1');
INSERT INTO owners VALUES (9, 'David', 'Schroeder', '2749 Blackhawk Trail', 'Madison', '6085559435', 'owner1');
INSERT INTO owners VALUES (10, 'Carlos', 'Estaban', '2335 Independence La.', 'Waunakee', '6085555487', 'owner2');

INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (1, 'Leo', '2010-09-07', 1, 1);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (2, 'Basil', '2012-08-06', 6, 2);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (3, 'Rosy', '2011-04-17', 2, 3);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (4, 'Jewel', '2010-03-07', 2, 3);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (5, 'Iggy', '2010-11-30', 3, 4);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (6, 'George', '2010-01-20', 4, 5);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (7, 'Samantha', '2012-09-04', 1, 6);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (8, 'Max', '2012-09-04', 1, 6);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (9, 'Lucky', '2011-08-06', 5, 7);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (10, 'Mulligan', '2007-02-24', 2, 8);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (11, 'Freddy', '2010-03-09', 5, 9);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (12, 'Lucky', '2010-06-24', 2, 10);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (13, 'Sly', '2012-06-08', 1, 10);

INSERT INTO visits(id,pet_id,visit_date,description) VALUES (1, 7, '2013-01-01', 'rabies shot');
INSERT INTO visits(id,pet_id,visit_date,description) VALUES (2, 8, '2013-01-02', 'rabies shot');
INSERT INTO visits(id,pet_id,visit_date,description) VALUES (3, 8, '2013-01-03', 'neutered');
INSERT INTO visits(id,pet_id,visit_date,description) VALUES (4, 7, '2013-01-04', 'spayed');

INSERT INTO chips(id, serial_number, model, geolocatable, pet_id) VALUES (1, '1', 'model1', true, 1);
INSERT INTO chips(id, serial_number, model, geolocatable, pet_id) VALUES (2, '2', 'model2', true, 2);
INSERT INTO chips(id, serial_number, model, geolocatable, pet_id) VALUES (3, '3', 'model3', false, 3);

INSERT INTO diseases VALUES (1, 'malisimo de la muerte', 'LOW', 'compra paracetamol', 1);
INSERT INTO diseases VALUES (2, 'tengo el hambre', 'MEDIUM', 'la vas a espichar', 2);
INSERT INTO diseases VALUES (3, 'necrosis', 'MEDIUM', 'no tenemos na que hacer', 3);
INSERT INTO diseases VALUES (4, 'me duele el costao', 'MEDIUM', 'me duele mucho', 4);
INSERT INTO diseases VALUES (5, 'me pica la cabeza', 'HIGH', 'y no se que hacer', 5);
INSERT INTO diseases VALUES (6, 'dame de comer', 'HIGH', 'humano... me puedes rascar?', 6);
INSERT INTO diseases VALUES (7, 'como una bola me voy a poner', 'HIGH', 'de tanto comer', 7);

INSERT INTO opinion(id,comentary,date,puntuation,user_username,vet_id) VALUES (1, 'Muy buen servicio', '2020-01-04 00:00:00',5,'owner1',1);
INSERT INTO opinion(id,comentary,date,puntuation,user_username,vet_id) VALUES (2, 'Me parece un borde', '2020-02-14 00:00:00',2,'owner1',2);
INSERT INTO opinion(id,comentary,date,puntuation,user_username,vet_id) VALUES (3, 'Servicio decente', '2020-02-29 00:00:00',4,'owner2',3);
INSERT INTO opinion(id,comentary,date,puntuation,user_username,vet_id) VALUES (4, 'Mi perrito ha curado rapido, MUCHAS GRACIAAS', '2020-03-10 00:00:00',5,'owner1',4);
INSERT INTO opinion(id,comentary,date,puntuation,user_username,vet_id) VALUES (5, 'Pipo era un buen perro', '2018-06-10 00:00:00',5,'admin1',5);
INSERT INTO opinion(id,comentary,date,puntuation,user_username,vet_id) VALUES (6, 'GUAU GUAU GUAAU', '2019-06-10 00:00:00',3,'owner1',6);

INSERT INTO rooms(id,name, floor) VALUES (1,'Quirofano1',1);
INSERT INTO rooms(id,name, floor) VALUES (2,'Quirofano2',1);
INSERT INTO rooms(id,name, floor) VALUES (3,'Quirofano3',2);
INSERT INTO rooms(id,name, floor) VALUES (4,'Quirofano4',2);
INSERT INTO rooms(id,name, floor) VALUES (5,'Quirofano5',2);


INSERT INTO room_medical_team(room_id, medical_team) VALUES (1,'Tijeras');
INSERT INTO room_medical_team(room_id, medical_team) VALUES (1,'Pinzas');
INSERT INTO room_medical_team(room_id, medical_team) VALUES (1,'Bandejas');
INSERT INTO room_medical_team(room_id, medical_team) VALUES (1,'Bisturi');
INSERT INTO room_medical_team(room_id, medical_team) VALUES (1,'Mascarillas');

INSERT INTO room_medical_team(room_id, medical_team) VALUES (2,'Tijeras');
INSERT INTO room_medical_team(room_id, medical_team) VALUES (2,'Pinzas');
INSERT INTO room_medical_team(room_id, medical_team) VALUES (2,'Bandejas');
INSERT INTO room_medical_team(room_id, medical_team) VALUES (2,'Bisturi');
INSERT INTO room_medical_team(room_id, medical_team) VALUES (2,'Mascarillas');

INSERT INTO room_medical_team(room_id, medical_team) VALUES (3,'Tijeras');
INSERT INTO room_medical_team(room_id, medical_team) VALUES (3,'Pinzas');
INSERT INTO room_medical_team(room_id, medical_team) VALUES (3,'Bandejas');
INSERT INTO room_medical_team(room_id, medical_team) VALUES (3,'Bisturi');
INSERT INTO room_medical_team(room_id, medical_team) VALUES (3,'Mascarillas');

INSERT INTO room_medical_team(room_id, medical_team) VALUES (4,'Tijeras');
INSERT INTO room_medical_team(room_id, medical_team) VALUES (4,'Pinzas');
INSERT INTO room_medical_team(room_id, medical_team) VALUES (4,'Bandejas');
INSERT INTO room_medical_team(room_id, medical_team) VALUES (4,'Bisturi');
INSERT INTO room_medical_team(room_id, medical_team) VALUES (4,'Mascarillas');

INSERT INTO bookings(date, pet_id, owner_id, vet_id, room_id) VALUES ('2020-01-04',1,1,1,1);
INSERT INTO bookings(date, pet_id, owner_id, vet_id, room_id) VALUES ('2020-03-02',2,2,2,2);
INSERT INTO bookings(date, pet_id, owner_id, vet_id, room_id) VALUES ('2019-11-04',3,3,3,3);
INSERT INTO bookings(date, pet_id, owner_id, vet_id, room_id) VALUES ('2019-12-15',4,3,4,4);
INSERT INTO bookings(date, pet_id, owner_id, vet_id, room_id) VALUES ('2020-04-02',5,4,5,1);
INSERT INTO bookings(date, pet_id, owner_id, vet_id, room_id) VALUES ('2020-02-08',6,5,6,2);




