FROM eclipse-temurin:17-jdk-alpine
ARG VERSION
ARG FILE=/target/agilemonkeys-challenge-api-$VERSION.jar
VOLUME /tmp
ADD $FILE app.jar
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/urandom", "-jar", "app.jar"]