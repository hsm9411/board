# Stage 1: Build the WAR file using Maven
FROM maven:3.8.4-openjdk-11 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn package

# Stage 2: Create the final image with Tomcat
FROM tomcat:9-jdk11-openjdk-slim
COPY --from=build /app/target/ROOT.war /usr/local/tomcat/webapps/ROOT.war
