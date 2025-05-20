FROM eclipse-temurin:17-jdk-focal as builder
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:17-jre-focal
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

# Install FFmpeg
RUN apt-get update && \
    apt-get install -y ffmpeg && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Create directories for video processing
RUN mkdir -p /tmp/stream-worker/input /tmp/stream-worker/output

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"] 