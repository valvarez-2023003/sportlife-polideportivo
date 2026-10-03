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
--  CANCHAS
-- ============================================================
CALL sp_create_cancha('F1', 'Cancha Fútbol 1 Norte',  'Fútbol',  TRUE,  200.00, @id_admin);
CALL sp_create_cancha('F2', 'Cancha Fútbol 2 Sur',    'Fútbol',  FALSE, 200.00, @id_admin);
CALL sp_create_cancha('F3', 'Cancha Fútbol 3 Central','Fútbol',  TRUE,  200.00, @id_admin);
CALL sp_create_cancha('T1', 'Cancha Tenis 1',         'Tenis',   FALSE, 150.00, @id_admin);
CALL sp_create_cancha('T2', 'Cancha Tenis 2',         'Tenis',   TRUE,  150.00, @id_admin);
CALL sp_create_cancha('T3', 'Cancha Tenis 3',         'Tenis',   FALSE, 150.00, @id_admin);
CALL sp_create_cancha('B1', 'Cancha Basquet 1',       'Basquet', TRUE,  150.00, @id_admin);
CALL sp_create_cancha('B2', 'Cancha Basquet 2',       'Basquet', FALSE, 150.00, @id_admin);
CALL sp_create_cancha('B3', 'Cancha Basquet 3',       'Basquet', TRUE,  150.00, @id_admin);
CALL sp_create_cancha('V1', 'Cancha Vóley 1', 'Vóley', TRUE,  150.00, @id_admin);
CALL sp_create_cancha('V2', 'Cancha Vóley 2', 'Vóley', FALSE, 150.00, @id_admin);
CALL sp_create_cancha('V3', 'Cancha Vóley 3', 'Vóley', TRUE,  150.00, @id_admin);

-- ============================================================
--  RESERVAS DE PRUEBA
-- ============================================================
SET @id_f1 = (SELECT id_cancha FROM Canchas WHERE codigo='F1' LIMIT 1);
SET @id_f2 = (SELECT id_cancha FROM Canchas WHERE codigo='F2' LIMIT 1);

CALL sp_test_crear_reserva_usuario('JGabriel', @id_f1, CURDATE(), '08:00:00', '10:00:00');
CALL sp_test_crear_reserva_usuario('KVBryan',  @id_f2, CURDATE(), '14:00:00', '16:00:00');
CALL sp_test_crear_reserva_usuario('JGabriel', @id_f1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '10:00:00', '12:00:00');
CALL sp_test_crear_reserva_usuario('KVBryan',  @id_f2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '16:00:00', '18:00:00');