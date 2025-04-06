package com.rmasci13.github;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}

//Turn on docker with: docker-compose up -d
//Get into SQL bash with: docker exec -it postgres bash
//Then do:                psql -U rmasci13
//Connect to db with: \c subscription
//Turn off at end with: docker-compose down


//Can go into Maven tab, install to create the jar
//Then cd into target, then java -jar SubscriptionTracker-0.0.1-SNAPSHOT.jar <--server.port=<number>>
//Allows you to create multiple instances of it, and therefore specify ports


//To Do
    //Refactor Subscription to work with User, User ID needs to be in the Post requests and stuff
    //Continue User
/*
{
    "username": "rmasci",
    "email": "rmasci@example.com",
    "password": "password"
}
{
  "serviceName": "Netflix",
  "cost": 15.99,
  "billingCycle": "MONTHLY",
  "lastPaymentDate": "2025-01-29",
  "category": "STREAMING",
  "paymentMethod": "CREDIT_CARD",
  "userID": 2
}
{
  "serviceName": "Xbox",
  "cost": 59.99,
  "billingCycle": "ANNUALLY",
  "lastPaymentDate": "2025-03-18",
  "category": "GAMING",
  "paymentMethod": "CREDIT_CARD",
  "userID": 2
}
{
  "serviceName": "Amazon",
  "cost": 119.99,
  "billingCycle": "ANNUALLY",
  "lastPaymentDate": "2025-03-18",
  "category": "DELIVERY",
  "paymentMethod": "PAYPAL",
   "userID": 2
}
{
  "serviceName": "Factor",
  "cost": 49.99,
  "billingCycle": "SEMI_ANNUALLY",
  "lastPaymentDate": "2025-03-18",
  "category": "FOOD",
  "paymentMethod": "PAYPAL",
  "userID": 2
}
 */
