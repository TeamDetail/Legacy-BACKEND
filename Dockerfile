FROM openjdk:21
LABEL authors="ganggun0113"
ENV TZ=Asia/Seoul
COPY ./build/libs/*-SNAPSHOT.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]