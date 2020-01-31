insert into account(account_num,balance,created_date_time,currency_code,first_name,last_modified_date_time,last_name) values(123456,5000,CURRENT_TIMESTAMP(),'USD','Anuj',CURRENT_TIMESTAMP(),'Dalal');

select * from account;

get account :-

http://localhost:8080/account/123456/

create account:-

http://localhost:8080/account/create/

{
    "firstName": "Yashika",
    "lastName": "Dalal",
    "currencyCode": "USD",
    "balance": 500000.00,
    "createdDateTime": "2020-01-26T22:34:10",
    "lastModifiedDateTime": "2020-01-26T22:34:10"
}

deposit funds:-

http://localhost:8080/account/123456/deposit/25000/

transfer Funds:-

http://localhost:8080/account/transferFunds/

{
    "currencyCode": "USD",
    "amount": 2000.00,
    "fromAccountId": 123457,
    "toAccountId": 123456
}