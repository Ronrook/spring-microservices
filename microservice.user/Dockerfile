FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline -B

COPY microservice.user/pom.xml microservice.user/
RUN ./mvnw dependency:go-offline -B -pl microservice.user

COPY microservice.user/src/ microservice.user/src/
RUN ./mvnw package -B -pl microservice.user -am -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app

RUN groupadd -r appuser && useradd -r -g appuser appuser

COPY --from=build /app/microservice.user/target/*.jar app.jar

RUN chown -R appuser:appuser /app
USER appuser

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
