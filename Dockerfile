FROM openjdk:17-alpine as build

# Set the working directory inside the container
WORKDIR /app

# Copy the local code to the container
COPY . .

# Build the JAR file (assuming Maven is used)
RUN ./mvnw clean package -Dmaven.test.skip

# Start a new stage from scratch to keep the image small
FROM openjdk:17-alpine

WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose port 8080 for the application
EXPOSE 8080

RUN addgroup --system javauser && adduser -S -s /usr/sbin/nologin -G javauser javauser
USER javauser

ENV url=${ENV_URL}
ENV user=${ENV_USER}
ENV pass=${ENV_PASS}
ENV schema=${ENV_SCHEMA}
ENV bucket=${ENV_BUCKET}
ENV region=${ENV_REGION}

ENTRYPOINT ["java", "-Xmx2g", "-Xms1g", "-XX:MaxMetaspaceSize=256m", "-jar", "app.jar"]
