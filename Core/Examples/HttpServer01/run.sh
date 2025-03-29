#!/bin/bash
mvn clean install
java -cp ./HttpServer01_lib/target/HttpServer01_lib-0.0.1.jar co.in.nnj.learn.server.Main
