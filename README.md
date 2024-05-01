# ShortLink URL Shortener

An application which encodes and decodes URLs. It is a REST API which exposes two endpints:
- `/encode`: encodes a URL. For example, `https://example.com/hello/world` could become `http://fdv.me/fnIen`
- `/decode`: decodes a URL to its original form. For example, `http://fdv.me/fnIen` would decode to `https://example.com/hello/world`

## Running Locally

Prerequisites:

- Java 17
- Maven

To run the application:

```sh
mvn spring-boot:run
```

And, send requests to the endpoints:

```sh
curl -X POST http://localhost:8080/encode \
-H "Content-Type: application/json" \
-d '{"url":"https://example.com/hello/world"}'
```

```sh
curl -X POST http://localhost:8080/decode \
-H "Content-Type: application/json" \
-d '{"url":"http://fdv.me/fnIen"}'
```

## Running Tests

To run tests, run the following command:

```sh
mvn test
```
or, to run a full build, including tests

```sh
mvn clean install
```