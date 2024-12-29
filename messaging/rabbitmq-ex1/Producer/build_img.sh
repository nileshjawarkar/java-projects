#!/bin/bash

cp -f target/rabbitmq-producer-1.0.0.war image/rmqproducer.war
docker build ./image --tag rmq-producer
