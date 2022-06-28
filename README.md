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