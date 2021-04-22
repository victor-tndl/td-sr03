<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Navigation Administrateur</title>
    </head>
    <body>
        <h1>Bonjour <c:out value="${sessionScope.sessionToken.userFirstName}"/></h1>
        <c:if test="${sessionScope.sessionToken.userIsAdmin == true}">
            <nav>
                <ul>
                    Connected <br>
                    <li><a href='newUser.jsp'>Create a new user</a></li>
                    <li><a href='newChat.jsp'>Create a new chat</a></li>
                    <li><a href='userManager'>List all users</a></li>
                    <li><a href='chatManager'>List all chats</a></li>
                    <li><a href='deconnexionController'>Deconnect</a></li>
                </ul>
            </nav>
        </c:if>
        <c:if test="${sessionScope.sessionToken.userIsAdmin != true}">
            <nav>
                <ul>
                    Connected <br>
                    <li><a href='newChat.jsp'>Create a new chat</a></li>
                    <li><a href='chatManager'>List your chats</a></li>
                    <li><a href='deconnexionController'>Deconnect</a></li>
                </ul>
            </nav>
        </c:if>

    </body>
</html>

