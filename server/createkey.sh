#!/bin/bash
# Script to generate RSA keypair in java keystore

KEY_PATH=./src/main/resources/io/craigmiller160/orgbuilder/server/keys
KEYSTORE_FILE=$KEY_PATH/orgkeystore.jceks
CERT_FILE=$KEY_PATH/orgapitoken.pem
TOKEN_KEY_ALIAS=orgapitoken
DATA_KEY_ALIAS=orgapidata
KEYSTORE_TYPE=JCEKS


function main {
    echo "The main function - NOT IMPLEMENTED"
}

function create_token_key {
    echo "Create the JSON Web Token RSA KeyPair"
    read -p "Press any key to continue"

    if [[ -f $KEYSTORE_FILE ]]; then
        echo "Deleting existing token RSA keypair from keystore"
        keytool -delete -alias $TOKEN_KEY_ALIAS -keystore $KEYSTORE_FILE -storetype $KEYSTORE_TYPE
    fi

    if [[ -f $CERT_FILE ]]; then
        echo "Deleting old token RSA public certificate"
        rm $CERT_FILE
    fi

    echo "Generating token RSA keypair."
    read -p "Press any key to continue"
    keytool -genkeypair -alias $TOKEN_KEY_ALIAS -keyalg RSA  -keysize 4096 -keystore $KEYSTORE_FILE -storetype $KEYSTORE_TYPE

    echo "Exporting token RSA public certificate."
    keytool -exportcert -alias $TOKEN_KEY_ALIAS -file $CERT_FILE -keystore $KEYSTORE_FILE -storetype $KEYSTORE_TYPE -rfc
}

function create_data_key {
    echo "Creating the database AES encryption key"
    read -p "Press any key to continue."

    if [[ -f $KEYSTORE_FILE ]]; then
        echo "Deleting existing database AES key from keystore"
        keytool -delete -alias $DATA_KEY_ALIAS -keystore $KEYSTORE_FILE -storetype $KEYSTORE_TYPE
    fi

    echo "Generating database AES key"
    keytool -genseckey -alias $DATA_KEY_ALIAS -keyalg AES -keysize 128 -keystore $KEYSTORE_FILE -storetype $KEYSTORE_TYPE
}

function create_database_ssl_certs {
    echo "Creating database ssl certs - NOT IMPLEMENTED"
}

function create_app_ssl_certs {
    echo "Creating app ssl certs - NOT IMPLEMENTED"
}

function script_help {
    echo "Create Key Script Commands:"
    echo "Use any combination of these flags to execute the operations"
    echo ""
    echo "  -t  =  Create the JSON Web Token RSA KeyPair."
    echo "  -d  =  Create the database AES encryption key."
    echo "  -s  =  Create the database SSL certificates."
    echo "  -S  =  Create the application SSL certificates."
}

if [[ $# -eq 0 ]]; then
    script_help
fi

main ${@}