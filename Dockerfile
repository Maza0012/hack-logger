FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

# ตรวจสอบว่า JAR ถูกสร้างจริง
RUN ls -la /app/target/

FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

# ตรวจสอบว่า JAR มี main class
RUN jar tf app.jar | grep MANIFEST

ENV DISCORD_TOKEN=""

CMD ["java", "-jar", "app.jar"]
