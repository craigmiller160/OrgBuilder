#!/bin/bash
# Script to generate RSA keypair in java keystore

KEY_PATH=./src/main/resources/io/craigmiller160/orgbuilder/server/keys


TOKEN_RSA_CERT=$KEY_PATH/orgapitoken-rsa-cert.pem
CA_KEY=$KEY_PATH/ca-key.pem
CA_CERT=$KEY_PATH/ca-cert.pem
MYSQL_SERVER_KEY=$KEY_PATH/mysql-server-key.pem
MYSQL_SERVER_REQ=$KEY_PATH/mysql-server-req.pem
MYSQL_SERVER_CERT=$KEY_PATH/mysql-server-cert.pem

KEYSTORE_FILE=$KEY_PATH/orgkeystore.jceks
KEYSTORE_TYPE=JCEKS
TOKEN_KEY_ALIAS=orgapitoken
DATA_KEY_ALIAS=orgapidata
CA_CERT_ALIAS=cacert

# These variables should be moved to a separate file outside of Version Control
KEYSTORE_PASS=orgapi
TOKEN_KEY_PASS=orgapi
DATA_KEY_PASS=orgapi
CA_CERT_PASS=orgapi
TOKEN_KEY_INFO="CN=Craig Miller, OU=Development, O=CraigMiller160, L=Tampa, S=Florida, C=US"
CA_CERT_INFO="/C=US/ST=Florida/L=Tampa/O=CraigMiller160 CA/CN=Craig Miller CA"
MYSQL_REQUEST_INFO="/C=US/ST=Florida/L=Tampa/O=CraigMiller160 MySQL/CN=Craig Miller MySQL"

function main {
    token=false
    data=false
    ca=false
    data_ssl=false

    for (( i=0; i < ${#1}; i++ )); do
        case ${1:$i:1} in
            -) ;;
            t) token=true ;;
            d) data=true ;;
            c) ca=true ;;
            m) data_ssl=true ;;
            *)
                echo "Error! Invalid input"
                exit 1
            ;;
        esac
    done

    if $token ; then
        create_token_key
    fi

    if $data ; then
        create_data_key
    fi

    if $ca ; then
        create_ca_cert
    fi

    if $data_ssl ; then
        create_database_ssl_certs
    fi
}

function create_token_key {
    echo ""
    echo "Create the JSON Web Token RSA KeyPair"

    if [[ -f $KEYSTORE_FILE ]]; then
        echo "Deleting existing token RSA keypair from keystore"
        keytool -delete \
            -alias $TOKEN_KEY_ALIAS \
            -keystore $KEYSTORE_FILE \
            -storetype $KEYSTORE_TYPE \
            -storepass $KEYSTORE_PASS
    fi

    if [[ -f $TOKEN_RSA_CERT ]]; then
        echo "Deleting old token RSA public certificate"
        rm $TOKEN_RSA_CERT
    fi

    echo "Generating token RSA keypair."
    keytool -genkeypair -noprompt \
        -alias $TOKEN_KEY_ALIAS \
        -dname "$TOKEN_KEY_INFO" \
        -keyalg RSA  \
        -keysize 4096 \
        -keystore $KEYSTORE_FILE \
        -storetype $KEYSTORE_TYPE \
        -storepass $KEYSTORE_PASS \
        -keypass $TOKEN_KEY_PASS


    echo "Exporting token RSA public certificate."
    keytool -exportcert \
        -alias $TOKEN_KEY_ALIAS \
        -file $TOKEN_RSA_CERT \
        -keystore $KEYSTORE_FILE \
        -storetype $KEYSTORE_TYPE \
        -keypass $TOKEN_KEY_PASS \
        -storepass $KEYSTORE_PASS \
        -rfc
}

