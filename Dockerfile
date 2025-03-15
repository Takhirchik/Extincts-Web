FROM maven:3.9.9-sapmachine-21 AS build
WORKDIR /extincts-web
COPY pom.xml .
RUN mvn --fail-never verify
COPY src src
RUN mvn -T 10C package

FROM sapmachine:21-jre-alpine
COPY --from=build /extincts-web/target/*.jar /extincts-web/*.jar
EXPOSE 8181
ENTRYPOINT ["java", "-jar", "/extincts-web/*.jar"]