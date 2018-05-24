<!DOCTYPE html>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="formatter" uri="https://epam.com/jsp/tlds/formatter" %>

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

<div class="container fluid">
    <jsp:include page="header.jsp"/>
    <div class="container" style="margin-outside: 20px; margin-top: 20px">
        <h5><spring:message code="text.account.greeting"/>, ${account_name}!</h5>
        <h5><spring:message code="text.account.subscriptions"/></h5>
        <table id="subscriptions" class="table table-bordered">
            <thead>
            <tr>
                <th scope="col">#</th>
                <th scope="col"><spring:message code="text.magazine.name"/></th>
                <th scope="col"><spring:message code="text.magazine.start_date"/></th>
                <th scope="col"><spring:message code="text.magazine.end_date"/></th>
                <th scope="col"><spring:message code="text.magazine.publisher"/></th>
                <th scope="col"><spring:message code="text.magazine.duration"/></th>
                <th scope="col"><spring:message code="text.action"/></th>
            </tr>
            </thead>
        </table>
    </div>
</div>
</body>

<script src="http://code.jquery.com/jquery-3.3.1.min.js"></script>

<script>
  window.getCookie = function (name) {
    match = document.cookie.match(new RegExp(name + '=([^;]+)'));
    if (match) return match[1];
  };
  $(document).ready(function () {

    var buttonUnSubscribe = $('<button />', {
      "class": 'btn btn-danger',
      text: "Unsubscribe"
    });

    var buttonProlong = $('<button />', {
      "class": 'btn btn-success pr',
      text: "Prolong"
    });

    var div = $('<div />', {
      "class": 'btn-group'
    }).append(buttonUnSubscribe).append(buttonProlong).get(0).outerHTML;

    var durInput = $('<input />', {
      class: 'form-control'
    }).get(0).outerHTML;

    var langJson;
    var locale = getCookie("local");
    if (locale === "ru") {
      langJson = '/json/datatable_ru.json'
    } else {
      langJson = '/json/datatable_en.json'
    }

    var table = $('#subscriptions').DataTable({
      "processing": true,
      "serverSide": true,
      "ajax": "/account/subscriptions",
      "order": [[3, "desc"]],
      "columns": [
        {"data": "id", sortable: false, searchable: false},
        {"data": "magazine.name", sortable: false, searchable: true, className: 'text-center'},
        {"data": "startDate", sortable: false, searchable: false, className: 'text-center'},
        {"data": "endDate", sortable: true, searchable: false, className: 'text-center'},
        {"data": "magazine.publisher.name", sortable: true, searchable: false, className: 'text-center'},
        {sortable: false, searchable: false, className: 'text-center'},
        {sortable: false, searchable: false, className: 'text-center'}
      ],
      language: {
        url: langJson
      },
      "columnDefs": [{
        "targets": 0,
        "visible": false
      }, {
        "targets": 6,
        "data": null,
        "defaultContent": div
      }, {
        "targets": 5,
        "data": null,
        "defaultContent": durInput
      }]

    });

    $('#subscriptions tbody').on('click', 'button', function () {
      var data = table.cell($(this).closest('tr'), 0).data();
      var duration = $(this).closest('tr').find('td').eq(4).find('input');
      if ($(this).hasClass('pr')) {
        $.ajax({
          type: "POST",
          url: "prolongate",
          data: {
            id: data,
            duration: duration.val()
          },
          success: function (data) {
            if (data == 'ok') {
              table.draw();
            }
          }
        });
        return;
      }
      $.ajax({
        type: "POST",
        url: "unsubscribe",
        data: {
          subscriptionId: data
        }, success: function (data) {
          table.draw();
        }
      });
    });

  });
</script>
<script type="text/javascript" charset="utf8" src="https://cdn.datatables.net/1.10.16/js/jquery.dataTables.js"></script>
<script type="text/javascript" charset="utf8"
        src="https://cdn.datatables.net/1.10.16/js/dataTables.bootstrap4.min.js"></script>
<%--<script type="text/javascript" src="js/prolongation-with-ajax.js"></script>--%>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
</html>