#/bin/bash

### Env variables 
set ARG_URL="jdbc:postgresql://db:5432/ctrefc"
set ENV_USER=controlefect
set ENV_PASS="123546"
set ENV_SCHEMA="controlefect"
set ENV_BUCKET="awue1athcef-pt-interfaces"
set ENV_REGION="us-east-1"


### build spring boot app with maven 
#./mvnw clean verify 

### From aws secret manager
#set ENV_PASS="$(aws secretsmanager get-secret-value --secret-id awue1athcef-pt-rds-secrets --query SecretString --output text --region us-east-1)" 

### Build docker image
docker build --build-arg ENV_URL=jdbc:postgresql://db:5432/ctrefc --build-arg ENV_USER=controlefect --build-arg ENV_PASS=123546 --build-arg ENV_SCHEMA=public --build-arg ENV_BUCKET=awue1athcef-pt-interfaces --build-arg ENV_REGION=us-east-1 -t awue1athcef-pt-ecr-fargate:latest .
