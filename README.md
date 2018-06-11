# Orbital 1 - API Server

## Tecnologías

* Java 8
* Maven 3
* JUnit 4
* [Dropwizard 1.0.5](http://www.dropwizard.io/1.0.5/docs/) - Framework para RESTful web services
* [DynamoDB](http://docs.aws.amazon.com/amazondynamodb/latest/developerguide/DynamoDBLocal.html#DynamoDBLocal.DownloadingAndRunning) - Base de
datos NOSQL de Amazon

## Otras librerías

* Guava
* Javaslang
* AssertJ
* Retrofit
* Cobertura

## Agentes para captura de métricas de performance 

* [Datadog](https://docs.datadoghq.com/guides/basic_agent_usage/)
* [New Relic](https://docs.newrelic.com/docs/agents/java-agent)

## Compilación

Compilación y ejecución de tests unitarios

```bash
mvn clean install
```

Compilación y ejecución sólo de tests de integración (requiere base de datos corriendo)

```bash
mvn clean integration-test -Dtest=*/*IntegrationTest.java
```

Compilación, ejecución de tests unitarios y generación de reporte de cobertura

```bash
mvn cobertura:cobertura
```

## Base de Datos
Para desarrollo local, es necesario [DynamoDB Local](http://docs.aws.amazon.com/amazondynamodb/latest/developerguide/DynamoDBLocal.html#DynamoDBLocal.DownloadingAndRunning).
Descargar, descomprimir y ejecutar el siguiente comando:

```bash
java -Djava.library.path=./DynamoDBLocal_lib -jar DynamoDBLocal.jar -port 8000 -sharedDb
```

Para poder loguearse a la base de datos es necesario configurar las [AWS Credentials](http://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/setup-credentials.html) del cliente **aws**, con la region **us-east-1**

## Ejecución del servidor
```bash
java server target/classes/local-config.yml
```

## Agente Datadog 

Para correr el agente:

```bash
sudo /etc/init.d/datadog-agent start
```

Para parar el agente:

```bash
sudo /etc/init.d/datadog-agent stop
```
