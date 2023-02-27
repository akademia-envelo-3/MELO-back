FROM openjdk:17-alpine
COPY target/melo-0.0.1-SNAPSHOT.jar melo-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/melo-0.0.1-SNAPSHOT.jar"]