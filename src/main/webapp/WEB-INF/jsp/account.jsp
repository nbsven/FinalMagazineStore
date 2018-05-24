<!DOCTYPE html>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <meta charset="utf-8">
</head>

<body>
<br><br><br><br>

<h2>My subscriptions</h2>
<table border="1">
    <tr>
        <th>Id</th>
        <th>Magazine name</th>
        <th>Publication date</th>
        <th>Price per month</th>
        <th>Duration of subscription</th>
    </tr>

    <c:forEach items="${subscriptions}" var="subscription">
        <tr>
            <td>${subscription.id}</td>
            <td>${subscription.name}</td>
            <td>${subscription.date}</td>
            <td>${subscription.price}</td>
            <td>${subscription.duration}</td>
        </tr>
    </c:forEach>
</table>

<br><br><br><br>

<a href=../catalog>See magazines catalog</a>

</body>
</html>