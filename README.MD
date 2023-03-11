# Authorization Server
The service runs on port 8091

## Development
To build the authorization service:
1. Ensure you have Java 11 enabled
2. Run the following command
```shell script
./gradlew clean build
```

To run the application with the default profile, run
```shell script
./gradlew bootRun
```
and to run it with the dev profile, run
```shell script
./gradlew bootRun --args='--spring.profiles.active=dev'
```

## Documentation
### Project Documentation
To generate the html KDoc documentation(equivalent of JavaDoc for Kotlin) with Dokka, run
```shell script
./gradlew dokkaHtml
```
The generated documentation should be found at /build/dokka/index.html. The browser can be used to view it 

### SpringDoc OpenAPI documentation (with Swagger-UI)
The api docs can be viewed by navigating to [API Documentation](http://localhost:8091/swagger-ui.html).
To view the api docs in json, navigate to [API Documentation in JSON](http://localhost:8091/v3/api-docs)

>NOTE: The API docs are available when running the dev profile

## Tests and Test Coverage
To run tests and check test coverage, run 
```shell script
./gradlew check
```
this will execute the jacocoTestReport and jacocoTestCoverageVerification tasks in addition to the tests. <br><br>
The jacocoTestReport task will generate html reports that can be found at /build/reports/jacoco/html/index.html.
The jacocoTestCoverageVerification task will check that the code coverage is above a specified value and if not, will 
cause the build to fail

<b>Auth yeah!</b>
