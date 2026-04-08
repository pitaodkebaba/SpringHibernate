FROM maven:3.9-eclipse-temurin-21 AS build-stage

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21

WORKDIR /app

#COPY wait-for-it.sh wait-for-it.sh
COPY --from=build-stage /app/target/project-0.0.1-SNAPSHOT.jar MusicBrowser.jar

EXPOSE 8080
#"./wait-for-it.sh", "localhost:3306", "--",
ENTRYPOINT [ "java", "-jar", "/app/MusicBrowser.jar" ]