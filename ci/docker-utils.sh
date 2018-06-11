#!/bin/sh

docker_with_login() {
	if [ ! -e ~/.docker/config.json ]; then
		command docker login -u gitlab-ci-token -p $CI_BUILD_TOKEN $CI_REGISTRY || fail "Unable to login with docker"
	fi
	command docker "$@"
}

docker_build() {
	docker_with_login build --pull -t $CI_REGISTRY_IMAGE:$1 .
}

docker_tag_push() {
	docker tag $CI_REGISTRY_IMAGE:$1 $CI_REGISTRY_IMAGE:$2  || fail "Unable to tag image"
	docker_with_login push $CI_REGISTRY_IMAGE:$2
}

list_old_images(){
	docker images --no-trunc --format "{{.Repository}} {{.Tag}} {{.ID}} {{.CreatedSince}}" \
		| grep "space-horse-api" \
		| grep -E "[0-9]+ (weeks|months)"
}

remove_old_images() {
    echo "The following images will be deleted (older than a week):"

	IMAGES_COUNT=$(list_old_images | wc -l || true)

	if [ $IMAGES_COUNT -gt 0 ]
		then
			echo "Starting to delete images..."
				list_old_images \
					| awk '{image_id=$3; print image_id }' \
					| sort | uniq \
					| xargs --no-run-if-empty docker rmi -f
		else
			echo -e "\tNo images found!"
	fi
}