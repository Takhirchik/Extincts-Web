FROM maven:3.9.9-sapmachine-21 AS build
WORKDIR /extincts-web
COPY pom.xml /extincts-web/pom.xml
COPY ./src /extincts-web/src
RUN mvn -f /extincts-web/pom.xml clean package -Dmaven.test.skip=true

FROM sapmachine:21-jre-alpine
COPY --from=build /extincts-web/target/*.jar /extincts-web/*.jar
EXPOSE 8181
ENTRYPOINT ["java", "-jar", "/extincts-web/*.jar"]