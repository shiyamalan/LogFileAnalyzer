-- Creates the login AbolrousHazem with password '340$Uuxwp7Mcxo7Khy'.  
CREATE LOGIN trtuser   
    WITH PASSWORD = 'password';  
GO  

-- Creates a database user for the login created above.  
CREATE USER trtuser FOR LOGIN trtuser;  
GO 