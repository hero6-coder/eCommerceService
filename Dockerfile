FROM adoptopenjdk/openjdk11@sha256:def864ee30f6657d781712ffbef162ce5b1aaeeb963e6faa1732de7e71287083
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} shopping-app.jar
ENTRYPOINT ["java","-jar","/shopping-app.jar"]
