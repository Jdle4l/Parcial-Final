# Parcial Final - Desarrollo de Sistemas de Información 3

## Integrantes

- Integrante 1
- Integrante 2
- Integrante 3
- Integrante 4
- Integrante 5

## Tecnologías

| Capa     | Stack                                                                 |
| -------- | --------------------------------------------------------------------- |
| Backend  | Spring Boot 4.1.2, Java 21, Maven, Spring Security, JWT (jjwt), MySQL |
| Frontend | Angular 22, Bootstrap 5                                               |

## Backend (`/backend`)

1. Copiar `src/main/resources/application-example.properties` a `application.properties` y configurar la base de datos y `jwt.secret`.
2. Ejecutar:

```bash
./mvnw spring-boot:run
```

- API: `http://localhost:8080`
- Swagger: `http://localhost:8080/swagger-ui/index.html`
- Usuario administrador inicial: `admin` / `1234567890`

### Endpoints

| Método | Ruta                       | Acceso       |
| ------ | -------------------------- | ------------ |
| POST   | `/auth/login`              | Público      |
| POST   | `/auth/register`           | Público      |
| GET    | `/user/me`                 | USER, ADMIN  |
| GET    | `/user/getall`             | USER, ADMIN  |
| GET    | `/user/count`              | USER, ADMIN  |
| GET    | `/user/getbyid/{id}`       | USER, ADMIN  |
| GET    | `/user/exists/{id}`        | USER, ADMIN  |
| POST   | `/user/createjson`         | ADMIN        |
| PUT    | `/user/updatejson?id=`     | ADMIN        |
| DELETE | `/user/deletebyid/{id}`    | ADMIN        |
| DELETE | `/user/deletebyname?name=` | ADMIN        |

Productos (`{tipo}` = `lacteo` o `carne`):

| Método | Ruta                                  | Acceso      |
| ------ | ------------------------------------- | ----------- |
| GET    | `/{tipo}/getall`                      | USER, ADMIN |
| GET    | `/{tipo}/getbyid/{id}`                | USER, ADMIN |
| POST   | `/{tipo}/createjson`                  | ADMIN       |
| PUT    | `/{tipo}/updatejson?id=`              | ADMIN       |
| DELETE | `/{tipo}/deletebyid/{id}`             | ADMIN       |
| PUT    | `/{tipo}/comprar?id=&cantidad=`       | USER        |

Las peticiones protegidas requieren el encabezado `Authorization: Bearer <token>`.

## Frontend (`/frontend`)

```bash
npm install
npm start
```

- Aplicación: `http://localhost:4200`
