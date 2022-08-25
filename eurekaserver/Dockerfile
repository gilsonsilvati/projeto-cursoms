FROM gradle:7.5-jdk18-alpine AS build
WORKDIR /app
COPY . .
RUN gradle bootJar

FROM openjdk:18-alpine
WORKDIR /app
COPY --from=build ./app/build/libs/*.jar ./
EXPOSE 8761
ENTRYPOINT java -jar eurekaserver-0.0.1-SNAPSHOT.jar