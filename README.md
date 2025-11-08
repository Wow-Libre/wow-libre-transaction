# Wow Libre Transaction Service

Microservicio de transacciones para el ecosistema Wow Libre. Maneja pagos, suscripciones, planes, productos y transacciones.

## 📋 Tabla de Contenidos

- [Requisitos](#requisitos)
- [Instalación](#instalación)
- [Configuración](#configuración)
- [Ejecución](#ejecución)
- [API Endpoints](#api-endpoints)
- [Base de Datos](#base-de-datos)
- [Docker](#docker)
- [Desarrollo](#desarrollo)

## 🔧 Requisitos

- **Java 21** (requerido)
- Maven 3.6+
- MySQL 8.0+
- Docker (opcional, para ejecutar con contenedores)
- **Lombok Plugin** (para IDEs: IntelliJ IDEA, Eclipse, VS Code)

## 🚀 Instalación

### 1. Clonar el repositorio

```bash
git clone <repository-url>
cd wow-libre-transaction
```

### 2. Configurar la base de datos

**Opción 1: Usando el script run.sh (Recomendado)**

```bash
# Asegúrate de tener configurado tu archivo .env con las credenciales de BD
./run.sh sql
```

**Opción 2: Manualmente con MySQL**

```bash
mysql -u root -p < src/main/resources/static/script.sql
```

El script `run.sh sql` usa automáticamente las credenciales de tu archivo `.env`, por lo que es la forma más conveniente de ejecutar el SQL.

### 3. Configurar variables de entorno (Local)

Edita `src/main/resources/application.yml` con tus credenciales de base de datos:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/transactions
    username: tu_usuario
    password: tu_contraseña
```

## ⚙️ Configuración

### Variables de Entorno (Producción)

Para producción, configura las siguientes variables de entorno:

```bash
# Base de datos
DB_TRANSACTION_HOST=jdbc:mysql://host:3306/transactions
DB_TRANSACTION_USERNAME=usuario
DB_TRANSACTION_PASSWORD=contraseña

# Servidor
TRANSACTION_SERVER_PORT=8092

# URLs de servicios
HOST_BASE_CORE=http://core-service:8091/core
PAYU_API_URL=https://api.payulatam.com/reports-api/4.0/service.cgi

# JWT
CORE_JWT_SECRET_KEY=tu_secret_key_aqui
```

### Perfiles de Spring

- **local**: Para desarrollo local
- **prod**: Para producción

## ▶️ Ejecución

### Usando el Script de Ejecución (Recomendado)

El proyecto incluye un script `run.sh` que facilita la ejecución:

```bash
# Modo desarrollo (foreground)
./run.sh dev

# Modo desarrollo (background)
./run.sh start

# Ver estado de la aplicación
./run.sh status

# Detener la aplicación
./run.sh stop

# Solo compilar
./run.sh build

# Ejecutar JAR compilado
./run.sh run

# Ejecutar con perfil específico
./run.sh run prod

# Verificar dependencias
./run.sh check

# Ejecutar script SQL
./run.sh sql

# Ejecutar script SQL específico
./run.sh sql ruta/al/script.sql

# Ver ayuda
./run.sh help
```

### Ejecución Manual

```bash
./mvnw spring-boot:run
```

O compilar y ejecutar:

```bash
./mvnw clean package
java -jar target/wow-libre-transaction-0.0.1-SNAPSHOT.jar
```

### Docker

```bash
docker build -t wow-libre-transaction .
docker run -p 8092:8092 \
  -e DB_TRANSACTION_HOST=jdbc:mysql://host:3306/transactions \
  -e DB_TRANSACTION_USERNAME=usuario \
  -e DB_TRANSACTION_PASSWORD=contraseña \
  wow-libre-transaction
```

## 📡 API Endpoints

### Base URL
```
http://localhost:8092/transaction
```

### Headers Requeridos
- `Accept-Language`: Idioma (es, en, pt)
- `transaction_id`: ID de transacción (opcional)

### Endpoints Principales

#### Planes
- `GET /api/plan` - Obtener planes disponibles

#### Productos
- `GET /api/products` - Obtener productos por categoría
- `GET /api/products/{reference}` - Obtener producto por referencia
- `GET /api/products/discount` - Obtener productos con descuento
- `GET /api/products/offer` - Obtener mejor oferta

#### Suscripciones
- `GET /api/subscription/pill-home` - Widget de suscripción (no autenticado)
- `GET /api/subscription/pill-user` - Widget de suscripción (autenticado)
- `GET /api/subscription` - Verificar suscripción activa
- `GET /api/subscription/benefits` - Obtener beneficios de suscripción
- `POST /api/subscription/claim-benefits` - Reclamar beneficios

#### Pagos
- `POST /api/payment` - Crear pago
- `POST /api/payment/notification` - Notificación de pago (webhook)

#### Transacciones
- `GET /api/transaction` - Obtener transacciones

#### Wallet
- `GET /api/wallet` - Obtener puntos del wallet

## 🗄️ Base de Datos

### Esquema

El servicio utiliza el esquema `transactions` en MySQL. Las tablas principales son:

- `plan` - Planes de suscripción
- `subscription` - Suscripciones de usuarios
- `product` - Productos disponibles
- `transaction` - Transacciones de pago
- `wallet` - Billetera de puntos
- `packages` - Paquetes asociados a productos

### Script SQL

El script de inicialización se encuentra en:
```
src/main/resources/static/script.sql
```

#### Ejecutar Script SQL

Puedes ejecutar el script SQL usando el comando `sql` del script `run.sh`:

```bash
# Ejecutar el script SQL por defecto (src/main/resources/static/script.sql)
./run.sh sql
```

El comando:
- ✅ Verifica que el cliente MySQL esté instalado
- ✅ Carga las variables de entorno desde `.env`
- ✅ Usa las mismas credenciales de base de datos que la aplicación
- ✅ Parsea automáticamente la URL JDBC de la configuración
- ✅ Muestra información de conexión antes de ejecutar

**Ejemplo de salida:**
```
ℹ️  Ejecutando script SQL: src/main/resources/static/script.sql
ℹ️  Conectando a MySQL...
ℹ️    Host: localhost
ℹ️    Puerto: 3306
ℹ️    Base de datos: transactions
ℹ️    Usuario: root
✅ Script SQL ejecutado correctamente
```

**Ejecutar un script SQL específico:**
```bash
./run.sh sql mi_script_personalizado.sql
```

**Nota:** El script usa las variables de entorno:
- `DB_TRANSACTION_HOST` (default: `jdbc:mysql://localhost:3306/transactions`)
- `DB_TRANSACTION_USERNAME` (default: `root`)
- `DB_TRANSACTION_PASSWORD` (default: `Wowlibre96@@`)

Si no tienes un archivo `.env`, el script usará los valores por defecto del perfil `local`.

## 🐳 Docker

### Build

```bash
docker build -t wow-libre-transaction .
```

### Run

```bash
docker run -d \
  --name wow-libre-transaction \
  -p 8092:8092 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e DB_TRANSACTION_HOST=jdbc:mysql://mysql:3306/transactions \
  -e DB_TRANSACTION_USERNAME=root \
  -e DB_TRANSACTION_PASSWORD=password \
  wow-libre-transaction
```

## 💻 Desarrollo

### Estructura del Proyecto

```
src/main/java/com/wow/libre/
├── application/          # Capa de aplicación (servicios)
├── domain/               # Capa de dominio (DTOs, modelos, puertos)
└── infrastructure/       # Capa de infraestructura (controladores, repositorios, entidades)
```

### Arquitectura

El proyecto sigue una arquitectura hexagonal (puertos y adaptadores):

- **Puertos de Entrada (in)**: Interfaces para servicios de aplicación
- **Puertos de Salida (out)**: Interfaces para repositorios y servicios externos
- **Adaptadores**: Implementaciones concretas de los puertos

### Solución de Problemas con Lombok

Si encuentras errores de compilación como `cannot find symbol: method getX()`, `setX()`, `builder()`, etc., sigue estos pasos:

1. **Verifica la versión de Java:**
```bash
java -version
# Debe mostrar Java 21
```

2. **Configura Java 21 (macOS con Homebrew):**
```bash
# Ver versiones disponibles
/usr/libexec/java_home -V

# Configurar Java 21
export JAVA_HOME=$(/usr/libexec/java_home -v 21)
```

3. **Limpiar y recompilar:**
```bash
rm -rf target
./mvnw clean compile
```

4. **En el IDE:**
   - IntelliJ: Settings → Build → Compiler → Annotation Processors → ✅ Enable
   - Verifica que el plugin de Lombok esté instalado
   - Reinicia el IDE

### Compilar

```bash
./mvnw clean compile
```

### Tests

```bash
./mvnw test
```

### Health Check

```bash
curl http://localhost:8092/transaction/actuator/health
```

## 📝 Notas

- El servicio corre en el puerto **8092** por defecto
- El contexto path es `/transaction`
- Requiere conexión con el servicio Core para algunas funcionalidades
- Integración con PayU y Stripe para procesamiento de pagos

## 🔐 Seguridad

- Autenticación mediante JWT
- Headers requeridos para identificación de transacciones
- Validación de tokens en endpoints protegidos

## 📞 Soporte

Para más información, consulta la documentación del proyecto Core o contacta al equipo de desarrollo.
