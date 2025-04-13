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
- **Responsive Web Interface**: Access your subscriptions from any device

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

## Prerequisites

- JDK 17 or higher
- Maven 3.6+
- Database (configured in application.yml)

## Installation & Setup

1. Clone the repository:
```bash
git clone https://github.com/rmasci13/SubscriptionTracker.git
cd SubscriptionTracker
```

2. Configure your database connection in `src/main/resources/application.yml`

3. Build the application:
```bash
mvn clean package
```

4. Run the application:
```bash
java -jar target/subscription-tracker-0.0.1-SNAPSHOT.jar
```

Alternatively, you can run it directly using Maven:
```bash
mvn spring-boot:run
```

5. Access the application at `http://localhost:8080`

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
- List of subscriptions
- User roles for access control

### Subscription
- Unique identifier
- Service name
- Cost
- Billing cycle (e.g., MONTHLY, YEARLY)
- Last payment date
- Category (e.g., ENTERTAINMENT, UTILITIES, STREAMING)
- Payment method (e.g., CREDIT_CARD, PAYPAL, BANK_TRANSFER)
- Associated user

### Enums
- **BillingCycle**: Defines the frequency of subscription payments
- **Category**: Organizes subscriptions by type
- **PaymentMethod**: Specifies how each subscription is paid

## Security

The application implements Spring Security with:
- Form-based authentication
- Password encryption
- Role-based access control
- CSRF protection (disabled for API testing with Postman)
- Remember-me functionality
- Custom login page


## Acknowledgments

- Built with Spring Boot and Spring Security
