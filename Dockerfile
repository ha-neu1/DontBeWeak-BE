# JDK11 이미지 사용.
FROM openjdk:11-jdk
VOLUME /tmp
# JAR_FILE 변수에 값을 저장.
# 즉, build가 되는 시점에 JAR_FILE 이라는 변수명에 build/libs/*.jar 표현식을 선언했다는 의미.
ARG JAR_FILE=build/libs/*.jar

# 변수에 저장된 것을 컨테이너 실행시 이름을 app.jar파일로 변경하여 컨테이너에 저장.
# 즉, 위에 선언한 JAR_FILE 을 app.jar 로 복사한다.
COPY ${JAR_FILE} app.jar

# 빌드된 이미지가 run될 때 실행할 명령어
# 즉, jar 파일을 실행하는 명령어 - (java -jar jar파일).
ENTRYPOINT ["java","-jar","/app.jar"]