FROM adoptopenjdk/openjdk11:ubi
ARG JAR_FILE=target/*.jar
ENV BOT_NAME=test.test_samen66_bot
ENV BOT_TOKEN=6030878826:AAH6QdwPi-tchvnJC6QtdeSqi1rrUxj8oGE
ENV BOT_DB_USERNAME=postgres
ENV BOT_DB_PASSWORD=secret
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-Dspring.datasource.password=${BOT_DB_PASSWORD}", "-Dbot.username=${BOT_NAME}", "-Dbot.token=${BOT_TOKEN}", "-Dspring.datasource.username=${BOT_DB_USERNAME}", "-jar", "/app.jar"]