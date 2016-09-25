#!/bin/bash
# Script to generate RSA keypair in java keystore

keytool -genkeypair -keyalg RSA -alias orgapikey -keysize 4096 -keystore ./orgkeystore -storetype JKS