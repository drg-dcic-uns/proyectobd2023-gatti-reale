USE parquimetros;

INSERT INTO Conductores VALUES (44321404, 'tobias', 'Gatti', 'ed gon', '2916446463', 1);
INSERT INTO Conductores VALUES (44490499, 'amparo', 'gutierrez', 'La cautiva y Los piquillines', '2915223437', 4);
INSERT INTO Conductores VALUES (41099560, 'Guido', 'Reale', 'Angel Brunel', '2915038166', 5);

INSERT INTO Automoviles VALUES('MBU793', 'volkswagen', 'a', 'rojo', 44321404);
INSERT INTO Automoviles VALUES('ASD123', 'volkswagen', 'a', 'rojo', 44490499);
INSERT INTO Automoviles VALUES('FGH456', 'volkswagen', 'a', 'rojo', 41099560);


INSERT INTO Tipos_tarjeta VALUES('A', 0.15);
INSERT INTO Tipos_tarjeta VALUES('B', 0.30);
INSERT INTO Tipos_tarjeta VALUES('C', 0.60);

INSERT INTO Tarjetas (patente,saldo,tipo,id_tarjeta)
VALUES('MBU793', 215.56, 'A',1);
INSERT INTO Tarjetas (patente,saldo,tipo,id_tarjeta)
VALUES('ASD123', 215.56, 'B',2);
INSERT INTO Tarjetas (patente,saldo,tipo, id_tarjeta)
VALUES('FGH456', 215.56, 'C',3);


INSERT INTO Recargas (fecha, hora, saldo_anterior, saldo_posterior,id_tarjeta)
VALUES('2023-06-20', '10:00:20', 333.44, 342.55,1);

INSERT INTO Inspectores (legajo, dni, nombre, apellido, password)
VALUES (1, 55555555, 'nombre1', 'Apellido1', md5('inspector'));

INSERT INTO Ubicaciones (calle, altura, tarifa)
VALUES ('Calle Principal', 100, 2.50);
INSERT INTO Ubicaciones (calle, altura, tarifa)
VALUES ('Calle Secundaria', 150, 2.50);
INSERT INTO Ubicaciones (calle, altura, tarifa)
VALUES ('Tercer Calle', 200, 2.50);

INSERT INTO Parquimetros (id_parq, numero, calle, altura)
VALUES (1, 001, 'Calle Principal', 100);
INSERT INTO Parquimetros (id_parq, numero, calle, altura)
VALUES (2, 002, 'Calle Secundaria', 150);
INSERT INTO Parquimetros (id_parq, numero, calle, altura)
VALUES (3, 003, 'Tercer Calle', 200);


INSERT INTO Estacionamientos (id_parq, fecha_ent, hora_ent, id_tarjeta)
VALUES (1, '2023-10-23', '18:00:00', 1);
INSERT INTO Estacionamientos (id_parq, fecha_ent, hora_ent, id_tarjeta)
VALUES (2, '2023-09-15', '17:30:00', 2);
INSERT INTO Estacionamientos (id_parq, fecha_ent, hora_ent, id_tarjeta)
VALUES (3, '2023-09-15', '19:15:00', 3);


INSERT INTO Asociado_con (id_asociado_con, dia, turno, calle, altura, legajo)
VALUES (1, 'lu', 't', 'Calle Principal', 100, 1);

INSERT INTO Multa (fecha, hora, id_asociado_con, patente)
VALUES ('2023-09-15', '11:30:00', 1, 'MBU793');