<html>
<head> <title>TODO supply a title</title> <meta charset="UTF-8">
<meta name="viewport" content="width=device-width,initial-scale=1.0">
</head>
<body>
	<form action="chatValidation" method="post">
		<label> Title </label> <input type="text" id="title" name="title"/><br>
		<label> Date </label> <input type="datetime-local" id="beginDate" name="beginDate"
		pattern="[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}" required><br>
		<label> Validity Time </label> <input type="datetime-local" id="endDate" name="endDate"
		pattern="[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}" required><br>
		<input type="submit" value="Submit">
	</form>

	<%@ include file="dashboard.jsp" %>
</body>
</html>