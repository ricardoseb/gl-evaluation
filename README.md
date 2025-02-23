# Microservicio de Usuarios - Spring Boot

Este proyecto es un microservicio para la creación y consulta de usuarios desarrollado con Spring Boot 2.5.14 y Gradle 7.4.

## Requisitos Previos

- Java 11
- Gradle 7.4

## Tecnologías Utilizadas

- Spring Boot 2.5.14
- Spring Data JPA
- H2 Database
- JWT
- JUnit

## Construcción y Ejecución

1. Clonar el repositorio:
```bash
git clone [URL_DEL_REPOSITORIO]
```
2. Ingresar a la raiz del directorio del proyecto:
```bash
cd repository-name
```
3. Compilar el proyecto:
```bash
./gradlew clean build
```
4. Ejecutar el proyecto:
```bash
./gradlew bootRun
```
La aplicación estará disponible en http://localhost:8080

## Documentación de la API

La documentación de la API está disponible a través de Swagger UI:
- [Doc](http://localhost:8080/swagger-ui.html)

### Especificación OpenAPI

También puedes acceder a la especificación OpenAPI en formato JSON:
- [Specification](http://localhost:8080/v3/api-docs)

### Contratos de la API

#### 1. Sign-up Request
```json
{
    "name": "String",
    "email": "string@dominio.com",
    "password": "a2asfGfdfdf4",
    "phones": [
        {
            "number": 123456789,
            "citycode": 1,
            "contrycode": "57"
        }
    ]
}
```
#### 2. Sign-up/Login Response
```json
{
    "id": "e5c6cf84-8860-4c00-91cd-22d3be28904e",
    "created": "Nov 16, 2021 12:51:43 PM",
    "lastLogin": "Nov 16, 2021 12:51:43 PM",
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "isActive": true,
    "name": "Julio Gonzalez",
    "email": "julio@testssw.cl",
    "password": "a2asfGfdfdf4",
    "phones": [
        {
            "number": 87650009,
            "cityCode": 7,
            "countryCode": "25"
        }
    ]
}
```
#### 3. Error Response
```json
{
    "error": [{
        "timestamp": "2024-02-22T10:30:45.123Z",
        "codigo": 400,
        "detail": "Mensaje de error"
    }]
}
```
## Validaciones

- Email: debe seguir el formato aaaaaaa@dominio.algo
- Password: debe contener:

    - Una mayúscula
    - Dos números (no consecutivos)
    - Mínimo 8 y máximo 12 caracteres
- Nombre y teléfonos son opcionales

## Base de Datos
El proyecto utiliza H2 como base de datos en memoria. La consola de H2 está disponible en:

- URL: http://localhost:8080/h2-console
- JDBC URL: jdbc:h2:mem:testdb
- Usuario: sa
- Password: [vacío]

## Diagramas
Los diagramas UML del proyecto se encuentran en la carpeta [`/diagrams`](./diagrams).

## Pruebas Unitarias y Cobertura de Código
El proyecto cuenta con pruebas unitarias para los servicios. Para ejecutar las pruebas:
```bash
./gradlew test
```
### Reporte de Cobertura
El proyecto utiliza JaCoCo para el análisis de cobertura de código. Para generar el reporte:
```bash
./gradlew jacocoTestReport
```
El reporte estará disponible en `build/reports/jacoco/test/html/index.html`