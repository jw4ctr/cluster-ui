FROM java:8
VOLUME /tmp
RUN mkdir /app
ADD target/vz-cluster-ui-jar-with-dependencies.jar /app/vz-cluster-ui.jar
ADD runboot.sh /app/
RUN bash -c 'touch /app/vz-cluster-ui.jar'
WORKDIR /app
RUN chmod a+x runboot.sh
EXPOSE 8080
CMD /app/runboot.sh