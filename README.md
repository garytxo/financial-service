# Parser Digital Coding Challenge

By [Gary Murray](mailto:garytxo@gmail.com)

## Scope of the work

Create a Java REST application for a financial institution that contains the information and operations for
Accounts​ and ​Transfers.​

This application should have the following ​persisted information​ for each entity type. You may decide the
persistence system:

| Account        |          | 
| ------------- |-------------|
| accountId     | Account​ Identifier |
| iban      | Account I​BAN (2 chars + 22 digits)      |  
| balance | Account​ balance     |    
| currency | Account​ currency    |
| openDate | Account​ opening date      |
| active | Account​ active     |



| Transfer        |          | 
| ------------- |-------------|
| source     | Source ​Account​ Identifier |
| destination      | Destination ​Account​ Identifier     |  
| amount | Amount to be transferred from ​source​ to ​destination     |    
| description | Transfer​ description (p.e. “Salary November 2018”)   |
| timestamp | Date and Time of the ​Transfer    |


### Please implement the following services:

- Account - Endpoint (​http://localhost:8080/Account​)
    
    * Create, Read, Update and Delete
- Transfers - Endpoint (​http://localhost:8080/Transfer​) 

    * Create, Read
    * Execute Transfer

- Please ensure that the services implement the following business rules:

    * Transfer​s can only be made between two active ​Accounts​ 
    * Transfer​s can be made between different currency ​Account​s. If a ​Transfer​ is done between two
      different currencies, the specific currency is defined by the destination ​Account​ and the exchange rate is got from:
      o https://api.exchangeratesapi.io/latest?base=<baseCode>&symbols=<currencyCode> 
      (p.e. https://api.exchangeratesapi.io/latest?base=GBP&symbols=EUR​)
    * Every m minutes all the ​Account​ balances are updated due to an Operational Banking Tax. This Tax details (rate and recurrence) should be defined in the configuration file.
    * Account​ AccountId is an internal field that shouldn’t be visible in the Read operations, instead the Account​ IBAN should be shown (and generated during Account creation)
    * The application should respond to REST commands on the specified Endpoints and the READ operations should provide sort and filter parameters (at least) by the following fields:
        * Account​: ​IBAN​, ​Balance​ and ​Currency
        * Transfer:​ ​Source​, ​Destination​ and ​dateTime
    * The application should be able to read on startup two CSV files from an input folder named “input” defined in the configuration
    which are define in the resource folder `src/main/resources/input`

## Prerequisite
In order to install and start the application please ensure that you have the following:
- Java JDK (v8+)
- Maven (v3+)
- MySQL (optional MySQL Workbench)


## Instructions
1. The code can be found in github  [financial-service](https://github.com/garytxo/financial-service)
2. Clone locally using
   `git clone git@github.com:garytxo/financial-service`
   
3. create the financial database schema by opening a terminal and executing the following command
    
    `mysql -uroot -Bse'CREATE DATABASE financial'`
4. Install dependencies and run test
    `cd financial-app`
    `mvn clean install`

5. Start your server using 
   
    `mvn spring-boot:run`
    
6. To view the REST API endpoints check the swagger documentation [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)


## API USAGE
The following are a list of useful API usages one can perform

-  Create a new account The following will create a new bank account with 100 euros deposit
`curl -X POST "http://localhost:8080/Account" -H "accept: */*" -H "Content-Type: application/json" -d "{ \"balance\": 100, \"currency\": \"EUR\"}"`

- Update an account status or currency :
`curl -X PUT "http://localhost:8080/Account/MC242531931898PO86I8BD25Q26" -H "accept: */*" -H "Content-Type: application/json" -d "{ \"currency\": \"USD\"}"`

- Delete an account by the IBAN number
`curl -X DELETE "http://localhost:8080/Account/MC242531931898PO86I8BD25Q26" -H "accept: */*"`

- Find all account with currency EUR and order by balance desc
`curl -X GET "http://localhost:8080/Account?currency=EUR&orderBy=balance&sortOrder=desc" -H "accept: */*"`

- Find all account with balance of 100 EUR and order by currency descending
`curl -X GET "http://localhost:8080/Account?balance=100&currency=EUR&orderBy=currency&sortOrder=asc" -H "accept: */*"`

- Create a transfer between to valid accounts which has not been executed
`curl -X POST "http://localhost:8080/Transfer" -H "accept: */*" -H "Content-Type: application/json" -d "{ \"amount\": 100, \"description\": \"Party at the Ritz\", \"destination\": \"AT931551366098047339\", \"source\": \"GB14HFLH31198266710918\"}"`

- Find all transfers 
`curl -X GET "http://localhost:8080/Transfer" -H "accept: */*"`

- Find transfer by for a certain destination account id and order by source desc
`curl -X GET "http://localhost:8080/Transfer?destination=GB09LUGV58378718496633&orderBy=source&sortOrder=desc" -H "accept: */*"`

- Execute a transfer 
`curl -X PUT "http://localhost:8080/Execute-Transfer/1" -H "accept: */*"`


## Discussion

* Implemented using Java 8 and artifacts are managed by gradle

* Frameworks used 
    * spring boot starter kits
    * mockito & junit test
    * hamcrest
    * swagger -> REST API documentation
    * Super CSV  ->  csv import
    
* swagger for REST documentation
* Spring ControllerAdvice for exception handling
* javax Validator for entity field validation
* create a HandlerMethodArgumentResolver deal with authenticating the logged user

* Decided to use a TTD approach to solve this challenge 
* Separate the business logic and DTO in a different artifacts 

## Things I could look for future enhancements 
- Use flyway to manage database changes
- Could use spring security to enhance the user authentication
- JWT could be used to store the authenticated user credentials 
- Enhance the customize search
- Consider using the [JSR 354: Money and Currency API](https://jcp.org/en/jsr/detail?id=354) which might make it for JDK 9