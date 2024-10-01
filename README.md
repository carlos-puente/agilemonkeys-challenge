# Introduction

This project was developed as a code challenge for The Agile Monkeys. It provides a REST API to
manage Customers and Users.

The API is built using Java with Spring Boot 3, and incorporates helpful tools such as MapStruct.
Customer and user data are stored in a PostgreSQL database, including customer profile pictures.
However, in a real-world application, storing images directly in a database is not ideal. Instead,
cloud storage solutions like Azure Blob Storage or a similar service would be more appropriate for
handling file uploads and downloads.

The project uses Maven for building and managing dependencies, as reflected in the project
structure.

### Requisites

This project was built using the following:

* Java JDK 17
* Apache Maven 3.9.5
* PostgreSQL 16.4
* IntelliJ IDEA (optional)
* Docker (optional, but recommended)
* GNU/Linux Ubuntu (optional, but recommended)
* git

Installation Guides

Here are some useful resources to help you install the required tools:

* SDKMAN! (for managing JDK & Maven) https://sdkman.io/usage/
* Install PostgreSQL on Ubuntu https://ubuntu.com/server/docs/install-and-configure-postgresql
* Install Docker on Ubuntu https://docs.docker.com/engine/install/ubuntu/

### Project structure and modules

The project is organized into four main modules:

- **agilemonkeys-challenge-base**: This module contains shared constants, exceptions, and utility
  functions, as well as our database components (entities, services, and repositories). The aim is
  to separate the model/domain from the business logic of the API.

- **agilemonkeys-challenge-auth**: This module is dedicated to our Spring Security configuration,
  handling authentication and authorization.

- **agilemonkeys-challenge-api**: This module serves as the core of our API, housing the endpoints
  and services that implement the business logic.

- **coverage**: This module generates a report on the test coverage for the project.

### Rationale for Structure

This modular structure is designed for simplicity. In a production environment, the project could be
further divided into several microservices, such as:

1. **agilemonkeys-challenge-public-api**: This service would expose public endpoints and manage
   security (authentication and authorization), accessible from the internet.

2. **agilemonkeys-challenge-customers**: This service would handle all customer-related
   functionalities, accessible only through the public API.

3. **agilemonkeys-challenge-users**: Similar to the customers module, this service would manage all
   user-related functionalities, also accessible only through the public API.

4. **agilemonkeys-common-library**: This module would contain shared elements, utilities, and client
   interfaces used across other modules.

### Endpoints

#### Auth Controller

This controller contains two endpoints to manage our authentication, one for user signup and another
for user login.

1. <code style="color : brown">POST</code> **`http://localhost:2801/auth/signup`**  
   *Not required in the requirements but useful for testing the register & authentication. It allows
   users to register with the role: `ROLE_USER`.*

   **Payload:**
   ```json
   {
       "username": "admin@admin.com",
       "password": "TEST!admin123",
       "fullName": "admin"
   }       
   ```

   **Response:**
   ```
   User created: admin@admin.com
   ```

2. <code style="color : brown">POST</code> **`http://localhost:2801/auth/login`**  
   *Handles our authentication and provides a JWT token to authorize our users. The response also
   contains the expiration time (2 hours).*

   **Payload:**
   ```json
   {
       "username": "admin@admin.com",
       "password": "TEST!admin123"
   }
   ```

   **Response:**
   ```json
   {
       "accessToken": "eyJhbGciOiJIUzI1NiJ9.ey[TRUNCATED FOR BREVITY]",
       "expiresIn": 3600000
   }
   ```

#### Customer Controller

This controller contains several endpoints to fetch and manage customer data. It is accessible by
users with `ROLE_USER` or `ROLE_ADMIN`.

