mvn clean package -DskipTests

docker build -t ramazanzor/todoapp .
docker run -p 8080:8080 -t ramazanzor/todoapp