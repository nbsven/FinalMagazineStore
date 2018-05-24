$(document).ready(function () {

  var langJson;
  var locale = getCookie("local");
  if (locale === "ru") {
    langJson = '/json/datatable_ru.json'
  } else {
    langJson = '/json/datatable_en.json'
  }

  var acceptButton = $('<button />', {
    "class": 'btn btn-success',
    text: "Accept"
  }).get(0).outerHTML;

  var table = $('#payments').DataTable({
    "processing": true,
    "serverSide": true,
    "paging": true,
    "ajax": "getUnpaidPaymentsData",
    "columns": [
      {"data": "id", sortable: false, searchable: false},
      {
        "data": "subscription.account.username",
        sortable: false,
        searchable: false,
        className: 'text-center'
      },
      {
        "data": "date",
        sortable: true,
        searchable: false,
        className: 'text-center'
      },
      {
        "data": "amount",
        sortable: false,
        searchable: false,
        className: 'text-center'
      },
      {sortable: false, searchable: false, className: 'text-center'}
    ],
    language: {
      url: langJson
    },
    "columnDefs": [{
      "targets": 4,
      "data": null,
      "defaultContent": acceptButton
    }, {
      "targets": 0,
      "visible": false
    }]
  });

  $('#payments').find('tbody').on('click', 'button', function () {
    var data = table.cell($(this).closest('tr'), 0).data();
    $.ajax({
      type: "POST",
      url: "pay",
      data: {
        paymentId: data
      }, success: function (data) {
        window.location = "payments";
      }
    });
  });
});
window.getCookie = function (name) {
  match = document.cookie.match(new RegExp(name + '=([^;]+)'));
  if (match) {
    return match[1];
  }
};