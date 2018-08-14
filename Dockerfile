FROM openjdk:8-jdk-alpine
COPY target/equitybot-kite-svc-1.0.jar app.jar
EXPOSE 9010
ENTRYPOINT ["java","-Dspring.profiles.active=kubernetes","-Djava.security.egd=file:/dev/./urandom","-jar","app.jar"]