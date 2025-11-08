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

- **Java 17** (requerido - no usar Java 21+ por problemas de compatibilidad con Lombok)
- Maven 3.6+
- MySQL 8.0+
- Docker (opcional, para ejecutar con contenedores)
- **Lombok Plugin** (para IDEs: IntelliJ IDEA, Eclipse, VS Code)

> ⚠️ **Nota importante:** Este proyecto está configurado para Java 17. Usar versiones más recientes (Java 21, 24, etc.) puede causar problemas de compilación con Lombok. Se recomienda usar Java 17.

## 🚀 Instalación

### 1. Clonar el repositorio

```bash
git clone <repository-url>
cd wow-libre-transaction
```

### 2. Configurar la base de datos

Ejecuta el script de setup para crear la base de datos y las tablas:

```bash
chmod +x setup.sh
./setup.sh
```

O manualmente:

```bash
mysql -u root -p < src/main/resources/static/script.sql
```

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
# Debe mostrar Java 17. Si muestra Java 21+, cambia a Java 17
```

2. **Configura Java 17 (macOS con Homebrew):**
```bash
# Ver versiones disponibles
/usr/libexec/java_home -V

# Configurar Java 17
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
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
