FROM maven:3.9.14-eclipse-temurin-25-noble AS builder

WORKDIR /app

COPY pom.xml .

COPY bot/pom.xml bot/

COPY ai-agent/pom.xml ai-agent/

COPY build-report-aggregate/pom.xml  build-report-aggregate/

COPY scrapper/pom.xml scrapper/

RUN mvn dependency:go-offline -B

COPY scrapper/src scrapper/src

RUN mvn package -pl scrapper -am -DskipTests

FROM eclipse-temurin:25-jre

WORKDIR /app

COPY --from=builder /app/scrapper/target/*.jar app.jar

RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

CMD ["java", "-jar", "app.jar"]
