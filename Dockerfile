FROM openjdk:17
WORKDIR /app
COPY target/tracking-number-generator-0.0.1-SNAPSHOT.jar.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
