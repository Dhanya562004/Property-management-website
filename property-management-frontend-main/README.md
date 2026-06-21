# Property Management System - Frontend Client

This directory contains the user interface and client-side logic for the Property Management System. Built using **React.js (v18)** and styled with **Tailwind CSS / Material Tailwind**, it provides a modern, responsive portal for seekers, vendors, and admins.

## Overview
The frontend client interfaces with the backend REST API to deliver three independent user experiences. It handles visual navigation, client-side route protection based on user roles, interactive analytics charts, and form validations for property and user listing creations.

## Features
- **Responsive Navigation**: Adaptive Navbars, Sidebars, and layout dashboards for Admin, Vendor, and Seeker views.
- **Protected Routes**: Custom routing logic wrapping React Router to restrict access to pages based on JWT roles.
- **Analytics Visualization**: Interactive line and bar charts using React ApexCharts for administrators to monitor user registrations and bookings.
- **Dynamic Forms**: Listing uploading forms that support multi-part file uploads (images) and validation.
- **Toast Notifications**: Non-intrusive alert popups using React Hot Toast to confirm user actions or show errors.

## Technologies Used
- **React.js (v18)** - Core frontend library
- **React Router DOM (v6)** - Client routing
- **Tailwind CSS & Material Tailwind React** - Styling & components
- **React ApexCharts** - Visual dashboard graphs
- **Axios** - HTTP client
- **React Hot Toast** - Client-side alert notifications

## Project Structure
```text
src/
├── apiServices/          # Axios wrappers that handle authorization headers and requests
├── auth/                 # Login, User Signup, and Vendor Signup pages
├── components/           # Reusable UI elements (Navbars, Sidebars, Footers)
├── layouts/              # Main layout wrappers for dashboard routing
├── views/                # Role-specific views and control dashboards:
│   ├── admin/            # Admin controls (category setup, vendor verification, stats)
│   ├── users/            # Seeker views (listings browser, booking request, wishlist)
│   └── vendors/          # Vendor views (property forms, booking tables)
└── config.json           # Stores backend server URL configuration
```

## Installation & Setup
1. Open a terminal in this directory:
   ```bash
   cd property-management-frontend-main
   ```
2. Install the node dependencies:
   ```bash
   npm install
   ```
3. Verify connection configuration in `src/config.json`:
   ```json
   {
     "url": "http://localhost:9291"
   }
   ```
4. Start the development server:
   ```bash
   npm start
   ```

## Usage
- Run the backend server first before booting the React client.
- When running locally, visit `http://localhost:3000` to view the site.
- Create user accounts or vendor profiles to explore distinct features.

## Future Improvements
- Implement responsive dark mode support.
- Cache loaded property listings using React Query to optimize performance.
- Add client-side internationalization (i18n) for multi-language support.

## Author
**Dhanya K**
- GitHub: [@Dhanya562004](https://github.com/Dhanya562004)
- Email: [kdhanya762@gmail.com](mailto:kdhanya762@gmail.com)

## License
Distributed under the ISC License.
