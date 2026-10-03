-- ============================================================
--  SPORTLIFE - DML (Data Manipulation Language)
--  Inserta datos de prueba (seed)
-- ============================================================

USE renta_de_canchas_sportlife_in4am;

-- ============================================================
--  USUARIO GERENTE INICIAL
-- ============================================================
INSERT INTO Users(name, lastname, email, `user`, password, role, phone, id_user)
VALUES('Cristofer', 'Ramos', 'cristoferramos@sportlife.gerencia.com',
       'GerenteCR', '@gerente#1', 'Gerente', NULL, UUID());

SET @id_gerente = (SELECT id_user FROM Users WHERE `user`='GerenteCR' AND role='Gerente' LIMIT 1);

-- ============================================================
--  ADMINISTRADOR Y RECEPCIONISTA
-- ============================================================
CALL sp_create_users('Victor', 'Alvarez', 'victoralvarez@sportlife.administracion.com',
                     'AdministradorVA', '@administrador#1', 'Administrador', @id_gerente);

CALL sp_create_users('Pablo', 'Rosales', 'pablorosales@sportlife.recepcion.com',
                     'RecepcionistaPR', '@recepcionista#1', 'Recepcionista', @id_gerente);

SET @id_admin = (SELECT id_user FROM Users WHERE `user`='AdministradorVA' AND role='Administrador' LIMIT 1);

-- ============================================================
--  CLIENTES
-- ============================================================
CALL sp_register_customer('Kenneth', 'Velasquez', 'kenneth_12@gmail.com',
                          'KVBryan', '@usuario#1', '55501234');

CALL sp_register_customer('Joaquin', 'Garcia', 'joaquin_8@gmail.com',
                          'JGabriel', '@usuario#2', '44332211');

-- ============================================================
--  CANCHAS (con código, techada y precio)
-- ============================================================
CALL sp_create_cancha('F1', 'Cancha Fútbol 1 Norte', 'Fútbol', TRUE,  200.00, @id_admin);
CALL sp_create_cancha('F2', 'Cancha Fútbol 2 Sur',   'Fútbol', FALSE, 200.00, @id_admin);
CALL sp_create_cancha('F3', 'Cancha Fútbol 3 Central','Fútbol', TRUE,  200.00, @id_admin);
CALL sp_create_cancha('T1', 'Cancha Tenis 1',         'Tenis',  FALSE, 150.00, @id_admin);
CALL sp_create_cancha('T2', 'Cancha Tenis 2',         'Tenis',  TRUE,  150.00, @id_admin);
CALL sp_create_cancha('T3', 'Cancha Tenis 3',         'Tenis',  FALSE, 150.00, @id_admin);
CALL sp_create_cancha('B1', 'Cancha Basquet 1',       'Basquet',TRUE,  150.00, @id_admin);
CALL sp_create_cancha('B2', 'Cancha Basquet 2',       'Basquet',FALSE, 150.00, @id_admin);
CALL sp_create_cancha('B3', 'Cancha Basquet 3',       'Basquet',TRUE,  150.00, @id_admin);

-- ============================================================
--  INVENTARIO
-- ============================================================
CALL sp_create_inventario('Balones de Fútbol', 'Equipamiento', 15, 175.00, 'Sport Equipment GT', @id_gerente);
CALL sp_create_inventario('Balones de Básquetbol', 'Equipamiento', 12, 150.00, 'Deportes Guatemala', @id_gerente);
CALL sp_create_inventario('Pelotas de Tenis', 'Equipamiento', 40, 35.00, 'Tennis Store GT', @id_gerente);
CALL sp_create_inventario('Redes para Cancha', 'Accesorios', 8, 250.00, 'Sport Equipment GT', @id_gerente);
CALL sp_create_inventario('Conos de Entrenamiento', 'Entrenamiento', 30, 25.00, 'Deportes Guatemala', @id_gerente);

-- ============================================================
--  RESERVAS DE PRUEBA
-- ============================================================
CALL sp_test_crear_reserva_usuario('JGabriel', 1, CURDATE(), '08:00: