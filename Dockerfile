FROM amazoncorretto:17.0.7
EXPOSE 8080
ADD target/real-estate-app-backend.jar real-estate-app-backend.jar
ENTRYPOINT ["java", "-jar", "real-estate-app-backend.jar"]