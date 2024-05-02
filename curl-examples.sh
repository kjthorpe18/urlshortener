curl -X POST http://localhost:8080/encode \
-H "Content-Type: application/json" \
-d '{"url":"http://www.example.com/hello"}'

curl -X POST http://localhost:8080/decode \
-H "Content-Type: application/json" \
-d '{"url":"http://short.est/ZGsfmn"}'