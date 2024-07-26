<p align="center" style="font-size: 24px;"><b>Appointment Management System</b></p>
Application Live Page: https://appointment-management-js-dd97db3d1139.herokuapp.com/login
Note: Please use starting credentials as follows
username: admin@admin.com

password: adminpassword

## Overview

**The Appointment Management System** is a fullstack application designed to manage appointments effectively, utilizing a microservices architecture to ensure scalability and flexibility. Built with Java and Spring Boot for the backend, and JavaScript, HTML, and CSS for the frontend, this system offers a comprehensive solution for appointment scheduling and management.

## Features

- **Microservices Architecture:** Leveraging microservices to ensure each service is scalable independently.
- **Service Discovery:** Utilizing Eureka server for service registration and discovery.
- **Security:** Integrated Spring Security with JWT for secure access to the application.
- **Role-Based Access Control:** Supports two roles—**ADMIN** and **STAFF**, with ADMIN having full access and STAFF limited to fetch operations.
- **Real-Time Notifications:** Uses Kafka for sending real-time email notifications upon various actions like creation, update, or deletion of appointments, clients, or staff.
- **Database:** Employs MySQL for robust data management.
- **Auto Creation of Admin:** Automatically creates an admin account if none exists upon application startup.

## API Endpoints

### Authentication and User Management

- **Register User:** `POST http://security/auth/register`
- **Register Admin:** `POST http://security/auth/admin/registerAdmin`
- **Login:** `POST http://security/auth/login`
- **Refresh Token:** `POST http://security/auth/refresh-token`
- **Send Password Reset Code:** `GET http://security/auth/send-reset-code?email={email}`
- **Change Password:** `POST http://security/auth/change-password`
- **Delete User:** `DELETE http://security/auth/delete?userId={userId}`

### Staff Management

- **Create Staff:** `POST http://security/auth/staff/admin/create`
- **Delete Staff:** `DELETE http://security/auth/staff/admin/delete?staffId={staffId}`
- **Update Staff:** `PUT http://security/auth/staff/admin/update`
- **Fetch Staff:** `GET http://security/auth/staff/admin-staff/fetch?staffId={staffId}`
- **Fetch All Staff:** `GET http://security/auth/staff/admin-staff/fetchAllStaff`

### Client Management

- **Create Client:** `POST http://security/auth/client/admin/create`
- **Delete Client:** `DELETE http://security/auth/client/admin/delete?clientId={clientId}`
- **Update Client:** `PUT http://security/auth/client/admin/update`
- **Fetch Client:** `GET http://security/auth/client/admin-staff/fetch?clientId={clientId}`
- **Fetch All Clients:** `GET http://security/auth/client/admin-staff/fetchAllClients`

### Appointment Management

- **Create Appointment:** `POST http://security/auth/appointment/admin/create`
- **Delete Appointment:** `DELETE http://security/auth/appointment/admin/delete?appointmentNumber={appointmentNumber}`
- **Update Appointment:** `PUT http://security/auth/appointment/admin/update`
- **Fetch Appointment:** `GET http://security/auth/appointment/admin-staff/fetch?appointmentNumber={appointmentNumber}`
