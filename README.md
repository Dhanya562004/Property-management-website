# Property Management Website

## Overview
I built this Property Management project to solve a very common problem: the hassle of renting and managing properties. Instead of keeping track of everything in messy spreadsheets, notes, or scattered chat messages, this web application serves as a single platform where seekers (tenants), vendors (landlords), and admins can easily interact. Seekers can browse and book properties, vendors can list their apartments or homes, and admins can keep the platform clean and verified.

---

## Features
Here is what the application can do:
* **Three User Roles:** Different dashboards and features for Seekers, Vendors, and Admins.
* **Property Listings & Searches:** Users can browse listings, filter by categories, and request bookings.
* **Vendor Approval System:** Landlords can register, but they can only post properties once the Administrator verifies and approves them.
* **Booking Management:** Landlords can keep track of incoming inquiries and booking requests for their properties.
* **Email Alerts:** The backend automatically sends email notifications whenever a booking is created or updated.
* **Role-Based Security:** Frontend routes are protected using client-side checks and JWT tokens so unauthorized users cannot access restricted dashboards.

---

## Technologies Used
For this project, I chose a modern full-stack web development suite:

### Frontend Client
* **HTML5 & CSS3:** Standard semantic markup and design layout.
* **Tailwind CSS (via CDN):** Modern styling and responsive components.
* **Vanilla JavaScript (ES6+):** Script engine handling DOM manipulation, event routing, session status, and page checks.
* **Fetch API:** Asynchronous API fetch client with middleware attachment.
* **ApexCharts (via CDN):** Line and bar chart visualizations for platform analytics.

### Backend API & Database
* **Java (JDK 17) & Spring Boot (v3.2.5):** Platform architecture core executing controllers, service interceptors, and data management.
* **Spring Data JPA & Hibernate:** Object-Relational Mapping (ORM) to automatically coordinate schema updates.
* **MySQL:** Main relational database storing profiles, categories, bookings, properties, and review data.
* **jjwt (JSON Web Token for Java):** Lightweight package compiling secure session payload access tokens.
* **Spring Security Crypto:** Encrypts user passwords using secure BCrypt hashing.
* **Spring Boot Starter Mail:** Dispatches booking confirmation notifications via SMTP mailers.
* **Maven:** Builds, compiles, and packages application dependencies.

---

## Project Structure
Here is how the repository is organized:
```text
Property-Management-Website/
│
├── property-management-frontend-main/     # Static Frontend application (HTML, CSS, JS)
│   ├── assets/                            # Global stylesheets (styles.css) and script logic (app.js)
│   └── *.html                             # HTML pages for all portals (Index, Login, Admin, Vendor, User)
│
└── property-mangement-backend-main/      # Spring Boot backend API application (Java)
    ├── src/                               # Java source files (controllers, services, repositories)
    ├── seed.sql                           # Database seeding file containing initial admin and categories
    └── pom.xml                            # Maven configuration file for dependencies
```

---

## Installation & Setup
To run this project locally, follow these steps:

### Backend Setup (Spring Boot)
1. Ensure you have **Java JDK 17** (or newer) and **MySQL Server** installed.
2. Create a database named `property_db` in your MySQL server.
3. Open the backend project and locate the `src/main/resources/application.properties` file. Configure it with your database credentials:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/property_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
   spring.datasource.username=your_mysql_username
   spring.datasource.password=your_mysql_password
   spring.jpa.hibernate.ddl-auto=update
   ```
4. Seed the database by executing the SQL statements inside `seed.sql` on your MySQL server to create the default categories and the default administrator user (`admin@property.com`, password `Admin@123`).
5. Run the backend application:
   ```bash
   mvn spring-boot:run
   ```

### Frontend Setup
1. No Node.js installation is required for the frontend.
2. Simply double-click `property-management-frontend-main/index.html` to open it in your browser, or run a lightweight local HTTP server (such as the VS Code Live Server extension, or `npx serve ./`).
3. The frontend is pre-configured to communicate with the local Spring Boot API server running at `http://localhost:9291`.

---

## Usage
* **As a Seeker:** Register an account, browse different property categories, use filters to search, and submit booking requests or reviews.
* **As a Vendor:** Sign up as a property owner. Once approved by the administrator, you can list your properties, upload images, and manage incoming inquiries.
* **As an Admin:** Log in with admin credentials (`admin@property.com` / `Admin@123`) to create categories, approve pending vendor applications, and view platform usage statistics.

---

## Future Improvements
Here are some features I plan to work on next:
* **Payment Integration:** Integrating Stripe or PayPal so seekers can pay booking deposits directly through the app.
* **In-app Chat:** Adding real-time messaging using WebSockets so renters and landlords can chat directly.
* **Map Integration:** Using Google Maps or Leaflet API to show property locations directly on a map.
* **Advanced Search Filters:** Adding filters based on specific amenities (like WiFi, parking, or pool) and price ranges.

---

## Challenges & Learning
Developing this project was a huge learning curve for me. Here are the key things I learned and solved:
* **Database Relationships:** Designing the database schema in MySQL and setting up correct Hibernate relationships (One-to-Many/Many-to-Many) between users, properties, and bookings taught me a lot about relational data modeling.
* **Role-Based Security:** Implementing secure routing and validating users' roles on both the Vanilla JS frontend and Spring Boot backend was tricky, but I got a good grasp of how JWTs are parsed and checked.
* **Vanilla JS Refactoring:** Refactoring the frontend from React to pure HTML/CSS/Vanilla JS taught me how to manage DOM events, write custom router guards, and establish clean API fetch communication dynamically.

---

## Contributing
If you'd like to contribute, feel free to do so!
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
