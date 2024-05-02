# ShortLink URL Shortener

An application which encodes and decodes URLs. It is a REST API which exposes two endpoints:

- `/encode`: encodes a URL. For example, `https://example.com/hello/world` could become `http://short.est/fnIeFf`
- `/decode`: decodes a URL to its original form. For example, `http://short.est/fnIeFf` would decode
  to `https://example.com/hello/world`

The base URL for the shortened URLs is configurable via an application property, `base.url`.

## Running Locally

Prerequisites:

- Java 17
- Maven

To run the application:

```sh
mvn spring-boot:run
```

And, send requests to the endpoints. For example:

```sh
curl -X POST http://localhost:8080/encode \
-H "Content-Type: application/json" \
-d '{"url":"https://example.com/hello/world"}'

{"url":"http://short.est/DwmII0"}
```

```sh
curl -X POST http://localhost:8080/decode \
-H "Content-Type: application/json" \
-d '{"url":"http://short.est/DwmII0"}'

{"url":"https://example.com/hello/world"}
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