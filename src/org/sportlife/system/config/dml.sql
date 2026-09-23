USE renta_de_canchas_sportlife_in4am;

CALL sp_crear_usuario(
    'Cristofer',
    'Ramos',
    'cristoferramos@sportlife.gerencia.com',
    'GerenteCR',
    '@gerente#1',
    'Gerente',
    NULL
);

CALL sp_crear_usuario(
    'Victor',
    'Alvarez',
    'victoralvarez@sportlife.administracion.com',
    'AdministradorVA',
    '@administrador#1',
    'Administrador',
    NULL
);

CALL sp_crear_usuario(
    'Pablo',
    'Rosales',
    'pablorosales@sportlife.recepcion.com',
    'RecepcionistaPR',
    '@recepcionista#1',
    'Recepcionista',
    NULL
);

CALL sp_crear_usuario(
    'Kenneth',
    'Velasquez',
    'kenneth_12@gmail.com',
    'KVBryan',
    '@usuario#1',
    'User',
    '55501234'
);

CALL sp_crear_usuario(
    'Joaquin',
    'Garcia',
    'joaquin_8@gmail.com',
    'JGabriel',
    '@usuario#2',
    'User',
    '44332211'
);

CALL sp_crear_cancha(
    'Cancha Fútbol 1',
    'Fútbol',
    200.00,
    'Disponible'
);

CALL sp_crear_cancha(
    'Cancha Fútbol 2',
    'Fútbol',
    200.00,
    'Disponible'
);

CALL sp_crear_cancha(
    'Cancha Fútbol 3',
    'Fútbol',
    200.00,
    'Disponible'
);

CALL sp_crear_cancha(
    'Cancha Tenis 1',
    'Tenis',
    150.00,
    'Disponible'
);

CALL sp_crear_cancha(
    'Cancha Tenis 2',
    'Tenis',
    150.00,
    'Disponible'
);

CALL sp_crear_cancha(
    'Cancha Tenis 3',
    'Tenis',
    150.00,
    'Disponible'
);

CALL sp_crear_cancha(
    'Cancha Basquet 1',
    'Basquet',
    150.00,
    'Disponible'
);

CALL sp_crear_cancha(
    'Cancha Basquet 2',
    'Basquet',
    150.00,
    'Disponible'
);

CALL sp_crear_cancha(
    'Cancha Basquet 3',
    'Basquet',
    150.00,
    'Disponible'
);

CALL sp_crear_inventario(
    'Balones de Fútbol',
    'Equipamiento',
    15,
    175.00,
    'Sport Equipment GT',
    'Disponible'
);

CALL sp_crear_inventario(
    'Balones de Básquetbol',
    'Equipamiento',
    12,
    150.00,
    'Deportes Guatemala',
    'Disponible'
);

CALL sp_crear_inventario(
    'Pelotas de Tenis',
    'Equipamiento',
    40,
    35.00,
    'Tennis Store GT',
    'Disponible'
);

CALL sp_crear_inventario(
    'Redes para Cancha',
    'Accesorios',
    8,
    250.00,
    'Sport Equipment GT',
    'Disponible'
);

CALL sp_crear_inventario(
    'Conos de Entrenamiento',
    'Entrenamiento',
    30,
    25.00,
    'Deportes Guatemala',
    'Disponible'
);

CALL sp_crear_reserva(
    1,
    'JGabriel',
    CURDATE(),
    '08:00:00',
    '10:00:00',
    400.00,
    'Confirmada'
);

CALL sp_crear_reserva(
    2,
    'KVBryan',
    CURDATE(),
    '14:00:00',
    '16:00:00',
    400.00,
    'Confirmada'
);

CALL sp_crear_reserva(
    3,
    'JGabriel',
    CURDATE(),
    '18:00:00',
    '20:00:00',
    400.00,
    'Confirmada'
);

CALL sp_crear_reserva(
    4,
    'KVBryan',
    DATE_ADD(CURDATE(), INTERVAL 1 DAY),
    '09:00:00',
    '11:00:00',
    300.00,
    'Confirmada'
);

CALL sp_crear_reserva(
    5,
    'JGabriel',
    DATE_ADD(CURDATE(), INTERVAL 2 DAY),
    '15:00:00',
    '17:00:00',
    300.00,
    'Confirmada'
);

CALL sp_crear_reserva(
    6,
    'KVBryan',
    DATE_ADD(CURDATE(), INTERVAL 3 DAY),
    '10:00:00',
    '12:00:00',
    300.00,
    'Confirmada'
);

CALL sp_crear_reserva(
    7,
    'JGabriel',
    DATE_ADD(CURDATE(), INTERVAL 5 DAY),
    '08:00:00',
    '10:00:00',
    300.00,
    'Confirmada'
);

CALL sp_crear_reserva(
    8,
    'KVBryan',
    DATE_ADD(CURDATE(), INTERVAL 7 DAY),
    '14:00:00',
    '16:00:00',
    300.00,
    'Confirmada'
);

CALL sp_crear_reserva(
    1,
    'JGabriel',
    DATE_ADD(CURDATE(), INTERVAL 10 DAY),
    '16:00:00',
    '18:00:00',
    400.00,
    'Confirmada'
);

CALL sp_crear_reserva(
    2,
    'KVBryan',
    DATE_ADD(CURDATE(), INTERVAL 12 DAY),
    '10:00:00',
    '12:00:00',
    400.00,
    'Confirmada'
);

CALL sp_crear_reserva(
    3,
    'JGabriel',
    DATE_ADD(CURDATE(), INTERVAL 15 DAY),
    '12:00:00',
    '14:00:00',
    400.00,
    'Confirmada'
);

CALL sp_crear_reserva(
    4,
    'KVBryan',
    DATE_ADD(CURDATE(), INTERVAL 18 DAY),
    '08:00:00',
    '10:00:00',
    300.00,
    'Confirmada'
);

CALL sp_crear_reserva(
    7,
    'JGabriel',
    DATE_ADD(CURDATE(), INTERVAL 20 DAY),
    '16:00:00',
    '18:00:00',
    300.00,
    'Confirmada'
);

CALL sp_crear_reserva(
    8,
    'KVBryan',
    DATE_ADD(CURDATE(), INTERVAL 25 DAY),
    '18:00:00',
    '20:00:00',
    300.00,
    'Confirmada'
);

CALL sp_crear_soporte(
    'AdministradorVA',
    'Falla técnica',
    'Problema en la pantalla de reservas',
    'La pantalla de reservas no muestra correctamente las canchas disponibles.',
    'Pendiente'
);

CALL sp_crear_soporte(
    'RecepcionistaPR',
    'Sistema',
    'Error al registrar reserva',
    'Al intentar registrar una reserva aparece un mensaje de error.',
    'Pendiente'
);

CALL sp_mostrar_usuarios();

CALL sp_mostrar_canchas();

CALL sp_mostrar_reservas();

CALL sp_mostrar_inventario();

CALL sp_mostrar_soporte();

CALL sp_mostrar_reservas_detalladas();

CALL sp_mostrar_usuarios_administrativos();

CALL sp_mostrar_clientes();

CALL sp_mostrar_canchas_disponibles();

CALL sp_calendario_mes(
    YEAR(CURDATE()),
    MONTH(CURDATE())
);
