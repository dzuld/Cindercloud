FROM frolvlad/alpine-oraclejdk8:slim

VOLUME /tmp
ADD build/libs/whitehat.jar whitehat.jar
RUN sh -c 'touch /whitehat.jar' && \
    mkdir config

ENV JAVA_OPTS=""

EXPOSE 8080

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /whitehat.jar" ]