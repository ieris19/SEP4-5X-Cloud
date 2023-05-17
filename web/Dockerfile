FROM eclipse-temurin:17.0.7_7-jre-alpine

RUN mkdir /opt/app
COPY target/web-*.jar /opt/app/web.jar
COPY secrets.properties config/secrets.properties
CMD ["java", "-jar", "/opt/app/web.jar"]