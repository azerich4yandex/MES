FROM eclipse-temurin:21
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*
WORKDIR /app
COPY target/receiving-order-service*.jar /app/receiving-order-service.jar
CMD ["java", "-jar", "/app/receiving-order-service.jar"]