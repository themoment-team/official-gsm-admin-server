#!/bin/bash

REPOSITORY=/home/ec2-user/official-gsm-admin-server

cd $REPOSITORY

docker build --platform linux/arm64 -t official-test-server:admin .

docker-compose up -d

docker image prune