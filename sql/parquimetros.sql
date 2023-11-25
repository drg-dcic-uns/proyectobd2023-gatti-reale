CREATE DATABASE parquimetros;

# selecciono la base de datos sobre la cual voy a hacer modificaciones
USE parquimetros;


CREATE TABLE Conductores (
 dni INT UNSIGNED NOT NULL ,
 nombre VARCHAR(45) NOT NULL, 
 apellido  VARCHAR(45) NOT NULL,
 direccion VARCHAR(45) NOT NULL, 
 telefono VARCHAR(45), 
 registro INT UNSIGNED NOT NULL, 
 
 CONSTRAINT pk_conductores
 PRIMARY KEY (dni)

) ENGINE=InnoDB;


CREATE TABLE Automoviles (
 patente CHAR(6),
 marca VARCHAR(45) NOT NULL,
 modelo VARCHAR(45) NOT NULL,
 color VARCHAR(45) NOT NULL,
 dni INT UNSIGNED NOT NULL ,

CONSTRAINT pk_automoviles
 PRIMARY KEY (patente),

 CONSTRAINT FK_Automoviles
 FOREIGN KEY (dni) REFERENCES Conductores(dni)

)ENGINE=InnoDB;


CREATE TABLE Tipos_tarjeta (
 tipo VARCHAR(45) NOT NULL ,
 descuento DECIMAL(3,2) UNSIGNED NOT NULL CHECK (descuento >= 0 AND descuento <= 1),

 CONSTRAINT pk_Tipos_tarjeta
 PRIMARY KEY (tipo)

) ENGINE=InnoDB;


CREATE TABLE Tarjetas  (
 patente CHAR(6) NOT NULL,
 id_tarjeta INT UNSIGNED NOT NULL AUTO_INCREMENT  ,
 saldo DECIMAL(5,2) NOT NULL, 
 tipo VARCHAR(25) NOT NULL,

 CONSTRAINT pk_tarjetas
 PRIMARY KEY (id_tarjeta),

 CONSTRAINT FK_Tarjetas
 FOREIGN KEY (patente) REFERENCES Automoviles(patente),
  CONSTRAINT FK_Tipos_tarjeta
 FOREIGN KEY (tipo) REFERENCES Tipos_tarjeta(tipo)


) ENGINE=InnoDB;


CREATE TABLE Recargas (
 id_tarjeta INT UNSIGNED NOT NULL,
 fecha  DATE NOT NULL,
 hora TIME NOT NULL,
 saldo_anterior DECIMAL(5,2) NOT NULL, 
 saldo_posterior DECIMAL(5,2) NOT NULL, 
 CONSTRAINT pk_recargas
 PRIMARY KEY (id_tarjeta,fecha,hora),
 
 CONSTRAINT FK_Recargas
 FOREIGN KEY (id_tarjeta) REFERENCES Tarjetas(id_tarjeta)

 
 

) ENGINE=InnoDB;


CREATE TABLE Inspectores (
 legajo INT UNSIGNED NOT NULL ,
 dni INT UNSIGNED NOT NULL ,
 nombre VARCHAR(45) NOT NULL, 
 apellido  VARCHAR(45) NOT NULL,
 password  VARCHAR(32) NOT NULL, 
 
 CONSTRAINT pk_Inspectores
 PRIMARY KEY (legajo)

) ENGINE=InnoDB;


CREATE TABLE Ubicaciones (
 calle VARCHAR(45),
 altura INT UNSIGNED NOT NULL,
 tarifa DECIMAL(5,2) UNSIGNED NOT NULL CHECK (tarifa >= 0),
 
 CONSTRAINT pk_Ubicaciones
 PRIMARY KEY(altura,calle)
) ENGINE=InnoDB;


CREATE TABLE Parquimetros (
 id_parq INT UNSIGNED NOT NULL ,
 numero INT UNSIGNED NOT NULL, 
 calle VARCHAR(45) NOT NULL,
 altura INT UNSIGNED NOT NULL,



 CONSTRAINT FK_Parquimetros_Ubicaciones
 FOREIGN KEY (altura,calle) REFERENCES Ubicaciones(altura,calle),

 CONSTRAINT pk_Parquimetros
 PRIMARY KEY(id_parq)

) ENGINE=InnoDB;


