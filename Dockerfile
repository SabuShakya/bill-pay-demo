FROM tomcat:8-jdk8-openjdk-slim

LABEL maintainer=”sabu.shakya@f1soft.com”

COPY /target/*.war /usr/local/tomcat/webapps/

WORKDIR /usr/local/tomcat/webapps/

EXPOSE 9090
CMD ["catalina.sh", "run"]
