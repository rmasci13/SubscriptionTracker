# Subscription Tracker

A Spring Boot web application for tracking and managing subscription services with user authentication.

## Description

Subscription Tracker is a web application that allows users to track their various subscription services in one centralized location. Built with Spring Boot and secured with Spring Security, the application helps users manage their recurring expenses by providing a clear overview of all active subscriptions, costs, and payment details.

## Features

- **User Authentication**: Secure login and user management system
- **Subscription Management**: Add, edit, and delete subscription entries
- **Subscription Details**: Track service name, cost, billing cycle, payment dates, and more
- **Categories**: Organize subscriptions by predefined categories
- **Payment Methods**: Track which payment method is used for each subscription
- **Multi-User Support**: Each user maintains their own subscription list

## Technologies

- **Backend**:
  - Java 17+
  - Spring Boot
  - Spring Security
  - Spring Data JPA
  - Hibernate
- **Frontend**:
  - Thymeleaf
  - HTML/CSS
- **Database**:
  - Compatible with various SQL databases (H2, PostgreSQL, MySQL)
- **Build Tool**:
  - Maven

## Default User

For development convenience, the application creates a default admin user on startup:
- Username: `admin`
- Password: A generated UUID that is printed in the console logs during startup

The password is regenerated each time the application starts if no admin user exists.

## Data Model

### User
- Unique identifier
- Username
- Email
- Password (encrypted)
- List of Subscriptions
- User Roles

### Subscription
- Unique ID
- Service name
- Cost
- Billing cycle (MONTHLY, QUARTERLY, SEMI-ANNUALLY, ANNUALLY)
- Last Payment Date
- Next Renewal Date
- Category (ENTERTAINMENT, UTILITIES, STREAMING, etc.)
- Payment Method Description
- Status
- Associated User

### Enums
- **BillingCycle**: Defines the frequency of subscription payments
- **Category**: Organizes subscriptions by type

## Security

The application implements Spring Security with:
- Form-based authentication
- Password encryption
- Role-based access control
- CSRF protection (disabled for API testing with Postman)
- Remember-me functionality
- Custom login page
