#!/bin/bash
# Script to generate RSA keypair in java keystore

KEY_PATH=./src/main/resources/io/craigmiller160/orgbuilder/server
KEYSTORE_FILE=$KEY_PATH/orgkeystore.jks
CERT_FILE=$KEY_PATH/orgapikey.cert

keytool -genkeypair -keyalg RSA -alias orgapikey -keysize 4096 -keystore $KEYSTORE_FILE -storetype JKS
keytool -exportcert -alias orgapikey -file $CERT_FILE -keystore $KEYSTORE_FILE -rfc