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
    IN name_p VARCHAR(50),
    IN lastname_p VARCHAR(50),
    IN email_p VARCHAR(50),
    IN user_p VARCHAR(20),
    IN password_p VARCHAR(35),
    IN role_p VARCHAR(20)
)
BEGIN
    INSERT INTO Users(name, lastname, email, user, password, role, id_user)
    VALUES (name_p, lastname_p, email_p, user_p, password_p, role_p, UUID());
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

CALL sp_create_users("Cristofer","Ramos","cristoferramos@sportlife.gerencia.com","GerenteCR","@gerente#1","Gerente");
CALL sp_create_users("Victor", "Alvarez", "victoralvarez@sportlife.administracion.com", "AdministradorVA", "@administrador#1", "Administrador");
CALL sp_create_users("Pablo", "Rosales", "pablorosales@sportlife.recepcion.com", "RecepcionistaPR", "@recepcionista#1", "Recepcionista");
CALL sp_get_all_users();