# Property Management Website - Frontend Client

## Overview
This is the frontend client for the Property Management platform, built using static **HTML, CSS, and Vanilla JavaScript** with **Tailwind CSS** styling via CDN. It provides a visual interface for Seekers (renters) to browse and book properties, Vendors (landlords) to manage listings, and Admins to approve vendors and view stats. I built this interface to be clean, fast, and responsive, ensuring that users have an intuitive experience without complicated React setups.

---

## Features
* **Role-Based Portals:** Dedicated pages that adjust depending on whether you log in as an Admin, a Vendor, or a Seeker.
* **Property Search & Filter:** Browse properties by categories with clean filter logic to find matching places quickly.
* **Interactive Dashboards:** Visual feedback loops, listing trackers, and admin charts powered by ApexCharts.
* **Role-Based Guards:** Dynamic guards in JavaScript that read JWT roles to redirect unauthorized users.
* **Responsive Layout:** Styled with Tailwind CSS to work smoothly across desktops, tablets, and mobile screens.

---

## Technologies Used
* **HTML5 & CSS3:** Standard HTML templates styled with CSS variables and Tailwind utilities to create clean visual themes.
* **Vanilla JavaScript:** Handle DOM manipulation, login events, and API fetch communications.
* **JWT Decode:** Manually parses authorization tokens inside JavaScript to enforce user role protection.
* **ApexCharts CDN:** Renders line charts and data analytics for the Admin panel.
* **Fetch API:** Handles API request calls and automatically attaches `access_token` headers.

---

## Project Structure
Here is how the frontend files are organized:
```text
property-management-frontend-main/
│
├── assets/
│   ├── css/
│   │   └── styles.css                 # Global styles, fonts, and animations
│   └── js/
│       └── app.js                     # Global API fetch wrapper, JWT parsing, and session guards
│
├── index.html                         # Platform landing page
├── login.html                         # Shared sign-in form for all users
├── register-user.html                 # Seeker sign-up form
├── register-vendor.html               # Vendor sign-up form
│
├── admin-dashboard.html               # Admin stats and bookings graph
├── admin-vendors.html                 # Vendor approval requests table
├── admin-categories.html              # Category and subcategory configurations
├── admin-properties.html              # Admin view of all properties and remarks
│
├── vendor-dashboard.html              # Landlord properties and status manager
├── vendor-bookings.html               # Accept or reject user booking requests
├── vendor-add-property.html           # Property publishing and image upload form
│
├── user-dashboard.html                # Seeker recommended/upcoming property tracks
├── user-categories.html               # Category list navigation page
├── user-properties.html               # Search results listings grid
├── user-property-detail.html          # Dynamic property gallery, specifications, and comments
├── user-wishlist.html                 # Seeker saved listings
└── user-bookings.html                 # Seeker booked listings status history
```

---

## Installation & Setup
Follow these steps to run the frontend client locally:

1. **Pre-requisites:** No complex dependencies or node modules are needed!
2. **Open Index Page:**
   * Simply double-click [index.html](file:///c:/Users/Deeksha/OneDrive/Documents/Project%202/property-management-frontend-main/index.html) to open it in your browser.
   * Alternatively, serve it using **Live Server** extension in VS Code or run any basic HTTP server:
     ```bash
     npx serve ./
     ```
   * The client application communicates directly with the Spring Boot backend listening at `http://localhost:9291`.

---

## Usage
* **Seeker Dashboard:** Sign up, navigate through property categories, view details, save properties to wishlist, and request bookings.
* **Vendor Dashboard:** Create property listings, upload images, and monitor booking inquiries from seekers.
* **Admin Dashboard:** Control category lists, manage pending vendor registration reviews, and inspect general dashboard statistics.

---

## Author
* **Developer:** Dhanya K
* **GitHub:** [@Dhanya562004](https://github.com/Dhanya562004)
* **Email:** [kdhanya762@gmail.com](mailto:kdhanya762@gmail.com)
