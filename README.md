# BankingSystem-ATM
Simple BankingSystem connecting to local database

1. Compile the java (JDBC) program
 javac BankingSystem.java
 javac p1.java
2. Please have your properties file including all information required to connect to your database
3. Call on the compiled java program with the properties file as a parameter.  First download the db2jcc4.jar (jdbc driver) to your current directory, then run the command below (This is for Mac)
    java -cp ":./db2jcc4.jar" p1 db.properties
  
    
IMPORTANT INFO: In my system, I will not allow any kind of actions on inactive/closed account, including deposit, withdraw, or transfer. All of these actions will be denied toward inactive account. The balance of inactive account will be always zero.

Screen #1: 
Welcome to the Self Services Banking System!
1. New Customer
2. Customer Login
3. Exit

For 1 and 2, I will ask if user want to continue this requirement in case that they may accidentally enter the wrong number. Enter y to continue, and anything to exist. Then I will prompt out all inputs one at a time, and check these inputs once together. If fail, then jump back to the prompt “Enter y to continue”. If success, 1 will jump back to screen#1, and 2 will jump to screen#2. Special: if user enter 0 for both pin and id, then system will jump to screen#3. 
Screen #2:
Customer Main Menu!
1. Open Account
2. Close Account
3. Deposite
4. Withdraw
5. Transfer
6. Account Summary
7. Exist

For all, except 7 and 6, the system will prompt out all inputs one at a time, and check these input once together. For both fail and success, system will jump back to screen#2. The screen will prompt out result telling if user fail or success. For 6, system will automatically return a summary.

Screen #3:
Customer Main Menu!
1. Account Summary for a Customer
2. Report A
3. Report B
4. Exist

For 1 and 3, the system will prompt out all inputs one at a time, and check these input once together. For both fail and success, system will jump back to screen#3, including 2. 2 will automatically return a summary. 


Additional Notes:
1. The special administrator ID and Pin (0,0) can be hardcoded in your program as a special ID/PIN.
2. Customer IDs are system generated and initiated by the “New Customer“ operation.
3. The customer and administrator main menus are the default top level menu after an operation.
4. You can open an account for someone else but you cannot close someone else’s account.
5. You can deposit into other people’s accounts but you can’t withdraw from them.
6. You can transfer money from your account to someone else but not the reverse.
7. All the range checking need to be handled both in DDLs and your application.
8. Customer account summary should not include accounts in the “closed/inactive” state.
9. For all the administrator reports, closed/inactive accounts will not be part of the reports.
10. For database connections information, use a properties file.
11. Must handle error condition gracefully (e.g. should not crash and exit because of any exceptions).
12. You will be given Java code framework and you need to put your logic inside.
    
    
