FROM openjdk:17-jdk-alpine
LABEL maintainer="vitalieparaskiv@gmail.com"
ADD target/api-gateway-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar","/app.jar"]

FROM openjdk:17-jdk-alpine
COPY target/api-gateway-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar","/app.jar"]