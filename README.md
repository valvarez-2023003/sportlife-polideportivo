# \# SPORTLIFE - Sistema de Gestión de Polideportivo

# 

# Sistema de gestión de reservas para un complejo deportivo, desarrollado en Java + JavaFX con MySQL. Permite administrar el catálogo de canchas, gestionar reservas, aprobar solicitudes de clientes y mantener un control del personal.

# 

# \## Tecnologías

# 

# \- Java 21 (JDK)

# \- JavaFX 21 (interfaz gráfica)

# \- MySQL 8+ (base de datos)

# \- NetBeans (IDE + Ant para build)

# \- MySQL Connector/J 9.6.0 (driver JDBC)

# 

# \## Módulos

# 

# El sistema tiene 4 roles con interfaces diferenciadas:

# 

# \### Gerente

# \- INICIO: Ver el catálogo de canchas con filtro por estado

# \- GESTIÓN PERSONAL: Crear, editar y eliminar administradores y recepcionistas

# \- CALENDARIO: Ver las reservas confirmadas

# 

# \### Administrador

# \- INICIO: Ver las canchas con filtro por estado

# \- EDITAR CANCHAS: CRUD completo del catálogo de canchas

# \- CALENDARIO: Ver las reservas confirmadas

# 

# \### Recepcionista

# \- INICIO: Ver y gestionar las solicitudes de reserva pendientes (confirmar/rechazar)

# \- RESERVAS: Ver todas las reservas, filtrar, editar, cancelar y eliminar

# \- CLIENTES: Lista de clientes registrados

# \- CALENDARIO: Ver las reservas confirmadas

# 

# \### Cliente

# \- INICIO: Ver el historial de sus reservas y emitir comprobantes

# \- NUEVA RESERVA: Solicitar una reserva eligiendo cancha, fecha y horario

# 

# \## Requisitos

# 

# \- JDK 21 instalado

# \- JavaFX SDK 21 descargado

# \- MySQL 8 o superior corriendo

# \- NetBeans (o cualquier IDE compatible con Ant)

# \- Driver mysql-connector-j-9.6.0.jar

# 

# \## Instalación

# 

# \### 1. Clonar el repositorio

# 

# &#x20;   git clone https://github.com/valvarez-2023003/sportlife-polideportivo.git

# &#x20;   cd sportlife-polideportivo

# 

# \### 2. Crear la base de datos

# 

# Abre MySQL Workbench y ejecuta en orden:

# 

# 1\. src/org/sportlife/system/config/01\_ddl\_sportlife.sql

# &#x20;  Crea la base de datos, las tablas y todos los procedimientos almacenados.

# 

# 2\. src/org/sportlife/system/config/02\_dml\_sportlife.sql

# &#x20;  Inserta los datos de prueba (usuarios, canchas, reservas).

# 

# \### 3. Configurar credenciales de la base de datos

# 

# Edita el archivo src/org/sportlife/system/config/Enviroment.java con tus datos:

# 

# &#x20;   public class Enviroment {

# &#x20;       public static final String LOCATION\_SERVICE = "localhost:3306";

# &#x20;       public static final String DATA\_BASE = "renta\_de\_canchas\_sportlife\_in4am";

# &#x20;       public static final String USER = "tu\_usuario";

# &#x20;       public static final String PASSWORD = "tu\_contraseña";

# &#x20;   }

# 

# \### 4. Configurar librerías en NetBeans

# 

# 1\. Abre el proyecto en NetBeans.

# 2\. Ve a Properties > Libraries > Compile.

# 3\. Añade:

# &#x20;  - JavaFX 21 (con javafx.base.jar, javafx.controls.jar, javafx.fxml.jar, javafx.graphics.jar)

# &#x20;  - mysql-connector-j-9.6.0.jar

# 4\. Ve a Properties > Run y añade en VM Options:

# 

# &#x20;   --module-path "ruta/a/javafx-sdk-21/lib" --add-modules javafx.controls,javafx.fxml

# 

# \### 5. Ejecutar

# 

# En NetBeans: Run > Run Project (F6).

# 

# \## Usuarios de Prueba

