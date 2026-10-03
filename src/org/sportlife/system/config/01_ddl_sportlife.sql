-- ============================================================
--  SPORTLIFE - DDL (Data Definition Language)
--  Crea base de datos, tablas y procedimientos almacenados
-- ============================================================

DROP DATABASE IF EXISTS renta_de_canchas_sportlife_in4am;
CREATE DATABASE renta_de_canchas_sportlife_in4am;
USE renta_de_canchas_sportlife_in4am;

-- ============================================================
--  TABLAS
-- ============================================================

CREATE TABLE Users(
    name VARCHAR(50) NOT NULL,
    lastname VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL,
    `user` VARCHAR(25) NOT NULL,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'User',
    phone VARCHAR(10) NULL,
    id_user VARCHAR(36) NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id_user)
);

CREATE TABLE Canchas(
    id_cancha INT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(20) NOT NULL UNIQUE,
    nombre VARCHAR(50) NOT NULL,
    tipo_deporte VARCHAR(50) NOT NULL,
    techada BOOLEAN NOT NULL DEFAULT FALSE,
    precio_por_hora DECIMAL(10,2) NOT NULL,
    estado VARCHAR(20) DEFAULT 'Disponible'
);

CREATE TABLE Reservas(
    id_reserva INT AUTO_INCREMENT PRIMARY KEY,
    id_cancha INT NOT NULL,
    id_user VARCHAR(36) NOT NULL,
    fecha_reserva DATE NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fin TIME NOT NULL,
    costo_total DECIMAL(10,2) NOT NULL,
    estado_reserva VARCHAR(20) DEFAULT 'Pendiente',
    CONSTRAINT fk_reserva_cancha FOREIGN KEY (id_cancha) REFERENCES Canchas(id_cancha),
    CONSTRAINT fk_reserva_user FOREIGN KEY (id_user) REFERENCES Users(id_user)
);

CREATE TABLE Inventario(
    id_inventario INT AUTO_INCREMENT PRIMARY KEY,
    nombre_producto VARCHAR(100) NOT NULL,
    categoria VARCHAR(50) NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    proveedor VARCHAR(100) NULL,
    estado VARCHAR(20) DEFAULT 'Disponible'
);

-- ============================================================
--  PROCEDIMIENTOS ALMACENADOS
-- ============================================================

DELIMITER $$

-- ---------- USUARIOS ----------
CREATE PROCEDURE sp_create_users(
    IN name_p VARCHAR(50),
    IN lastname_p VARCHAR(50),
    IN email_p VARCHAR(50),
    IN user_p VARCHAR(25),
    IN password_p VARCHAR(100),
    IN role_p VARCHAR(20),
    IN id_gerente_p VARCHAR(36)
)
BEGIN
    DECLARE rol_gerente VARCHAR(20);

    SELECT role INTO rol_gerente FROM Users WHERE id_user=id_gerente_p LIMIT 1;

    IF rol_gerente IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT='Error: El usuario indicado no existe.';
    ELSEIF rol_gerente<>'Gerente' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT='Error: Solo un Gerente puede agregar usuarios o personal.';
    ELSEIF role_p NOT IN('Gerente','Administrador','Recepcionista','User') THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT='Error: El rol indicado no es valido.';
    ELSE
        INSERT INTO Users(name, lastname, email, `user`, password, role, phone, id_user)
        VALUES(name_p, lastname_p, email_p, user_p, password_p, role_p, NULL, UUID());
    END IF;
END $$

CREATE PROCEDURE sp_register_customer(
    IN name_p VARCHAR(50),
    IN lastname_p VARCHAR(50),
    IN email_p VARCHAR(50),
    IN user_p VARCHAR(25),
    IN password_p VARCHAR(100),
    IN phone_p VARCHAR(10)
)
BEGIN
    INSERT INTO Users(name, lastname, email, `user`, password, role, phone, id_user)
    VALUES(name_p, lastname_p, email_p, user_p, password_p, 'User', phone_p, UUID());
END $$

