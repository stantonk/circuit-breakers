FROM adoptopenjdk/openjdk11:jdk-11.0.11_9-debian-slim

RUN mkdir -p /srv
WORKDIR /srv
COPY target/testservice-1.0.0.jar /srv/testservice-1.0.0.jar
# IMPORTANT: separate config for backendservice
COPY backendservice.yml /srv/config.yml
CMD ["java", "-jar", "testservice-1.0.0.jar", "server", "config.yml"]