1. Get the .zip file from GD.
Unzip it to a <Vikta_launch directory> and follow the Readme if there is one applicable.

2. Switch Docker into "Linux based containers" mode

3. Run Git Bush shell(assumption: it is installed) in the <Vikta_launch directory>.

4. Execute
export COMPOSE_CONVERT_WINDOWS_PATHS=1

in the Bush shell console to avoid Docker volume access issues (https://github.com/docker/compose/issues/4303)

5. Execute
./0_vikta_pull.sh

to pull the application's image from DockerHub.

7. Execute
./1_vikta_run.sh -p

to run the application using dockerized MySql and generated data.

NOTEs:
- your workstation shall run Win Professional
- your workstation shall have at least 6-8GB of RAM. 16GB is preferable.