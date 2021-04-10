<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>
<body>
    <h1>One user as the same family name and unser name. Do you want to persist this user ?  </h1>
    <form method='POST' action='userValidation'>
        Yes <input type='radio' name='validate' value='yes' /> 
        No <input type='radio' name='validate' value='no' />
        <input type='hidden' name='User first name' value='" + firstName + "'/>
        <input type='hidden' name='User familly name' value='" + familyName + "'/>
        <input type='hidden' name='User email' value='" + login + "'/>
        <input type='hidden' name='gender' value='" + gender + "'/>
        <input type='hidden' name='User password' value='" + password + "' /><br>
        <input type ='submit' value='Envoyer' name='validator' />
    </form>    
</body>
</html>
