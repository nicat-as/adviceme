FROM openjdk:11-jre-slim

EXPOSE 8080

RUN mkdir /app

COPY /build/libs/*.jar /app/equso.jar

ENTRYPOINT ["java","-jar", "/app/equso.jar","--spring.profiles.active=default"]
