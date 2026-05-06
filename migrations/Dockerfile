FROM alpine:3.22.4 AS builder

RUN apk add --no-cache wget

RUN wget -q https://repo1.maven.org/maven2/org/postgresql/postgresql/42.7.11/postgresql-42.7.11.jar -O /tmp/postgresql.jar


FROM liquibase/liquibase:5.0.1

USER root

COPY --from=builder /tmp/postgresql.jar /liquibase/lib/postgresql.jar

USER liquibase
