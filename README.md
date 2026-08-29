# msa-court-reservation-reservations

Microservicio de reservas de canchas para la aplicación de reservas deportivas.

## Descripción

Este servicio gestiona reservas de canchas con validaciones de disponibilidad, límite de reservas activas por usuario y manejo de estados (`ACTIVO` y `CANCELADO`).

## Requisitos

- Java 21
- Gradle
- PostgreSQL

## Configuración de base de datos

La aplicación usa PostgreSQL en entorno local con estas variables opcionales:

- `DB_USERNAME` (por defecto: `postgres`)
- `DB_PASSWORD` (por defecto: `mysecretpassword`)

La conexión configurada es:

- URL: `jdbc:postgresql://localhost:5432/reservas_db`

## Flyway

El proyecto está configurado con Flyway para gestionar migraciones SQL.

Las migraciones se encuentran en:

- `src/main/resources/db/migration/`

Archivos incluidos:

- `V1__create_schema.sql`: crea la tabla `reservation`.
- `V2__seed_initial_test_data.sql`: inserta datos iniciales de prueba.


## Ejecutar la aplicación

```bash
./gradlew bootRun
```

## Ejecutar pruebas

```bash
./gradlew test
```

## Endpoints principales

El servicio expone operaciones de creación, consulta, cancelación y búsqueda de reservas bajo el contexto:

- `/reservations`

La documentación OpenAPI/Swagger se puede consultar en:

- `http://localhost:8083/reservations/swagger-ui/index.html`

