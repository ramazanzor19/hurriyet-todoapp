## Requirements
- Docker
- Docker Compose
- Maven

## Build
Run the below command on the project base path
```
mvn clean install
```
## Test
Run the below command on the project base path
```
mvn test
```
## Run with Dependencies
Run the container with below command, it will also configure the couchbase. You can edit couchbase/configure.sh file to change couchbase setup.

```
docker-compose up
```

You can check the swagger on below url after startup
```
http://localhost:8080/swagger-ui.html
```