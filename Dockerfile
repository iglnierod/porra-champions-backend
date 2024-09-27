FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/porra-champions-0.0.1-SNAPSHOT.jar /app/porra-champions-0.0.1-SNAPSHOT.jar

EXPOSE 1906

ENTRYPOINT ["java","-jar","/app/porra-champions-0.0.1-SNAPSHOT.jar"]