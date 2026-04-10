# Budowanie aplikacji
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
WORKDIR /app
# Kopiowanie pom.xml, aby pobrać zależności
COPY pom.xml .
RUN mvn dependency:go-offline -B
# Kopiowanie kodu źródłowego i budowanie aplikacji
COPY src ./src
RUN mvn clean package -DskipTests

# Uruchamianie aplikacji
FROM eclipse-temurin:21-jre-alpine

# Ustawianie katalogu roboczego
WORKDIR /app

USER root
RUN apk upgrade --no-cache

# Tworzenie nieuprzywilejowanego użytkownika do uruchamiania aplikacji
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

# Kopiowanie zbudowanego pliku JAR z etapu budowania
COPY --from=builder /app/target/*.jar api-gateway.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "api-gateway.jar"]