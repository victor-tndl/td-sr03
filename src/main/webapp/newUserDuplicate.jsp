<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>
<body>
<c:out value="${requestScope.firstName}"/>
    <h1>One user as the same family name and unser name. Do you want to persist this user ?</h1>
    <form method="POST" action="userValidation">
        Yes <input type="radio" name="validate" value="yes" /> 
        No <input type="radio" name="validate" value="no" />
        <input type="hidden" name="firstName" value="${requestScope.firstName}"/>
        <input type="hidden" name="familyName" value="${requestScope.famillyName}"/>
        <input type="hidden" name="login" value="${requestScope.login}"/>
        <input type="hidden" name="gender" value="${requestScope.gender}"/>
        <input type="hidden" name="password" value="${requestScope.password}" /><br>
        <input type ="submit" value="Post" name="validator" />
    </form>    
</body>
</html>
