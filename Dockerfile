FROM openjdk:17-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} admin-efectivo-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/admin-efectivo-0.0.1-SNAPSHOT.jar"]
EXPOSE 8080
