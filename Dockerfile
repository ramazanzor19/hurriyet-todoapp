FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} todoapp.jar
ENTRYPOINT ["java","-jar","/todoapp.jar"]