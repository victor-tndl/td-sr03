<html>
<head> <title>TODO supply a title</title> <meta charset="UTF-8"> <meta name="viewport" content="width=device-width,
initial-scale=1.0">
</head>
<body>
	<form action="userValidation" method="post">
		<label> First name </label> <input type="text" id="frname" name="firstName"/><br>
		<label> Familly name </label> <input type="text" id="faname" name="familyName"/><br>
		<label> Email (login)</label> <input type="email" id="email" name="login"/> <br>
		<label> Password </label> <input type="password" id="psw" name="password"/><br/>
		<label> male </label> <input type="radio" id="male" name="gender" value="Male" checked/><br>
		<label> female </label> <input type="radio" id="female" name="gender" value="Female"/> <br>
		<label> Admin </label> <input type="checkbox" name="admin" value="Admin"/> <br>
		<input type="submit" value="Submit">
	</form>
</body>
</html>