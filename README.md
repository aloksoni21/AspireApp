 ### Loan Application
## Overview
This is a Spring Boot application that allows authenticated users to apply for loans, view their loans, make repayments, and have an admin approve loans. The project demonstrates the use of Spring Boot, JPA, and Spring Security to create a simple loan management system.

## Features
User Registration and Authentication: Users can register and authenticate using Spring Security.<br>
Apply for Loans: Authenticated users can apply for loans specifying the amount and term.<br>
Admin Approval: Admin can approve pending loans.<br>
View Loans: Users can view their specific loans.<br>
Make Repayments: Users can make repayments towards approved loans.<br>
Automatic Loan Status Update: The loan status is updated automatically upon full repayment.<br>

## Requirements
Java 11 or higher<br>
Maven<br>
H2 Database (for development)<br>

## Setup
Clone the Repository<br>
sh
Copy code
git clone https://github.com/ShivChauhan87/AspireApp.git<br>
cd AspireApp
## Build the Project
sh
Copy code
mvn clean install
Run the Application
sh
Copy code
mvn spring-boot:run
The application will start and be accessible at http://localhost:8080.

## Database Configuration
The application uses an H2 in-memory database for development purposes. You can access the H2 console at http://localhost:8080/h2-console. The default JDBC URL is jdbc:h2:mem:testdb.

## H2 Database Configuration
Ensure you have the following configuration in your application.properties file:

## properties
Copy code
spring.datasource.url=jdbc:h2:mem:testdb<br>
spring.datasource.driverClassName=org.h2.Driver<br>
spring.datasource.username=sa<br>
spring.datasource.password=password<br>
spring.h2.console.enabled=true<br>
spring.h2.console.path=/h2-console<br>
spring.datasource.platform=h2<br>
spring.jpa.hibernate.ddl-auto=update<br>

## API Endpoints
You can import the Postman collection to explore the available endpoints.

## Postman Collection
<a href="https://github.com/ShivChauhan87/AspireApp/blob/main/Loan%20Application%20APIs.postman_collection.json">Loan Application APIs.postman_collection.json</a>

## Endpoints
User Registration and Authentication
Register a new user

http
Copy code
POST /users/register
Request body:

json
Copy code
{
    "username": "user1",
    "password": "password"
}

## Login

The application uses HTTP Basic Authentication. Use your username and password to access protected endpoints.



## Security
The application uses Spring Security for authentication and authorization. The endpoints for applying for loans, viewing loans, and making repayments require authentication. The endpoint for approving loans is restricted to users with admin privileges.

## Example Data
For testing purposes, you can use the following example data:

User Credentials:

Username: user1

Password: password

Username: admin

Password: adminpassword

## Notes
Ensure you replace getUserIdFromPrincipal(userDetails) in LoanController with the actual user ID retrieval logic.
Customize the exception handling and validation logic as needed.
For a production setup, use a persistent database and configure proper security measures.
