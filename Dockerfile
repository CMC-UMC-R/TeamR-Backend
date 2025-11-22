# build stage

FROM bellsoft/liberica-openjdk-alpine:21 AS build-stage

WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

RUN ./gradlew dependencies

COPY src src

RUN ./gradlew clean build -x test

# run stage

FROM bellsoft/liberica-openjdk-alpine:21

WORKDIR /app

COPY --from=build-stage /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]