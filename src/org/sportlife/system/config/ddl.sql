DROP DATABASE IF EXISTS renta_de_canchas_sportlife_in4am;

CREATE DATABASE renta_de_canchas_sportlife_in4am;

USE renta_de_canchas_sportlife_in4am;

DELIMITER //

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
    CONSTRAINT fk_reserva_cancha
        FOREIGN KEY (id_cancha)
        REFERENCES Canchas(id_cancha),
    CONSTRAINT fk_reserva_user
        FOREIGN KEY (id_user)
        REFERENCES Users(id_user)
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
    CONSTRAINT fk_soporte_usuario
        FOREIGN KEY (id_usuario_reporta)
        REFERENCES Users(id_user)
);

CREATE PROCEDURE sp_crear_usuario(
    IN p_name VARCHAR(100),
    IN p_lastname VARCHAR(100),
    IN p_email VARCHAR(150),
    IN p_user VARCHAR(100),
    IN p_password VARCHAR(255),
    IN p_role VARCHAR(50),
    IN p_phone VARCHAR(20)
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
        p_name,
        p_lastname,
        p_email,
        p_user,
        p_password,
        p_role,
        p_phone,
        UUID()
    );
END //

CREATE PROCEDURE sp_check_user_exists_strict(
    IN p_username_or_email VARCHAR(100)
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
    WHERE `user` = p_username_or_email
       OR email = p_username_or_email
    LIMIT 1;
END //

CREATE PROCEDURE sp_verify_user_password(
    IN p_username_or_email VARCHAR(100),
    IN p_password VARCHAR(255)
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
    WHERE (`user` = p_username_or_email
       OR email = p_username_or_email)
      AND password = p_password
    LIMIT 1;
END //

CREATE PROCEDURE sp_register_customer(
    IN p_name VARCHAR(50),
    IN p_lastname VARCHAR(50),
    IN p_email VARCHAR(50),
    IN p_user VARCHAR(25),
    IN p_password VARCHAR(35),
    IN p_phone VARCHAR(10)
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
        p_name,
        p_lastname,
        p_email,
        p_user,
        p_password,
        'User',
        p_phone,
        UUID()
    );
END //

CREATE PROCEDURE sp_crear_cancha(
    IN p_nombre VARCHAR(100),
    IN p_tipo_deporte VARCHAR(50),
    IN p_precio_por_hora DECIMAL(10,2),
    IN p_estado VARCHAR(50)
)
BEGIN
    INSERT INTO Canchas(
        nombre,
        tipo_deporte,
        precio_por_hora,
        estado
    )
    VALUES(
        p_nombre,
        p_tipo_deporte,
        p_precio_por_hora,
        p_estado
    );
END //

CREATE PROCEDURE sp_crear_inventario(
    IN p_nombre_producto VARCHAR(100),
    IN p_categoria VARCHAR(100),
    IN p_cantidad INT,
    IN p_precio_unitario DECIMAL(10,2),
    IN p_proveedor VARCHAR(100),
    IN p_estado VARCHAR(50)
)
BEGIN
    INSERT INTO Inventario(
        nombre_producto,
        categoria,
        cantidad,
        precio_unitario,
        proveedor,
        estado
    )
    VALUES(
        p_nombre_producto,
        p_categoria,
        p_cantidad,
        p_precio_unitario,
        p_proveedor,
        p_estado
    );
END //

CREATE PROCEDURE sp_crear_reserva(
    IN p_id_cancha INT,
    IN p_usuario VARCHAR(100),
    IN p_fecha_reserva DATE,
    IN p_hora_inicio TIME,
    IN p_hora_fin TIME,
    IN p_costo_total DECIMAL(10,2),
    IN p_estado_reserva VARCHAR(50)
)
BEGIN
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
        p_id_cancha,
        (
            SELECT id_user
            FROM Users
            WHERE `user` = p_usuario
            LIMIT 1
        ),
        p_fecha_reserva,
        p_hora_inicio,
        p_hora_fin,
        p_costo_total,
        p_estado_reserva
    );
END //

CREATE PROCEDURE sp_crear_soporte(
    IN p_usuario VARCHAR(100),
    IN p_tipo_problema VARCHAR(100),
    IN p_asunto VARCHAR(200),
    IN p_descripcion TEXT,
    IN p_estado VARCHAR(50)
)
BEGIN
    INSERT INTO SoporteTecnico(
        id_usuario_reporta,
        tipo_problema,
        asunto,
        descripcion,
        estado
    )
    VALUES(
        (
            SELECT id_user
            FROM Users
            WHERE `user` = p_usuario
            LIMIT 1
        ),
        p_tipo_problema,
        p_asunto,
        p_descripcion,
        p_estado
    );
END //

CREATE PROCEDURE sp_mostrar_usuarios()
BEGIN
    SELECT * FROM Users;
END //

CREATE PROCEDURE sp_mostrar_canchas()
BEGIN
    SELECT * FROM Canchas;
END //

CREATE PROCEDURE sp_mostrar_reservas()
BEGIN
    SELECT * FROM Reservas;
END //

CREATE PROCEDURE sp_mostrar_inventario()
BEGIN
    SELECT * FROM Inventario;
END //

CREATE PROCEDURE sp_mostrar_soporte()
BEGIN
    SELECT * FROM SoporteTecnico;
END //

CREATE PROCEDURE sp_mostrar_reservas_detalladas()
BEGIN
    SELECT
        r.id_reserva,
        c.nombre AS cancha,
        c.tipo_deporte,
        CONCAT(u.name, ' ', u.lastname) AS cliente,
        u.`user` AS usuario,
        r.fecha_reserva,
        r.hora_inicio,
        r.hora_fin,
        r.costo_total,
        r.estado_reserva
    FROM Reservas r
    INNER JOIN Canchas c
        ON r.id_cancha = c.id_cancha
    INNER JOIN Users u
        ON r.id_user = u.id_user
    ORDER BY
        r.fecha_reserva ASC,
        r.hora_inicio ASC;
END //

CREATE PROCEDURE sp_mostrar_usuarios_administrativos()
BEGIN
    SELECT
        id_user,
        name,
        lastname,
        email,
        `user`,
        role
    FROM Users
    WHERE role IN(
        'Gerente',
        'Administrador',
        'Recepcionista'
    );
END //

CREATE PROCEDURE sp_mostrar_clientes()
BEGIN
    SELECT
        id_user,
        name,
        lastname,
        email,
        `user`,
        phone,
        role
    FROM Users
    WHERE role = 'User';
END //

CREATE PROCEDURE sp_mostrar_canchas_disponibles()
BEGIN
    SELECT
        id_cancha,
        nombre,
        tipo_deporte,
        precio_por_hora,
        estado
    FROM Canchas
    WHERE estado = 'Disponible';
END //

CREATE PROCEDURE sp_calendario_mes(
    IN p_anio INT,
    IN p_mes INT
)
BEGIN
    SELECT
        r.id_reserva,
        r.fecha_reserva,
        r.hora_inicio,
        r.hora_fin,
        c.id_cancha,
        c.nombre AS cancha,
        c.tipo_deporte,
        CONCAT(u.name, ' ', u.lastname) AS cliente,
        u.`user` AS usuario,
        r.costo_total,
        r.estado_reserva
    FROM Reservas r
    INNER JOIN Canchas c
        ON r.id_cancha = c.id_cancha
    INNER JOIN Users u
        ON r.id_user = u.id_user
    WHERE YEAR(r.fecha_reserva) = p_anio
      AND MONTH(r.fecha_reserva) = p_mes
    ORDER BY
        r.fecha_reserva ASC,
        r.hora_inicio ASC;
END //

CREATE PROCEDURE sp_actualizar_usuario(
    IN p_usuario VARCHAR(100),
    IN p_phone VARCHAR(20)
)
BEGIN
    UPDATE Users
    SET phone = p_phone
    WHERE `user` = p_usuario;
END //

CREATE PROCEDURE sp_actualizar_cancha(
    IN p_id_cancha INT,
    IN p_precio DECIMAL(10,2)
)
BEGIN
    UPDATE Canchas
    SET precio_por_hora = p_precio
    WHERE id_cancha = p_id_cancha;
END //

CREATE PROCEDURE sp_actualizar_inventario(
    IN p_id_inventario INT,
    IN p_cantidad INT
)
BEGIN
    UPDATE Inventario
    SET cantidad = p_cantidad
    WHERE id_inventario = p_id_inventario;
END //

CREATE PROCEDURE sp_actualizar_reserva(
    IN p_id_reserva INT,
    IN p_id_cancha INT,
    IN p_usuario VARCHAR(100),
    IN p_fecha_reserva DATE,
    IN p_hora_inicio TIME,
    IN p_hora_fin TIME,
    IN p_costo_total DECIMAL(10,2),
    IN p_estado_reserva VARCHAR(50)
)
BEGIN
    UPDATE Reservas
    SET
        id_cancha = p_id_cancha,
        id_user = (
            SELECT id_user
            FROM Users
            WHERE `user` = p_usuario
            LIMIT 1
        ),
        fecha_reserva = p_fecha_reserva,
        hora_inicio = p_hora_inicio,
        hora_fin = p_hora_fin,
        costo_total = p_costo_total,
        estado_reserva = p_estado_reserva
    WHERE id_reserva = p_id_reserva;
END //

CREATE PROCEDURE sp_actualizar_soporte(
    IN p_id_soporte INT,
    IN p_respuesta TEXT,
    IN p_estado VARCHAR(50)
)
BEGIN
    UPDATE SoporteTecnico
    SET
        respuesta_gerente = p_respuesta,
        estado = p_estado,
        fecha_respuesta = CURRENT_TIMESTAMP
    WHERE id_soporte = p_id_soporte;
END //

CREATE PROCEDURE sp_eliminar_inventario(
    IN p_id_inventario INT
)
BEGIN
    DELETE FROM Inventario
    WHERE id_inventario = p_id_inventario;
END //

CREATE PROCEDURE sp_eliminar_cancha(
    IN p_id_cancha INT
)
BEGIN
    DELETE FROM Canchas
    WHERE id_cancha = p_id_cancha;
END //

CREATE PROCEDURE sp_eliminar_reserva(
    IN p_id_reserva INT
)
BEGIN
    DELETE FROM Reservas
    WHERE id_reserva = p_id_reserva;
END //

DELIMITER ;