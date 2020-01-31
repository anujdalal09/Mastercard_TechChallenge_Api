# Mastercard Account Service

### README
For this service, I am using SpringBoot along with H2 in-memory database. The Endpoints will be served by local 
Tomcat that is embedded with SpringBoot.

I am using Gradle as the build tool.

You can build the service by using the following command at 'account' directory level
  ```
gradle clean build
  ```
After successful build a jar will be generated inside 'account/build/libs/'

Run the jar with the following command
  ```
java -jar account-0.0.1-SNAPSHOT.jar
  ```
You can hit the endpoints using the following requests

##### Create Account Request:

POST http://localhost:8080/account/create/

Request Body:
  ```
{
    "firstName": "TestFN1",
    "lastName": "TestLN1",
    "currencyCode": "USD",
    "balance": 5000.00
}
  ```
or use the following cURL command
  ```
curl --location --request POST 'http://localhost:8080/account/create/' \
--header 'Content-Type: application/json' \
--data-raw '{
    "firstName": "TestFN1",
    "lastName": "TestLN1",
    "currencyCode": "USD",
    "balance": 5000.00
}'
  ```
NOTE: We can add validation on the CREATE request to make sure that the currency code entered by the user is a valid currency supported by Mastercard
currency conversion API. However, for simplicity, we do not check that for now. Mostly, fields like currency code would usually come from a prepopulated list of available 
currency codes on the front end. If a user enters invalid currency code, account will be created with that currency code for now. 
If a transfer to an invalid currency code will be initiated, mastercard API will give an ERROR during transfer.


##### Fund Account Request:

PUT http://localhost:8080/account/{accountNum}/deposit/{amount}

or use the following cURL below
  ```
curl --location --request PUT 'http://localhost:8080/account/{accountNum}/deposit/{amount}' \
--header 'Content-Type: application/json' \
--data-raw ''
  ```
  
The request will return 200 status with the json response of the updated account details with the balance updated, 
otherwise will return error response with the error message which is returned from the service.
This endpoint handles negative scenario of not having a negative amount or zero amount deposited to the account.

##### Transfer Funds Request (with multi currency transfer supported):

PUT http://localhost:8080/account/transferFunds/

Request Body
  ```
{
    "amount": 20.00,
    "fromAccountId": 123456,
    "toAccountId": 123457
}
  ```
or use the following cURL command
  ```
curl --location --request PUT 'http://localhost:8080/account/transferFunds/' \
--header 'Content-Type: application/json' \
--data-raw '{
    "amount": 20.00,
    "fromAccountId": 123456,
    "toAccountId": 123457
}'
  ```
The request will return 200 status with the json response of the account where amount is transferred i.e. toAccountId 
with the updated account details, otherwise will return error response with the error message which is returned from the service.
This endpoint handles following negative scenarios 
1. NOT having a negative amount or zero amount transferred to the destination account.
2. If source account does not have enough balance to transfer, will return exception
3. If the currency code of source or destination account is not supported by mastercard currecy-conversion api, will return an error


You can access the in-memory database by using the following URL while the service is running. Make sure database field is 'jdbc:h2:mem:testdb'. No credentials are needed. 
You can just hit Connect and it will connect to the database
  ```
http://localhost:8080/h2-console
  ```
  
In order to use Mastercard currency-conversion API, following parameters in the '/account/src/main/resources/application.yaml' file need to be updated
with appropriate values

consumerKey
keyAlias
keyPassword
privateKeyPath


Appropriate Unit tests are written in Mockito. Tests are available in AccountServiceTest.java class

