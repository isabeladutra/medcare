FROM openjdk:17-jdk-slim


WORKDIR /app

COPY medcare-1.0.4.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]