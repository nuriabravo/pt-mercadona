# Mercadona API - Prueba Técnica

- Nuria Bravo
- Java 21, Spring Boot, PostgreSQL, IntelliJ IDEA
- Se ha seguido la convención estándar de GitHub: [Conventional Commits](https://gist.github.com/qoomon/5dfcdf8eec66a051ecd85625518cfd13)
- Se han creado ramas para las distintas funcionalidades y se han ido haciendo PR.

## Índice
- [Arquitectura](#arquitectura)
- [Instalación en local](#instalación-en-local)
- [Endpoints](#endpoints)
- [Decisiones tomadas](#decisiones-tomadas)
- [Limitaciones](#limitaciones)
- [Testing](#testing)
- [Ideas de mejora](#ideas-de-mejora)

## Arquitectura

El proyecto sigue una arquitectura hexagonal, con tres capas principales:

- **`domain`**
- **`application`**
- **`infrastructure`**

## Módulos utilizados

- [Spring Web](https://mvnrepository.com/artifact/org.springframework/spring-web)
- [Spring Data JPA](https://mvnrepository.com/artifact/org.springframework.data/spring-data-jpa)
- [Spring Validation](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-validation)
- [Lombok](https://mvnrepository.com/artifact/org.projectlombok/lombok)
- [MapStruct](https://mvnrepository.com/artifact/org.mapstruct/mapstruct)
- [Flyway](https://mvnrepository.com/artifact/org.flywaydb/flyway-database-postgresql)
- [PostgreSQL](https://mvnrepository.com/artifact/org.postgresql/postgresql)
- [springdoc-openapi](https://mvnrepository.com/artifact/org.springdoc/springdoc-openapi-starter-webmvc-api)

## Instalación en local

### Requisitos

- [Java 21 (LTS)](https://jdk.java.net/21/)
- [PostgreSQL](https://www.postgresql.org/)
- [Docker](https://www.docker.com/) (para la API externa de la iteración 3)
- [IntelliJ IDEA](https://www.jetbrains.com/idea/) (opcional)
- [Postman](https://www.postman.com/downloads/) (opcional)

### Instrucciones

1. Crear una base de datos PostgreSQL, por ejemplo, `mercadona`.
2. Configurar la conexión en `src/main/resources/application.yml`:
```yaml
   spring:
     datasource:
       url: jdbc:postgresql://localhost:5432/mercadona
       username: <usuario>
       password: <password>
```
3. Arrancar la aplicación. Flyway crea el esquema
   y los datos automáticamente al arrancar no es necesario ejecutar ningún script
   SQL a mano. Las migraciones están en `src/main/resources/db/migration`.
4. (Opcional, solo necesario para probar la iteración 3)
   Levantar la API externa de tiendas. Desde la raíz del proyecto:
```bash
   docker compose up -d
```
Esto monta `jameral/stores` en `http://localhost:8081`. Ver la sección [Limitaciones](#limitaciones) más abajo.

5. (Opcional) En `src/main/resources/postman` hay una colección Postman preparada para probar
   los endpoints.

## Datos de prueba

Al arrancar, Flyway inserta automáticamente el catálogo de secciones (Horno, Cajas,
Pescadería, Verduras, Droguería) además de tiendas y trabajadores. También se pueden crear a través
de la API (Postman) o manualmente.

## Endpoints

### Auth

No requiere autenticación.

Usuarios para acceder:
| username | password |
|--------|---------|
| admin | admin123 |
| nuria | nuria123 |

| Método | Endpoint | Request Body | Response | Descripción |
|--------|---------|--------------|---------|-------------|
| POST   | `/api/login` | `{ "username": "string", "password": "string" }` | `LoginResponse` | Autentica un usuario y devuelve un token de acceso junto con su fecha de expiración. |

### Workers

Requiere autenticación.
| Método | Endpoint | Request Body | Response | Descripción |
|--------|---------|--------------|---------|-------------|
| GET    | `/api/workers` | — | `List<WorkerResponseDto>` | Obtiene todos los trabajadores. |
| GET    | `/api/workers/{id}` | — | `WorkerResponseDto` | Obtiene un trabajador por su ID. |
| POST   | `/api/workers` | `{ "firstName": "string", "lastName": "string", "dni": "string", "contractHours": number, "storeId": number }` | `WorkerResponseDto` | Crea un nuevo trabajador. |
| PUT    | `/api/workers/{id}` | `{ "firstName": "string", "lastName": "string", "dni": "string", "contractHours": number, "storeId": number }` | `WorkerResponseDto` | Actualiza un trabajador existente por ID. |
| DELETE | `/api/workers/{id}` | — | `204 No Content` | Elimina un trabajador por ID. |

### Worker Section Assignments

Requiere autenticación.
| Método | Endpoint | Request Body | Response | Descripción |
|--------|---------|--------------|---------|-------------|
| GET    | `/api/workers/{workerId}/assignments` | — | `List<AssignmentResponseDto>` | Obtiene las asignaciones de un trabajador. |
| POST   | `/api/workers/{workerId}/assignments` | `{ "sectionId": number, "hours": number }` | `AssignmentResponseDto` | Asigna al trabajador a una sección durante N horas. Rechaza con `409` si se exceden las horas de contrato disponibles. |
| DELETE | `/api/workers/{workerId}/assignments/{assignmentId}` | — | `204 No Content` | Desasigna al trabajador de una sección. |

### Store Reports

Requiere autenticación.
| Método | Endpoint | Request Body | Response | Descripción |
|--------|---------|--------------|---------|-------------|
| GET    | `/api/stores/{storeId}/reports/status` | — | `StoreStatusReportDto` | Estado de la tienda: secciones y trabajadores asignados con sus horas. |
| GET    | `/api/stores/{storeId}/reports/uncovered-hours` | — | `StoreHoursReportDto` | Secciones de la tienda con horas sin cubrir y cuántas horas faltan. |
| GET    | `/api/stores/{storeCode}/reports/skills` | — | `StoreSkillsReportDto` | Aptitudes requeridas por las secciones de una tienda, buscada por código. |

### Swagger

No requiere autenticación.

Documentación interactiva en `http://localhost:8080/swagger-ui.html`

## Decisiones tomadas

### Validación de horas al editar el contrato de un trabajador
Al actualizar un trabajador (`PUT /api/workers/{id}`), si las nuevas `contractHours` son
inferiores a las horas que el trabajador ya tiene asignadas en secciones, la operación se
rechaza con `409 Conflict`. Esto evita que un trabajador quede con más horas asignadas que
las permitidas por su contrato, manteniendo el estilo que se aplica al
crear una asignación (`POST /api/workers/{id}/assignments`).

### Relación con la API externa de tiendas
El enunciado no especifica cómo relacionar las tiendas del sistema local con las de la
API externa (`jameral/stores`), que identifica sus tiendas por un `id` sin ningún
campo equivalente al `code`. Por la ausencia de un campo común,
se asume que el `id` de la tienda en el sistema local coincide con el `id` en la API externa,
por ser la única relación posible.

## Limitaciones

### Imagen Docker de la API de tiendas
La imagen `jameral/stores` solo está publicada para arquitectura `linux/arm64`, lo que
provoca `exec format error` en otros sistemas. El `docker-compose.yml` incluido
especifica `platform: linux/arm64` para que Docker gestione la emulación (en mi caso, he utilizado QEMU para la emulación).

Además, se ha detectado un bug en la propia imagen. Su script
`data.sql` intenta insertar datos antes de que Hibernate/JPA haya creado el esquema.
Este fallo es interno, en la imagen proporcionada.

El desarrollo está diseñado para funcionar independientemente
del sistema externo (contenedor no disponible o API fallando). Los informes de tienda se generan
igualmente, con `storeAddress` en `null`, sin que el fallo afecte al resto de la respuesta.

## Testing
Se han desarrollado 4 test unitarios, 2 de integración y 1 test E2E.
Lo ideal hubiera sido cubrir todo el proyecto pero por falta de tiempo se han elegido esas clases concretas.

##  Internacionalización
A pesar de que no se ha desarrollado al completo, todos los errores que se manejan en el proyecto se han planteado de cara a hacer una internalización.
Mediante códigos para más adelante hacer las traducciones.

## Ideas de mejora
Estas son algunas mejoras que no se han desarrollado por el tiempo limitado de la prueba,
pero que se considerarían en un desarrollo real:
- Gestión de documentos para los reportes (pdf, docx...)
- Cobertura de tests ≥ 80%
- Separación de `StoreReportMapper` en mappers específicos por tipo de informe
  (`StoreStatusReportMapper`, `StoreHoursReportMapper`, `StoreSkillsReportMapper`)
- Hasheo del token almacenado en BBDD (actualmente se guarda en texto plano,
  a diferencia de la contraseña, que sí usa BCrypt)
- Perfiles (`test`, `dev`, `prod`) con configuración diferenciada
- Añadir logs para mayor control de la traza de la petición.
