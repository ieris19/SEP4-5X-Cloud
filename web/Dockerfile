FROM eclipse-temurin:17.0.7_7-jre-alpine

RUN mkdir /opt/app
WORKDIR /opt/app
COPY target/web-api-service.jar web.jar
COPY init.sh start.sh
CMD ["sh", "start.sh"]