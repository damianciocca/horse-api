#!/bin/sh

fail(){
	echo "Exiting with error: $1"
	exit 1
}

retry(){
	local COUNTER=0
	local COMMAND="$1"
	local TIMES=$2
	local WAIT_TIME=$3
	until [ $COUNTER -ge $TIMES ]
 	do
		$COMMAND && return 0
		let COUNTER=COUNTER+1
		sleep $WAIT_TIME
	done
	return 1
}

configure-ssh-agent(){
  # install ssh-agent
  which ssh-agent || ( apk update  && apk add openssh-client  )
  # run ssh-agent
  eval $(ssh-agent -s)
  # add ssh key stored in SSH_PRIVATE_KEY variable to the agent store
  ssh-add <(echo "$SSH_PRIVATE_KEY")
  # disable host key checking (NOTE: makes you susceptible to man-in-the-middle attacks)
  # WARNING: use only in docker container, if you use it with shell you will overwrite your user's ssh config
  mkdir -p ~/.ssh
  echo -e "Host *\n\tStrictHostKeyChecking no\n\n" > ~/.ssh/config
}