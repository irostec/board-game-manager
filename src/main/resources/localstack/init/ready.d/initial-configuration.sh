#!/bin/bash

awslocal cloudformation deploy \
    --stack-name parameters \
    --template-file "/etc/localstack/boardgamemanager/cloudformation/parameters.yml"
