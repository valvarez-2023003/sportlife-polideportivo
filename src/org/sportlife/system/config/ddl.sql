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
    id_user VARCHAR(36) NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id_user)
);

DELIMITER $$
CREATE PROCEDURE sp_create_users(
    IN id_user_p VARCHAR(36),
    IN name_p VARCHAR(50),
    IN lastname_p VARCHAR(50),
    IN email_p VARCHAR(50),
    IN user_p VARCHAR(20),
    IN password_p VARCHAR(35),
    IN role_p VARCHAR(20)
)
BEGIN
    INSERT INTO Users(id_user, name, lastname, email, user, password, role)
    VALUES (id_user_p, name_p, lastname_p, email_p, user_p, password_p, role_p);
END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_get_all_users()
BEGIN
    SELECT id_user, name, lastname, email, user, password, role 
    FROM Users;
END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_update_users(
    IN id_user_p VARCHAR(36),
    IN name_p VARCHAR(50),
    IN lastname_p VARCHAR(50),
    IN email_p VARCHAR(50),
    IN user_p VARCHAR(25),
    IN password_p VARCHAR(35),
    IN role_p VARCHAR(20)
)
BEGIN
    UPDATE Users
    SET name = name_p, lastname = lastname_p, email = email_p,
        user = user_p, password = password_p, role = role_p
    WHERE id_user = id_user_p;
END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_delete_users(
    IN id_user_p VARCHAR(36)
)
BEGIN
    DELETE FROM Users WHERE id_user = id_user_p;
END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_check_user_exists_strict(
    IN user_or_email_p VARCHAR(50)
)
BEGIN
    SELECT id_user, name, lastname, email, user, role
    FROM Users
    WHERE CAST(user AS BINARY) = CAST(user_or_email_p AS BINARY) 
       OR CAST(email AS BINARY) = CAST(user_or_email_p AS BINARY);
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
    WHERE (CAST(user AS BINARY) = CAST(user_or_email_p AS BINARY) 
        OR CAST(email AS BINARY) = CAST(user_or_email_p AS BINARY))
      AND CAST(password AS BINARY) = CAST(password_p AS BINARY);
END $$
DELIMITER ;

CALL sp_create_users("id-gerente-01", "Gerente", "Gerente_principal", "gerente_sportlife@sport.com", "Gerente_1", "12345678", "Gerente");
CALL sp_create_users("id-admin-01", "Admin", "Principal", "admin_sportlife@sport.com", "Admin_1", "admin1234", "Administrador");
CALL sp_create_users("id-recep-01", "Recepcionista", "Recepcionista", "recepcion_sportlife@sport.com", "Recepcion_1", "recep1234", "Recepcionista");

CALL sp_get_all_users();

CALL sp_delete_users("id-gerente-01");

CALL sp_update_users("id-recep-01", "Joaquin", "Recepcionista", "recepcion_sportlife@sport.com", "Recepcion_1", "recep1234", "Recepcionista"); 
CALL sp_check_user_exists_strict("gerente_sportlife@sport.com");