CREATE PROCEDURE sp_get_administrative_staff()
BEGIN
    SELECT id_user, name, lastname, email, `user`, password, role
    FROM Users
    WHERE role IN('Gerente','Administrador','Recepcionista');
END $$

CREATE PROCEDURE sp_get_registered_customers()
BEGIN
    SELECT id_user, name, lastname, email, `user`, password, phone, role
    FROM Users
    WHERE role='User';
END $$

CREATE PROCEDURE sp_get_all_users()
BEGIN
    SELECT id_user, name, lastname, email, `user`, password, phone, role
    FROM Users;
END $$

CREATE PROCEDURE sp_update_users(
    IN id_user_p VARCHAR(36),
    IN name_p VARCHAR(50),
    IN lastname_p VARCHAR(50),
    IN email_p VARCHAR(50),
    IN user_p VARCHAR(25),
    IN password_p VARCHAR(100),
    IN role_p VARCHAR(20),
    IN id_gerente_p VARCHAR(36)
)
BEGIN
    DECLARE rol_gerente VARCHAR(20);

    SELECT role INTO rol_gerente FROM Users WHERE id_user=id_gerente_p LIMIT 1;

    IF rol_gerente IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT='Error: El usuario indicado no existe.';
    ELSEIF rol_gerente<>'Gerente' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT='Error: Solo un Gerente puede editar usuarios o personal.';
    ELSEIF role_p NOT IN('Gerente','Administrador','Recepcionista','User') THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT='Error: El rol indicado no es valido.';
    ELSE
        UPDATE Users
        SET name=name_p, lastname=lastname_p, email=email_p,
            `user`=user_p, password=password_p, role=role_p
        WHERE id_user=id_user_p;
    END IF;
END $$

CREATE PROCEDURE sp_delete_users(
    IN id_user_p VARCHAR(36),
    IN id_gerente_p VARCHAR(36)
)
BEGIN
    DECLARE rol_gerente VARCHAR(20);

    SELECT role INTO rol_gerente FROM Users WHERE id_user=id_gerente_p LIMIT 1;

    IF rol_gerente IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT='Error: El usuario indicado no existe.';
    ELSEIF rol_gerente<>'Gerente' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT='Error: Solo un Gerente puede eliminar usuarios o personal.';
    ELSEIF id_user_p=id_gerente_p THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT='Error: El Gerente no puede eliminarse a si mismo.';
    ELSE
        DELETE FROM Users WHERE id_user=id_user_p;
    END IF;
END $$

CREATE PROCEDURE sp_check_user_exists_strict(
    IN user_or_email_p VARCHAR(50)
)
BEGIN
    SELECT id_user, name, lastname, email, `user`, role
    FROM Users
    WHERE CAST(`user` AS BINARY)=CAST(user_or_email_p AS BINARY)
       OR CAST(email AS BINARY)=CAST(user_or_email_p AS BINARY);
END $$

CREATE PROCEDURE sp_verify_user_password(
    IN user_or_email_p VARCHAR(50),
    IN password_p VARCHAR(100)
)
BEGIN
    SELECT id_user, name, lastname, email, `user`, role
    FROM Users
    WHERE (
        CAST(`user` AS BINARY)=CAST(user_or_email_p AS BINARY)
        OR CAST(email AS BINARY)=CAST(user_or_email_p AS BINARY)
    )
    AND CAST(password AS BINARY)=CAST(password_p AS BINARY);
END $$

-- ---------- CANCHAS ----------
CREATE PROCEDURE sp_create_cancha(
    IN codigo_p VARCHAR(20),
    IN nombre_p VARCHAR(50),
    IN tipo_deporte_p VARCHAR(50),
    IN techada_p BOOLEAN,
    IN precio_por_hora_p DECIMAL(10,2),
    IN id_admin_p VARCHAR(36)
)
BEGIN
    DECLARE rol_admin VARCHAR(20);

    SELECT role INTO rol_admin FROM Users WHERE id_user=id_admin_p LIMIT 1;

    IF rol_admin IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT='Error: El usuario no existe.';
    ELSEIF rol_admin<>'Administrador' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT='Error: Solo un Administrador puede crear canchas.';
    ELSE
        INSERT INTO Canchas(codigo, nombre, tipo_deporte, techada, precio_por_hora, estado)
        VALUES(codigo_p, nombre_p, tipo_deporte_p, techada_p, precio_por_hora_p, 'Disponible');
    END IF;
