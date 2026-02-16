# User Registration and Authentication System

**IT342 - Laboratory 1 & 2**

## Project Overview

This project implements a secure, full-stack **User Registration and Authentication System** as a multi-platform solution supporting web and mobile clients. The system features a high-fidelity geometric UI inspired by modern design principles, with bold shapes (circles, rounded cards) and a vibrant color palette creating a visually distinctive user experience.

### Architecture

- **RESTful Backend**: Spring Boot API handling authentication logic, user data persistence, and JWT token generation
- **Web Client**: Next.js 14 application with server-side rendering and responsive Tailwind CSS layouts
- **Mobile Client**: Native Android application using Kotlin with Retrofit for seamless API communication

### Security Features

- **BCrypt Password Hashing**: Industry-standard bcrypt algorithm for secure password storage
- **JWT Token-Based Authentication**: Stateless session management with secure token validation
- **Client-Side Validation**: Real-time input validation using regex patterns for email format and password strength requirements (minimum 8 characters, at least one uppercase letter, one lowercase letter, one digit, and one special character)
- **Protected Endpoints**: Secured API routes requiring valid JWT tokens for access

## Technologies Used

- **Backend:** Java 17, Spring Boot, Spring Security, JPA, MySQL (XAMPP)
- **Web Frontend:** Next.js 14, React, Tailwind CSS, Lucide React (Icons)
- **Mobile Frontend:** Kotlin, Android Studio, XML Layouts, Retrofit 2, OkHttp
- **Security:** BCrypt, JWT, Regex-based Password Validation

## Folder Structure

```
.
├── backend/   # Spring Boot REST API (Java 17)
├── web/       # Next.js Web Application
├── docs/      # Project documentation & FRS PDF
└── mobile/    # Mobile app placeholder (future development)
```

## Setup Instructions

### 1. Backend (Spring Boot)

1. Ensure **XAMPP** is running (Apache and MySQL).
2. Create a MySQL database named `auth_db`.
3. Configure your database credentials in `backend/src/main/resources/application.properties`.
4. Run the application using your IDE or `./mvnw spring-boot:run`.

### 2. Web App (Next.js)

1. Navigate to the web directory: `cd web`
2. Install dependencies: `npm install`
3. Run the development server: `npm run dev`
4. Open [http://localhost:3000](http://localhost:3000) in your browser.

### 3. Mobile App (Session 2)

1. Open the mobile/ folder in Android Studio.
2. Base URL Configuration: Ensure the Retrofit client is pointing to http://10.0.2.2:8080 (the special loopback for the Android Emulator).
3. Permissions: Check AndroidManifest.xml for <uses-permission android:name="android.permission.INTERNET" /> and android:usesCleartextTraffic="true".
4. Build and run on an Android Emulator (API 30+).

---

## API Endpoints

| Method   | Endpoint             | Description                                | Access        |
| :------- | :------------------- | :----------------------------------------- | :------------ |
| **POST** | `/api/auth/register` | Register a new user account                | Public        |
| **POST** | `/api/auth/login`    | Authenticate user and return JWT           | Public        |
| **GET**  | `/api/user/me`       | Fetch currently authenticated user profile | Private (JWT) |