1. <code style="color : brown">POST</code> **`http://localhost:2801/api/v1/customer`**  
   *Retrieves customer data. Although a `listAll` was required in the specifications, it has been
   replaced with an advanced search endpoint that supports request body filters and pagination. This
   endpoint supports pagination, sorting, quick search, and includes several filters (firstName,
   lastName, email, phoneNumber, and id).*

   **Payload:**
   ```json
   {
       "_page": "1",
       "_limit": "10",
       "_sort": "lastName",
       "_order": "asc",
       "q": "david"
   }
   ```

   **Response:**
   ```json
   {
       "content": [
           {
               "id": 114,
               "firstName": "David",
               "lastName": "Smith",
               "email": "david2576.smith2332@outlook.com",
               "phoneNumber": "615530523",
               "picture": "picture_77.jpg",
               "createdBy": "TESTDATA",
               "creationDate": "2024-07-16",
               "lastModifiedBy": "SystemUser",
               "lastModifiedDate": "2024-07-16"
           },
           {
               "id": 92,
               "firstName": "David",
               "lastName": "Rodriguez",
               "email": "david2727.rodriguez2178@outlook.com",
               "phoneNumber": "617409045",
               "picture": "picture_55.jpg",
               "createdBy": "TESTDATA",
               "creationDate": "2024-08-07",
               "lastModifiedBy": "SystemUser",
               "lastModifiedDate": "2024-08-07"
           },
           {
               "id": 103,
               "firstName": "David",
               "lastName": "Garcia",
               "email": "david1213.garcia1355@gmail.com",
               "phoneNumber": "658301134",
               "picture": "picture_66.jpg",
               "createdBy": "TESTDATA",
               "creationDate": "2024-07-27",
               "lastModifiedBy": "SystemUser",
               "lastModifiedDate": "2024-07-27"
           },
           {
               "id": 128,
               "firstName": "David",
               "lastName": "Anderson",
               "email": "david287.anderson1606@outlook.com",
               "phoneNumber": "610569389",
               "picture": "picture_91.jpg",
               "createdBy": "TESTDATA",
               "creationDate": "2024-07-02",
               "lastModifiedBy": "SystemUser",
               "lastModifiedDate": "2024-07-02"
           }
       ],
       "metaData": {
           "currentPage": 1,
           "pageSize": 10,
           "totalPages": 1,
           "totalElements": 4
       }
   }
   ```

2. <code style="color : green">GET</code> **`http://localhost:2801/api/v1/customer/{id}`**  
   *Obtains detailed information about a customer.*

   **Example:** `http://localhost:2801/api/v1/customer/128`

   **Response:**
   ```json
   {
       "id": 128,
       "firstName": "David",
       "lastName": "Anderson",
       "email": "david287.anderson1606@outlook.com",
       "phoneNumber": "610569389",
       "picture": "picture_91.jpg",
       "createdBy": "TESTDATA",
       "creationDate": "2024-07-02",
       "lastModifiedBy": "SystemUser",
       "lastModifiedDate": "2024-07-02"
   }
   ```

3. <code style="color : brown">POST</code> **`http://localhost:2801/api/v1/customer/create`**  
   *Creates a new customer. Only customer data is required; to upload their picture, a separate
   upload endpoint is available.*

   **Payload:**
   ```json
   {
       "firstName": "testFirstName",
       "lastName": "testLastName",
       "email": "email@test.com",
       "phoneNumber": "625535541"
   }
   ```

   **Response:**
   ```json
   {
       "id": 138,
       "firstName": "testFirstName",
       "lastName": "testLastName",
       "email": "email@test.com",
       "phoneNumber": "625535541",
       "picture": null,
       "createdBy": "admin@admin.com",
       "creationDate": "2024-10-01",
       "lastModifiedBy": "admin@admin.com",
       "lastModifiedDate": "2024-10-01"
   }
   ```

4. <code style="color : green">GET</code> *
   *`http://localhost:2801/api/v1/customer/{id}/picture/download`**  
   *Downloads the picture of the specified customer.*

   **Example:** `http://localhost:2801/api/v1/customer/128/picture/download`

5. <code style="color : brown">POST</code> *
   *`http://localhost:2801/api/v1/customer/{id}/picture/upload`**  
   *Uploads a picture for the specified customer. The endpoint uses a form-data body and expects a "
   picture" field with the file.*

   **cURL Example:**
   ```bash
   curl --location 'http://localhost:2801/api/v1/customer/128/picture/upload' \
       --header 'Authorization: ••••••' \
       --form 'picture=@"/home/carlos/Descargas/avatar.png"'
   ```

