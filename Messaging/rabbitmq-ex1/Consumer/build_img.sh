#!/bin/bash
cp -f ext_jars/amqp-client-5.7.1.jar image/amqp-client-5.7.1.jar
cp -f target/rabbitmq-consumer-1.0.0.jar image/rmqconsumer.jar
docker build ./image --tag rmq-consumer
