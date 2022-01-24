###
## Preconditions
## - Docker is installed
####  https://docs.docker.com/v17.12/install/
####  https://www.tutorialspoint.com/docker/installing_docker_on_linux.htm
####  https://www.tutorialspoint.com/docker/docker_installation.htm
## - DockerCompose is installed
####  https://docs.docker.com/compose/install/
## - The .sh files have proper permissions
####  chmod u+x *.sh
####  or
####  sudo chmod u+x *.sh
## - Ports 5054 and 3306 MUST be available. If you have MySQL server running - please stop it.
###

# Pull the image.
./0_vikta_pull.sh

# Launch the app in demo mode (no DB(h2), data will be generated) in a container.
./1_vikta_run.sh -p

# Have a look at the logs to know when the app is up and running.
docker logs vikta --follow

## Variations

# Launch the app in semi-working mode (MySql DB, DB and data will be generated) in a container.
./1_vikta_run.sh -p -t mysql_create

# Note: MySQL admin user credentials are:
# MyQL Server port is ```3306```
# root/ROOpass
# target DB - ```vikta_db```

# Launch stopped app having previously generated/saved data preserved (MySql DB).
#  Assumption: the app was launched in a container, MySql is the DB.
#  Some data has already been added or generated so you would like to keep it.
./1_vikta_run.sh -t mysql

### Troubleshooting notes:
## - it may take up to three(3) minutes to run Vikta+MySql+generate_data for the first time.
## - in case you cannot open localhost:5054 and there is an SqlException in the log upon such start - just try to run ./1_vikta....sh one more time.

# Visit http://localhost:5054/
#  or
# http://localhost:5054/swagger-ui.html

# In case of pre-generated data usage there will be
## [!] admin user: admin/123qweadmin
# and
#  [!] a regular one: qq/123

# If you select to go with "empty" DB admin user account will be generated anyway:
## [!] admin user: admin/123qweadmin
======