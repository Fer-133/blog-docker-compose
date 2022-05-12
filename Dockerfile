FROM maven:latest as builder
COPY . /usr/src/app
WORKDIR /usr/src/app
RUN mvn -DskipTests=true package

FROM openjdk:17.0.2
COPY --from=builder /usr/src/app/target/*.jar /usr/app/
WORKDIR /usr/app
EXPOSE 8080
CMD ["java", "-jar", "blogSecurizado3-0.0.1-SNAPSHOT.jar"]