CREATE TABLE Estacionamientos (
 id_parq INT UNSIGNED NOT NULL ,
 fecha_ent DATE NOT NULL, 
 hora_ent TIME NOT NULL,
 fecha_sal DATE , 
 hora_sal TIME ,
 id_tarjeta INT UNSIGNED NOT NULL ,

 CONSTRAINT FK_Estacionamientos_Tarjetas
 FOREIGN KEY (id_tarjeta) REFERENCES Tarjetas(id_tarjeta),

 CONSTRAINT FK_Estacionamientos_Parquimetros
 FOREIGN KEY (id_parq) REFERENCES Parquimetros(id_parq),

 CONSTRAINT pk_Estacionamientos
 PRIMARY KEY (id_parq,fecha_ent,hora_ent)

) ENGINE=InnoDB;


CREATE TABLE Accede (
 fecha DATE NOT NULL, 
 hora TIME NOT NULL,
 legajo INT UNSIGNED NOT NULL ,
 id_parq INT UNSIGNED NOT NULL ,


 CONSTRAINT FK_Accede
 FOREIGN KEY (id_parq) REFERENCES Parquimetros(id_parq),

 CONSTRAINT FK_Accede_Inspectores
 FOREIGN KEY (legajo) REFERENCES Inspectores(legajo),

 CONSTRAINT pk_Accede 
 PRIMARY KEY(id_parq,fecha,hora)


) ENGINE=InnoDB;

CREATE TABLE Asociado_con (
 id_asociado_con INT UNSIGNED NOT NULL AUTO_INCREMENT,
 dia ENUM('do', 'lu', 'ma', 'mi', 'ju', 'vi', 'sa') NOT NULL, 
 turno ENUM('m', 't') NOT NULL,
 calle VARCHAR(45) NOT NULL,
 altura INT UNSIGNED NOT NULL,
 legajo INT UNSIGNED NOT NULL ,

 CONSTRAINT FK_Asociado_con_Inspectores
 FOREIGN KEY (legajo) REFERENCES Inspectores(legajo),
 
 CONSTRAINT FK_Asociado_con_Ubicaciones
 FOREIGN KEY (altura,calle) REFERENCES Ubicaciones(altura,calle),
 
 CONSTRAINT pk_Asociado_con
 PRIMARY KEY (id_asociado_con)

) ENGINE=InnoDB;

CREATE TABLE Multa (
 numero INT UNSIGNED NOT NULL AUTO_INCREMENT,
 fecha DATE NOT NULL, 
 hora TIME NOT NULL,
 id_asociado_con INT UNSIGNED NOT NULL,
 patente CHAR(6) NOT NULL,


 CONSTRAINT FK_Multa_Asociado_con
 FOREIGN KEY (id_asociado_con) REFERENCES Asociado_con(id_asociado_con),
 
 CONSTRAINT FK_Multa_Automoviles
 FOREIGN KEY (patente) REFERENCES Automoviles(patente),

 CONSTRAINT pk_Multa
 PRIMARY KEY (numero)

) ENGINE=InnoDB;


CREATE VIEW estacionados AS
SELECT DISTINCT p.calle, p.altura, s.fecha_ent, s.hora_ent, s.patente
FROM Parquimetros p
INNER JOIN (
    SELECT t.id_tarjeta, e.fecha_ent, e.hora_ent, t.patente, e.id_parq
    FROM Tarjetas t
    INNER JOIN Estacionamientos e
    ON t.id_tarjeta = e.id_tarjeta
    WHERE (e.hora_ent IS NOT NULL AND e.fecha_ent IS NOT NULL AND e.hora_sal IS NULL AND e.fecha_sal IS NULL)
)AS s
ON p.id_parq = s.id_parq;


