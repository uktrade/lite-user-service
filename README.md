# lite-user-service

## Endpoints

* `GET /user-privileges/{userId}` (requires JWT Authorization header)

## Admin endpoints

* `GET /ready`

## JWT Authorisation Header
Endpoints which use JWT for authorisation require the "Authorization" header in the following format

```
Authorization: Bearer <JWT>
```

For example:

```
curl -X GET -H "Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJTb21lIGxpdGUgc2VydmljZSIsImlhdCI6MTUwNzU0MjM3NiwiZXhwIjoxNjAyMjM2Nzc2LCJhdWQiOiJsaXRlIiwic3ViIjoiMTIzNDU2IiwiZW1haWwiOiJleGFtcGxlQGV4YW1wbGUuY29tIn0.wC_Jc4cOoM4UFX7UHHD3hCUcz8b9UPL_ImncY5FtAho" '/user-privileges/{userId}'
```

Which decodes to:

```json 
{
  "typ": "JWT",
  "alg": "HS256"
}.
{
  "iss": "Some lite service",
  "iat": 1507542376,
  "exp": 1602236776,
  "aud": "lite",
  "sub": "123456",
  "email": "example@example.com"
}
```

Signed with `HMAC SHA-256` and key `demo-secret-which-is-very-long-so-as-to-hit-the-byte-requirement`

See [lite-dropwizard-common](https://github.com/uktrade/lite-dropwizard-common) JWT for more information.