# Property Management System

A comprehensive Full-Stack Property Management web application that connects property seekers, vendors, and administrators. The application allows users to search, view, comment on, and book properties, while offering vendors and admins a powerful suite of management tools, dashboard analysis, and asset storage integrations.

---

## 🏗️ Architecture & Project Structure

The project is structured as a monorepo containing separate frontend and backend applications:

```text
property-management-website/
│
├── property-management-frontend-main/     # React.js Client Application
│   ├── public/                            # Static assets
│   └── src/                               # React components, pages, routes, and services
│
└── property-mangement-backend-main/      # Node.js / Express API Server
    ├── src/                               # Controllers, helpers, models, and middleware
    ├── index.js                           # Server entrypoint
    └── routes.js                          # Express API routing configuration
```

---

## 🚀 Key Features

- **🏠 Property Management**: Complete CRUD operations for listing, updating, and viewing properties.
- **📊 Admin Analysis Dashboard**: Integrated analytics using `React ApexCharts` to monitor platform activity, category performance, and bookings.
- **🔐 Secure Authentication**: Role-based access control (Admin, Vendor, User) powered by JWT (JSON Web Tokens) and password hashing with `bcrypt`.
- **📁 Cloud Media Storage**: Direct file uploading and management for property images using **AWS S3** integration.
- **📧 Automated Notifications**: Transactional emails for bookings and account registrations powered by **Nodemailer**.
- **💬 Interactive Comments**: Review and feedback loops for listed properties.
- **📅 Property Booking**: Booking flow for reserving properties.

---

## 🛠️ Technology Stack

### Frontend
- **Framework**: React.js (v18)
- **Routing**: React Router DOM (v6)
- **Styling**: Tailwind CSS & Material Tailwind React
- **Charts**: React ApexCharts
- **HTTP Client**: Axios
- **Notifications**: React Hot Toast

### Backend
- **Runtime**: Node.js
- **Framework**: Express.js
- **Database**: MongoDB via Mongoose
- **Cloud Service**: AWS SDK (S3)
- **Authentication**: JWT & Bcrypt
- **File Handling**: Multer
- **Email Service**: Nodemailer

---

## ⚙️ Installation & Configuration

### Prerequisites
- [Node.js](https://nodejs.org/) (v16+ recommended)
- [MongoDB](https://www.mongodb.com/) (local instance or MongoDB Atlas URI)
- [AWS S3 Bucket](https://aws.amazon.com/s3/) (for property image uploads)

---

### 1. Backend Setup

1. Navigate to the backend directory:
   ```bash
   cd property-mangement-backend-main
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Configure Environment Variables:
   Create a `.env` file in the root of the backend directory. You can use the provided `.env.template` as a guide:
   ```env
   PORT=9291
   DATABASE_URI=your_mongodb_connection_uri
   TOKENKEY=your_jwt_secret_token

   # AWS S3 Configurations
   AWS_BUCKET_NAME=your_s3_bucket_name
   AWS_ACCESS_KEY_ID=your_aws_access_key_id
   AWS_ACCESS_KEY_SECRET=your_aws_secret_access_key
   AWS_URL=your_s3_bucket_base_url

   # Nodemailer SMTP Configuration
   USER=your_smtp_username
   PASS=your_smtp_password
   EMAIL=sender_email_address
   ```

4. Start the backend server:
   ```bash
   npm start
   ```
   The backend server will run on `http://localhost:9291` (or the port specified in your `.env` file).

---

### 2. Frontend Setup

1. Navigate to the frontend directory:
   ```bash
   cd property-management-frontend-main
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Start the React development server:
   ```bash
   npm start
   ```
   The application will boot up at `http://localhost:3000`.

---

## 📬 API Routes Reference

Below are the primary API modules exposed by the backend (configured in `routes.js`):

- **Authentication**: `/api/account`
- **Admin Settings**: `/api/admin`
- **Category Management**: `/api/category`
- **Subcategory Management**: `/api/subcategory`
- **Property Operations**: `/api/properties`
- **Comment System**: `/api/comments`
- **User Management**: `/api/user`
- **Bookings**: `/api/property`
- **Dashboard & Analytics**: `/api/project`
