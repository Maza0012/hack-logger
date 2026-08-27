FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

# ตรวจสอบขนาด JAR (ควรใหญ่กว่า 10MB เพราะมี dependencies)
RUN ls -lh /app/target/

FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

# ตรวจสอบ Main-Class ใน MANIFEST (ใช้ JDK แทน JRE)
RUN jar tf app.jar | grep -i manifest || echo "No MANIFEST found"

# ตั้งค่า Environment
ENV DISCORD_TOKEN=""

# รัน Bot
CMD ["java", "-jar", "app.jar"]
