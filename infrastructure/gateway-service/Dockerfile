FROM eclipse-temurin:21
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*
WORKDIR /app
COPY target/gateway-service*.jar /app/gateway-service.jar
CMD ["sh", "-c", "sleep 25 && java -jar /app/gateway-service.jar"]