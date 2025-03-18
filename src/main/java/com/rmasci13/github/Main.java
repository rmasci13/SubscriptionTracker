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



//TO DO
    //Category enum