FROM openjdk:11-jdk-alpine
COPY build/libs/dontbeweak-*.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]