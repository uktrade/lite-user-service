# lite-user-service

## Endpoints

* `GET /user-privileges/{userId}` mapped to `UserPrivilegesView` (requires JWT Authorization header) 

## Admin endpoints

* `GET /ready`

## API Jar

Classes needed to consume the endpoints of this service exposed via a published jar, see the following:
* Gradle: `compile group: 'uk.gov.bis.lite', name: 'lite-user-service-api', version: '1.0'`
* SBT: `libraryDependencies += "uk.gov.bis.lite" % "lite-user-service-api" % "1.0"`

## JWT Authorisation Header
Endpoints which use JWT for authorisation require the "Authorization" header in the following format

```
Authorization: Bearer <JWT>
```

For example:

```
curl -X GET -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJTb21lIGxpdGUgYXBwbGljYXRpb24iLCJleHAiOjE1MzkzNjIwMzMsImp0aSI6InA0MDJRMzFkRXlTeTNiWUxlc2Q5a2ciLCJpYXQiOjE1MDc4MjYwMzMsIm5iZiI6MTUwNzgyNTkxMywic3ViIjoiMTIzNDU2IiwiZW1haWwiOiJleGFtcGxlQGV4YW1wbGUuY29tIiwiZnVsbE5hbWUiOiJNciBUZXN0In0.qlu5a6hAVvUO-XrftkLCk_1xqhYjWtCaotR7narg7EU" '/user-privileges/{userId}'
```

Which decodes to:

```json 
{
  "typ": "JWT",
  "alg": "HS256"
}.
{
  "iss": "Some lite application",
  "exp": 1539362033,
  "jti": "p402Q31dEySy3bYLesd9kg",
  "iat": 1507826033,
  "nbf": 1507825913,
  "sub": "123456",
  "email": "example@example.com",
  "fullName": "Mr Test"
}
```

Signed with `HMAC SHA-256` and key `demo-secret-which-is-very-long-so-as-to-hit-the-byte-requirement`

See [lite-dropwizard-common](https://github.com/uktrade/lite-dropwizard-common) JWT for more information.


## Example responses

GET /user-privileges/24492

```json
{
    "userAccountType": "EXPORTER",
    "customers": [
        {
            "customerId": "SAR1",
            "role": "ADMIN"
        }
    ],
    "sites": [
        {
            "siteId": "SAR1_SITE1",
            "role": "ADMIN"
        }
    ]
}
```