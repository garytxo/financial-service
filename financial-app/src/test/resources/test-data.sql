insert into bank_account (id,currency,iban_number,open_on, status)
values (111,'EUR','TESTS020903200500041045040A111', now(), 'ACTIVE');
insert into bank_account (id,currency,iban_number,open_on, status)
 values (112,'EUR','TESTS020903200500041045040A112', now(), 'ACTIVE');
insert into bank_account (id,currency,iban_number,open_on, status)
 values (113,'GBP','TESTS020903200500041045040A113', now(), 'ACTIVE');
insert into bank_account (id,currency,iban_number,open_on, status)
values (114,'EUR','TESTS020903200500041045040A114', now(), 'ACTIVE');
insert into bank_account (id,currency,iban_number,open_on, status)
values (115,'EUR','TESTS020903200500041045040A115', now(), 'DISABLED');

insert into bank_account_transaction (id,amount,created_on,description,type,bank_account_id)
values(211,1000,now(),'test data','CREDIT',111);
insert into bank_account_transaction (id,amount,created_on,description,type,bank_account_id)
values(212,10,now(),'test data two','CREDIT',111);
insert into bank_account_transaction (id,amount,created_on,description,type,bank_account_id)
values(213,-10,now(),'test data debit','DEBIT',113);
insert into bank_account_transaction (id,amount,created_on,description,type,bank_account_id)
values(214,100,now(),'test data','CREDIT',113);
insert into bank_account_transaction (id,amount,created_on,description,type,bank_account_id)
values(215,500,now(),'test data','CREDIT',115);