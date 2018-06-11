FROM maven:3.3.9-jdk-8-alpine

MAINTAINER luciano.bernal@etermax.com

WORKDIR space-horse-api

ENV MAVEN_SETTINGS $MAVEN_HOME/ref/settings-docker.xml

ADD settings.xml $MAVEN_SETTINGS
ADD pom.xml pom.xml
RUN mvn -B -s $MAVEN_SETTINGS install -DskipTests=true

ADD src src

RUN mvn -B -s $MAVEN_SETTINGS install -DskipTests=true

ADD newrelic newrelic
ADD newrelic/newrelic.yml /opt/newrelic/newrelic.yml

ADD run.sh run.sh

EXPOSE 8090

CMD ./run.sh