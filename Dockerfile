FROM openjdk:11-jdk
COPY build/libs/dontbeweak*SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]