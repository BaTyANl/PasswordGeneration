# Генератор пароля
## Описание:
Приложение для получения случайного пароля на основе параметров, указанных пользователем, например, длина пароля и исключение цифр и специальных символов. В приложении используется API сервис по генерации случайного пароля [Password Generator API](https://api-ninjas.com/api/passwordgenerator).
## Запрос и ответ:
Для получения пароля отправляется GET запрос в формате: 
localhost:8080/api/v1/passgen/create?length=20&excludeNumbers=true&excludeSpecialChars=true
