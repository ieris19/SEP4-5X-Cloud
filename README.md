# Venue Environment System - Cloud

This is the cloud infrastructure for the Venue Environment System. It is composed of two microservices that fulfill
different tasks.

## Services:

### Data Collector—LoRaWAN Network Service

This service is responsible for collecting data from the LoRaWAN network. It is a Spring Boot application that uses
a WebSocket connection to receive data from the LoRaWAN network. It then parses the data and stores it in a MongoDB
database.

### Web Service—REST API

This service is responsible for providing access to the data for the frontend. It is a Spring Boot application that
provides a REST API to access the data stored in the MongoDB database. It provides endpoints to access the data and
further specifications can be found in
the [API documentation](https://github.com/ieris19/SEP4-5X-Cloud/blob/028e6a08fcc2932fce106d979acf534086b596ac/web/api-doc.md).

## Usage:

The system allows for two ways of operating it, either through raw Java or through Docker containers. Our workflow
includes the use of Docker containers, but the system can be run without them as well. The only requirement is that
the secrets are provided to the application. The following sections describe how to get the servicces running in both
environments.

### Java-powered

The application can be run through pure Java. Thanks to the Spring Boot framework, the application is packaged as a JAR
file that is executable through the `java -jar` command. The JAR file contains both the compiled bytecode for this
application and the JAR files for all the dependencies, so it is self-contained. It contains a custom class loader
courtesy of the Spring Boot framework that allows it to load JAR nested within the JAR file itself, a capability that
is not present in the default Java class loader.

In order for the setup to be complete, the application will look for a file called `secrets.properties`
under directory `config/` in the current working directory (the one where the `java -jar` command is being executed
from). Make sure the file exists and contains the correct values. The format of the file is one key-value pair per
line as `key=value`.

### Docker-powered

Docker is a containerization technology that allows for the creation of isolated environments that contain all the
dependencies and configuration needed to run an application. This means that the application can be run by spinning up
a container from the images published to DockerHub
as [ieris19/sep4-lorawan-service](hub.docker.com/repository/docker/ieris19/sep4-lorawan-service) and
[ieris19/sep4-web-service](https://hub.docker.com/repository/docker/ieris19/sep4-web-service)

The images setup a suitable environment for the application to run in, but they still need to be provided with the
secrets. This is done by passing them as environmental variables to the container, either as part of the `docker run`
command or as part of a `.env` file. Regardless of the method, the container should start up and run the application
smoothly once the secrets are provided.

## Secrets
The application requires to be provided with two secrets in order to run. These are the MongoDB URL and the LoRaWAN
URL. Both of these are URLs that point to the respective services, and they should contain the user/password or token
needed to authenticate with the service. 

The MongoDB URL should point to a MongoDB database, and the application will create everything it needs to run in it.
MongoDB Atlas is a good option for this, as it provides a free tier that is more than enough for this application.

MongoDB URLs are written in the format of `mongodb+srv://<user>:<password>@<cluster>.mongodb.net` (assuming the
database is hosted in MongoDB Atlas). The user and password are the ones used to authenticate with the database, and
the cluster is the name of the cluster in MongoDB Atlas. It is recommended to create a new user for this application
for both security and to prevent the connection from being dropped due to logging in from multiple locations.

The LoRaWAN URL should point to a WebSocket endpoint that will provide the data to the application. The application
will receive data from the WebSocket and send replies to it as well. Remember to include the protocol in the URL
(e.g. `ws://` or `wss://`).

The `secrets.properties` file should look as follows, with the values replaced with the actual secrets 
(Please note that in the placeholders, we have placed the environmental variables names that we use in the Docker 
environment, so you can use them if you are running the application in a Docker):

```properties
lorawan.url=${lorawan_url}
mongodb.url=${mongodb_url}
```
