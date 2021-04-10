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
                            <a href="forumManager"> Delete </a>
                        </span>
                    </li>
                </c:forEach>
            </ul>
        </nav>
    </body>
</html>
