# Property Management Website - Backend API

## Overview
This is the backend API service for the Property Management platform. It acts as the core brain of the application, managing user accounts, authentication sessions, database records, email alerts, and property information. I built this backend to provide a secure and structured RESTful API layer that our frontend client can communicate with.

---

## Features
Here are the main features implemented in this backend service:
* **RESTful Endpoints:** Dedicated endpoints for login, user/vendor registration, property listings, and bookings.
* **Role-Based Guards:** API requests are checked using authorization guards to ensure only validated Seekers, Vendors, or Admins can access their respective features.
* **Database Management:** Handles entity mappings and structured relationships between users, listings, categories, and bookings.
* **Automated Notification Mailer:** Built-in mail service that dispatches email confirmations whenever a booking status changes or an inquiry is sent.
* **Secure Sessions:** Utilizes JSON Web Token (JWT) signing and verification to maintain stateless authentication.

---

## Technologies Used
* **Java & Spring Boot:** The framework used to compile, run, and host the REST API service.
* **MySQL:** The relational database system used to store and query the system data.
* **Hibernate / Spring Data JPA:** Used as the Object-Relational Mapping (ORM) framework to interact with MySQL cleanly.
* **Java Mail Sender:** Integrated to handle transactional emails.
* **JWT (JSON Web Token):** Standard library for generating secure tokens for role authentication.

---

## Project Structure
Here is how the backend project files are structured:
```text
property-mangement-backend-main/
│
├── Project.postman_collection.json    # Reference Postman collection for API endpoints
├── pom.xml                            # Maven project configuration file
└── src/
    └── main/
        ├── java/com/property/
        │   ├── controllers/           # REST endpoints (auth, properties, bookings)
        │   ├── models/                # Entity models (User, Property, Booking, Category)
        │   ├── repositories/          # Spring Data JPA repositories
        │   └── services/              # Business logic (auth, email, property validation)
        └── resources/
            └── application.properties # Spring configuration (DB credentials, server port)
```

---

## Installation & Setup
Follow these steps to set up the backend locally:

1. **Pre-requisites:** Make sure you have **Java JDK 17** (or newer) and **MySQL Server** installed.
2. **Database Configuration:**
   * Create a database named `property_db` in your MySQL database.
   * Open `src/main/resources/application.properties` and add your database settings:
     ```properties
     spring.datasource.url=jdbc:mysql://localhost:3306/property_db
     spring.datasource.username=your_mysql_username
     spring.datasource.password=your_mysql_password
     spring.jpa.hibernate.ddl-auto=update
     ```
3. **Run the API:**
   Run the application from your IDE or use Maven in the terminal:
   ```bash
   ./mvnw spring-boot:run
   ```
   The backend server will launch and listen for requests.

---

## Usage
Once the server is running, you can test the API endpoints using the included Postman collection:
* Import `Project.postman_collection.json` into Postman.
* Register a user with `POST /api/auth/register`.
* Log in with `POST /api/auth/login` to obtain a JWT token.
* Include the token in the `Authorization` header for protected calls, like booking a property or adding listings.

---

## Future Improvements
* **Secure Payment Integration:** Adding Stripe API controllers on the backend to verify and process booking payments.
* **WebSockets for Chat:** Setting up a Socket handler to manage real-time text communications between tenants and landlords.
* **Caching with Redis:** Adding a cache layer for property listings to speed up search queries and reduce database load.

---

## Challenges & Learning
* **Relational Mapping:** Setting up Hibernate annotations (like `@ManyToOne` and `@OneToMany`) and resolving circular dependencies during JSON serialization was a challenge that taught me a lot about relational data modeling.
* **Custom Security Configurations:** Configuring Spring Security to guard specific endpoints while leaving public listings open was a great learning experience.

---

## Contributing
1. Fork this repository.
2. Create your feature branch (`git checkout -b feature/AmazingFeature`).
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`).
4. Push to the branch (`git push origin feature/AmazingFeature`).
5. Open a Pull Request.

---

## Author
* **Developer:** Dhanya K
* **GitHub:** [@Dhanya562004](https://github.com/Dhanya562004)
* **Email:** [kdhanya762@gmail.com](mailto:kdhanya762@gmail.com)
