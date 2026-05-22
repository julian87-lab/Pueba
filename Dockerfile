FROM eclipse-temurin:21-jdk

COPY ./target/prueba-1.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]