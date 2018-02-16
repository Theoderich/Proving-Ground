FROM centos:7.4.1708

RUN yum update -y && \
yum install -y wget && \
yum install -y java-1.8.0-openjdk java-1.8.0-openjdk-devel && \
yum clean all

COPY entrypoint.sh /entrypoint.sh
COPY healthcheck.sh /healthcheck.sh

RUN chmod +x /entrypoint.sh && \
chmod +x /healthcheck.sh

ENTRYPOINT ["./entrypoint.sh"]
HEALTHCHECK CMD ./healthcheck.sh

COPY proving-ground-webapp.jar /application/proving-ground-webapp.jar

EXPOSE 8080


