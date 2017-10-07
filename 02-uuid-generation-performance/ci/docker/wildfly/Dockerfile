FROM java
MAINTAINER Bartosz Kamiński, http://highcohesionloosecoupling.com
ENV VERSION 10.1.0.Final
ENV INSTALL_DIR /opt
ENV WILDFLY_HOME ${INSTALL_DIR}/wildfly-${VERSION}
ENV DEPLOYMENT_DIR ${WILDFLY_HOME}/standalone/deployments/
RUN yum install -y unzip less \
  && yum clean all \
  && curl -O http://download.jboss.org/wildfly/${VERSION}/wildfly-${VERSION}.zip \
  && unzip wildfly-${VERSION}.zip -d ${INSTALL_DIR} \
  && rm wildfly-${VERSION}.zip \
  && rm ${WILDFLY_HOME}/bin/standalone.conf \
  && chmod a+x ${WILDFLY_HOME}/bin/standalone.sh
ADD standalone.conf ${WILDFLY_HOME}/bin/
ENTRYPOINT ${WILDFLY_HOME}/bin/standalone.sh -c standalone-full.xml -b=0.0.0.0
EXPOSE 8080 8787