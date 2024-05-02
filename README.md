# ShortLink URL Shortener

An application which encodes and decodes URLs. It is a REST API which exposes two endpoints:

- `/encode`: encodes a URL. For example, `https://example.com/hello/world` could become `http://short.est/fnIeFf`
- `/decode`: decodes a URL to its original form. For example, `http://short.est/fnIeFf` would decode
  to `https://example.com/hello/world`

The application stores data in memory, and so data is not persisted between sessions. i.e. an encoded URL can only be
decoded within the same session.

### Configuration

The base URL for the shortened URLs is configurable via an application property `base.url`. Encoded URLs will contain
this base URL plus a random alphanumeric path of a configurable length via an application property `path.length`. Note
that a shorter path length can result in more collisions with previously generated URLs and degrade performance.

### URL Validation

| Part of the URL | Required? | Examples                   | Notes                                                   |
|-----------------|-----------|----------------------------|---------------------------------------------------------|
| Protocol        | Yes       | `http://`, `https://`      | Currently, only `http://` and `https://` are acceptable |
| Subdomain       | No        | `www.`                     |                                                         |
| Domain          | Yes       | `example.com`              |                                                         |
| Port            | No        | `:8080`                    |                                                         |
| Path            | No        | `/path/to/resource`        |                                                         |
| Query String    | No        | `?key1=value1&key2=value2` |                                                         |
| Fragment        | No        | `#section1`                |                                                         |

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

Or, to run a full build, including tests:

```sh
mvn clean install
```

Then, run the created `jar` (note: version may differ):

```sh
java -jar target/urlshortener-0.0.1.jar
```