# ---------- Build stage ----------
FROM maven:3.9-eclipse-temurin-17 AS build

# container ke andar working folder
WORKDIR /app

# yaha 'login' tumhara Spring Boot project ka folder hai
# poora project copy kar do
COPY login/ .

# jar build karo (tests skip)
RUN mvn -B -DskipTests package

# ---------- Run stage ----------
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# build stage se jar file copy karo
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

