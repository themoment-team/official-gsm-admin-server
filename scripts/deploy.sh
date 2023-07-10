#!/bin/bash

REPOSITORY=/home/ec2-user/official-gsm-admin-server

cd $REPOSITORY

docker build --platform linux/amd64 -t official-test-server:admin .

docker-compose up -d

docker rmi $(docker images -f "dangling=true" -q)