# Transactions API

API RESTful para gestión de transacciones con soporte de relaciones padre-hijo y cálculo de sumas transitivas.

## Tecnologías

- Java 21
- Spring Boot
- Maven
- JUnit + MockMvc
- Docker
- Springdoc OpenAPI (Swagger)

## Funcionalidades

- Crear y reemplazar transacciones por ID
- Consultar transacciones por tipo
- Calcular la suma total de una transacción incluyendo todos sus descendientes

## Decisiones de diseño

**Arquitectura en capas** — Controller → Service → Repository, con interfaz
`TransactionRepository` que desacopla la lógica de negocio del mecanismo de almacenamiento.

**Almacenamiento** — `ConcurrentHashMap<Long, Transaction>` para garantizar thread-safety
en un entorno con múltiples requests concurrentes.

**Suma transitiva** — Implementada con BFS iterativo. Se eligió sobre recursión para evitar
`StackOverflowError` en árboles de gran profundidad.

**Detección de ciclos** — Validación explícita en el `PUT` que recorre los ancestros antes
de persistir. El sistema garantiza que la jerarquía siempre es un árbol válido.

**Manejo de errores** — `@RestControllerAdvice` centraliza el manejo de excepciones.
`400` para errores de validación y ciclos, `404` para recursos inexistentes.

## Estructura del proyecto

```
├── controller/   # Endpoints HTTP
├── service/      # Lógica de negocio
├── repository/   # Persistencia en memoria
├── model/        # Entidades
├── dto/          # Requests / Responses
└── exception/    # Manejo de errores
```

## Correr con Docker

```bash
docker build -t transactions-api .
docker run -p 8080:8080 transactions-api
```

La API estará disponible en `http://localhost:8080`

## Correr localmente

```bash
./mvnw spring-boot:run
```
## Testing

Se desarrolló la API siguiendo TDD, escribiendo cada test antes de la implementación.
Los tests de integración usan `@SpringBootTest` y `MockMvc` cubriendo:

- Creación y reemplazo de transacciones
- Validaciones de input (amount, type, parent_id)
- Detección de ciclos y self-parenting
- Consulta por tipo
- Suma transitiva incluyendo el caso del enunciado
- Manejo de errores (400, 404)

### Correr los tests

```bash
./mvnw test
```

## Documentación interactiva

Con la aplicación corriendo:

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Endpoints

| Método | Endpoint                     | Descripción                        |
|--------|------------------------------|------------------------------------|
| `PUT`  | `/transactions/{id}`         | Crear o reemplazar una transacción |
| `GET`  | `/transactions/types/{type}` | Obtener IDs por tipo               |
| `GET`  | `/transactions/sum/{id}`     | Obtener suma transitiva            |