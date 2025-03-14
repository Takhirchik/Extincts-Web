FROM maven:3.9.9-sapmachine-21 AS build
WORKDIR /extincts-web
COPY src ./src
COPY pom.xml .

RUN mvn clean package

FROM sapmachine:21-jre-alpine
WORKDIR /extincts-web
COPY --from=build ./target/*.jar ./extincts-web-1.0-SNAPSHOT.jar
CMD ["java", "-jar", "/extincts-web/extincts-web-1.0-SNAPSHOT.jar"]