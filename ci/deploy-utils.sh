    #!/bin/sh

source ci/common-utils.sh

deploy(){
	local USER=$1
	local SERVER=$2
	local OUTER_PORT=$3
	local ENVIRONMENT=$4

	configure-ssh-agent

	ssh "$USER@$SERVER" "docker stop $CI_PROJECT_NAME" 										|| echo "Unable to stop container"
	ssh "$USER@$SERVER" "docker rm $CI_PROJECT_NAME" 										|| echo "Unable to remove container"
	ssh "$USER@$SERVER" "docker login -u gitlab-ci-token -p $CI_BUILD_TOKEN $CI_REGISTRY"  	|| fail "Unable to login with docker"
	ssh "$USER@$SERVER" "docker pull  $CI_REGISTRY_IMAGE:$VERSION"							|| fail "Unable to docker pull"
	ssh "$USER@$SERVER" "echo $ENV_CONFIG"
	ssh "$USER@$SERVER" "docker run -d -p 8090:8090 -p 9015:9015 --net=host -e ENV_CONFIG=./src/main/resources/stg-config.yml -e ENVIRONMENT=$ENVIRONMENT --name $CI_PROJECT_NAME $CI_REGISTRY_IMAGE:$VERSION" || fail "Unable to run container"
}

prod_deploy(){
	local USER=$1
	local SERVER=$2
	local OUTER_PORT=$3
	local ENVIRONMENT=$4

	configure-ssh-agent

	ssh "$USER@$SERVER" "docker stop $CI_PROJECT_NAME" 										|| echo "Unable to stop container"
	ssh "$USER@$SERVER" "docker rm $CI_PROJECT_NAME" 										|| echo "Unable to remove container"
	ssh "$USER@$SERVER" "docker login -u gitlab-ci-token -p $CI_BUILD_TOKEN $CI_REGISTRY"  	|| fail "Unable to login with docker"
	ssh "$USER@$SERVER" "docker pull  $CI_REGISTRY_IMAGE:$PROD_VERSION"							|| fail "Unable to docker pull"
	ssh "$USER@$SERVER" "echo $ENV_CONFIG"
	ssh "$USER@$SERVER" "docker run -d --log-opt max-size=500m --log-opt max-file=1 -p 8090:8090 -p 9015:9015 --net=host -e ENV_CONFIG=./src/main/resources/prod-config.yml -e ENVIRONMENT=$ENVIRONMENT --name $CI_PROJECT_NAME $CI_REGISTRY_IMAGE:$PROD_VERSION" || fail "Unable to run container"
}

function compose() {
    local USER=$1
    local SERVER=$2
    local CONFIG_FILE=$3

    configure-ssh-agent
    ssh "$USER@$SERVER" "sudo docker login -u gitlab-ci-token -p $CI_BUILD_TOKEN $CI_REGISTRY" || fail "Unable to login with docker"
    ssh "$USER@$SERVER" "export TAG='$DEV_VERSION' && sudo docker-compose -f $CONFIG_FILE pull horse-api && docker-compose -f $CONFIG_FILE up -d --force-recreate" || fail "Unable to start docker compose"
}


function dev_deploy(){
	local USER=$1
	local SERVER=$2
	local OUTER_PORT=$3
	local ENVIRONMENT=$4

	configure-ssh-agent

    ssh "$USER@$SERVER" "docker stop $CI_PROJECT_NAME" 										|| echo "Unable to stop container"
	ssh "$USER@$SERVER" "docker rm $CI_PROJECT_NAME" 										|| echo "Unable to remove container"
	ssh "$USER@$SERVER" "docker login -u gitlab-ci-token -p $CI_BUILD_TOKEN $CI_REGISTRY"  	|| fail "Unable to login with docker"
	ssh "$USER@$SERVER" "docker pull  $CI_REGISTRY_IMAGE:$DEV_VERSION"							|| fail "Unable to docker pull"
	ssh "$USER@$SERVER" "echo $ENV_CONFIG"
    ssh "$USER@$SERVER" "docker run -d -p 8090:8090 -p 9015:9015 -e ENV_CONFIG=./src/main/resources/dev-config.yml -e ENVIRONMENT=$ENVIRONMENT --net=host --name $CI_PROJECT_NAME $CI_REGISTRY_IMAGE:$DEV_VERSION"
}

check_service_availability(){
	local SERVER=$1
	local PORT=$2
    echo "Checking if application is up."
    retry "curl -s -S $SERVER:$PORT/admin/ping" 8 5
    if [ $? -eq 0 ]
		then
			echo Service is up!
			return 0
		else
			echo Unable to check service availability. Max retries reached, so giving up
			return 1
	fi
}