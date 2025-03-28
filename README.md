# Tracking Number Generator API

A simple Tracking Number Generator built with Spring Boot. This project demonstrates the use of RESTful API that generates unique tracking numbers for parcels.

## Features

- Generate unique tracking number wrt order created time.

## Technologies Used

- Java 17
- Spring Boot
- PostgreSQL
- Maven
- JUnit
- Mockito
- Lombok

## Getting Started

### Prerequisites

- JDK 17
- Maven
- PostgreSQL

### Installation

1. **Clone the repository**

    ```bash
    git clone <REPOSITORY_URL>
    cd tracking-number-generator
    ```

2. **Build the project**

    ```bash
    mvn clean install
    ```

3. **Run the application**

   You can run the application using Maven:

    ```bash
    mvn spring-boot:run
    ```
   Or using JAR file
   ```bash
   java -jar target/tracking-number-generator-0.0.1-SNAPSHOT.jar
   ```

4. **Database Configuration**

   Ensure your PostgreSQL database is running and update the `application.properties` with your database credentials:

    ```properties
    spring.application.name=Tracking Number Generator

    server.port=8080

    spring.datasource.url=jdbc:postgresql://localhost:5432/tracking_db
    spring.datasource.username=your-username
    spring.datasource.password=your-password
    spring.datasource.driver-class-name=org.postgresql.Driver
    spring.jpa.hibernate.ddl-auto=update
    ```
5. **The APIs will be accessible at**
   ```
   http://localhost:8080/api/tracking/next-tracking-number
   ```
6. **Testing the API**
   You can test the endpoint using curl :
   ```
   curl "http://localhost:8080/api/tracking/next-tracking-number?origin_country_id=MB&destination_country_id=IN&weight=1.234&customer_id=de619854-b59b-425e-9db4-943979e1bd49&order_created_at=2018-11-20T19:49:58+08:00&customer_name=RedBox Logistics&customer_slug=redbox-logistics"
   ```
   Or using Postman app
   ```
   http://localhost:8080/api/tracking/next-tracking-number?origin_country_id=MB&destination_country_id=IN&weight=1.234&customer_id=de619854-b59b-425e-9db4-943979e1bd49&order_created_at=2018-11-20T19:49:58+08:00&customer_name=RedBox Logistics&customer_slug=redbox-logistics
   ```   
   API Response
   ```
   {
    "trackingNumber": "MBINDE6198714599",
    "createdAt": "2025-03-18T13:35:29.840393300Z",
    "customerId": "de619854-b59b-425e-9db4-943979e1bd49",
    "weight": 1.234,
    "originCountryId": "MB",
    "destinationCountryId": "IN",
    "orderCreatedAt": "2018-11-20T19:49:59 08:00",
    "customerName": "RedBox Logistics",
    "customerSlug": "redbox-logistics"
   }
   ```
### Assumptions
1. The generated tracking number must satisfy the following:
   - It must match the regex pattern: ^[A-Z0-9]{1,16}$.
   - It must be unique; no duplicate tracking numbers should be generated.
   - The generation process should be efficient.
   - The system should be scalable, capable of handling concurrent requests, and should be able to scale horizontally.
2. The API should return a JSON object containing at least the following fields:
    - tracking_number: The generated tracking number.
    - created_at: The timestamp when the tracking number was generated (in RFC 3339 format) 
3. Unit tests & unit test coverage included using JUnit and Mockito
4. Validations are in place to ensure data integrity.
5. Best practices are followed to ensure Clean code, readability and maintainability.