END $$

CREATE PROCEDURE sp_update_cancha(
    IN id_cancha_p INT,
    IN codigo_p VARCHAR(20),
    IN nombre_p VARCHAR(50),
    IN tipo_deporte_p VARCHAR(50),
    IN techada_p BOOLEAN,
    IN precio_por_hora_p DECIMAL(10,2),
    IN estado_p VARCHAR(20),
    IN id_admin_p VARCHAR(36)
)
BEGIN
    DECLARE rol_admin VARCHAR(20);

    SELECT role INTO rol_admin FROM Users WHERE id_user=id_admin_p LIMIT 1;

    IF rol_admin IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT='Error: El usuario no existe.';
    ELSEIF rol_admin<>'Administrador' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT='Error: Solo un Administrador puede editar las canchas.';
    ELSE
        UPDATE Canchas
        SET codigo=codigo_p, nombre=nombre_p, tipo_deporte=tipo_deporte_p,
            techada=techada_p, precio_por_hora=precio_por_hora_p, estado=estado_p
        WHERE id_cancha=id_cancha_p;
    END IF;
END $$

CREATE PROCEDURE sp_delete_cancha(
    IN id_cancha_p INT,
    IN id_admin_p VARCHAR(36)
)
BEGIN
    DECLARE rol_admin VARCHAR(20);

    SELECT role INTO rol_admin FROM Users WHERE id_user=id_admin_p LIMIT 1;

    IF rol_admin IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT='Error: El usuario no existe.';
    ELSEIF rol_admin<>'Administrador' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT='Error: Solo un Administrador puede eliminar las canchas.';
    ELSE
        DELETE FROM Canchas WHERE id_cancha=id_cancha_p;
    END IF;
END $$

CREATE PROCEDURE sp_get_all_canchas()
BEGIN
    SELECT id_cancha, codigo, nombre, tipo_deporte, techada, precio_por_hora, estado
    FROM Canchas
    ORDER BY codigo ASC;
END $$

CREATE PROCEDURE sp_get_available_canchas()
BEGIN
    SELECT c.id_cancha, c.codigo, c.nombre, c.tipo_deporte, c.techada, c.precio_por_hora, c.estado
    FROM Canchas c
    WHERE c.estado='Disponible'
      AND NOT EXISTS(
          SELECT 1 FROM Reservas r
          WHERE r.id_cancha=c.id_cancha
            AND r.fecha_reserva=CURDATE()
            AND r.estado_reserva<>'Cancelada'
            AND CURTIME()>=r.hora_inicio
            AND CURTIME()<r.hora_fin
      )
    ORDER BY c.codigo ASC;
END $$

-- ---------- RESERVAS ----------
CREATE PROCEDURE sp_create_reserva(
    IN id_cancha_p INT,
    IN id_user_p VARCHAR(36),
    IN fecha_reserva_p DATE,
    IN hora_inicio_p TIME,
    IN hora_fin_p TIME
)
BEGIN
    DECLARE cantidad_reservas INT DEFAULT 0;
    DECLARE precio DECIMAL(10,2);
    DECLARE horas INT;
    DECLARE costo_calculado DECIMAL(10,2);

    IF hora_fin_p<=hora_inicio_p THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT='Error: La hora final debe ser mayor que la hora inicial.';
    END IF;

    SELECT COUNT(*) INTO cantidad_reservas
    FROM Reservas
    WHERE id_cancha=id_cancha_p
      AND fecha_reserva=fecha_reserva_p
      AND estado_reserva NOT IN ('Cancelada','Rechazada')
      AND hora_inicio_p<hora_fin
      AND hora_fin_p>hora_inicio;

    IF cantidad_reservas>0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT='Error: La cancha no esta disponible en ese horario.';
    ELSE
        SELECT precio_por_hora INTO precio FROM Canchas WHERE id_cancha=id_cancha_p;
        SET horas = TIMESTAMPDIFF(HOUR, hora_inicio_p, hora_fin_p);
        SET costo_calculado = precio * horas;

        INSERT INTO Reservas(id_cancha, id_user, fecha_reserva, hora_inicio, hora_fin, costo_total, estado_reserva)
        VALUES(id_cancha_p, id_user_p, fecha_reserva_p, hora_inicio_p, hora_fin_p, costo_calculado, 'Pendiente');
    END IF;
