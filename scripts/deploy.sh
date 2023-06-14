#!/bin/bash

REPOSITORY=/home/ec2-user/official-gsm-admin-server

cd $REPOSITORY

docker build -t official-admin-test-image .

docker-compose up -d

docker image prune