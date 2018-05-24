<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"
          integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.10.16/css/dataTables.bootstrap4.min.css">
    <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.10.16/css/jquery.dataTables.css">
</head>

<style>
    .dataTables_wrapper .dataTables_paginate .paginate_button:hover {
        background: none;
        border: 0;
        box-shadow: none;
    }
</style>

<body>
<c:url value="/add_magazine" var="add_action"/>
<c:url value="/delete" var="delete_magazine"/>
<div class="container fluid">
    <jsp:include page="header.jsp"/>
    <br/>
    <div class="container" style="margin-outside: 20px; margin-top: 20px">
        <c:if test="${role=='ROLE_ADMIN'}">
            <div>
                <form:form class="form-inline" action="${add_action}" method="post" modelAttribute="newMagazine">
                    <form:input type="text" class="form-control mb-2 mr-sm-2" name="magazineName" path="name"/>
                    <form:select class="form-control mb-2 mr-sm-2" path="publisher">
                        <form:options items="${publishers}" itemLabel="name"/>
                    </form:select>
                    <form:input type="number" class="form-control mb-2 mr-sm-2" name="magazineDatePublication" max="31"
                                min="1"
                                path="publicationDate"/>
                    <form:input type="text" class="form-control mb-2 mr-sm-2" name="magazinePrice" path="price"/>
                    <input class="btn btn-success mb-2" type="submit"
                           value="<spring:message code="text.addMagazineButton"/>">
                </form:form>
            </div>
        </c:if>
        <br/>
        <h2><spring:message code="text.catalog"/></h2>

        <table id="catalog" class="table table-striped table-bordered">
            <thead>
            <tr>
                <th scope="col">#</th>
                <th scope="col"><spring:message code="text.magazine.name"/></th>
                <th scope="col"><spring:message code="text.magazine.publication_date"/></th>
                <th scope="col"><spring:message code="text.magazine.publisher"/></th>
                <th scope="col"><spring:message code="text.magazine.price"/></th>
                <c:if test="${role=='ROLE_USER'}">
                    <th scope="col"><spring:message code="text.duration"/></th>
                    <th scope="col"><spring:message code="text.total_amount"/></th>
                </c:if>
                <th scope="col"><spring:message code="text.action"/></th>
            </tr>
            </thead>
        </table>

    </div>
</div>

</body>
<script src="http://code.jquery.com/jquery-3.3.1.min.js" integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8="
        crossorigin="anonymous"></script>


<c:if test="${role=='ROLE_USER'}">
    <script src="js/catalog_user.js"></script>
</c:if>

<c:if test="${role=='ROLE_ADMIN'}">
    <script src="js/catalog_admin.js"></script>
</c:if>

<script type="text/javascript" charset="utf8" src="https://cdn.datatables.net/1.10.16/js/jquery.dataTables.js"></script>

<script type="text/javascript" charset="utf8" src="https://cdn.datatables.net/1.10.16/js/dataTables.bootstrap4.min.js">

</script>

<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"
        integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl"
        crossorigin="anonymous"></script>

</html>