# Control de Efectivo - Backend

Repositorio para la aplicacion Spring Boot de Control de Efectivo.

---

## Prerequisitos

Para desplegar el proyecto en ambiente local de desarrollo se deben contar con las siguientes herramientas y programas

1. Instalado **JDK** version 17
2. Instalado **maven**  
3. Cliente de **Git** 
4. Cuenta ATH habilitada en Artifactory Nexus ([link](https://devops-nexus.ath.net/))
5. Cuenta ATH habilitada en el repositorio Github ([link](https://devops-github.ath.net/))

---

## Configuracion ambiente local de desarrollo

1. Para clonar el repositorio se debe configurar el cliente Git con las opciones **core.autocrlf** y **http.sslVerify**, para esto se de ejecutar los siguientes comandos en una terminal:

```shell
git config --global core.autocrlf true
git config --global http.sslVerify "false"
```

2. Configurar settings de maven para agregar las credenciales de la cuenta ATH habilitada en Artifactory Nexus, para esto se debe incluir en el archivo **settings.xml** la siguientes lineas, se debe reemplzar con las credenciales correspondientes: 

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

3. Compilar proyecto spring boot con maven con el siguiente comando:

```shell
mvn package
```

4. Lanzar aplicacion spring boot con el comando siguiente: 

```shell
mvn boot:run
```

5. Configuracion para UnitTests

