#!/usr/bin/env bash

set -e
set -x

app_name='vikta'
db_name='vikta-mysql'

db_type='h2'
pregenerated_data='no'
mysql_host='vikta-mysql'
mysql_port='3306'

print_help() {
    echo "vikta_run.sh [OPTIONS] to be passed to 'docker run' as evn variables.,"
    echo
    echo "[-p|--pre-generated-data, -t|--db-type=<value>, -P|--mysql-port=<value>, -H|--mysql-host=<value>]"
    echo
    echo "vikta_run.sh without params is just a shortcut for docker-compose up"
}

print_tips() {
    echo "useful commands"
    echo
    echo "docker ps"
    echo "docker logs ${app_name} --follow"
    echo "docker volume ls"
}

parse_parameters() {
    # read options like -t|--db-type [mysql, mysql_create, h2], -p|--pre-generated-data [yes/no]
    while [[ $# -gt 0 ]]
    do
        key="${1}"
        case ${key} in
        -t|--db-type)
            db_type="${2}"
            shift # past argument
            ;;
        -p|--pre-generated-data)
            pregenerated_data='yes'
            ;;
        -P|--mysql-port)
            mysql_port="${2}"
            shift
            ;;
        -H|--mysql-host)
            mysql_host="${2}"
            shift
            ;;
        -h|--help)
            print_help
            exit 0 # past argument
            ;;
        *)    # unknown option
            shift # past argument
            ;;
        esac
        shift
    done
}

stop_container_and_clean_up() {
    name="$1"
    echo "request to stop container ${name} is received"
    if [[ "$(docker container ps -aq --filter name="^${name}$")" ]]
    then
        echo "container ${name} is found"
        if [[ "$(docker container ps -aq --filter status=running --filter name="^${name}$")" ]]
        then
            echo "container ${name} is running and will be stopped"
            docker stop "${name}"
        fi
        echo "container ${name} will be removed"
        docker rm "${name}"
    fi
}

run_container_app() {
    echo "container ${app_name} is going to be started"
    docker-compose run \
    --service-ports \
    -e DB=${db_type} \
    -e PREGENERATED_DATA=${pregenerated_data} \
    -e MYSQL_HOST=${mysql_host} \
    -e MYSQL_PORT=${mysql_port} \
    --name "${app_name}" \
    --detach \
    --no-deps \
    "${app_name}"
}

run_container_db() {
    echo "container ${db_name} is going to be started"
    docker-compose run \
    --service-ports \
    --volume=vikta_vikta-mysql-data-volume:/var/lib/mysql \
    --volume=./my.cnf:/etc/mysql/conf.d/my.cnf \
    --name "${db_name}" \
    --detach \
    "${db_name}"
}

# if no args provided run docker-compose up and exit
if [[ $# -eq 0 ]]
then
    stop_container_and_clean_up ${app_name}
    stop_container_and_clean_up ${db_name}
    echo "services are going to be started using docker-compose"
    docker-compose up --detach
else
    parse_parameters $@
    if [[ "${db_type}" =~ ^mysql ]]
    then
        echo "${db_name} will be restarted"
        stop_container_and_clean_up "${db_name}"
        run_container_db
    fi
    stop_container_and_clean_up "${app_name}"
    run_container_app
fi
print_tips
