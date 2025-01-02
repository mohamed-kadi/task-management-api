# task-management-api
A Spring-Boot REST API for task management

# Task Management API

## Project Overview
A RESTful API built with Spring Boot for managing tasks and todo items. This project serves as a demonstration of Spring Boot best practices and enterprise-level application development.

## Technology Stack
- **Java 17**: Latest LTS (Long Term Support) version offering enhanced performance and new features
- **Spring Boot 3.2.0**: Framework for building production-ready applications
- **Maven**: Dependency management and build tool
- **H2 Database**: In-memory database for development and testing
- **Lombok**: Reduces boilerplate code in Java classes

## Dependencies Explained
- **Spring Web**: Provides REST API capabilities and web application features
- **Spring Data JPA**: Simplifies database operations using Java Persistence API
- **H2 Database**: Lightweight, in-memory database ideal for development
- **Lombok**: Java library that automatically plugs into your editor to reduce boilerplate code
- **SpringDoc OpenAPI**: Provides Swagger documentation for the API

## Features
- User registration and authentication
- JWT based security
- CRUD operations for tasks
- Search and filter tasks
- H2 Database Console access
- Protected endpoints

## Project Setup
1. **Prerequisites**
   - Java JDK 17 or higher
   - Maven 3.x
   - Git
   - IDE (VS Code)
   - Swagger & Postman (for testing)

2. **Initialize Project**
   ```bash
   # Clone the repository
   git clone https://github.com/mohamed-kadi/task-management-api.git
   cd task-management-api
   
   # Build the project
   mvn clean install
   ```

3. **Project Structure**
   ``` 
   src/main/java/com/example/taskmanagement/
   ├── config/
   │   └── SecurityConfig.java
   ├── controllers/
   │   ├── AuthController.java
   │   └── TaskController.java
   ├── models/
   │   ├── User.java
   │   ├── Task.java
   │   └── Role.java
   ├── repositories/
   │   ├── UserRepository.java
   │   └── TaskRepository.java
   ├── security/
   │   ├── JwtTokenProvider.java
   │   ├── JwtAuthenticationFilter.java
   │   └── CustomUserDetailsService.java
   └── services/
   └── TaskService.java
      ```

## Running the Application
```bash
mvn spring-boot:run
```
The application will start on `http://localhost:8080`

## API Documentation and Testing

## API Endpoints
- GET /api/tasks - Get all tasks
- GET /api/tasks/{id} - Get task by ID
- POST /api/tasks - Create new task
- PUT /api/tasks/{id} - Update task
- DELETE /api/tasks/{id} - Delete task
- GET /api/tasks/status/{status} - Get tasks by status
- GET /api/tasks/search?keyword={keyword} - Search tasks

### Swagger UI Documentation
When the application is running locally, you can access:
- Interactive API documentation: `http://localhost:8080/swagger-ui.html`
- Raw API documentation: `http://localhost:8080/api-docs`

### Testing with Postman
1. Download the Postman collection: `Task-Management-API.postman_collection.json`
2. Import the collection into Postman
3. Test endpoints with pre-configured requests

### Example API Requests

#### Create Task
```http
POST /api/tasks
Content-Type: application/json

{
    "title": "Learn Spring Boot",
    "description": "Complete the tutorial",
    "status": "PENDING"
}
 ```
#### Update Task
```http
PUT /api/tasks/1
Content-Type: application/json

{
   "title": "Learn Spring Boot",
   "description": "Tutorial completed!",
   "status": "IN_PROGRESS"
}
```
### Authentication Endpoints

#### Register User
```http
POST /api/auth/register
Content-Type: application/json

{
    "username": "testuser",
    "email": "test@example.com",
    "password": "test123"
}
```
#### Login User
```http
POST /api/auth/login
Content-Type: application/json

{
    "username": "testuser",
    "password": "test123"
}
```
Response:
```json
{
    "token": "eyJhbGciOiJIUzI1...",
    "type": "Bearer"
}
```
### Security Configuration
#### JWT Configuration
Add to application.properties:
```properties
# JWT Properties
app.jwtSecret=af3c1a2b3d4e5f6789abcdef01234567890bcdef
app.jwtExpirationInMs=86400000
```
#### Using JWT Token
All task endpoints require Authentication token in header:
```auth
Authorization: Bearer your-jwt-token
```
#### Token Expiration

Tokens expire after configured time (default 24 hours)
Expired tokens will return 401 Unauthorized
Login again to get new token

#### Error Handling

- 400: Bad Request (Invalid input)
- 401: Unauthorized (Invalid/expired token)
- 403: Forbidden (Insufficient permissions)
- 404: Not Found
- 500: Internal Server Error

#### Testing Authentication Flow

1. Register new user using /api/auth/register
2. Login with credentials to get JWT token
3. Add token to request headers for all task endpoints
4. Test token expiration (default 24h)

## Database Configuration

The application supports two database configurations:

### In-Memory Database (Development/Testing)
```properties

# Database Configuration
spring.datasource.url=jdbc:h2:mem:taskdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# JPA Configuration
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# H2 Console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```
* Data is temporary and resets when application restarts
* Perfect for development and testing
* Access H2 Console at: http://localhost:8080/h2-console

### Persistent Database (Local Development)
```properties

# Database Configuration
spring.datasource.url=jdbc:h2:file:./data/taskdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# JPA Configuration
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# H2 Console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```
* Data persists after application restart
* Stored in ./data/taskdb files
* Suitable for local development with data persistence

### Using H2 Console

1. Start the application
2. Go to http://localhost:8080/h2-console
3. Enter credentials:
   - JDBC URL: (use either memory or file URL as configured)
   - Username: sa
   - Password: (leave empty)
4. Click "Connect"

## Development Progress
- [x] Initial project setup
- [x] Basic CRUD operations for tasks
- [x] Task search functionality
- [x] Status filtering
- [x] Swagger API documentation
- [x] Postman collection
- [x] Database configuration
- [x] User authentication    <!--Updated-->
- [ ] Task categories and labels

## Contributing
1. Create a feature branch (`git checkout -b feature/amazing-feature`)
2. Commit your changes (`git commit -m 'Add some amazing feature'`)
3. Push to the branch (`git push origin feature/amazing-feature`)
4. Open a Pull Request

## License
This project is licensed under the MIT License - see the LICENSE file for details