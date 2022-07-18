# Statement microservice
### Project Structure

![img_17.png](img_17.png)

### Pre requisite (Java 8 +,maven 3+ ,ms db files)

### Steps
#### Clone https://github.com/ybdahe/statements.git
#### Import as maven project
#### run mvn clean Install -DskipTests or mvn clean Install command
#### run StatementsApplication.java will start spring boot application on 8080 port
#### Test Actuators after application up http://localhost:8080/actuator/health
#### Test Swagger link http://localhost:8080/swagger-ui/
![img_18.png](img_18.png)

## Service Flow
###1:- Call Auth api to get token 
#### Api :- http://localhost:8080/api/auth/signin
![img.png](img.png)
token gets stored in variable jwtToken
![img_2.png](img_2.png)
User Can not log in twice if session is active/token valid expiry 5 min(see application.properties)
#### statement.app.jwtExpirationMs= 300000
if user try log in twice get error
![img_1.png](img_1.png)
###2:- call statement api using jwtToken and params required
### Admin User
with account number
![img_3.png](img_3.png)

![img_4.png](img_4.png)
Error with no token or expired/invalid token

![img_11.png](img_11.png)

With Proper Token Got zero statement as we don't have statements record for 2022 and if we don't pass date range it took last 3 months statements
![img_5.png](img_5.png)

Pass Date range 
account number encrypted using org.springframework.security.crypto.password.PasswordEncoder
![img_6.png](img_6.png)

Pass Amount range
![img_7.png](img_7.png)

### Normal User
auth/signin
![img_8.png](img_8.png)
call statement api ,
Got zero statement as we don't have statements record for 2022 and if we don't pass date range it took last 3 months statements

![img_9.png](img_9.png)

Error(401) if param passed
![img_10.png](img_10.png)

###3:- Use logout api to logout

logout user success

![img_12.png](img_12.png)

logout error if try twice logout

![img_13.png](img_13.png)

### Sonar Scan result :- Zero issue left , all resolved

![img_15.png](img_15.png)

### Unit Test case and Code Coverage result
Note:- 61% current coverage can extend >80 %
(Due to less time not able to cover all code). 

![img_16.png](img_16.png)

### Dockerized (see Docker file in classpath) :- Can be improved
#### docker build -t statements.
#### docker run -p 8080:8080 -t statements

### Postman Collection attached(see in classpath)
File Statements.postman_collection.json