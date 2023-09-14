FROM eclipse-temurin:17-jdk-alpine

EXPOSE 8080

ARG ENV_URL
ARG ENV_USER
ARG ENV_PASS
ARG ENV_SCHEMA
ARG ENV_BUCKET
ARG ENV_REGION
ARG JAR_FILE
RUN echo "env_pass: ${ENV_PASS}"
RUN addgroup --system javauser && adduser -S -s /usr/sbin/nologin -G javauser javauser
USER javauser

COPY ${JAR_FILE} /app.jar

ENV url=${ENV_URL}
ENV user=${ENV_USER}
ENV pass=${ENV_PASS}
ENV schema=${ENV_SCHEMA}
ENV bucket=${ENV_BUCKET}
ENV region=${ENV_REGION}

ENTRYPOINT ["java", "-Xmx6g", "-Xms4g", "-XX:MaxMetaspaceSize=256m", "-jar","/app.jar"]
