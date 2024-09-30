#
# Build stage
#
FROM maven:3.8.3-openjdk-17 AS build
COPY src /home/app/src
COPY pom.xml /home/app
COPY target/app.jar /home/app
RUN mvn -f /home/app/pom.xml clean package
EXPOSE 8080
# Run the App
ENTRYPOINT ["java", "-jar", "/home/app/app.jar"]