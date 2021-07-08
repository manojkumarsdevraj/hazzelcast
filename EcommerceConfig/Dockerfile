FROM rmuller/jessie-oraclejre8

ENV GCSFUSE_REPO gcsfuse-jessie
ENV CIMM_DB_URL jdbc:oracle:thin:@db1:1521:prod
ENV CIMM_DB_USER production
ENV CIMM_DB_ACCESS prod123##
ENV CIMM_SOLR_URL http://solr:8983/solr
ENV CIMM2BCENTRAL_URL http://cimm2bc:8080/cimm2bcentral
ENV DROOLS_URL http://drools/

RUN apt-get update && apt-get install --yes --no-install-recommends \
    ca-certificates \
    curl \
  && echo "deb http://packages.cloud.google.com/apt $GCSFUSE_REPO main" \
    | tee /etc/apt/sources.list.d/gcsfuse.list \
  && curl https://packages.cloud.google.com/apt/doc/apt-key.gpg | apt-key add - \
  && apt-get update \
  && apt-get install --yes gcsfuse \
  && apt-get clean && rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/* 

RUN apt-get update && apt-get install -y git
RUN apt-get update && apt-get install -y telnet 
WORKDIR /opt/jboss/
COPY JBoss-5.1.0.GA/ /opt/jboss/
RUN chmod -R 777 /opt/jboss/
ENV JAVA_OPTS "-Dcimm.host.address=localhost -Dcimm.db.connection=$CIMM_DB_URL -Dcimm.db.user=$CIMM_DB_USER -Dcimm.db.access.token=$CIMM_DB_ACCESS"
RUN mkdir -p /var/persistent/ 
WORKDIR /opt/
COPY ecommerce.war  /opt/jboss/server/ecommerce/deploy/ecommerce.ear/ecommerce.war/
RUN chmod +X /opt/jboss/bin
CMD ["/opt/jboss/bin/run.sh", "-c", "ecommerce", "-b", "0.0.0.0"]
EXPOSE 8080
