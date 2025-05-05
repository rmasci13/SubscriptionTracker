# Use a JDK that matches your Java version
FROM eclipse-temurin:23-jdk as builder

WORKDIR /app

# Copy the built jar from Maven target folder
COPY target/SubscriptionTracker-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]