#Procedure conectar
DELIMITER !
create procedure conectar(IN id_tarjeta INTEGER,IN id_parq INTEGER)
begin
    DECLARE fechaEntrada , fechaSalida DATE;
    DECLARE horaEntrada, horaSalida TIME;
    DECLARE tarjeta ,parquimetro INTEGER;
    DECLARE saldoActual DECIMAL(5,2);
    DECLARE descuentoAplicado DECIMAL(3,2);
    DECLARE tiempoTranscurrido INTEGER;
    DECLARE tiempoRestante INTEGER;
    DECLARE tarifaActual DECIMAL(5,2);
    DECLARE abierto BOOL;
    DECLARE codigo_SQL CHAR(5) DEFAULT '00000';
    DECLARE codigo_MYSQL INT DEFAULT 0;
    DECLARE mensaje_error TEXT;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
        BEGIN
            GET DIAGNOSTICS CONDITION 1 codigo_MYSQL= MYSQL_ERRNO,
            codigo_SQL= RETURNED_SQLSTATE,
            mensaje_error= MESSAGE_TEXT;
            SELECT 'SQLEXCEPTION, transaccion abortada' AS resultado,
            codigo_MySQL, codigo_SQL, mensaje_error;
            ROLLBACK;

        end ;

    START TRANSACTION;

        if(id_tarjeta is not null and id_parq is not null and EXISTS(SELECT t.id_tarjeta from tarjetas t where t.id_tarjeta=id_tarjeta) and EXISTS(SELECT p.id_parq from parquimetros p  where p.id_parq=id_parq)) then
            set tarjeta=id_tarjeta;
            set abierto=false;
            SELECT saldo, descuento INTO saldoActual, descuentoAplicado FROM tarjetas t natural join tipos_tarjeta tt where tarjeta=t.id_tarjeta limit 1;
            if(EXISTS(SELECT fecha_ent,hora_ent,fecha_sal,hora_sal FROM estacionamientos e WHERE tarjeta=e.id_tarjeta order by fecha_sal,hora_sal desc)) then
                SELECT fecha_ent,hora_ent,fecha_sal,hora_sal,e.id_parq INTO fechaEntrada,horaEntrada,fechaSalida,horaSalida,parquimetro FROM estacionamientos e WHERE tarjeta=e.id_tarjeta order by fecha_sal,hora_sal desc limit 1;
                if(fechaSalida is NULL and  horaSalida is null) then
                    SELECT saldo INTO saldoActual FROM tarjetas t 
                    WHERE t.id_tarjeta = tarjeta 
                    LIMIT 1 
                    FOR UPDATE;
                    SELECT tarifa INTO tarifaActual FROM parquimetros p NATURAL JOIN ubicaciones u WHERE p.id_parq=parquimetro limit 1;
                    set abierto=true;
                    set fechaSalida=CURDATE();
                    set horaSalida=CURTIME();
                    set tiempoTranscurrido= TIMESTAMPDIFF(MINUTE,CONCAT(fechaEntrada, ' ', horaEntrada),CONCAT(fechaSalida, ' ', horaSalida));
                    set saldoActual=GREATEST(-999.99, saldoActual-(tiempoTranscurrido*tarifaActual*(1-descuentoAplicado)));
                    UPDATE estacionamientos e set e.fecha_sal=fechaSalida,e.hora_sal=horaSalida  where e.id_tarjeta=tarjeta and e.fecha_ent=fechaEntrada and e.hora_ent=horaEntrada;
                    UPDATE tarjetas t set t.saldo=saldoActual where t.id_tarjeta=tarjeta;
                    SELECT 'Cierre' as operacion, saldoActual as saldo ,tiempoTranscurrido, fechaEntrada as Fecha_apertura, fechaSalida as Fecha_cierre, horaEntrada, horaSalida;
                end if ;

            end if;

           if(not EXISTS(SELECT fecha_ent,hora_ent, fecha_sal, hora_sal FROM estacionamientos e WHERE tarjeta=e.id_tarjeta order by fecha_sal,hora_sal desc limit 1) or abierto=false) then
                if(saldoActual>0) then
                    set fechaEntrada=CURDATE();
                    set horaEntrada=CURTIME();
                    set  parquimetro=id_parq;
                    SELECT tarifa INTO tarifaActual FROM parquimetros p NATURAL JOIN ubicaciones u WHERE p.id_parq=parquimetro limit 1;
                    set tiempoRestante = saldoActual/(tarifaActual*(1-descuentoAplicado));
                    INSERT INTO estacionamientos(id_tarjeta,id_parq,fecha_ent,hora_ent,fecha_sal,hora_sal)VALUES (tarjeta,parquimetro,fechaEntrada,horaEntrada,null,null);
                    SELECT 'Apertura' as operacion, 'La operacion se realizo con exito' as resultado ,tiempoRestante;
                else
                    begin
                        SELECT 'Saldo de la tarjeta insuficiente' as resultado,'' as operacion;
                    end;
                end if;

            end if ;
        ELSE
            IF (NOT EXISTS(SELECT t.id_tarjeta FROM tarjetas t WHERE t.id_tarjeta = id_tarjeta) OR id_tarjeta IS NULL) THEN
                SELECT 'Error tarjeta inexistente' AS resultado, '' AS operacion;
            ELSE
                SELECT 'Error parquimetro inexistente' AS resultado, '' AS operacion;
            END IF;
    END IF;

    COMMIT;
