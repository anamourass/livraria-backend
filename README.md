# Livraria Backend

## Overview
This project is a Spring Boot application that provides a RESTful API for managing a library of books. It uses an H2 in-memory database for data storage and JPA for object-relational mapping.

## Project Structure
The project is organized as follows:

```
livraria-backend
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── livraria
│   │   │           ├── LivrariaApplication.java
│   │   │           ├── controller
│   │   │           │   └── LivroController.java
│   │   │           ├── model
│   │   │           │   └── Livro.java
│   │   │           ├── repository
│   │   │           │   └── LivroRepository.java
│   │   │           ├── service
│   │   │           │   ├── LivroService.java
│   │   │           │   └── impl
│   │   │           │       └── LivroServiceImpl.java
│   │   │           └── enums
│   │   │               ├── Categoria.java
│   │   │               └── Tamanho.java
│   │   └── resources
│   │       ├── application.properties
│   │       └── data.sql
│   └── test
│       └── java
│           └── com
│               └── livraria
│                   ├── LivrariaApplicationTests.java
│                   ├── controller
│                   │   └── LivroControllerTest.java
│                   └── service
│                       └── LivroServiceTest.java
├── pom.xml
└── README.md
```

## Setup Instructions

1. **Clone the Repository**
   ```
   git clone <repository-url>
   cd livraria-backend
   ```

2. **Build the Project**
   Use Maven to build the project:
   ```
   mvn clean install
   ```

3. **Run the Application**
   You can run the application using the following command:
   ```
   mvn spring-boot:run
   ```

4. **Access the API**
   The API will be available at `http://localhost:8080/api/livros`. You can use tools like Postman or curl to interact with the API.

## API Endpoints

- **GET /api/livros**: Retrieve all books.
- **GET /api/livros/{id}**: Retrieve a book by ID.
- **POST /api/livros**: Create a new book.
- **PUT /api/livros/{id}**: Update an existing book.
- **DELETE /api/livros/{id}**: Delete a book by ID.

## Database Initialization
The application uses an H2 in-memory database. Sample data can be initialized using the `data.sql` file located in the `src/main/resources` directory.

## Testing
Unit tests are provided for the application. You can run the tests using:
```
mvn test
```

## Dependencies
This project uses the following key dependencies:
- Spring Boot Starter Web
- Spring Boot Starter Data JPA
- H2 Database

## License
This project is licensed under the MIT License. See the LICENSE file for more details.