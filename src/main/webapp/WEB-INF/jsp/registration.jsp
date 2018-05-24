<!DOCTYPE html>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"
          integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.10.16/css/dataTables.bootstrap4.min.css">
    <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.10.16/css/jquery.dataTables.css">
</head>
<body>

<div class="container">
    <jsp:include page="header.jsp"/>
    <div class="row" style="margin-top : 20px">
        <c:if test="${message != null}">
            <div class="col-md-4 offset-md-4">
                <c:if test="${success != null}">
                    <div class="alert alert-success" role="alert">
                            ${message}
                    </div>
                </c:if>


                <c:if test="${success == null}">
                    <div class="alert alert-danger" role="alert">
                            ${message}
                    </div>
                </c:if>
            </div>
        </c:if>
    </div>


    <div class="row">
        <div class="col-md-4 offset-md-4">
            <h4><spring:message code="text.registration"/></h4>
        </div>
    </div>
    <div class="row">
        <div class="col-md-4 offset-md-4">
            <form action="<c:url value="/create_account"/>" method="post">
                <div class="form-group">
                    <label for="name"><spring:message code="text.username"/></label>
                    <input type="text" name="name" required class="form-control" maxlength="20" id="name"
                           placeholder="<spring:message code="text.username"/>">
                </div>
                <div class="form-group">
                    <label for="password"><spring:message code="text.password"/></label>
                    <input type="password" name="password" required class="form-control" maxlength="20" id="password"
                           placeholder="<spring:message code="text.password"/>">
                </div>
                <button type="submit" class="btn btn-primary"><spring:message code="text.sign_up"/></button>
            </form>
        </div>
    </div>
</div>

</body>

<script src="http://code.jquery.com/jquery-3.3.1.min.js" integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8="
        crossorigin="anonymous"></script>

<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"
        integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl"
        crossorigin="anonymous"></script>
</html>