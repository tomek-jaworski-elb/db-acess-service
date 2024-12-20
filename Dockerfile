FROM eclipse-temurin:17-jre-noble
LABEL authors="tomaszja"
ARG VERSION=1.2
WORKDIR /app
COPY target/db-access-service-${VERSION}.jar app.jar
EXPOSE 8085
RUN apt update && apt install -y netcat-traditional && apt clean
CMD ["java", "-jar", "app.jar"]