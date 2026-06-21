# Property Management Hub

## Overview
Property Management Hub is a full-stack, role-based web application designed to connect property seekers, independent vendors (property owners), and platform administrators. Built with a React frontend and a Node.js/Express REST API, the platform provides tailored dashboards and workflows for listing, searching, reviewing, and booking rental properties. It simplifies the real estate discovery process by organizing listings cleanly, handling secure roles, and managing media uploads.

## Features
- **User Dashboard**: Seekers can search properties by category, add listings to a wishlist, submit booking requests, and post reviews.
- **Vendor Dashboard**: Property owners can list new properties, upload images, and view all bookings made for their properties.
- **Admin Dashboard**: Administrators can approve new vendors, manage taxonomy categories/subcategories, and view platform metrics via analytical charts.
- **Media Uploads**: Direct property listing image uploads securely integration-configured using AWS S3.
- **Email Confirmation**: Automatic, transactional notifications sent to users via Nodemailer when a booking is created.
- **Role-Based Auth**: Secure authorization using JSON Web Tokens (JWT) and Bcrypt-based password hashing.

## Technologies Used
- **Frontend**: React.js (v18), React Router DOM (v6), Tailwind CSS, Material Tailwind React, Axios, React ApexCharts, React Hot Toast
- **Backend**: Node.js, Express.js, MongoDB (with Mongoose ODM), AWS SDK (v2), Multer, Nodemailer, Bcrypt, JSON Web Tokens

## Project Structure
```text
property-management-website/
│
├── property-management-frontend-main/     # React.js Client Application
│   ├── public/                            # Static assets and HTML wrapper
│   └── src/                               # Frontend components, routing, and views
│       ├── apiServices/                   # API wrapper for backend communication
│       ├── auth/                          # Authentication views (Login, Signup)
│       ├── components/                    # Global components (Navbars, Sidebars)
│       ├── layouts/                       # Layout containers for various roles
│       └── views/                         # Dashboard views for Admin, Vendor, and Seekers
│
└── property-mangement-backend-main/      # Node.js / Express API Server
    ├── src/                               # Models, controllers, helpers, and middleware
    ├── index.js                           # App entry point
    └── routes.js                          # Router declarations
```

## Installation & Setup

### Prerequisites
Make sure you have the following installed on your machine:
- [Node.js](https://nodejs.org/) (v16.x or newer recommended)
- [MongoDB](https://www.mongodb.com/) (either running locally or a MongoDB Atlas URI)
- An active **AWS S3 Bucket** (for handling image uploads)
- An **SMTP Mail Server** or Gmail App Password (for email alerts)

### Step 1: Set up the Backend API
1. Navigate to the backend directory:
   ```bash
   cd property-mangement-backend-main
   ```
2. Install the backend dependencies:
   ```bash
   npm install
   ```
3. Set up your environment variables. Duplicate the `.env.template` file to create a `.env` file:
   ```bash
   cp .env.template .env
   ```
4. Open the `.env` file and populate it with your actual credentials:
   ```env
   PORT=9291
   DATABASE_URI=your_mongodb_connection_uri
   TOKENKEY=your_jwt_secret_token

   # AWS S3 Settings
   AWS_BUCKET_NAME=your_s3_bucket_name
   AWS_ACCESS_KEY_ID=your_aws_access_key_id
   AWS_ACCESS_KEY_SECRET=your_aws_secret_access_key
   AWS_URL=your_s3_bucket_base_url

   # Email Service (Nodemailer)
   USER=your_smtp_username
   PASS=your_smtp_password
   EMAIL=sender_email_address
   ```
5. Start the server:
   ```bash
   npm start
   ```
   By default, the backend will run at `http://localhost:9291`.

### Step 2: Set up the Frontend Client
1. Open a new terminal window and navigate to the frontend directory:
   ```bash
   cd property-management-frontend-main
   ```
2. Install the client dependencies:
   ```bash
   npm install
   ```
3. Check `src/config.json` to make sure it points to your local backend API address:
   ```json
   {
     "url": "http://localhost:9291"
   }
   ```
4. Start the React development server:
   ```bash
   npm start
   ```
   The browser should automatically open `http://localhost:3000`.

## Usage
- **Find Properties**: As a Seeker, browse through categories, filter listings, check details, review/comment, and request a booking.
- **Post Listings**: Sign up as a Vendor. Once approved by the administrator, upload images and details of your properties and track booking inquiries.
- **Admin Control**: Use admin credentials to create categories and subcategories, verify/approve registered vendors, and view dashboard statistics.

## Screenshots / Demo
*(Add screenshots or walk-through GIFs of your application dashboards here)*

| Seeker Property Search | Admin Analytics Dashboard |
| --- | --- |
| ![Property Search Placeholder](https://via.placeholder.com/400x250?text=Seeker+Dashboard) | ![Admin Analytics Dashboard](https://via.placeholder.com/400x250?text=Admin+Analytics) |

## Future Improvements
- **Online Payments Integration**: Add Stripe or PayPal payment gates to handle booking deposits and rent transactions securely.
- **Direct Messaging**: Build a real-time messaging system using Socket.io to allow seekers to chat directly with vendors.
- **Interactive Maps**: Integrate Google Maps or Leaflet API to enable location-based property searching.
- **Advanced Filtering**: Enable filter search options by price range, star rating, and amenities list.

## Challenges & Learning
Developing this project helped me learn several modern development patterns:
- **Role-Based Protected Routes**: Managing routing logic for three distinct roles was a challenge. I learned how to set up route guards in React Router that verify JWT roles before rendering dashboards.
- **Handling Multi-Part File Uploads**: Writing the controller for image uploads taught me how to wire Multer with AWS S3 SDK. Managing access policies on AWS IAM was also a great learning experience.
- **Aggregation Queries in MongoDB**: Gathering clean statistical summaries for the admin dashboard dashboard charts required learning how to write custom MongoDB aggregation pipelines.

## Contributing
Contributions are always welcome!
1. Fork the Repository.
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`).
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`).
4. Push to the branch (`git push origin feature/AmazingFeature`).
5. Open a Pull Request.

## Author
**Dhanya K**
- GitHub: [@Dhanya562004](https://github.com/Dhanya562004)
- Email: [kdhanya762@gmail.com](mailto:kdhanya762@gmail.com)

## License
Distributed under the ISC License. See `package.json` for details.
