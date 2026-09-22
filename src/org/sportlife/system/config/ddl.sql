DROP DATABASE IF EXISTS renta_de_canchas_sportlife_in4am;
CREATE DATABASE renta_de_canchas_sportlife_in4am;
USE renta_de_canchas_sportlife_in4am;

CREATE TABLE Users(
    name VARCHAR(50) NOT NULL,
    lastname VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL,
    `user` VARCHAR(25) NOT NULL,
    password VARCHAR(35) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'User',
    phone VARCHAR(10) NULL,
    id_user VARCHAR(36) NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id_user)
);

CREATE TABLE Canchas(
    id_cancha INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    tipo_deporte VARCHAR(50) NOT NULL,
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
    estado_reserva VARCHAR(20) DEFAULT 'Confirmada',
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

CREATE TABLE SoporteTecnico(
    id_soporte INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario_reporta VARCHAR(36) NOT NULL,
    tipo_problema VARCHAR(50) NOT NULL,
    asunto VARCHAR(100) NOT NULL,
    descripcion TEXT NOT NULL,
    fecha_reporte DATETIME DEFAULT CURRENT_TIMESTAMP,
    estado VARCHAR(20) DEFAULT 'Pendiente',
    respuesta_gerente TEXT NULL,
    fecha_respuesta DATETIME NULL,
    CONSTRAINT fk_soporte_usuario FOREIGN KEY (id_usuario_reporta) REFERENCES Users(id_user)
);

DELIMITER $$

CREATE PROCEDURE sp_create_users(
    IN name_p VARCHAR(50),
    IN lastname_p VARCHAR(50),
    IN email_p VARCHAR(50),
    IN user_p VARCHAR(25),
    IN password_p VARCHAR(35),
    IN role_p VARCHAR(20),
    IN id_gerente_p VARCHAR(36)
)
BEGIN
    DECLARE rol_gerente VARCHAR(20);

    SELECT role
    INTO rol_gerente
    FROM Users
    WHERE id_user=id_gerente_p
    LIMIT 1;

    IF rol_gerente IS NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT='Error: El usuario indicado no existe.';
    ELSEIF rol_gerente<>'Gerente' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT='Error: Solo un Gerente puede agregar usuarios o personal.';
    ELSEIF role_p NOT IN('Gerente','Administrador','Recepcionista','User') THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT='Error: El rol indicado no es valido.';
    ELSE
        INSERT INTO Users(
            name,
            lastname,
            email,
            `user`,
            password,
            role,
            phone,
            id_user
        )
        VALUES(
            name_p,
            lastname_p,
            email_p,
            user_p,
            password_p,
            role_p,
            NULL,
            UUID()
        );
    END IF;
END $$

CREATE PROCEDURE sp_register_customer(
    IN name_p VARCHAR(50),
    IN lastname_p VARCHAR(50),
    IN email_p VARCHAR(50),
    IN user_p VARCHAR(25),
    IN password_p VARCHAR(35),
    IN phone_p VARCHAR(10)
)
BEGIN
    INSERT INTO Users(
        name,
        lastname,
        email,
        `user`,
        password,
        role,
        phone,
        id_user
    )
    VALUES(
        name_p,
        lastname_p,
        email_p,
        user_p,
        password_p,
        'User',
        phone_p,
        UUID()
    );
END $$

CREATE PROCEDURE sp_get_administrative_staff()
BEGIN
    SELECT
        id_user,
        name,
        lastname,
        email,
        `user`,
        password,
        role
    FROM Users
    WHERE role IN('Gerente','Administrador','Recepcionista');
END $$

CREATE PROCEDURE sp_get_registered_customers()
BEGIN
    SELECT
        id_user,
        name,
        lastname,
        email,
        `user`,
        password,
        phone,
        role
    FROM Users
    WHERE role='User';
END $$

CREATE PROCEDURE sp_get_all_users()
BEGIN
    SELECT
        id_user,
        name,
        lastname,
        email,
        `user`,
        password,
        phone,
        role
    FROM Users;
END $$

CREATE PROCEDURE sp_update_users(
    IN id_user_p VARCHAR(36),
    IN name_p VARCHAR(50),
    IN lastname_p VARCHAR(50),
    IN email_p VARCHAR(50),
    IN user_p VARCHAR(25),
    IN password_p VARCHAR(35),
    IN role_p VARCHAR(20),
    IN id_gerente_p VARCHAR(36)
)
BEGIN
    DECLARE rol_gerente VARCHAR(20);

    SELECT role
    INTO rol_gerente
    FROM Users
    WHERE id_user=id_gerente_p
    LIMIT 1;

    IF rol_gerente IS NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT='Error: El usuario indicado no existe.';
    ELSEIF rol_gerente<>'Gerente' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT='Error: Solo un Gerente puede editar usuarios o personal.';
    ELSEIF role_p NOT IN('Gerente','Administrador','Recepcionista','User') THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT='Error: El rol indicado no es valido.';
    ELSE
        UPDATE Users
        SET name=name_p,
            lastname=lastname_p,
            email=email_p,
            `user`=user_p,
            password=password_p,
            role=role_p
        WHERE id_user=id_user_p;
    END IF;
END $$

CREATE PROCEDURE sp_delete_users(
    IN id_user_p VARCHAR(36),
    IN id_gerente_p VARCHAR(36)
)
BEGIN
    DECLARE rol_gerente VARCHAR(20);

    SELECT role
    INTO rol_gerente
    FROM Users
    WHERE id_user=id_gerente_p
    LIMIT 1;

    IF rol_gerente IS NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT='Error: El usuario indicado no existe.';
    ELSEIF rol_gerente<>'Gerente' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT='Error: Solo un Gerente puede eliminar usuarios o personal.';
    ELSEIF id_user_p=id_gerente_p THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT='Error: El Gerente no puede eliminarse a si mismo.';
    ELSE
        DELETE FROM Users
        WHERE id_user=id_user_p;
    END IF;
END $$

CREATE PROCEDURE sp_check_user_exists_strict(
    IN user_or_email_p VARCHAR(50)
)
BEGIN
    SELECT
        id_user,
        name,
        lastname,
        email,
        `user`,
        role
    FROM Users
    WHERE CAST(`user` AS BINARY)=CAST(user_or_email_p AS BINARY)
       OR CAST(email AS BINARY)=CAST(user_or_email_p AS BINARY);
END $$

CREATE PROCEDURE sp_verify_user_password(
    IN user_or_email_p VARCHAR(50),
    IN password_p VARCHAR(35)
)
BEGIN
    SELECT
        id_user,
        name,
        lastname,
        email,
        `user`,
        role
    FROM Users
    WHERE (
        CAST(`user` AS BINARY)=CAST(user_or_email_p AS BINARY)
        OR CAST(email AS BINARY)=CAST(user_or_email_p AS BINARY)
    )
    AND CAST(password AS BINARY)=CAST(password_p AS BINARY);
END $$

CREATE PROCEDURE sp_create_cancha(
    IN nombre_p VARCHAR(50),
    IN tipo_deporte_p VARCHAR(50),
    IN precio_por_hora_p DECIMAL(10,2),
    IN id_admin_p VARCHAR(36)
)
BEGIN
    DECLARE rol_admin VARCHAR(20);

    SELECT role
    INTO rol_admin
    FROM Users
    WHERE id_user=id_admin_p
    LIMIT 1;

    IF rol_admin IS NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT='Error: El usuario no existe.';
    ELSEIF rol_admin<>'Administrador' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT='Error: Solo un Administrador puede crear canchas.';
    ELSE
        INSERT INTO Canchas(
            nombre,
            tipo_deporte,
            precio_por_hora,
            estado
        )
        VALUES(
            nombre_p,
            tipo_deporte_p,
            precio_por_hora_p,
            'Disponible'
        );
    END IF;
END $$

CREATE PROCEDURE sp_update_cancha(
    IN id_cancha_p INT,
    IN nombre_p VARCHAR(50),
    IN tipo_deporte_p VARCHAR(50),
    IN precio_por_hora_p DECIMAL(10,2),
    IN estado_p VARCHAR(20),
    IN id_admin_p VARCHAR(36)
)
BEGIN
    DECLARE rol_admin VARCHAR(20);

    SELECT role
    INTO rol_admin
    FROM Users
    WHERE id_user=id_admin_p
    LIMIT 1;

    IF rol_admin IS NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT='Error: El usuario no existe.';
    ELSEIF rol_admin<>'Administrador' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT='Error: Solo un Administrador puede editar las canchas.';
    ELSE
        UPDATE Canchas
        SET nombre=nombre_p,
            tipo_deporte=tipo_deporte_p,
            precio_por_hora=precio_por_hora_p,
            estado=estado_p
        WHERE id_cancha=id_cancha_p;
    END IF;
END $$

CREATE PROCEDURE sp_delete_cancha(
    IN id_cancha_p INT,
    IN id_admin_p VARCHAR(36)
)
BEGIN
    DECLARE rol_admin VARCHAR(20);

    SELECT role
    INTO rol_admin
    FROM Users
    WHERE id_user=id_admin_p
    LIMIT 1;

    IF rol_admin IS NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT='Error: El usuario no existe.';
    ELSEIF rol_admin<>'Administrador' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT='Error: Solo un Administrador puede eliminar las canchas.';
    ELSE
        DELETE FROM Canchas
        WHERE id_cancha=id_cancha_p;
    END IF;
END $$

CREATE PROCEDURE sp_get_available_canchas()
BEGIN
    SELECT
        c.id_cancha,
        c.nombre,
        c.tipo_deporte,
        c.precio_por_hora,
        c.estado
    FROM Canchas c
    WHERE c.estado='Disponible'
      AND NOT EXISTS(
          SELECT 1
          FROM Reservas r
          WHERE r.id_cancha=c.id_cancha
            AND r.fecha_reserva=CURDATE()
            AND r.estado_reserva<>'Cancelada'
            AND CURTIME()>=r.hora_inicio
            AND CURTIME()<r.hora_fin
      );
END $$

CREATE PROCEDURE sp_create_reserva(
    IN id_cancha_p INT,
    IN id_user_p VARCHAR(36),
    IN fecha_reserva_p DATE,
    IN hora_inicio_p TIME,
    IN hora_fin_p TIME,
    IN costo_total_p DECIMAL(10,2)
)
BEGIN
    DECLARE cantidad_reservas INT DEFAULT 0;

    IF hora_fin_p<=hora_inicio_p THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT='Error: La hora final debe ser mayor que la hora inicial.';
    END IF;

    SELECT COUNT(*)
    INTO cantidad_reservas
    FROM Reservas
    WHERE id_cancha=id_cancha_p
      AND fecha_reserva=fecha_reserva_p
      AND estado_reserva<>'Cancelada'
      AND hora_inicio_p<hora_fin
      AND hora_fin_p>hora_inicio;

    IF cantidad_reservas>0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT='Error: La cancha no esta disponible en ese horario.';
    ELSE
        INSERT INTO Reservas(
            id_cancha,
            id_user,
            fecha_reserva,
            hora_inicio,
            hora_fin,
            costo_total,
            estado_reserva
        )
        VALUES(
            id_cancha_p,
            id_user_p,
            fecha_reserva_p,
            hora_inicio_p,
            hora_fin_p,
            costo_total_p,
            'Confirmada'
        );
    END IF;
END $$

CREATE PROCEDURE sp_test_crear_reserva_usuario(
    IN user_identifier_p VARCHAR(50),
    IN id_cancha_p INT,
    IN fecha_reserva_p DATE,
    IN hora_inicio_p TIME,
    IN hora_fin_p TIME,
    IN costo_p DECIMAL(10,2)
)
BEGIN
    DECLARE v_id_cliente VARCHAR(36);

    SET v_id_cliente=NULL;

    SELECT id_user
    INTO v_id_cliente
    FROM Users
    WHERE `user`=user_identifier_p
       OR email=user_identifier_p
    LIMIT 1;

    IF v_id_cliente IS NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT='Error: El usuario indicado no existe en la base de datos.';
    ELSE
        CALL sp_create_reserva(
            id_cancha_p,
            v_id_cliente,
            fecha_reserva_p,
            hora_inicio_p,
            hora_fin_p,
            costo_p
        );
    END IF;
END $$

CREATE PROCEDURE sp_get_all_reservas()
BEGIN
    SELECT
        r.id_reserva,
        c.nombre AS cancha,
        c.tipo_deporte,
        CONCAT(u.name,' ',u.lastname) AS cliente,
        r.fecha_reserva,
        r.hora_inicio,
        r.hora_fin,
        r.costo_total,
        r.estado_reserva
    FROM Reservas r
    INNER JOIN Canchas c ON r.id_cancha=c.id_cancha
    INNER JOIN Users u ON r.id_user=u.id_user
    ORDER BY r.fecha_reserva ASC,r.hora_inicio ASC;
END $$

CREATE PROCEDURE sp_get_calendario_reservas()
BEGIN
    WITH RECURSIVE fechas AS(
        SELECT CURDATE() AS fecha
        UNION ALL
        SELECT DATE_ADD(fecha,INTERVAL 1 DAY)
        FROM fechas
        WHERE fecha<DATE_ADD(CURDATE(),INTERVAL 30 DAY)
    )
    SELECT
        f.fecha,
        c.id_cancha,
        c.nombre AS cancha,
        c.tipo_deporte,
        c.precio_por_hora,
        CASE
            WHEN r.id_reserva IS NOT NULL THEN 'No disponible'
            ELSE 'Disponible'
        END AS disponibilidad,
        r.hora_inicio,
        r.hora_fin,
        CASE
            WHEN r.id_reserva IS NOT NULL THEN CONCAT(u.name,' ',u.lastname)
            ELSE 'Sin reserva'
        END AS cliente,
        CASE
            WHEN r.id_reserva IS NOT NULL THEN r.estado_reserva
            ELSE 'Libre'
        END AS estado
    FROM fechas f
    CROSS JOIN Canchas c
    LEFT JOIN Reservas r
        ON r.id_cancha=c.id_cancha
        AND r.fecha_reserva=f.fecha
    LEFT JOIN Users u
        ON r.id_user=u.id_user
    ORDER BY f.fecha ASC,c.nombre ASC,r.hora_inicio ASC;
END $$

CREATE PROCEDURE sp_get_inventario()
BEGIN
    SELECT
        id_inventario,
        nombre_producto,
        categoria,
        cantidad,
        precio_unitario,
        proveedor,
        estado
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

    SELECT role
    INTO rol_gerente
    FROM Users
    WHERE id_user=id_gerente_p
    LIMIT 1;

    IF rol_gerente IS NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT='Error: El usuario no existe.';
    ELSEIF rol_gerente<>'Gerente' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT='Error: Solo un Gerente puede agregar productos al inventario.';
    ELSE
        INSERT INTO Inventario(
            nombre_producto,
            categoria,
            cantidad,
            precio_unitario,
            proveedor,
            estado
        )
        VALUES(
            nombre_producto_p,
            categoria_p,
            cantidad_p,
            precio_unitario_p,
            proveedor_p,
            'Disponible'
        );
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

    SELECT role
    INTO rol_gerente
    FROM Users
    WHERE id_user=id_gerente_p
    LIMIT 1;

    IF rol_gerente IS NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT='Error: El usuario no existe.';
    ELSEIF rol_gerente<>'Gerente' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT='Error: Solo un Gerente puede editar el inventario.';
    ELSE
        UPDATE Inventario
        SET nombre_producto=nombre_producto_p,
            categoria=categoria_p,
            cantidad=cantidad_p,
            precio_unitario=precio_unitario_p,
            proveedor=proveedor_p,
            estado=estado_p
        WHERE id_inventario=id_inventario_p;
    END IF;
END $$

CREATE PROCEDURE sp_delete_inventario(
    IN id_inventario_p INT,
    IN id_gerente_p VARCHAR(36)
)
BEGIN
    DECLARE rol_gerente VARCHAR(20);

    SELECT role
    INTO rol_gerente
    FROM Users
    WHERE id_user=id_gerente_p
    LIMIT 1;

    IF rol_gerente IS NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT='Error: El usuario no existe.';
    ELSEIF rol_gerente<>'Gerente' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT='Error: Solo un Gerente puede eliminar productos del inventario.';
    ELSE
        DELETE FROM Inventario
        WHERE id_inventario=id_inventario_p;
    END IF;
END $$

CREATE PROCEDURE sp_crear_soporte(
    IN id_usuario_p VARCHAR(36),
    IN tipo_problema_p VARCHAR(50),
    IN asunto_p VARCHAR(100),
    IN descripcion_p TEXT
)
BEGIN
    DECLARE rol_usuario VARCHAR(20);

    SELECT role
    INTO rol_usuario
    FROM Users
    WHERE id_user=id_usuario_p
    LIMIT 1;

    IF rol_usuario IS NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT='Error: El usuario no existe.';
    ELSEIF rol_usuario NOT IN('Administrador','Recepcionista') THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT='Error: Solo un Administrador o Recepcionista puede crear reportes de soporte.';
    ELSE
        INSERT INTO SoporteTecnico(
            id_usuario_reporta,
            tipo_problema,
            asunto,
            descripcion,
            estado
        )
        VALUES(
            id_usuario_p,
            tipo_problema_p,
            asunto_p,
            descripcion_p,
            'Pendiente'
        );
    END IF;
END $$

CREATE PROCEDURE sp_get_soporte_gerente(
    IN id_gerente_p VARCHAR(36)
)
BEGIN
    DECLARE rol_gerente VARCHAR(20);

    SELECT role
    INTO rol_gerente
    FROM Users
    WHERE id_user=id_gerente_p
    LIMIT 1;

    IF rol_gerente IS NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT='Error: El usuario no existe.';
    ELSEIF rol_gerente<>'Gerente' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT='Error: Solo un Gerente puede consultar la bandeja de soporte.';
    ELSE
        SELECT
            s.id_soporte,
            CONCAT(u.name,' ',u.lastname) AS reportado_por,
            u.role AS rol_reportante,
            s.tipo_problema,
            s.asunto,
            s.descripcion,
            s.fecha_reporte,
            s.estado,
            s.respuesta_gerente,
            s.fecha_respuesta
        FROM SoporteTecnico s
        INNER JOIN Users u
            ON s.id_usuario_reporta=u.id_user
        ORDER BY
            CASE
                WHEN s.estado='Pendiente' THEN 1
                WHEN s.estado='En revisión' THEN 2
                WHEN s.estado='Resuelto' THEN 3
                ELSE 4
            END,
            s.fecha_reporte DESC;
    END IF;
END $$

CREATE PROCEDURE sp_responder_soporte(
    IN id_soporte_p INT,
    IN respuesta_p TEXT,
    IN estado_p VARCHAR(20),
    IN id_gerente_p VARCHAR(36)
)
BEGIN
    DECLARE rol_gerente VARCHAR(20);

    SELECT role
    INTO rol_gerente
    FROM Users
    WHERE id_user=id_gerente_p
    LIMIT 1;

    IF rol_gerente IS NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT='Error: El usuario no existe.';
    ELSEIF rol_gerente<>'Gerente' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT='Error: Solo un Gerente puede responder reportes de soporte.';
    ELSEIF estado_p NOT IN('Pendiente','En revisión','Resuelto','Cerrado') THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT='Error: El estado indicado no es valido.';
    ELSE
        UPDATE SoporteTecnico
        SET respuesta_gerente=respuesta_p,
            estado=estado_p,
            fecha_respuesta=CURRENT_TIMESTAMP
        WHERE id_soporte=id_soporte_p;
    END IF;
END $$

CREATE PROCEDURE sp_get_mis_reportes_soporte(
    IN id_usuario_p VARCHAR(36)
)
BEGIN
    SELECT
        s.id_soporte,
        s.tipo_problema,
        s.asunto,
        s.descripcion,
        s.fecha_reporte,
        s.estado,
        s.respuesta_gerente,
        s.fecha_respuesta
    FROM SoporteTecnico s
    WHERE s.id_usuario_reporta=id_usuario_p
    ORDER BY s.fecha_reporte DESC;
END $$

DELIMITER ;

INSERT INTO Users(
    name,
    lastname,
    email,
    `user`,
    password,
    role,
    phone,
    id_user
)
VALUES(
    'Cristofer',
    'Ramos',
    'cristoferramos@sportlife.gerencia.com',
    'GerenteCR',
    '@gerente#1',
    'Gerente',
    NULL,
    UUID()
);

SET @id_gerente=(
    SELECT id_user
    FROM Users
    WHERE `user`='GerenteCR'
    AND role='Gerente'
    LIMIT 1
);

CALL sp_create_users(
    'Victor',
    'Alvarez',
    'victoralvarez@sportlife.administracion.com',
    'AdministradorVA',
    '@administrador#1',
    'Administrador',
    @id_gerente
);

CALL sp_create_users(
    'Pablo',
    'Rosales',
    'pablorosales@sportlife.recepcion.com',
    'RecepcionistaPR',
    '@recepcionista#1',
    'Recepcionista',
    @id_gerente
);

CALL sp_register_customer(
    'Kenneth',
    'Velasquez',
    'kenneth_12@gmail.com',
    'KVBryan',
    '@usuario#1',
    '55501234'
);

CALL sp_register_customer(
    'Joaquin',
    'Garcia',
    'joaquin_8@gmail.com',
    'JGabriel',
    '@usuario#2',
    '44332211'
);

SET @id_admin=(
    SELECT id_user
    FROM Users
    WHERE `user`='AdministradorVA'
    AND role='Administrador'
    LIMIT 1
);

SET @id_gerente=(
    SELECT id_user
    FROM Users
    WHERE `user`='GerenteCR'
    AND role='Gerente'
    LIMIT 1
);

CALL sp_create_cancha(
    'Cancha Fútbol 1',
    'Fútbol',
    200.00,
    @id_admin
);

CALL sp_create_cancha(
    'Cancha Fútbol 2',
    'Fútbol',
    200.00,
    @id_admin
);

CALL sp_create_cancha(
    'Cancha Fútbol 3',
    'Fútbol',
    200.00,
    @id_admin
);

CALL sp_create_cancha(
    'Cancha Tenis 1',
    'Tenis',
    150.00,
    @id_admin
);

CALL sp_create_cancha(
    'Cancha Tenis 2',
    'Tenis',
    150.00,
    @id_admin
);

CALL sp_create_cancha(
    'Cancha Tenis 3',
    'Tenis',
    150.00,
    @id_admin
);

CALL sp_create_cancha(
    'Cancha Basquet 1',
    'Basquet',
    150.00,
    @id_admin
);

CALL sp_create_cancha(
    'Cancha Basquet 2',
    'Basquet',
    150.00,
    @id_admin
);

CALL sp_create_cancha(
    'Cancha Basquet 3',
    'Basquet',
    150.00,
    @id_admin
);

CALL sp_create_inventario(
    'Balones de Fútbol',
    'Equipamiento',
    15,
    175.00,
    'Sport Equipment GT',
    @id_gerente
);

CALL sp_create_inventario(
    'Balones de Básquetbol',
    'Equipamiento',
    12,
    150.00,
    'Deportes Guatemala',
    @id_gerente
);

CALL sp_create_inventario(
    'Pelotas de Tenis',
    'Equipamiento',
    40,
    35.00,
    'Tennis Store GT',
    @id_gerente
);

CALL sp_create_inventario(
    'Redes para Cancha',
    'Accesorios',
    8,
    250.00,
    'Sport Equipment GT',
    @id_gerente
);

CALL sp_create_inventario(
    'Conos de Entrenamiento',
    'Entrenamiento',
    30,
    25.00,
    'Deportes Guatemala',
    @id_gerente
);

CALL sp_test_crear_reserva_usuario(
    'JGabriel',
    1,
    CURDATE(),
    '08:00:00',
    '10:00:00',
    400.00
);

CALL sp_test_crear_reserva_usuario(
    'KVBryan',
    2,
    CURDATE(),
    '14:00:00',
    '16:00:00',
    400.00
);

CALL sp_test_crear_reserva_usuario(
    'JGabriel',
    1,
    DATE_ADD(CURDATE(),INTERVAL 1 DAY),
    '10:00:00',
    '12:00:00',
    400.00
);

CALL sp_test_crear_reserva_usuario(
    'KVBryan',
    2,
    DATE_ADD(CURDATE(),INTERVAL 2 DAY),
    '16:00:00',
    '18:00:00',
    400.00
);

CALL sp_crear_soporte(
    @id_admin,
    'Falla técnica',
    'Problema en la pantalla de reservas',
    'La pantalla de reservas no muestra correctamente las canchas disponibles.'
);

CALL sp_crear_soporte(
    (
        SELECT id_user
        FROM Users
        WHERE `user`='RecepcionistaPR'
        LIMIT 1
    ),
    'Sistema',
    'Error al registrar reserva',
    'Al intentar registrar una reserva aparece un mensaje de error.'
);

CALL sp_get_administrative_staff();
CALL sp_get_registered_customers();
CALL sp_get_all_users();

CALL sp_get_calendario_reservas();
CALL sp_get_available_canchas();
CALL sp_get_all_reservas();
CALL sp_get_inventario();
CALL sp_get_soporte_gerente(@id_gerente);