# Control de Efectivo - Backend

Repositorio para la aplicacion Spring Boot de Control de Efectivo.

---

## Prerequisitos

Para desplegar el proyecto en ambiente local de desarrollo se deben contar con las siguientes herramientas y programas

1. Instalado **JDK** version 17
2. Instalado **MAVEN**  
3. Cliente de **GIT** 
4. Cliente de comandos AWS cli
5. Cuenta ATH habilitada en Artifactory Nexus ([link](https://devops-nexus.ath.net/))
6. Cuenta ATH habilitada en el repositorio Github ([link](https://devops-github.ath.net/))

---
## Configuracion ambiente local de desarrollo

1) Para clonar el repositorio se debe configurar el cliente Git con las opciones **core.autocrlf** y **http.sslVerify**, para esto se de ejecutar los siguientes comandos en una terminal:

```shell
git config --global core.autocrlf true
git config --global http.sslVerify "false"
```

2) Configurar settings de maven para agregar las credenciales de la cuenta ATH habilitada en Artifactory Nexus, para esto se debe incluir en el archivo **settings.xml** la siguientes lineas, se debe reemplzar con las credenciales correspondientes: 

```xml
<servers>
      <server>
        <id>maven-central</id>
        <username>usuario_ath</username>
        <password>contrasena</password>
      </server>
  </servers>
```

Este archivo **settings.xml** se debe copiar al directorio ``home_usuario/.m2`` o se pasa como para metro al comando maven con la opcion ``--settings ruta_archivo/settings.xml`` 


3) Compilar proyecto spring boot con maven con el siguiente comando:

```shell
mvn package
```

4) Lanzar aplicacion spring boot con el comando siguiente: 

```shell
mvn boot:run
```

5) Configuracion para UnitTests

---
## Configuración con Docker y docker-compose

En el archivo ``Dockerfile`` se encuentran definidas las variable requeridas para construir la image docker

- ENV_URL 
- ENV_USER 
- ENV_PASS 
- ENV_SCHEMA 
- ENV_BUCKET 
- ENV_REGION 
- JAR_FILE 

Se hace uso del archivo ``build-docker.sh`` para asignar estas variables y realizar el proceso de construcicon de la imagen docker.

###### 1) Configurar base de datos local 

En el archivo ``docker-compose.yaml`` asignar valores para las variables de ambiente en el servicio 'db' de base datos y en el servicio 'app'.

###### 1) Construir imagen docker

Actualizar variables en archivo ``build-docker.sh`` 

```shell
sh build-docker.sh 
```

###### 3) Ejecutar docker-compose

```shell
 docker compose -f docker-compose.yaml --profile cef up -d
```

###### 4) detener servicios docker

```shell
docker compose -f docker-compose.yaml --profile cef down
```

Profiles docker configurados:
- cef (Servicios aplicacion backend y base de datos Postgres)
- db (solo base de datos Postgres)
- app (solo aplicacion backend )

---
## Configuración AWS y upload de imagen docker

###### 1) configurar credenciales AWS 

Con el siguiente comando se realiza la configuracion de credenciales:

```shell
aws configure
```

###### 2) Upload de imagen a servicio AWS

```shell
aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin 652041729658.dkr.ecr.us-east-1.amazonaws.com
docker push 652041729658.dkr.ecr.us-east-1.amazonaws.com/awue1athcef-pt-ecr-fargate:latest
```

