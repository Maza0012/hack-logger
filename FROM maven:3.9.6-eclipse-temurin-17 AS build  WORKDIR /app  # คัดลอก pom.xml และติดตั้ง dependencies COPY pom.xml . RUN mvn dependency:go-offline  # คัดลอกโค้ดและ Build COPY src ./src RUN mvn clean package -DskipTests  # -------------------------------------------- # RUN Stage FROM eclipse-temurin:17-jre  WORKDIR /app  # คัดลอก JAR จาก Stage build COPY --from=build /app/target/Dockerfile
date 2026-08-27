FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# คัดลอก pom.xml และติดตั้ง dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# คัดลอกโค้ดและ Build
COPY src ./src
RUN mvn clean package -DskipTests

# --------------------------------------------
# RUN Stage
FROM eclipse-temurin:17-jre

WORKDIR /app

# คัดลอก JAR จาก Stage build
COPY --from=build /app/target/*.jar app.jar

# ตั้งค่า Environment Variable (Railway จะ override)
ENV DISCORD_TOKEN=""

# รัน Bot
CMD ["java", "-jar", "app.jar"]
