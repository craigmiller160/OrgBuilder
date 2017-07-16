#!/bin/bash
# Shell script to deploy the server war to Tomcat

if [[ $# -lt 3 ]]; then
    echo "Error! Incorrect number of arguments: $#"
    exit 1
fi

TOMCAT_WEBAPPS="/var/lib/tomcat8/webapps"
SERVER_WAR_FILENAME=$1
SERVER_DEPLOY_DIR=$2
ARTIFACT_DIR_PATH=$3

echo $SERVER_WAR_FILENAME
echo $SERVER_DEPLOY_DIR
echo $ARTIFACT_DIR_PATH

sudo rm -rf $TOMCAT_WEBAPPS/$SERVER_DEPLOY_DIR
sudo cp $ARTIFACT_DIR_PATH/$SERVER_WAR_FILENAME $TOMCAT_WEBAPPS
sudo 7z x -o$TOMCAT_WEBAPPS/$SERVER_DEPLOY_DIR $TOMCAT_WEBAPPS/$SERVER_WAR_FILENAME
sudo rm $TOMCAT_WEBAPPS/$SERVER_WAR_FILENAME
sudo chown -R tomcat8:tomcat8 $TOMCAT_WEBAPPS/$SERVER_DEPLOY_DIR