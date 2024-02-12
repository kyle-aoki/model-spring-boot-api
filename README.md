```
docker run -d -p 5432:5432 -e POSTGRES_PASSWORD=hunter2 postgres
docker run -d -p 6379:6379 redis

./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```
