DROP DATABASE IF EXISTS renta_de_canchas_sportlife_in4am;
CREATE DATABASE renta_de_canchas_sportlife_in4am;
USE renta_de_canchas_sportlife_in4am;

CREATE TABLE Users(
    name VARCHAR(50) NOT NULL,
    lastname VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL,
    user VARCHAR(25) NOT NULL,
    password VARCHAR(35) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'User',
    phone VARCHAR(10) NULL,
    id_user VARCHAR(36) NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id_user)
);

DELIMITER $$
CREATE PROCEDURE sp_create_users(
    IN name_p VARCHAR(50),
    IN lastname_p VARCHAR(50),
    IN email_p VARCHAR(50),
    IN user_p VARCHAR(20),
    IN password_p VARCHAR(35),
    IN role_p VARCHAR(20)
)
BEGIN
    INSERT INTO Users(name, lastname, email, user, password, role, phone, id_user)
    VALUES (name_p, lastname_p, email_p, user_p, password_p, role_p, NULL, UUID());
END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_register_customer(
    IN name_p VARCHAR(50),
    IN lastname_p VARCHAR(50),
    IN email_p VARCHAR(50),
    IN user_p VARCHAR(25),
    IN password_p VARCHAR(35),
    IN phone_p VARCHAR(10)
)
BEGIN
    INSERT INTO Users(name, lastname, email, user, password, role, phone, id_user)
    VALUES (name_p, lastname_p, email_p, user_p, password_p, 'User', phone_p, UUID());
END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_get_administrative_staff()
BEGIN
    SELECT id_user, name, lastname, email, user, password, role 
    FROM Users
    WHERE role IN ('Gerente', 'Administrador', 'Recepcionista');
END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_get_registered_customers()
BEGIN
    SELECT id_user, name, lastname, email, user, password, phone, role 
    FROM Users
    WHERE role = 'User';
END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_get_all_users()
BEGIN
    SELECT id_user, name, lastname, email, user, password, phone, role 
    FROM Users;
END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_check_user_exists_strict(
    IN user_or_email_p VARCHAR(50)
)
BEGIN
    SELECT id_user, name, lastname, email, user, role
    FROM Users
    WHERE BINARY user = user_or_email_p OR BINARY email = user_or_email_p;
END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_verify_user_password(
    IN user_or_email_p VARCHAR(50),
    IN password_p VARCHAR(35)
)
BEGIN
    SELECT id_user, name, lastname, email, user, role
    FROM Users
    WHERE (BINARY user = user_or_email_p OR BINARY email = user_or_email_p)
      AND BINARY password = password_p;
END $$
DELIMITER ;

CALL sp_create_users("Cristofer", "Ramos", "cristoferramos@sportlife.gerencia.com", "GerenteCR", "@gerente#1", "Gerente");
CALL sp_create_users("Victor", "Alvarez", "victoralvarez@sportlife.administracion.com", "AdministradorVA", "@administrador#1", "Administrador");
CALL sp_create_users("Pablo", "Rosales", "pablorosales@sportlife.recepcion.com", "RecepcionistaPR", "@recepcionista#1", "Recepcionista");

CALL sp_register_customer("Kenneth", "Velasquez", "kenneth_12@gmail.com", "KVBryan", "@usuario#1", "55501234");
CALL sp_register_customer("Joaquin", "Garcia", "joaquin_8@gmail.com", "JGabriel", "@usuario#2", "44332211");

CALL sp_get_administrative_staff();

CALL sp_get_registered_customers();

CALL sp_get_all_users();