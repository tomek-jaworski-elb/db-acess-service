FROM eclipse-temurin:17-jre-alpine
LABEL authors="tomaszja"
ARG VERSION=1.2
WORKDIR /app
COPY target/db-access-service-${VERSION}.jar app.jar
EXPOSE 8085
CMD ["java", "-jar", "app.jar"]