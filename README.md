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

## Project Setup
1. **Prerequisites**
   - Java JDK 17 or higher
   - Maven 3.x
   - Git
   - IDE (VS Code)

2. **Initialize Project**
   ```bash
   # Clone the repository
   git clone [repository-url]
   cd task-management-api
   
   # Build the project
   mvn clean install
   ```

3. **Project Structure**
   ```
   task-management-api/
   ├── src/
   │   ├── main/
   │   │   ├── java/
   │   │   │   └── com/example/taskmanagement/
   │   │   └── resources/
   │   └── test/
   │       └── java/
   ├── .gitignore
   ├── pom.xml
   └── README.md
   ```

## Running the Application
```bash
mvn spring-boot:run
```
The application will start on `http://localhost:8080`

## Development Progress
- [x] Initial project setup
- [ ] Basic CRUD operations for tasks
- [ ] User authentication
- [ ] Task categories and labels
- [ ] API documentation

## Contributing
1. Create a feature branch (`git checkout -b feature/amazing-feature`)
2. Commit your changes (`git commit -m 'Add some amazing feature'`)
3. Push to the branch (`git push origin feature/amazing-feature`)
4. Open a Pull Request

## License
This project is licensed under the MIT License - see the LICENSE file for details
