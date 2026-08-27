FROM maven:3.9.6-eclipse-temurin-17

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src

# รันโดยตรงผ่าน Maven (ไม่ต้องสร้าง JAR)
CMD ["mvn", "compile", "exec:java", "-Dexec.mainClass=DiscordBot"]
