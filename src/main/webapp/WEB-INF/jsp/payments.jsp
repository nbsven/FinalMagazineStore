<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="formatter" uri="https://epam.com/jsp/tlds/formatter" %>

<html>
<head>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"
          integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.10.16/css/dataTables.bootstrap4.min.css">
    <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.10.16/css/jquery.dataTables.css">
</head>
<body>

<style>
    .dataTables_wrapper .dataTables_paginate .paginate_button:hover {
        background: none;
        border: 0;
        box-shadow: none;
    }
</style>


<div class="container fluid">
    <jsp:include page="header.jsp"/>
    <br/>
    <label>
        <select class="form-control inputstl" onchange="location = this.value;">
            <c:if test="${paymentView=='unpaid'}">
                <option value="/payments?payment_view=unpaid"><spring:message code="text.unpaid_payments"/></option>
                <option value="/payments?payment_view=paid"><spring:message code="text.paid_payments"/></option>
            </c:if>
            <c:if test="${paymentView=='paid'}">
                <option value="/payments?payment_view=paid"><spring:message code="text.paid_payments"/></option>
                <option value="/payments?payment_view=unpaid"><spring:message code="text.unpaid_payments"/></option>
            </c:if>
        </select>
    </label>

    <c:if test="${paymentView=='unpaid'}">
        <h5><spring:message code="text.unpaid_payments"/></h5>
    </c:if>
    <c:if test="${paymentView=='paid'}">
        <h5><spring:message code="text.paid_payments"/></h5>
    </c:if>
    <table id="payments" class="table table-bordered">
        <thead>
        <tr>
            <th scope="col" class="text-center">#</th>
            <th scope="col" class="text-center"><spring:message code="text.payment.username"/></th>
            <th scope="col" class="text-center"><spring:message code="text.payment.date"/></th>
            <th scope="col" class="text-center"><spring:message code="text.payment.amount"/></th>
            <c:if test="${paymentView=='unpaid'}">
                <th scope="col" class="text-center"><spring:message code="text.payment.paid"/></th>
            </c:if>
        </tr>
        </thead>
        <c:if test="${paymentView=='paid'}">
            <tfoot>
            <td scope="col" class="text-center" colspan="3"><spring:message code="text.total_amount"/></td>
            <td id="total_amount" scope="<col>" class="text-center">
                -1
            </td>
            </tfoot>
        </c:if>
    </table>
</div>

</body>
<script src="http://code.jquery.com/jquery-3.3.1.min.js" integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8="
        crossorigin="anonymous"></script>

<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"
        integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl"
        crossorigin="anonymous"></script>

<c:if test="${paymentView=='unpaid'}">
    <script src="js/unpaid_payments.js"></script>
</c:if>

<c:if test="${paymentView=='paid'}">
    <script src="js/paid_payments.js"></script>
</c:if>


<script type="text/javascript" charset="utf8" src="https://cdn.datatables.net/1.10.16/js/jquery.dataTables.js"></script>

<script type="text/javascript" charset="utf8" src="https://cdn.datatables.net/1.10.16/js/dataTables.bootstrap4.min.js">

</script>
</html>