<!DOCTYPE html>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
    <meta charset="utf-8">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"
          integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.10.16/css/dataTables.bootstrap4.min.css">
</head>

<body>

<div class="container fluid">
    <jsp:include page="header.jsp"/>

    <br/>

    <h2><spring:message code="text.modification"/></h2>

    <form:form class="form-inline" action="/modify_magazine" method="post" modelAttribute="modifiedMagazine">

        <table class="table table-bordered">
            <tr>
                <th><spring:message code="text.status"/></th>
                <th><spring:message code="text.magazine.name"/></th>
                <th><spring:message code="text.magazine.publication_date"/></th>
                <th><spring:message code="text.magazine.publisher"/></th>
                <th><spring:message code="text.magazine.price"/></th>
            </tr>


            <tr>
                <td><spring:message code="text.old"/></td>
                <td>${magazine.name}</td>
                <td>${magazine.publicationDate}</td>
                <td>${magazine.publisher.name}</td>
                <td>${magazine.price}</td>
            </tr>


            <tr>

                <td><spring:message code="text.new"/></td>

                <div hidden>
                    <form:input type="number" value="${magazine.id}" class="form-control mb-2 mr-sm-2" name="magazineId"
                                path="id"/>
                </div>

                <td>
                    <form:input type="text" class="form-control mb-2 mr-sm-2" name="magazineName" path="name"/>
                </td>

                <td>
                    <form:input type="number" class="form-control mb-2 mr-sm-2" name="magazineDatePublication" max="31"
                                min="1"
                                path="publicationDate"/>
                </td>

                <td>
                    <form:select class="form-control mb-2 mr-sm-2" path="publisher">
                        <form:options items="${publishers}" itemLabel="name"/>
                    </form:select>
                </td>

                <td>
                    <form:input type="text" class="form-control mb-2 mr-sm-2" name="magazinePrice" path="price"/>
                </td>

            </tr>

        </table>
        <br/>

        <input class="btn btn-success mb-2" type="submit"
               value=<spring:message code="text.edit"/>>

    </form:form>
</div>
</body>

<script src="http://code.jquery.com/jquery-3.3.1.min.js" integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8="
        crossorigin="anonymous"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"
        integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl"
        crossorigin="anonymous"></script>

</html>