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

### Benchmark

```text
❯❯❯ ab -p create.json -T application/json -c 10 -n 2000 http://localhost:8080/reservations
This is ApacheBench, Version 2.3 <$Revision: 1843412 $>
Copyright 1996 Adam Twiss, Zeus Technology Ltd, http://www.zeustech.net/
Licensed to The Apache Software Foundation, http://www.apache.org/

Benchmarking localhost (be patient)
Completed 200 requests
Completed 400 requests
Completed 600 requests
Completed 800 requests
Completed 1000 requests
Completed 1200 requests
Completed 1400 requests
Completed 1600 requests
Completed 1800 requests
Completed 2000 requests
Finished 2000 requests


Server Software:        
Server Hostname:        localhost
Server Port:            8080

Document Path:          /reservations
Document Length:        117 bytes

Concurrency Level:      10
Time taken for tests:   0.729 seconds
Complete requests:      2000
Failed requests:        0
Non-2xx responses:      2000
Total transferred:      444000 bytes
Total body sent:        424000
HTML transferred:       234000 bytes
Requests per second:    2745.31 [#/sec] (mean)
Time per request:       3.643 [ms] (mean)
Time per request:       0.364 [ms] (mean, across all concurrent requests)
Transfer rate:          595.17 [Kbytes/sec] received
                        568.37 kb/s sent
                        1163.54 kb/s total

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0    0   0.1      0       2
Processing:     1    3   2.1      3      22
Waiting:        0    2   1.6      2      20
Total:          1    4   2.1      3      22

Percentage of the requests served within a certain time (ms)
  50%      3
  66%      4
  75%      4
  80%      5
  90%      6
  95%      8
  98%     10
  99%     12
 100%     22 (longest request)
```