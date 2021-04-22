<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="fr">
    <head>
        <meta charset="UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Chats</title>
        <script>
            async function deleteFunction(chatId) {
  
                // Awaiting fetch which contains 
                // method, headers and content-type
                const response = await fetch('chatManager?chat_id='+chatId, {
                    method: 'DELETE',
                    headers: {
                        'Content-type': 'application/json'
                    },
                }).then(response => {
                    if (response.status === 200) {
                        location.reload();
                    } else if (response.status === 401) {
                        window.location.href = './toConnexion.jsp';
                    } else if (response.status === 404) {
                        window.location.href = './deletechatError.jsp';
                    }
                })
                .catch(error => {
                    window.location.href = './deletechatError.jsp';
                });
                return;
            }
        </script>
    </head>
    <body>
        <%@ include file="dashboard.jsp" %><br>
        <h1>chats's list:</h1> <br>
        <nav>
            <ul>
                <c:forEach items="${requestScope.allChats}" var="chat">
                    <li>
                        Title: <c:out value="${chat.title}"/> -
                        Owner: <c:out value="${chat.owner.familyName}"/> -
                        Begin: <c:out value="${chat.beginDate}"/> - 
                        End: <c:out value="${chat.endDate}"/>
                        <span>
                            <button type="button" class="deleteButton" onClick="deleteFunction(${chat.id})"> Delete </button>
                        </span>
                        <span>
                            <button type="button" class="accesButton" onClick="location.href='chatManager?chat_id=${chat.id}'";> Access </button>
                        </span>
                    </li>
                </c:forEach>
            </ul>
        </nav>
    </body>
</html>
