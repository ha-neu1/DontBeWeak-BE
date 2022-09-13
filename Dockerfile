FROM adoptopenjdk/openjdk11:jre-11.0.6_10-alpine
COPY build/libs/dontbeweak*SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]