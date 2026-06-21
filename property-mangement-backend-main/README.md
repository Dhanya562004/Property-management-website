# Property Management System - Backend API

This directory contains the Node.js and Express.js REST API that powers the Property Management Hub. It handles database operations, authentication routes, automated notification mailers, and integrates with AWS S3 for media uploads.

## Overview
The backend server functions as a JSON API, exposing endpoints for user accounts, vendor approvals, property listings, search filters, comments, and booking aggregations. It validates all incoming requests using JWT middleware and stores data inside a MongoDB database.

## Features
- **RESTful Endpoints**: Dedicated modules for authentication, users, property CRUD, reviews, and booking transactions.
- **Role-Based Middlewares**: Intercepts requests to enforce permissions depending on user role (Admin, Vendor, Seeker).
- **Secure Authentication**: Uses JSON Web Tokens (JWT) for session management and Bcrypt to securely hash passwords.
- **S3 Media Storage**: Seamless upload handlers routing images directly to an AWS S3 bucket.
- **Automated Mailer**: Connects to an SMTP server using Nodemailer to send confirmations upon booking creation.

## Technologies Used
- **Runtime**: Node.js
- **Server Framework**: Express.js
- **Database**: MongoDB (via Mongoose ODM)
- **File Handling**: Multer
- **Cloud Integration**: AWS SDK (v2)
- **Security**: Bcrypt, JSON Web Tokens (JWT), CORS
- **Email Service**: Nodemailer

## Project Structure
```text
src/
├── controllers/          # Controllers for authentication, admin tools, properties, etc.
├── helpers/              # Helper utilities (AWS S3 config, Nodemailer setup)
├── middleware/           # Authentication validation and upload handlers
└── models/               # Mongoose schemas (Users, Properties, Bookings, Categories)
```

## Installation & Setup
1. Open a terminal in this directory:
   ```bash
   cd property-mangement-backend-main
   ```
2. Install the node packages:
   ```bash
   npm install
   ```
3. Set up the `.env` file from the template:
   ```bash
   cp .env.template .env
   ```
4. Update the newly created `.env` file with your Mongo, JWT, AWS S3, and SMTP credentials:
   ```env
   PORT=9291
   DATABASE_URI=your_mongodb_connection_uri
   TOKENKEY=your_jwt_secret_token
   AWS_BUCKET_NAME=your_s3_bucket_name
   AWS_ACCESS_KEY_ID=your_aws_access_key_id
   AWS_ACCESS_KEY_SECRET=your_aws_secret_access_key
   AWS_URL=your_s3_bucket_base_url
   USER=your_smtp_username
   PASS=your_smtp_password
   EMAIL=sender_email_address
   ```
5. Start the server:
   ```bash
   npm start
   ```

## Usage
- The server starts on `http://localhost:9291` by default.
- Integrate with the frontend React client or test endpoints directly using Postman.
- A Postman collection is included in this folder as `Project.postman_collection.json` for reference.

## Future Improvements
- Refactor to TypeScript for stronger type-safety.
- Write integration tests using Supertest and Jest.
- Set up Winston or Morgan logging library to capture runtime application errors.

## Author
**Dhanya K**
- GitHub: [@Dhanya562004](https://github.com/Dhanya562004)
- Email: [kdhanya762@gmail.com](mailto:kdhanya762@gmail.com)

## License
Distributed under the ISC License.
