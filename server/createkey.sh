#!/bin/bash
# Script to generate RSA keypair in java keystore

KEY_PATH=./src/main/resources/io/craigmiller160/orgbuilder/server
KEYSTORE_FILE=$KEY_PATH/orgkeystore.jceks
CERT_FILE=$KEY_PATH/orgapitoken.cert
TOKEN_KEY_ALIAS=orgapitoken
DATA_KEY_ALIAS=orgapidata

if [[ -f $KEYSTORE_FILE ]]; then
    echo "Deleting old keystore"
    rm $KEYSTORE_FILE
fi

if [[ -f $CERT_FILE ]]; then
    echo "Deleting old cert file"
    rm $CERT_FILE
fi

echo "Generating RSA keypair"
keytool -genkeypair -alias $TOKEN_KEY_ALIAS -keyalg RSA  -keysize 4096 -keystore $KEYSTORE_FILE -storetype JCEKS

echo "Generating AES key"
keytool -genseckey -alias $DATA_KEY_ALIAS -keyalg AES -keysize 128 -keystore $KEYSTORE_FILE -storetype JCEKS

echo "Exporting RSA cert"
keytool -exportcert -alias $TOKEN_KEY_ALIAS -file $CERT_FILE -keystore $KEYSTORE_FILE -storetype JCEKS -rfc