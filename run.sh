#!/bin/bash
NEW_RELIC=
echo "Starting orbital1-api. "
echo "Configuration file: " $ENV_CONFIG
#export XMS=-Xms1G
#export XMX=-Xmx3G
#./src/main/resources/stg-config.yml
echo "Environment: " $ENVIRONMENT
if [[ -n $ENVIRONMENT ]]
    then
        NEW_RELIC="-javaagent:newrelic/newrelic.jar -Dnewrelic.environment=$ENVIRONMENT"
fi
export XMS=-Xms1G
export XMX=-Xmx10G

java -jar $XMS $XMX $NEW_RELIC ./target/space-horse-api*.jar server $ENV_CONFIG

