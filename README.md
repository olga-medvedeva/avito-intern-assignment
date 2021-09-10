# avito-intern-assignment
Microservice for working with user balance

Java + Spring Boot + PostgreSQL + Mockito + Docker-compose

Service has 3 DB tables:
- user
  Which contains colomns: userId and amount;
- currency
  Which contains colomns: currencyName and currencyValue;
- transaction
  Which contains colomns: id, amount, comment, date, transaction type, userId
When service starts only currency table has any informaition in it, user table and transaction table remains empty until first deposit request.

Service has 4 functions:  
- Check [GET]:
  Takes id parameter and not required currency code.
  (Service has data base with currecy codes and their value which updates every hour from https://exchangeratesapi.io/ api)
  If currecy code is given, then serviсу converts RUB into the specified currency and returns result in BigDecimal format (if currency is not given, service  just returns the result from DB);
  
- Deposit [POST]:
  Takes changeRequest object (userId, amount and comment (userId and amount have validation, comment not required)), if user with specified id isn't found, service creates new user with that id, deposit him specified amount and saves his into DB.

- Withdraw [POST]:
  Takes changeRequest object (same as deposit function), if user with specified id isn't found or user has less amount of money than specified withdraw amount, service throws Exception. Otherwise service withdraws money and saves user into DB.

- Transfer [POST]:
  Takes transferRequest object (idToWithdraw, idToDeposit, amount and comment (all parameters except for comment is validated, comment not required)). Service checks if both user exist, if user to withdraw doesn't exit or doesn't have enought money service will throw Exception, if user to deposit isn't exit, service (same as deposit function) will create new one.
  If everything is correct, service will withdraw money from user first, then will deposit it to the other user, and will save them into DB at the end.
  
After saving results into DB (deposit, withdraw, transfer functions) service will create new Transaction object and will save it into transaction table so information about every 
successful transaction will be availible in DB.

Both services (userService and currencyService) have unit tests as well as userController (+ int tests).

I tested program in Postman.  
Deposit:  
[POST] http://localhost:8080/user/deposit  
RequestBody
{  
    "userId" : 1,  
    "amount" : 500  
}  

![1](https://user-images.githubusercontent.com/90202470/132867366-418ab35e-b303-4071-9cb3-11e0451a82e2.png)  

    
[POST] http://localhost:8080/user/deposit  
RequestBody  
{  
    "userId" : 5,  
    "amount" : 3000  
}  

![2](https://user-images.githubusercontent.com/90202470/132867494-c175a0a7-999a-4c95-aab1-d6b548fb9d7c.png)  

Check:  
[GET] http://localhost:8080/user/5/check?currency=USD  

![3](https://user-images.githubusercontent.com/90202470/132867615-d65e5055-a4db-4742-8064-3cc1c5a718bc.png)  

Withdraw:  
[POST] http://localhost:8080/user/withdraw  
RequestBody  
{  
    "userId" : 5,  
    "amount" : 1350.50  
}  

![4](https://user-images.githubusercontent.com/90202470/132867750-146b86fd-66c0-408a-90f3-2e5a1b0a75cb.png)  

Transfer:  
[POST] http://localhost:8080/user/transfer  
RequestBody  
{  
    "idToWithdraw" : 1,  
    "idToDeposit" : 3,  
    "amount" : 246  
}  

![5](https://user-images.githubusercontent.com/90202470/132868144-096e3a90-b1ed-4bc0-b00b-52b65512683e.png)



