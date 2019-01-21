FROM anapsix/alpine-java:latest

VOLUME /tmp
ADD target/wallet-0.0.1-SNAPSHOT.jar web.jar
RUN sh -c 'touch /web.jar' && \
    mkdir config

ENV JAVA_OPTS=""

EXPOSE 8080

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /web.jar" ]