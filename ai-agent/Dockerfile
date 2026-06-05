FROM maven:3.9.14-eclipse-temurin-25-noble AS builder

WORKDIR /app

COPY pom.xml .
COPY bot/pom.xml bot/
COPY ai-agent/pom.xml ai-agent/
COPY build-report-aggregate/pom.xml  build-report-aggregate/
COPY avro-schemes/pom.xml avro-schemes/
COPY scrapper/pom.xml scrapper/

RUN mvn dependency:go-offline -B

COPY ai-agent/src ai-agent/src
COPY avro-schemes/src avro-schemes/src

RUN mvn package -pl ai-agent -am -DskipTests

FROM eclipse-temurin:25-jre

WORKDIR /app

COPY --from=builder /app/ai-agent/target/*.jar app.jar

RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

CMD ["java", "-jar", "app.jar"]
