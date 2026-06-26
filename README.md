# Spring Security Login — JWT Auth API

API REST de autenticación stateless con Spring Security 6, JWT, y roles. Proyecto de portfolio orientado a implementación real en producción.

## Stack

| Tecnología | Versión | Uso |
|---|---|---|
| Java | 21 | Lenguaje principal |
| Spring Boot | 3.3 | Framework base |
| Spring Security | 6 | Autenticación y autorización |
| PostgreSQL | 16 | Base de datos |
| Flyway | 10 | Migraciones de schema |
| jjwt | 0.12 | Generación/validación JWT |
| Testcontainers | latest | Tests de integración |
| Docker | - | Containerización |

## Arquitectura

```
POST /auth/register  →  Registro de nuevo usuario
POST /auth/login     →  Login, devuelve Access + Refresh Token
POST /auth/refresh   →  Rota el Refresh Token, devuelve nuevo Access Token
POST /auth/logout    →  Revoca el Refresh Token activo

GET  /api/me         →  Datos del usuario autenticado  [ROLE_USER]
GET  /api/admin/**   →  Endpoints protegidos            [ROLE_ADMIN]
```

## Flujo de autenticación

```
Cliente              API
  |                   |
  |-- POST /login --> |
  |                   | valida credenciales
  |                   | genera Access Token (15 min)
  |                   | genera Refresh Token (7 días, guardado en DB)
  |<-- 200 tokens --- |
  |                   |
  |-- GET /api/me --> | (con Access Token en header)
  |   Authorization:  |
  |   Bearer <token>  | valida JWT (sin hit a DB)
  |<-- 200 user ----- |
  |                   |
  |-- POST /refresh-> | (cuando Access Token expira)
  |                   | valida Refresh Token en DB
  |                   | revoca token viejo (rotación)
  |                   | genera nuevo par de tokens
  |<-- 200 tokens --- |
```

## Seguridad implementada

- Contraseñas hasheadas con **BCrypt** (strength 10)
- **Access Token** de vida corta (15 min) — stateless, validado sin DB
- **Refresh Token** de vida larga (7 días) — guardado en DB, permite revocación real
- **Token rotation** — al hacer refresh, el token viejo se invalida
- **Logout real** — revoca el Refresh Token en DB
- Headers de seguridad HTTP configurados vía Spring Security
- Validación de inputs con Bean Validation

## Cómo correr localmente

### Solo la base de datos (desarrollo)

```bash
docker compose up postgres -d
mvn spring-boot:run
```

### Aplicación completa con Docker

```bash
docker compose up --build
```

La API queda disponible en `http://localhost:8080`  
Swagger UI: `http://localhost:8080/swagger-ui.html`

### Variables de entorno

Copiar `.env.example` como `.env` y ajustar:

```bash
cp .env.example .env
```

### Propiedades configurables

| Propiedad                                            | Descripción                                                                          | Obligatorio |
|------------------------------------------------------|--------------------------------------------------------------------------------------|-------------|
| ignaciorudyk.authentication.enabled                  | Habilita o deshabilita la dependencia (por defecto es true).                         | No          |
| ignaciorudyk.authentication.secret-key               | Secret key.                                                                          | Sí          |
| ignaciorudyk.authentication.access-token-expiration  | Define el access token expiration en MS.                                             | No          |
| ignaciorudyk.authentication.refresh-token-expiration | Define el refresh token expiration en MS.                                            | No          |
| spring.datasource.url                                | Url de la base de datos del consumidor de la dependencia.                            | Sí          |
| spring.datasource.username                           | Usuario de la base de datos.                                                         | Sí          |
| spring.datasource.password                           | Contraseña de la base de datos.                                                      | Sí          |
| spring.datasource.driver-class-name                  | Indica qué driver JDBC debe utilizar Spring para conectarse a la base de datos.      | Sí          |
| spring.jpa.properties.hibernate.dialect              | Indica qué tipo de SQL generar para la base de datos.                                | Sí          |
| spring.flyway.enabled                                | Habilita o deshabilita la dependencia de Flyway.                                     | No          |
| spring.flyway.locations                              | Indica la ruta dónde debe buscar Flyway los scripts SQL de migración.                | Sí          |
| spring.flyway.baseline-on-migrate                    | Toma la instancia de la base de datos que fue creada sin flayway como punto inicial. | No          |
| springdoc.api-docs.path                              | Cambia la URL donde Springdoc expone el documento OpenAPI en formato JSON.           | No          |
| springdoc.swagger-ui.path                            | Permite cambiar la URL donde se muestra la interfaz de Swagger UI.                   | No          |
| springdoc.swagger-ui.tags-sorter                     | Define cómo se ordenan los tags en Swagger UI.                                       | No          |

## Tests

```bash
# Unit tests
mvn test

# Integration tests (requiere Docker para Testcontainers)
mvn verify
```

## Estructura del proyecto

```
src/main/java/com/portfolio/auth/
├── config/       SecurityConfig, JwtConfig, OpenApiConfig
├── controller/   AuthController, UserController
├── service/      AuthService, JwtService, UserService
├── repository/   UserRepository, RefreshTokenRepository
├── model/        User, RefreshToken, Role
├── dto/          AuthDTOs (Register, Login, Response...)
├── security/     JwtFilter, CustomUserDetails, handlers
└── exception/    GlobalExceptionHandler, excepciones custom
```

## Decisiones de diseño

**¿Por qué JWT stateless?**  
Los Access Tokens se validan sin consultar la DB en cada request. Escala horizontalmente sin necesidad de sesiones compartidas (Redis, etc.).

**¿Por qué Refresh Token en DB?**  
Permite logout real y rotación. Un Access Token expirado es inútil; un Refresh Token revocado no puede obtener nuevos tokens.

**¿Por qué Flyway?**  
Los cambios de schema son código, están versionados, y son reproducibles en cualquier entorno.