# 

# | Rol | Usuario | Contraseña | Email |

# |---|---|---|---|

# | Gerente | GerenteCR | @gerente#1 | cristoferramos@sportlife.gerencia.com |

# | Administrador | AdministradorVA | @administrador#1 | victoralvarez@sportlife.administracion.com |

# | Recepcionista | RecepcionistaPR | @recepcionista#1 | pablorosales@sportlife.recepcion.com |

# | Cliente | KVBryan | @usuario#1 | kenneth\_12@gmail.com |

# | Cliente | JGabriel | @usuario#2 | joaquin\_8@gmail.com |

# 

# \## Ciclo de Vida de una Reserva

# 

# Una reserva pasa por los siguientes estados:

# 

# \- Pendiente: El cliente solicitó la reserva, aún no ha sido revisada.

# \- Confirmada: El recepcionista aprobó la solicitud.

# \- Rechazada: El recepcionista NO aprobó la solicitud.

# \- Cancelada: Una reserva previamente confirmada fue anulada.

# 

# Flujo:

# 

# &#x20;   Pendiente -> Confirmada (botón Confirmar del recepcionista)

# &#x20;   Pendiente -> Rechazada  (botón Rechazar del recepcionista)

# &#x20;   Confirmada -> Cancelada (botón Cancelar del recepcionista)

# 

# \## Estructura del Proyecto

# 

# &#x20;   sportlife-polideportivo/

# &#x20;   ├── src/

# &#x20;   │   └── org/sportlife/system/

# &#x20;   │       ├── ClasePrincipal.java          # Punto de entrada

# &#x20;   │       ├── config/

# &#x20;   │       │   ├── ConexionDB.java          # Singleton de conexión a MySQL

# &#x20;   │       │   ├── Enviroment.java          # Credenciales (no se sube al repo)

# &#x20;   │       │   ├── 01\_ddl\_sportlife.sql     # Script DDL

# &#x20;   │       │   └── 02\_dml\_sportlife.sql     # Script DML

# &#x20;   │       ├── controller/                  # Controladores de las vistas

# &#x20;   │       ├── model/                       # Modelos (User, Cancha, Reserva)

# &#x20;   │       ├── repository/                  # Acceso a datos (JDBC + SPs)

# &#x20;   │       ├── service/                     # Lógica de negocio

# &#x20;   │       ├── utils/                       # Utilidades (Validations, SessionManager, etc.)

# &#x20;   │       ├── view/                        # Archivos FXML

# &#x20;   │       └── resource/

# &#x20;   │           ├── images/                  # Logos y fondos

# &#x20;   │           └── styles/                  # Hojas CSS

# &#x20;   ├── nbproject/                           # Configuración de NetBeans

# &#x20;   └── README.md

# 

# \## Patrones de Diseño Utilizados

# 

# \- Singleton: ConexionDB, SceneManager, SessionManager

# \- Repository: Acceso a datos encapsulado con SPs de MySQL

# \- Service Layer: Lógica de negocio separada de los controladores

# \- Enum: TipoVista, LoginStatus, RegisterStatus para evitar strings mágicos

# \- MVC: Modelo-Vista-Controlador con FXML

# 

# \## Notas Importantes

# 

# \### Seguridad

# Las contraseñas se almacenan en texto plano para simplificar el proyecto académico.

# En un entorno de producción, se debería implementar hashing con bcrypt o SHA-256.

# 

# \### Validación de datos

# Toda la validación de formularios (email, teléfono, longitud de campos, contraseñas coincidentes) se hace desde Java, antes de enviar los datos a MySQL.

# 

# \### Reglas de negocio

# Las reglas de acceso están validadas tanto en Java como en MySQL (mediante SPs). Por ejemplo:

# \- Solo un Gerente puede crear usuarios.

# \- Solo un Administrador puede crear canchas.

# \- Solo un Recepcionista puede confirmar reservas.

# 

# \## Autor

# 

# Cristofer Ramos

# Proyecto académico - 2026

# 

# \## Licencia

# 

# Proyecto académico sin fines de lucro. Uso educativo.

