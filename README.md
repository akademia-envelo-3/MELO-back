# MELO-back
### Pre requisition
1. [ ] JAVA 17
2. [ ] Installed Maven
3. [ ] Installed Docker
### Build
To build and start application in docker. Run file [packageandcomposeup.bat](packageandcomposeup.bat)</br>
Windows:
```shell
./packageandcomposeup.bat
```
### Access
Link to api documentation:</br>
[http://localhost:8080/api/swagger-ui/index.html](http://localhost:8080/api/swagger-ui/index.html)

To receive authorization token you need to send 
```http
POST: http://localhost:8081/realms/melorealm/protocol/openid-connect/token
```
with body:

| key           | value                            |
|---------------|:---------------------------------|
| client_id     | melo-client                      |
| username      | employee / admin                 |
| password      | abcDEF123                        |
| grant_type    | password                         |
| client_secret | NZB84wphJPah5w3mo2TMnFzHe6Oq1qTR | 

###
To access database via pgAdmin:

[http://localhost:8082/](http://localhost:8082/)
```properties
login: admin@admin.com
password: sa
```
###
To access keycloak:

[http://localhost:8081/](http://localhost:8081/)

```properties
login: admin
password: admin
```
###
Configuration of mail server is in file: [application.properties](src/main/resources/application.properties)