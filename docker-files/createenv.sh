#!/usr/bin/env bash

CATALINA_HOME=/usr/local/tomcat
SET_ENV_FILE=$CATALINA_HOME/bin/setenv.sh
rm -f $SET_ENV_FILE
touch $SET_ENV_FILE
chmod +r $SET_ENV_FILE

# Used in `apollo-config.groovy`

# Variables for connecting to the cetral database.
echo "export WEBAPOLLO_DB_HOST=${WEBAPOLLO_DB_HOST}" >> $SET_ENV_FILE
echo "export WEBAPOLLO_DB_NAME=${WEBAPOLLO_DB_NAME}" >> $SET_ENV_FILE
echo "export WEBAPOLLO_DB_USERNAME=${WEBAPOLLO_DB_USERNAME}" >> $SET_ENV_FILE
echo "export WEBAPOLLO_DB_PASSWORD=${WEBAPOLLO_DB_PASSWORD}" >> $SET_ENV_FILE

# Indicates if we want to start our own postgres server
echo "export WEBAPOLLO_START_POSTGRES=${WEBAPOLLO_START_POSTGRES}" >> $SET_ENV_FILE

# Indicates if we use Chado.  Default 'true'.
echo "export WEBAPOLLO_USE_CHADO=${WEBAPOLLO_USE_CHADO}" >> $SET_ENV_FILE

# Chado parameters
echo "export CHADO_DB_HOST=${CHADO_DB_HOST}" >> $SET_ENV_FILE
echo "export CHADO_DB_NAME=${CHADO_DB_NAME}" >> $SET_ENV_FILE
echo "export CHADO_DB_USERNAME=${CHADO_DB_USERNAME}" >> $SET_ENV_FILE
echo "export CHADO_DB_PASSWORD=${CHADO_DB_PASSWORD}" >> $SET_ENV_FILE

# Regular WebApollo options
echo "export WEBAPOLLO_COMMON_DATA=${WEBAPOLLO_COMMON_DATA}" >> $SET_ENV_FILE
echo "export WEBAPOLLO_MINIMUM_INTRON_SIZE=${WEBAPOLLO_MINIMUM_INTRON_SIZE}" >> $SET_ENV_FILE
echo "export WEBAPOLLO_HISTORY_SIZE=$WEBAPOLLO_HISTORY_SIZE" >> $SET_ENV_FILE
echo "export WEBAPOLLO_OVERLAPPER_CLASS=$WEBAPOLLO_OVERLAPPER_CLASS" >> $SET_ENV_FILE
echo "export WEBAPOLLO_CDS_FOR_NEW_TRANSCRIPTS=$WEBAPOLLO_CDS_FOR_NEW_TRANSCRIPTS" >> $SET_ENV_FILE
echo "export WEBAPOLLO_FEATURE_HAS_DBXREFS=$WEBAPOLLO_FEATURE_HAS_DBXREFS" >> $SET_ENV_FILE
echo "export WEBAPOLLO_FEATURE_HAS_ATTRS=$WEBAPOLLO_FEATURE_HAS_ATTRS" >>$SET_ENV_FILE
echo "export WEBAPOLLO_FEATURE_HAS_PUBMED=$WEBAPOLLO_FEATURE_HAS_PUBMED" >>$SET_ENV_FILE
echo "export WEBAPOLLO_FEATURE_HAS_GO=$WEBAPOLLO_FEATURE_HAS_GO" >>$SET_ENV_FILE
echo "export WEBAPOLLO_FEATURE_HAS_COMMENTS=$WEBAPOLLO_FEATURE_HAS_COMMENTS" >>$SET_ENV_FILE
echo "export WEBAPOLLO_FEATURE_HAS_STATUS=$WEBAPOLLO_FEATURE_HAS_STATUS" >> $SET_ENV_FILE
echo "export WEBAPOLLO_TRANSLATION_TABLE=$WEBAPOLLO_TRANSLATION_TABLE" >> $SET_ENV_FILE
echo "export WEBAPOLLO_SPLICE_DONOR_SITES=$WEBAPOLLO_SPLICE_DONOR_SITES" >> $SET_ENV_FILE
echo "export WEBAPOLLO_SPLICE_ACCEPTOR_SITES=$WEBAPOLLO_SPLICE_ACCEPTOR_SITES" >> $SET_ENV_FILE
echo "export WEBAPOLLO_GFF3_SOURCE=$WEBAPOLLO_GFF3_SOURCE" >>$SET_ENV_FILE
echo "export WEBAPOLLO_GOOGLE_ANALYTICS_ID=$WEBAPOLLO_GOOGLE_ANALYTICS_ID" >> $SET_ENV_FILE

# If we want to register a user at the offset.
echo "export APOLLO_ADMIN_EMAIL=$APOLLO_ADMIN_EMAIL" >> $SET_ENV_FILE
echo "export APOLLO_ADMIN_PASSWORD=$APOLLO_ADMIN_PASSWORD" >> $SET_ENV_FILE
echo "export APOLLO_ADMIN_FIRST_NAME=$APOLLO_ADMIN_FIRST_NAME" >> $SET_ENV_FILE
echo "export APOLLO_ADMIN_LAST_NAME=$APOLLO_ADMIN_LAST_NAME" >> $SET_ENV_FILE

# If we want to change authentication methods
echo "export WEBAPOLLO_REMOTE_USER_AUTH=$WEBAPOLLO_REMOTE_USER_AUTH" >> $SET_ENV_FILE
echo "export WEBAPOLLO_USER_PASSWORD_AUTH=$WEBAPOLLO_USER_PASSWORD_AUTH" >> $SET_ENV_FILE
