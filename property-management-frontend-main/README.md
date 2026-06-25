# Property Management Website - Frontend Client

## Overview
This is the frontend React client for the Property Management platform. It provides a visual interface for Seekers (renters) to browse and book properties, Vendors (landlords) to manage listings, and Admins to approve vendors and view stats. I built this interface to be clean, fast, and responsive, ensuring that users have an intuitive experience without complicated layouts.

---

## Features
* **Role-Based Views:** Dynamic views and sidebars that adjust depending on whether you log in as an Admin, a Vendor, or a Seeker.
* **Property Search & Filter:** Browse properties by categories with clean filter logic to find matching places quickly.
* **Interactive Dashboards:** Visual feedback loops, listing trackers, and admin charts powered by React charts.
* **Client-Side Route Protection:** Wrappers around React Router paths that read JWT roles to block unauthorized users from restricted pages.
* **Responsive Layout:** Designed to work smoothly across desktops, tablets, and mobile screens.

---

## Technologies Used
* **HTML & CSS:** Standard HTML templates styled with CSS and custom Tailwind utilities to create clean visual themes.
* **JavaScript (React):** The foundation for building reusable UI components and managing application states.
* **React Router:** For seamless, client-side page routing and role-based route guard wrapping.
* **Axios:** Used to handle clean HTTP request calls and inject user JWT tokens in Authorization headers.

---

## Project Structure
Here is how the frontend files are organized:
```text
property-management-frontend-main/
│
├── public/                            # Static assets and index.html template
└── src/
    ├── apiServices/                   # Centralized Axios request wrappers
    ├── auth/                          # Login and registration view components
    ├── components/                    # Core UI components (navbars, sidebars, tables)
    ├── layouts/                       # Layout containers for dashboards
    ├── views/                         # Dashboard pages grouped by roles (admin, users, vendors)
    ├── App.js                         # Central router and role route protection mapping
    ├── index.js                       # Entry point mounting the React app
    └── index.css                      # Global styles and Tailwind configuration imports
```

---

## Installation & Setup
Follow these steps to run the frontend client locally:

1. **Pre-requisites:** Make sure you have **Node.js** (v18 or newer recommended) installed.
2. **Navigate to Directory:**
   ```bash
   cd property-management-frontend-main
   ```
3. **Install Packages:**
   ```bash
   npm install
   ```
4. **Run the Project:**
   ```bash
   npm start
   ```
   The application will compile and launch automatically at `http://localhost:3000`.

---

## Usage
* **Seeker Dashboard:** Sign up, navigate through property categories, view details, leave reviews, and request bookings.
* **Vendor Dashboard:** Create property listings, upload images, and monitor booking inquiries from seekers.
* **Admin Dashboard:** Control category lists, manage pending vendor registration reviews, and inspect general dashboard statistics.

---

## Future Improvements
* **Interactive Map Views:** Integrating Leaflet or Google Maps to show listing pins directly on a map interface.
* **Dark Mode Toggle:** Adding support for global CSS themes to allow users to switch to dark mode.
* **Offline Access:** Caching previously loaded listings locally to permit viewing without network connection.

---

## Challenges & Learning
* **Protected Routing:** Implementing React Router guards that dynamically intercept route transitions and verify role permissions from encrypted JWT storage was a challenging but rewarding logic task.
* **State Management:** Keeping navbar statuses, user sessions, and listing views synchronized across multiple dashboard pages helped improve my React state management skills.

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
