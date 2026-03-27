FROM eclipse-temurin:21
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*
WORKDIR /app
COPY target/discovery-service*.jar /app/discovery-service.jar
CMD ["java", "-jar", "/app/discovery-service.jar"]