6. <code style="color : blue">PUT</code> **`http://localhost:2801/api/v1/customer/update`**  
   *Updates the data of a customer. The customer ID is mandatory. Fields that are not included or
   are null will be skipped.*

   **Payload:**
   ```json
   {
       "id": 128,
       "firstName": "testFirstNameUPDATED",
       "lastName": "testLastNameUPDATED",
       "email": "email@test.comUPDATED",
       "phoneNumber": "62553554wwww1"
   }
   ```

   **Response:**
   ```json
   {
       "id": 128,
       "firstName": "testFirstNameUPDATED",
       "lastName": "testLastNameUPDATED",
       "email": "email@test.comUPDATED",
       "phoneNumber": "62553554wwww1",
       "picture": "picture_91.jpg",
       "createdBy": "TESTDATA",
       "creationDate": "2024-07-02",
       "lastModifiedBy": "admin@admin.com",
       "lastModifiedDate": "2024-10-01"
   }
   ```

7. <code style="color : orangered">DELETE</code> *
   *`http://localhost:2801/api/v1/customer/{id}/delete`**  
   *Removes (hard delete) a customer. The user is permanently removed from the database.*

   **Example:** `http://localhost:2801/api/v1/customer/128/delete`  
   *Returns no content.*

#### User Controller

The User Controller provides endpoints for fetching and managing user data. Access to these
endpoints is restricted to Admin users (those with the `ROLE_ADMIN` authority).

1. <code style="color : brown">POST</code> <b>http://localhost:2801/api/v1/user </b> -> Retrieves a
   paginated response containing user data. This endpoint supports pagination, sorting, quick
   search, and various filters (such as `username`, `fullName`, and `isAdmin`).

   **Payload:**
   ```json
   {
     "_page": "1",
     "_limit": "5"
   }
   ```

   **Response:**
   ```json
   {
     "content": [
       {
         "id": 1,
         "username": "admin@admin.com",
         "fullName": "admin",
         "roles": [
           "ROLE_ADMIN"
         ],
         "createdBy": "SIGNUP-PROCESS",
         "creationDate": "2024-09-30",
         "lastModifiedBy": "SIGNUP-PROCESS",
         "lastModifiedDate": "2024-09-30"
       },
       {
         "id": 2,
         "username": "user1",
         "fullName": "User Full Name 1",
         "roles": [
           "ROLE_USER"
         ],
         "createdBy": "admin",
         "creationDate": "2024-10-01",
         "lastModifiedBy": "admin",
         "lastModifiedDate": "2024-10-01"
       },
       {
         "id": 11,
         "username": "user10",
         "fullName": "User Full Name 10",
         "roles": [
           "ROLE_USER"
         ],
         "createdBy": "admin",
         "creationDate": "2024-10-01",
         "lastModifiedBy": "admin",
         "lastModifiedDate": "2024-10-01"
       },
       {
         "id": 12,
         "username": "user11",
         "fullName": "User Full Name 11",
         "roles": [
           "ROLE_USER"
         ],
         "createdBy": "admin",
         "creationDate": "2024-10-01",
           "lastModifiedBy": "admin",
           "lastModifiedDate": "2024-10-01"
       },
       {
         "id": 13,
         "username": "user12",
         "fullName": "User Full Name 12",
         "roles": [
           "ROLE_ADMIN"
         ],
         "createdBy": "admin",
         "creationDate": "2024-10-01",
         "lastModifiedBy": "admin",
         "lastModifiedDate": "2024-10-01"
       }
     ],
     "metaData": {
       "currentPage": 1,
       "pageSize": 5,
       "totalPages": 7,
       "totalElements": 31
     }
   }
   ```

This structured approach provides a clear overview of the endpoint's functionality, payload
requirements, and expected response format.

