FROM gradle:7.5-jdk18-alpine AS build
WORKDIR /app
COPY . .
RUN gradle bootJar

FROM openjdk:18-alpine
WORKDIR /app
COPY --from=build ./app/build/libs/*.jar ./

ARG EUREKA_SERVER=xpto-eureka

ENTRYPOINT java -jar -Dspring.profiles.active=production msclientes-0.0.1-SNAPSHOT.jar