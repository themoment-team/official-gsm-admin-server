#!/bin/bash

REPOSITORY=/home/ec2-user/official-gsm-admin-server

cd $REPOSITORY

docker build -t official-test-server:admin .

docker-compose up -d

docker rmi $(docker images -f "dangling=true" -q)