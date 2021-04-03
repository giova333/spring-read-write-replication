#!/bin/bash

set -e

echo ************Creating Write Replica*************

docker run -d \
 --name mysql_master \
 -e MYSQL_ROOT_PASSWORD=root \
 -e MYSQL_USER=user \
 -e MYSQL_PASSWORD=password \
 -e MYSQL_DATABASE=test \
 -e REPLICATION_USER=replication_user \
 -e REPLICATION_PASSWORD=password \
 -p 3307:3306 \
 actency/docker-mysql-replication:5.7

sleep 10s

 echo ************Creating Read Replica*************

 docker run -d \
  --name mysql_slave \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_USER=user \
  -e MYSQL_PASSWORD=password \
  -e MYSQL_DATABASE=test \
  -e REPLICATION_USER=replication_user \
  -e REPLICATION_PASSWORD=password \
  -p 3308:3306 \
  --restart on-failure	\
  --link mysql_master:master \
  actency/docker-mysql-replication:5.7