2. <code style="color : green">GET</code> <b>http://localhost:2801/api/v1/user/{{id}} </b> ->
   Retrieves the information of a user with the specified ID.

   **Example:**
   ```
   http://localhost:2801/api/v1/user/1
   ```

   **Response:**
   ```json
   {
     "id": 1,
     "username": "admin@admin.com",
     "fullName": "admin",
     "roles": [
       "ROLE_ADMIN"
     ],
     "createdBy": "SIGNUP-PROCESS",
     "creationDate": "2024-09-30",
     "lastModifiedBy": "SIGNUP-PROCESS",
     "lastModifiedDate": "2024-09-30"
   }
   ```

3. <code style="color : brown">POST</code> <b>http://localhost:2801/api/v1/user/create </b> ->
   Creates a new user.

   **Payload:**
   ```json
   {
     "username": "testUserCreated",
     "password": "testPassword!1234",
     "fullName": "testCarlosUser",
     "roles": ["ROLE_USER", "ROLE_ADMIN"]
   }
   ```

   **Response:**
   ```json
   {
     "id": 32,
     "username": "testUserCreated",
     "fullName": "testCarlosUser",
     "roles": [
       "ROLE_USER",
       "ROLE_ADMIN"
     ],
     "createdBy": "admin@admin.com",
     "creationDate": "2024-10-01",
     "lastModifiedBy": "admin@admin.com",
     "lastModifiedDate": "2024-10-01"
   }
   ```

4. <code style="color : blue">PUT</code> <b>http://localhost:2801/api/v1/user/update </b> -> Updates
   an existing user. The changeable fields are: `fullName`, `roles`, and `password`. Fields not
   included in the request will be skipped.

   **Payload:**
   ```json
   {
     "id": 32,
     "password": "testPassword2!Asd23",
     "fullName": "testCarlosUser21",
     "roles": ["ROLE_ADMIN"]
   }
   ```

   **Response:**
   ```json
   {
     "id": 32,
     "username": "testUserCreated",
     "fullName": "testCarlosUser21",
     "roles": [
       "ROLE_ADMIN"
     ],
     "createdBy": "admin@admin.com",
     "creationDate": "2024-10-01",
     "lastModifiedBy": "admin@admin.com",
     "lastModifiedDate": "2024-10-01"
   }
   ```

5. <code style="color : DarkBlue">
   PATCH</code> <b> http://localhost:2801/api/v1/user/{{id}}/update/admin-status </b> -> Changes the
   admin status of a user with the given ID.

   **Example URL (Set Admin Status to True):**
   ```
   http://localhost:2801/api/v1/user/11/update/admin-status?isAdmin=true
   ```

   **Response:**
   ```json
   {
     "id": 11,
     "username": "user10",
     "fullName": "User Full Name 10",
     "roles": [
       "ROLE_USER",
       "ROLE_ADMIN"
     ],
     "createdBy": "admin",
     "creationDate": "2024-10-01",
     "lastModifiedBy": "admin",
     "lastModifiedDate": "2024-10-01"
   }
   ```

   **Example URL (Set Admin Status to False):**
   ```
   http://localhost:2801/api/v1/user/11/update/admin-status?isAdmin=false
   ```

   **Response:**
   ```json
   {
     "id": 11,
     "username": "user10",
     "fullName": "User Full Name 10",
     "roles": [
       "ROLE_USER"
     ],
     "createdBy": "admin",
     "creationDate": "2024-10-01",
     "lastModifiedBy": "admin",
     "lastModifiedDate": "2024-10-01"
   }
   ```


6. <code style="color : Orangered">
   DELETE</code> <b>http://localhost:2801/api/v1/user/{{id}}/delete</b> -> Deletes a user with the
   given ID.

   **Example URL:**
   ```
   http://localhost:2801/api/v1/user/11/delete
   ```

   *Returns no content.*

#### Error Handling

The application handles most errors comprehensively, returning detailed responses that provide
essential information for debugging and user feedback. For example, a response for a user not found
error might look like this:

```json
{
  "message": [
    "User not found with the given id."
  ],
  "timestamp": "2024-10-01T15:05:46.023141038",
  "status": "404 NOT_FOUND",
  "errorCode": "USER_NOT_FOUND"
}
```