function create_data_key {
    echo ""
    echo "Creating the database AES encryption key"

    if [[ -f $KEYSTORE_FILE ]]; then
        echo "Deleting existing database AES key from keystore"
        keytool -delete \
            -alias $DATA_KEY_ALIAS \
            -keystore $KEYSTORE_FILE \
            -storetype $KEYSTORE_TYPE \
            -storepass $KEYSTORE_PASS \
            -keypass $DATA_KEY_PASS
    fi

    echo "Generating database AES key"
    keytool -genseckey \
        -alias $DATA_KEY_ALIAS \
        -keyalg AES \
        -keysize 128 \
        -keystore $KEYSTORE_FILE \
        -storetype $KEYSTORE_TYPE \
        -storepass $KEYSTORE_PASS \
        -keypass $DATA_KEY_PASS
}

function create_ca_cert {
    echo ""
    echo "Creating self-signed Certificate Authority certificate"

    if [[ -f $KEYSTORE_FILE ]]; then
        echo "Deleting existing CA certificate from keystore"
        keytool -delete \
            -alias $CA_CERT_ALIAS \
            -keystore $KEYSTORE_FILE \
            -storetype $KEYSTORE_TYPE \
            -storepass $KEYSTORE_PASS \
            -keypass $CA_CERT_PASS
    fi

    if [[ -f $CA_KEY ]]; then
        echo "Deleting existing CA private key"
        rm $CA_KEY
    fi

    if [[ -f $CA_CERT ]]; then
        echo "Deleting existing CA certificate"
        rm $CA_CERT
    fi

    echo "Generating new CA private key"
    openssl genrsa 4096 > $CA_KEY

    echo "Generating new CA certificate"
    openssl req \
        -sha256 \
        -new \
        -x509 \
        -nodes \
        -days 3650 \
        -subj "$CA_CERT_INFO" \
        -key $CA_KEY \
        -out $CA_CERT

    echo "Importing CA certificate into KeyStore"
    keytool -import \
        -noprompt \
        -trustcacerts \
        -alias $CA_CERT_ALIAS \
        -keystore $KEYSTORE_FILE \
        -storetype $KEYSTORE_TYPE \
        -storepass $KEYSTORE_PASS \
        -keypass $CA_CERT_PASS \
        -file ca-cert.pem
}

function create_database_ssl_certs {
    echo ""
    echo "Creating MySQL SSL certificates"

    if [[ ! -f $CA_CERT ]]; then
        echo "Cannot find CA certificate, please generate it before trying to generate the MySQL SSL certificate"
        exit 1
    fi

    if [[ -f $MYSQL_SERVER_KEY ]]; then
        echo "Deleting existing MySQL server private key."
        rm $MYSQL_SERVER_KEY
    fi

    if [[ -f $MYSQL_SERVER_REQ ]]; then
        echo "Deleting existing MySQL server certificate request."
        rm $MYSQL_SERVER_REQ
    fi

    if [[ -f $MYSQL_SERVER_CERT ]]; then
        echo "Deleting existing MySQL server certificate."
        rm $MYSQL_SERVER_CERT
    fi

    echo "Generating new MySQL server private key and certificate request."
    openssl req  \
        -sha256 \
        -newkey rsa:4096 \
        -days 730 \
        -nodes \
        -subj "$MYSQL_REQUEST_INFO" \
        -keyout $MYSQL_SERVER_KEY \
        -out $MYSQL_SERVER_REQ

    echo "Converting MySQL server private key to RSA format."
    openssl rsa \
        -in $MYSQL_SERVER_KEY \
        -out $MYSQL_SERVER_KEY

    echo "Generating new MySQL server certificate."
    openssl x509 \
        -sha256 \
        -req \
        -in $MYSQL_SERVER_REQ \
        -days 730 \
        -CA $CA_CERT \
        -CAkey $CA_KEY \
        -set_serial 01 \
        -out $MYSQL_SERVER_CERT
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
    echo "  -c  =  Create the self-signed Certificate Authority certificate."
    echo "  -m  =  Create the database SSL certificates."
}

if [[ $# -eq 0 ]]; then
    script_help
fi

if [[  $# -gt 1 ]]; then
    echo "Error! Too many arguments"
    exit 1
fi

main $1