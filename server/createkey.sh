#!/bin/bash
# Script to generate RSA keypair in java keystore

KEY_PATH=./src/main/resources/io/craigmiller160/orgbuilder/server
KEYSTORE_FILE=$KEY_PATH/orgkeystore.jceks
CERT_FILE=$KEY_PATH/orgapikey.cert

if [[ -f $KEYSTORE_FILE ]]; then
    rm $KEYSTORE_FILE
fi

if [[ -f $CERT_FILE ]]; then
    rm $CERT_FILE
fi

echo "Generating RSA keypair"
keytool -genkeypair -keyalg RSA -alias orgapikey -keysize 4096 -keystore $KEYSTORE_FILE -storetype JCEKS
echo "Exporting RSA cert"
keytool -exportcert -alias orgapikey -file $CERT_FILE -keystore $KEYSTORE_FILE -storetype JCEKS -rfc