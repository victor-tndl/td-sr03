<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="model.Forum"%>
<%@ page import="java.util.List"%>
<!DOCTYPE html>
<html lang="fr">
    <head>
        <meta charset="UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Forums</title>
        <script>
            async function deleteFunction(forumId) {
  
                // Awaiting fetch which contains 
                // method, headers and content-type
                const response = await fetch('forumManager?forum_id='+forumId, {
                    method: 'delete',
                    headers: {
                        'Content-type': 'application/json'
                    },
                }).then(response => {
                    if (response.status === 200) {
                        location.reload();
                    } else if (response.status === 401) {
                        window.location.href = './toConnexion.jsp';
                    } else if (response.status === 404) {
                        window.location.href = './deleteForumError.jsp';
                    }
                })
                .catch(error => {
                    window.location.href = './deleteForumError.jsp';
                });
                return;
            }
        </script>
    </head>
    <body>
        <%@ include file="dashboard.jsp" %><br>
        <h1>Forums's list:</h1> <br>
        <nav>
            <ul>
                <c:forEach items="${requestScope.allForums}" var="forum">
                    <li>
                        Title: <c:out value="${forum.title}"/> -
                        Owner: <c:out value="${forum.owner.familyName}"/> -
                        Begin: <c:out value="${forum.beginDate}"/> - 
                        End: <c:out value="${forum.endDate}"/>
                        <span>
                            <button type="button" class="deleteButton" onClick="deleteFunction(${forum.id})"> Delete </button>
                        </span>
                    </li>
                </c:forEach>
            </ul>
        </nav>
    </body>
</html>
