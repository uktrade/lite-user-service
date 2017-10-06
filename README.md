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
curl -X GET -H "Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJPbmxpbmUgSldUIEJ1aWxkZXIiLCJpYXQiOjE1MDcxMjE1ODAsImV4cCI6MTUzODY1NzU4NywiYXVkIjoibGl0ZSIsInN1YiI6IjEyMzQ1NiIsImVtYWlsIjoiZXhhbXBsZUBleGFtcGxlLmNvbSJ9.wUKTzHkQoym-KCWzFUFrXeEKRQ3y3to-CBeHbqOxW4s" '/user-privileges/{userId}'
```

Which decodes to:

```json 
 {
  "typ": "JWT",
  "alg": "HS256"
 }.
 {
  "iss": "Online JWT Builder",
  "iat": 1507121580,
  "exp": 1538657587,
  "aud": "lite",
  "sub": "123456",
  "email": "example@example.com"
 }
```

Signed with `HMAC SHA-256` and key `demo-secret-which-is-very-long-so-as-to-hit-the-byte-requirement`

See [lite-dropwizard-common](https://github.com/uktrade/lite-dropwizard-common) JWT for more information.