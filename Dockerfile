FROM openjdk:11-alpine
COPY build/libs/dontbeweak*SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]