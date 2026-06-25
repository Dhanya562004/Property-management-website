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
* **Java & Spring Boot:** The backend API is built using Spring Boot. It handles all our REST endpoints, user authentication, role authorizations, and database transactions.
* **MySQL:** I used MySQL as our main relational database. It stores structured tables for users, properties, bookings, and categories, maintaining relationship integrity.
* **HTML & CSS:** The front-end user interface is designed using HTML and clean, responsive CSS (along with Tailwind styling) to make the experience smooth and modern.
* **JavaScript (React):** The interactive client application is built with React.js, making the UI snappy and fast by handling states efficiently.

---

## Project Structure
Here is how the repository is organized:
```text
Property-Management-Website/
│
├── property-management-frontend-main/     # React.js client application (HTML, CSS, JS)
│   ├── public/                            # Static assets and index.html
│   └── src/                               # React source code (components, layouts, views)
│
└── property-mangement-backend-main/      # Spring Boot backend API application (Java)
    ├── src/                               # Java source files (controllers, services, repositories)
    └── pom.xml                            # Maven configuration file for dependencies
```

---

## Installation & Setup
To run this project locally, follow these steps:

### Backend Setup (Spring Boot)
1. Ensure you have **Java JDK 17** (or newer) and **MySQL Server** installed.
2. Create a database named `property_db` in your MySQL server.
3. Open the backend project and locate the `src/main/resources/application.properties` (or `application.yml`) file. Configure it with your database credentials:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/property_db
   spring.datasource.username=your_mysql_username
   spring.datasource.password=your_mysql_password
   spring.jpa.hibernate.ddl-auto=update
   ```
4. Run the backend application:
   ```bash
   ./mvnw spring-boot:run
   ```

### Frontend Setup (React)
1. Ensure you have **Node.js** installed.
2. Navigate to the frontend directory:
   ```bash
   cd property-management-frontend-main
   ```
3. Install the dependencies:
   ```bash
   npm install
   ```
4. Start the React development server:
   ```bash
   npm start
   ```
5. Open your browser and go to `http://localhost:3000` to interact with the application.

---

## Usage
* **As a Seeker:** Register an account, browse different property categories, use filters to search, and submit booking requests or reviews.
* **As a Vendor:** Sign up as a property owner. Once approved by the administrator, you can list your properties, upload images, and manage incoming inquiries.
* **As an Admin:** Log in with admin credentials to create categories, approve pending vendor applications, and view platform usage statistics.

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
* **Role-Based Security:** Implementing secure routing and validating users' roles on both the React frontend and Spring Boot backend was tricky, but I got a good grasp of how JWTs are parsed and checked.
* **Component Reusability:** Learning how to structure components in React so they could be shared across Seeker, Vendor, and Admin dashboards saved me a lot of duplicate code.

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
