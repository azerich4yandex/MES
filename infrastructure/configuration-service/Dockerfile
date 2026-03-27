FROM eclipse-temurin:21
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*
WORKDIR /app
COPY target/configuration-service*.jar /app/configuration-service.jar
CMD ["java", "-jar", "/app/configuration-service.jar"]