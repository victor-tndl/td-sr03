<html>
    <head>
        <title>Connexion</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
    </head>
    <body>
        <form action="connexionController" method="POST">
            <div>
                <label for="username">Username:</label>
                <input type="text" id="username" name="username">
            </div>
            <div>
                <label for="pass">Password (8 characters minimum):</label>
                <input type="password" id="pass" name="password" required>
            </div>
            <input type="submit" value="Sign in">
        </form>
    </body>
</html>
