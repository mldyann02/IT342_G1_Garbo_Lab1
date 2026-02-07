# User Registration and Authentication System

**IT342 - Laboratory 1 & 2**

## Project Overview

This project implements a secure User Registration and Authentication system. It features a high-fidelity geometric UI inspired by modern design principles, focusing on robust security measures including **BCrypt password encryption**, **JWT-based session management**, and **strict client-side validation**.

## Technologies Used

- **Backend:** Java 17, Spring Boot, Spring Security, JPA, MySQL (XAMPP)
- **Web Frontend:** Next.js 14, React, Tailwind CSS, Lucide React (Icons)
- **State Management:** React Hooks (useState, useEffect)
- **Security:** BCrypt, JWT, Regex-based Password Validation

## Folder Structure

├── backend/ # Spring Boot REST API (Java 17)
├── web/ # Next.js Web Application
├── docs/ # Project documentation & FRS PDF
└── mobile/ # Mobile app placeholder (future development)

---

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

1. Navigate to the mobile directory: `cd mobile`
2. _Implementation pending._
3. Follow platform-specific instructions (Android/iOS) once source code is initialized.

---

## API Endpoints

| Method   | Endpoint             | Description                                | Access        |
| :------- | :------------------- | :----------------------------------------- | :------------ |
| **POST** | `/api/auth/register` | Register a new user account                | Public        |
| **POST** | `/api/auth/login`    | Authenticate user and return JWT           | Public        |
| **GET**  | `/api/user/me`       | Fetch currently authenticated user profile | Private (JWT) |
