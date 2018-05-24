<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"
          integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
</head>
<body>
<c:url value="/add_publisher" var="add_action"/>

<div class="container fluid">
    <jsp:include page="header.jsp"/>
    <div class="container" style="margin-outside: 20px; margin-top: 20px">

        <c:if test="${role=='ROLE_ADMIN'}">
            <div>
                <form:form class="form-inline" action="${add_action}" method="post" modelAttribute="publisher">
                    <div class="row">
                        <div class="col">
                            <label for="pubName"><spring:message code="text.publisher.name"/></label>
                            <form:input type="text" class="form-control mb-2 mr-sm-2" name="publisherName"
                                        path="name" id="pubName"/>
                        </div>


                        <div class="col">
                            <label for="pubPhone"><spring:message code="text.publisher.phone"/></label>
                            <form:input type="text" class="form-control mb-2 mr-sm-2"
                                        name="telephoneNumber"
                                        path="telephoneNumber" id="pubPhone"/>
                        </div>

                        <input class="btn btn-success mb-2" type="submit"
                               value="<spring:message code="text.publisher.add"/>">

                    </div>
                </form:form>
            </div>
        </c:if>

        <table class="table table-bordered">

            <tr>
                <th class="text-center" scope="col"><spring:message code="text.publisher.name"/></th>
                <th class="text-center" scope="col"><spring:message code="text.publisher.phone"/></th>
                <th class="text-center" scope="col"><spring:message code="text.action"/></th>
            </tr>

            <c:forEach items="${publishers}" var="publisher">
                <tr>
                    <td class="text-center">${publisher.name}</td>
                    <td class="text-center">${publisher.telephoneNumber}</td>
                    <td class="text-center">
                        <form action="<c:url value="/delete_publisher"/>" method="post">
                            <input type="hidden" name="publisherId" value="${publisher.id}">
                            <input type="submit" class="btn btn-danger"
                                   value="<spring:message code="text.delete"/>">
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </div>
</div>

</body>
<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"
        integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN"
        crossorigin="anonymous"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"
        integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl"
        crossorigin="anonymous"></script>
</html>