END $$

CREATE PROCEDURE sp_test_crear_reserva_usuario(
    IN user_identifier_p VARCHAR(50),
    IN id_cancha_p INT,
    IN fecha_reserva_p DATE,
    IN hora_inicio_p TIME,
    IN hora_fin_p TIME
)
BEGIN
    DECLARE v_id_cliente VARCHAR(36);

    SET v_id_cliente=NULL;

    SELECT id_user INTO v_id_cliente
    FROM Users
    WHERE `user`=user_identifier_p OR email=user_identifier_p
    LIMIT 1;

    IF v_id_cliente IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT='Error: El usuario indicado no existe en la base de datos.';
    ELSE
        CALL sp_create_reserva(id_cancha_p, v_id_cliente, fecha_reserva_p, hora_inicio_p, hora_fin_p);
    END IF;
END $$

CREATE PROCEDURE sp_get_all_reservas()
BEGIN
    SELECT
        r.id_reserva,
        c.codigo AS codigo_cancha,
        c.nombre AS nombre_cancha,
        c.tipo_deporte,
        c.techada,
        c.precio_por_hora,
        CONCAT(u.name,' ',u.lastname) AS cliente,
        u.email AS email_cliente,
        r.fecha_reserva,
        r.hora_inicio,
        r.hora_fin,
        TIMESTAMPDIFF(HOUR, r.hora_inicio, r.hora_fin) AS total_horas,
        r.costo_total,
        r.estado_reserva
    FROM Reservas r
    INNER JOIN Canchas c ON r.id_cancha=c.id_cancha
    INNER JOIN Users u ON r.id_user=u.id_user
    ORDER BY r.fecha_reserva ASC, r.hora_inicio ASC;
END $$

CREATE PROCEDURE sp_get_reservas_pendientes()
BEGIN
    SELECT
        r.id_reserva,
        c.codigo AS codigo_cancha,
        c.nombre AS nombre_cancha,
        c.tipo_deporte,
        c.techada,
        c.precio_por_hora,
        CONCAT(u.name,' ',u.lastname) AS cliente,
        u.email AS email_cliente,
        r.fecha_reserva,
        r.hora_inicio,
        r.hora_fin,
        TIMESTAMPDIFF(HOUR, r.hora_inicio, r.hora_fin) AS total_horas,
        r.costo_total,
        r.estado_reserva
    FROM Reservas r
    INNER JOIN Canchas c ON r.id_cancha=c.id_cancha
    INNER JOIN Users u ON r.id_user=u.id_user
    WHERE r.estado_reserva='Pendiente'
    ORDER BY r.fecha_reserva ASC, r.hora_inicio ASC;
END $$

CREATE PROCEDURE sp_confirmar_reserva(IN id_reserva_p INT)
BEGIN
    UPDATE Reservas
    SET estado_reserva='Confirmada'
    WHERE id_reserva=id_reserva_p
      AND estado_reserva<>'Confirmada';
END $$

CREATE PROCEDURE sp_rechazar_reserva(IN id_reserva_p INT)
BEGIN
    UPDATE Reservas
    SET estado_reserva='Rechazada'
    WHERE id_reserva=id_reserva_p
      AND estado_reserva<>'Rechazada';
END $$

