# Password Generator
## Описание:
An app for receiving a random password based on parameters specifiedby the user. The parameters are length and exclusion of numbers and special symbols. API service for password generation [Password Generator API](https://api-ninjas.com/api/passwordgenerator) is used in this app.
## Technologies used:
* Java 17
* Spring Boot
* Maven
## Request and response:
The user sends GET request: 
localhost:8080/api/v1/passgen/create?length=20&excludeNumbers=true&excludeSpecialChars=true

And receives a JSON response with the generated password:

![image](https://github.com/BaTyANl/PasswordGeneration/assets/159899923/fa38b2c1-49ab-4ada-a009-3d9105bae153)
## Database:
In project was used PostrgreSQL database. There is ManyToOne connection between user and password and ManyToMany between user and website. 
### Scheme
The structure of database:
![image](https://github.com/BaTyANl/PasswordGeneration/assets/159899923/d4d1d5fa-53df-4876-862d-8ed05ba8d70c)
### Reponse
Demonstration of the connection:
![image](https://github.com/BaTyANl/PasswordGeneration/assets/159899923/ad8e4b91-a9b6-4d7e-909f-6b68d1dd55fa)


