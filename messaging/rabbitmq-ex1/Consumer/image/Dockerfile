FROM amazoncorretto:8-alpine-jre
RUN mkdir -p /opt/consumer
COPY rmqconsumer.jar /opt/consumer/
COPY amqp-client-5.7.1.jar /opt/consumer/
COPY slf4j-api-1.7.9.jar /opt/consumer/
COPY slf4j-simple-1.7.9.jar /opt/consumer/

CMD ["/usr/bin/java", "-classpath", \
".:/opt/consumer/amqp-client-5.7.1.jar:/opt/consumer/slf4j-simple-1.7.9.jar:/opt/consumer/slf4j-api-1.7.9.jar:/opt/consumer/rmqconsumer.jar",\
"com.nj2.Service"]

