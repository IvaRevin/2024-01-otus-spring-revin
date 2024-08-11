FROM maven:3.8.5-openjdk-17 AS builder
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
RUN mvn -f /usr/src/app/pom.xml clean package

FROM bellsoft/liberica-openjdk-alpine:17
ARG JAR_NAME="message-sender-0.0.1-SNAPSHOT.jar"
COPY --from=builder /usr/src/app/target/$JAR_NAME /usr/app/$JAR_NAME
EXPOSE 8080
ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,address=*:5005,server=y,suspend=n", "-jar", "/usr/app/message-sender-0.0.1-SNAPSHOT.jar"]
