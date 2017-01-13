FROM jboss/wildfly

ENV DEPLOY_DIR=/opt/jboss/wildfly/standalone/deployments

EXPOSE 8080 8443

RUN cp /opt/jboss/wildfly/bin/standalone.conf /opt/jboss/wildfly/bin/standalone.conf.orig
RUN cp /opt/jboss/wildfly/standalone/configuration/standalone.xml /opt/jboss/wildfly/standalone/configuration/standalone.xml.orig
COPY standalone.conf /opt/jboss/wildfly/bin/standalone.conf
COPY standalone.xml /opt/jboss/wildfly/standalone/configuration/standalone.xml
COPY *.war /opt/jboss/wildfly/standalone/deployments/

VOLUME /opt/logs 