CREATE PROCEDURE sp_cancelar_reserva(IN id_reserva_p INT)
BEGIN
    UPDATE Reservas
    SET estado_reserva='Cancelada'
    WHERE id_reserva=id_reserva_p
      AND estado_reserva='Confirmada';
END $$

CREATE PROCEDURE sp_update_reserva(
    IN id_reserva_p INT,
    IN fecha_reserva_p DATE,
    IN hora_inicio_p TIME,
    IN hora_fin_p TIME,
    IN estado_reserva_p VARCHAR(20)
)
BEGIN
    UPDATE Reservas
    SET fecha_reserva=fecha_reserva_p,
        hora_inicio=hora_inicio_p,
        hora_fin=hora_fin_p,
        estado_reserva=estado_reserva_p
    WHERE id_reserva=id_reserva_p;
END $$

CREATE PROCEDURE sp_delete_reserva(IN id_reserva_p INT)
BEGIN
    DELETE FROM Reservas WHERE id_reserva=id_reserva_p;
END $$

-- ---------- INVENTARIO ----------
CREATE PROCEDURE sp_get_inventario()
BEGIN
    SELECT id_inventario, nombre_producto, categoria, cantidad, precio_unitario, proveedor, estado
    FROM Inventario
    ORDER BY nombre_producto ASC;
END $$

CREATE PROCEDURE sp_create_inventario(
    IN nombre_producto_p VARCHAR(100),
    IN categoria_p VARCHAR(50),
    IN cantidad_p INT,
    IN precio_unitario_p DECIMAL(10,2),
    IN proveedor_p VARCHAR(100),
    IN id_gerente_p VARCHAR(36)
)
BEGIN
    DECLARE rol_gerente VARCHAR(20);

    SELECT role INTO rol_gerente FROM Users WHERE id_user=id_gerente_p LIMIT 1;

    IF rol_gerente IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT='Error: El usuario no existe.';
    ELSEIF rol_gerente<>'Gerente' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT='Error: Solo un Gerente puede agregar productos al inventario.';
    ELSE
        INSERT INTO Inventario(nombre_producto, categoria, cantidad, precio_unitario, proveedor, estado)
        VALUES(nombre_producto_p, categoria_p, cantidad_p, precio_unitario_p, proveedor_p, 'Disponible');
    END IF;
END $$

CREATE PROCEDURE sp_update_inventario(
    IN id_inventario_p INT,
    IN nombre_producto_p VARCHAR(100),
    IN categoria_p VARCHAR(50),
    IN cantidad_p INT,
    IN precio_unitario_p DECIMAL(10,2),
    IN proveedor_p VARCHAR(100),
    IN estado_p VARCHAR(20),
    IN id_gerente_p VARCHAR(36)
)
BEGIN
    DECLARE rol_gerente VARCHAR(20);

    SELECT role INTO rol_gerente FROM Users WHERE id_user=id_gerente_p LIMIT 1;

    IF rol_gerente IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT='Error: El usuario no existe.';
    ELSEIF rol_gerente<>'Gerente' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT='Error: Solo un Gerente puede editar el inventario.';
    ELSE
        UPDATE Inventario
        SET nombre_producto=nombre_producto_p, categoria=categoria_p, cantidad=cantidad_p,
            precio_unitario=precio_unitario_p, proveedor=proveedor_p, estado=estado_p
        WHERE id_inventario=id_inventario_p;
    END IF;
END $$

CREATE PROCEDURE sp_delete_inventario(
    IN id_inventario_p INT,
    IN id_gerente_p VARCHAR(36)
)
BEGIN
    DECLARE rol_gerente VARCHAR(20);

    SELECT role INTO rol_gerente FROM Users WHERE id_user=id_gerente_p LIMIT 1;

    IF rol_gerente IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT='Error: El usuario no existe.';
    ELSEIF rol_gerente<>'Gerente' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT='Error: Solo un Gerente puede eliminar productos del inventario.';
    ELSE
        DELETE FROM Inventario WHERE id_inventario=id_inventario_p;
    END IF;
END $$

DELIMITER ;