END !
DELIMITER ;


DELIMITER //

CREATE TRIGGER traza_recargas
AFTER UPDATE ON Tarjetas
FOR EACH ROW
BEGIN
    IF NEW.saldo > OLD.saldo THEN
        INSERT INTO Recargas (id_tarjeta, fecha, hora, saldo_anterior, saldo_posterior)
        VALUES (NEW.id_tarjeta, CURDATE(), CURTIME(), OLD.saldo, NEW.saldo);
    END IF;
END //

DELIMITER ;





CREATE USER 'parquimetro'@'%' IDENTIFIED BY 'parq';
GRANT EXECUTE ON PROCEDURE parquimetros.conectar TO 'parquimetro'@'%';
GRANT SELECT ON parquimetros.Automoviles TO 'parquimetro'@'%';
GRANT SELECT ON parquimetros.Conductores TO 'parquimetro'@'%';
GRANT SELECT ON parquimetros.Tipos_tarjeta TO 'parquimetro'@'%';
GRANT SELECT ON parquimetros.Tarjetas TO 'parquimetro'@'%';
GRANT SELECT ON parquimetros.Ubicaciones TO 'parquimetro'@'%';
GRANT SELECT ON parquimetros.Parquimetros TO 'parquimetro'@'%';
GRANT SELECT ON parquimetros.Estacionamientos TO 'parquimetro'@'%';

CREATE USER 'admin'@'localhost' IDENTIFIED BY 'admin';
GRANT ALL PRIVILEGES ON parquimetros.* TO 'admin'@'localhost' WITH GRANT OPTION;

CREATE USER 'venta'@'%' IDENTIFIED BY 'venta';
GRANT INSERT ON parquimetros.Tarjetas TO 'venta'@'%';
GRANT INSERT ON parquimetros.Recargas TO 'venta'@'%';
GRANT UPDATE (saldo) ON parquimetros.Tarjetas TO 'venta'@'%';



CREATE USER 'inspector'@'%' IDENTIFIED BY 'inspector';

GRANT SELECT ON parquimetros.Parquimetros TO 'inspector'@'%';
GRANT SELECT ON parquimetros.Asociado_con TO 'inspector'@'%';
GRANT SELECT ON parquimetros.Automoviles TO 'inspector'@'%';
GRANT SELECT ON parquimetros.Inspectores TO 'inspector'@'%';
GRANT SELECT ON parquimetros.Ubicaciones TO 'inspector'@'%';
GRANT UPDATE ON parquimetros.Multa TO 'inspector'@'%';
GRANT INSERT ON parquimetros.Multa TO 'inspector'@'%';
GRANT INSERT ON parquimetros.Accede TO 'inspector'@'%';
GRANT SELECT ON parquimetros.estacionados TO 'inspector'@'%';
GRANT SELECT (id_parq,calle,altura) ON parquimetros.Parquimetros TO 'inspector'@'%';









