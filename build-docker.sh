#/bin/bash

### Env variables 
set ENV_URL="jdbc:postgresql://awue1athcef-pt-rds-cluster.c59hzuikncxn.us-east-1.rds.amazonaws.com:5432/ctrefc"
set ENV_USER="controlefect"
set ENV_SCHEMA="controlefect"
set ENV_BUCKET="awue1athcef-pt-interfaces"
set ENV_REGION="us-east-1"
set JAR_FILE="admin-efectivo-0.0.1.jar"

### build spring boot app with maven 
./mvnw clean verify 

### From aws secret manager
set ENV_PASS="$(aws secretsmanager get-secret-value --secret-id awue1athcef-pt-rds-secrets --query SecretString --output text --region us-east-1)" 

### Build docker image
docker build --build-arg ENV_URL=$ENV_URL,ENV_USER=$ENV_USER,ENV_SCHEMA=$ENV_SCHEMA,ENV_BUCKET=$ENV_BUCKET,ENV_REGION=$ENV_REGION,JAR_FILE=$JAR_FILE -t 652041729658.dkr.ecr.us-east-1.amazonaws.com/awue1athcef-pt-ecr-fargate:latest .

### upload docker image to aws ECR
docker push 652041729658.dkr.ecr.us-east-1.amazonaws.com/awue1athcef-pt-ecr-fargate:latest

