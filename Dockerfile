FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
EXPOSE 8080
ENV url=jdbc:postgresql://awue1athcef-pt-rds-cluster.c59hzuikncxn.us-east-1.rds.amazonaws.com:5432/ctrefc
ENV user=controlefect
ENV pass=123
ENV schema=controlefect
ENV bucket=awue1athcef-pt-interfaces
ENV region=us-east-1

ARG JAR_FILE=target/admin-efectivo-0.0.1.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