Each error response includes the following components:

- **message**: A descriptive message indicating the nature of the error.
- **timestamp**: The exact time when the error occurred, formatted in ISO 8601.
- **status**: The HTTP status code indicating the error type (e.g., `404 NOT_FOUND`).
- **errorCode**: A specific error code that can be used by the UI to display meaningful messages to
  the application user.

This structured approach helps ensure that users and developers can quickly understand and address
issues as they arise.

### SetUp

#### IntelliJ Local Execution

1. ** Dowload repository **
   ```bash
   $ git clone https://github.com/carlos-puente/agilemonkeys-challenge.git
      ```

2. **Create the Store Schema in PostgreSQL**  
   Liquibase will handle the creation of application tables, but you need to manually create the
   schema before launching the application and triggering Liquibase. Please run the following SQL
   command:
   ```sql
   CREATE SCHEMA store;
   ```

2. **Add Environment Variables in IntelliJ**  
   Configure the `AgileMonkeysChallengeApplication` with the following environment variables:
   ```plaintext
   database-password=testPassword;
   database-url=jdbc:postgresql://localhost:5432/store;
   database-username=postgres;
   jwt-secret=9a4f2c8d3b7a1e6f45c8a0b3f267d8b1d4e6f3c8a9d2b5f8e3a9c8b5f6v8a3d9;
   schema-name=store;
   ```

3. **Maven Clean & Install**  
   Perform a Maven clean and install for the entire project. You can either use the Maven options
   within IntelliJ IDEA or execute the following command in your terminal:
   ```bash
   $ mvn clean install
   ```

4. **Launch the Project**  
   Start the application in IntelliJ IDEA.

5. **Create User Using `/auth/signup`**  
   Register a new user via the signup endpoint.

6. **Change User Authorities in the Database**  
   To convert the user to an admin, execute the following SQL command:
   ```sql
   UPDATE public.user_roles
   SET role_sk = (SELECT sk FROM public.roles WHERE name = 'ROLE_ADMIN')
   WHERE user_sk = (SELECT sk FROM public."user" WHERE username = 'admin@admin.com');
   ```

7. **Login and Use the API**  
   Now you can log in and start using the API!

---

#### Using Docker & Docker Compose

As an alternative, you can run the application using Docker. Follow these steps:

1. **Maven Clean & Install**  
   Execute a Maven clean and install for the project, preferably using an IDE like IntelliJ IDEA.

2. **Navigate to the Project Directory**  
   Open a terminal and change to the directory:
   ```bash
   $ cd YourPath/agilemonkeys-challenge/agilemonkeys-challenge-api
   ```

3. **Run Docker Build**  
   Build the Docker image with the following command:
   ```bash
   $ docker build --build-arg VERSION=0.0.1-SNAPSHOT -t agilemonkeys-api .
   ```

4. **Change to the Docker Compose Directory**  
   Enter the docker-compose folder:
   ```bash
   $ cd docker-compose
   ```

5. **Create a `.env` File**  
   Create a `.env` file with the following environment variables:
   ```plaintext
   POSTGRESDB_USER=postgres
   POSTGRESDB_ROOT_PASSWORD=123456
   POSTGRESDB_DATABASE=store
   POSTGRESDB_LOCAL_PORT=5433
   POSTGRESDB_DOCKER_PORT=5432
   SECRET_KEY=9a4f2c8d3b7a1e6f45c8a0b3f267d8b1d4e6f3c8a9d2b5f8e3a9c8b5f6v8a3d9
   SPRING_LOCAL_PORT=2801
   SPRING_DOCKER_PORT=2801
   ```

6. **Run Docker Compose**  
   Execute the following command to launch the PostgreSQL database and the API application:
   ```bash
   $ docker compose up
   ```

With these steps completed, your setup should be ready for development and testing.

#### Test Data

In the file `src/test/java/me/carlosjai/agilemonkeyschallenge/api/util/TestDataGenerator.java`,
there are two disabled tests designed to generate customer and user database inserts. You can enable
these tests to generate the sql insertas, and populate your database with sample data for testing
purposes.




