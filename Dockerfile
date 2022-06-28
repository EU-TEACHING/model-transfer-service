FROM maven:3.6.3-openjdk-11 as build
WORKDIR /workspace

COPY . ./
RUN mvn clean package -DskipTests
ARG JAR_FILE=target/*.jar
RUN cp ${JAR_FILE} /app.jar

FROM adoptopenjdk/openjdk11:alpine-jre
COPY --from=build /app.jar /app.jar
ENTRYPOINT ["java","-jar","/app.jar"]