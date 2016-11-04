#!/bin/bash
# Shell script to deploy the server war to Tomcat

TOMCAT_WEBAPPS=$1
SERVER_WAR_FILENAME=$2
SERVER_DEPLOY_DIR=$3
ARTIFACT_DIR_PATH=$4

sudo rm -rf $TOMCAT_WEBAPPS/$SERVER_DEPLOY_DIR
sudo cp $ARTIFACT_DIR_PATH/$SERVER_WAR_FILENAME $TOMCAT_WEBAPPS
sudo 7z x -o$TOMCAT_WEBAPPS/$SERVER_DEPLOY_DIR $TOMCAT_WEBAPPS/$SERVER_WAR_FILENAME
sudo rm $TOMCAT_WEBAPPS/$SERVER_WAR_FILENAME
sudo chown -R tomcat8:tomcat8 $TOMCAT_WEBAPPS/$SERVER_DEPLOY_DIR