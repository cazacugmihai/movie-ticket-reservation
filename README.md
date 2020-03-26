# Getting Started

### Requirements

- JDK 1.8 or later
- Gradle 6.3 or later

### Run the tests

> ./gradlew test

### Setup the application to use a local MySQL server

Start an instance of MySQL by using:
> docker run -d --name mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=secret -e MYSQL_DATABASE=movie-ticket-reservation mysql

and modify `build.gradle` file
> comment the `h2` dependency and uncomment the `mysql` one

and `application.yml` file
> uncomment the configuration below `DB - MySQL` line 

### Build an executable JAR

You can run the application by using `./gradlew bootRun`. 

If you also want to populate the database before using the API, start the application by using ` ./gradlew bootRun --args='--spring.profiles.active=with-data'`.

Alternatively, you can build the JAR file by using `./gradlew build` and then run the JAR file, as follows:
> java -jar build/libs/movie-ticket-reservation-0.0.1-SNAPSHOT.jar 

or:
> java -jar build/libs/movie-ticket-reservation-0.0.1-SNAPSHOT.jar --spring.profiles.active=with-data  

### Use the Service

Now that the service is up, we can use these two endpoints:
> curl --request POST \
    --url http://localhost:8080/reservations \
    --header 'content-type: application/json' \
    --data '{
  	"email": "jane.doe@email.com",
  	"movieId": 1,
  	"seats": 2
  }'

> curl --request DELETE \
    --url http://localhost:8080/reservations/4

In case of using the H2 database, we have an UI for it: http://localhost:8080/h2. The username is `sa`.

### TODO

- [ ] document the REST service (e.g. by using Swagger)
- [x] use Spring Actuator (for exposing the application metrics as REST endpoints)
- [ ] use Micrometer (for exposing the Actuator metrics to external monitoring systems)
- [ ] use Docker/Kubernetes
- [ ] use Jenkins/Gitlab (for CI/CD)
- [ ] support API versioning
- [ ] support HATEOAS?

Optimization ideas:
- generate all the tickets and update only some fields (e.g.: email and reserved status)
- use Redis (which supports persistence and transactions)
