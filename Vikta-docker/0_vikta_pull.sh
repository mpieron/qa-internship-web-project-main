#!/usr/bin/env bash
set -e

echo ===
echo === Preconditions
echo === - Docker is installed
echo ======  https://docs.docker.com/v17.12/install/
echo ======  https://www.tutorialspoint.com/docker/installing_docker_on_linux.htm
echo ======  https://www.tutorialspoint.com/docker/docker_installation.htm
echo === - DockerCompose is installed
echo ======  https://docs.docker.com/compose/install/
echo === - The .sh files have proper permissions
echo ======  chmod u+x *.sh
echo === - Ports 5054 and 3306 are available.
echo ===

echo "Have a look at the vikta_readme file."
echo $(cat ./readme.txt)
echo ===

# TODO: Avoid using personal repos, put the image to a GD's repository.
docker pull lzvnevg/vikta:latest
docker tag lzvnevg/vikta:latest eng_qe-practice-dev/vikta:latest