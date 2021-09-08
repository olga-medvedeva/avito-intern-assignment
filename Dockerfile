FROM adoptopenjdk/openjdk16:jdk-16.0.1_9-alpine

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} intern-assignment-0.0.1-SNAPSHOT.jar


EXPOSE 8080
ENTRYPOINT ["java", "-jar", "intern-assignment-0.0.1-SNAPSHOT.jar"]
