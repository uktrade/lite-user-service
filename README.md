# LITE User Service

Interface for retrieving user account details from SPIRE via SOAP.

## Running locally

* `git clone git@github.com:uktrade/lite-user-service.git`
* `cd lite-user-service` 
* `cp src/main/resources/sample-config.yaml src/main/resources/config.yaml`
* `./gradlew run`

You will need to update `config.yaml` with passwords for the mocking service from Vault.

You will also need to run a local Redis, e.g. `docker run -p 6379:6379 --name my-redis -d redis:latest`. If your Redis
is not running with default settings, you will need to update connection details in `config.yaml`.

## Endpoints

* `GET /user-privileges/{userId}` produces `UserPrivilegesView`, requires JWT Authorization header
* `GET /user-details/{userId}` produces `UserDetailsView`, requires JWT Authorization header
* `GET /user-account-type/{userId}` produces `UserAccountTypeView`, requires basic auth header with service login

Results are cached using [lite-dropwizard-common/redis-cache](https://github.com/uktrade/lite-dropwizard-common/tree/master/redis-cache).

## Admin endpoints

* `GET /ready`

## JWT Authorisation Header

Endpoints which use JWT for authorisation require the "Authorization" header in the following format:

```
Authorization: Bearer <JWT>
```

For example:

```
curl -X GET -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJTb21lIGxpdGUgYXBwbGljYXRpb24iLCJleHAiOjE1MzkzNjIwMzMsImp0aSI6InA0MDJRMzFkRXlTeTNiWUxlc2Q5a2ciLCJpYXQiOjE1MDc4MjYwMzMsIm5iZiI6MTUwNzgyNTkxMywic3ViIjoiMTIzNDU2IiwiZW1haWwiOiJleGFtcGxlQGV4YW1wbGUuY29tIiwiZnVsbE5hbWUiOiJNciBUZXN0In0.qlu5a6hAVvUO-XrftkLCk_1xqhYjWtCaotR7narg7EU" '/user-privileges/{userId}'
```

See [lite-dropwizard-common](https://github.com/uktrade/lite-dropwizard-common) JWT for more information.