# Model Transfer Service

A Spring Boot microservice for file transferring through Kafka.

# Building the Docker image

At the root folder of the project execute:

```
docker build -t model-transfer-service:v0.1 .
```

# Running the all-in-one example

At the root folder of the project execute:

```
cd .docker/all-in-one
docker-compose up -d
```

This will start 3 edge and 1 cloud service together with the Kafka
broker and zookeeper.
You could play around by moving files in the folders:

```
mts1/outgoing, mts2/outgoing, mts3/outgoing, cloud/outgoing
```

and check the traffic in the corresponding `ingoing` files.

# Running services one-by-one

The `.docker/one-by-one` folder contains preconfigured services
together with their passwords.
You could change the `kafka.bootstrapAddress` environment variable to
match a specified Kafka broker and bring up the service.

CAUTION: These passwords are just for demo purposes. Don't use them
in production.

# Running SFTP mode in host.

For convenience an application-sftp.properties is provided in the `src/main/resources`
path. You can override application.properties with it before packaging.

The properties can be overriden at runtime using the equivalent environment variables.

You can package the application with:

```
mvn clean package -DskipTests
```

This will create a jar file in the target folder. This can be run by:

```
java -jar <jar_filename>.jar
```

and can be configured to run as a service in a Linux machine.
All dependencies are included.

## SFTP mode details

The SFTP mode performs the following actions:

- monitors the `mts.folders.monitored` folder and if files are created
in it they get transferred to the designated folder
`mts.sftp.remote.uploads.folder` in the SFTP server.
- runs a scheduled task every minute to check connectivity and sends
any cached files in `mts.folders.monitored` that couldn't be sent before.
- runs a scheduled task every minute to download files from
`mts.sftp.remote.downloads.folder` in the SFTP server.