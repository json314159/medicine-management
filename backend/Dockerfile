FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml ./
COPY settings.xml ./
COPY src ./src
RUN --mount=type=cache,target=/root/.m2 mvn -s settings.xml -B -ntp clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
RUN useradd --system --uid 10001 medicine \
    && mkdir -p /data \
    && chown medicine:medicine /data
COPY --from=build /app/target/medicine-management-api-1.0.0.jar app.jar
USER medicine
EXPOSE 8080
ENTRYPOINT ["java", "-Xms128m", "-Xmx384m", "-jar", "/app/app.jar"]
