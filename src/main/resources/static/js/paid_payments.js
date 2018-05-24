$(document).ready(function () {

  var langJson;
  var locale = getCookie("local");
  if (locale === "ru") {
    langJson = '/json/datatable_ru.json'
  } else {
    langJson = '/json/datatable_en.json'
  }

  var table = $('#payments').DataTable({
    "processing": true,
    "serverSide": true,
    "paging": true,
    "ajax": "getPaidPaymentsData",
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
    ],
    language: {
      url: langJson
    },
    "columnDefs": [{
      "targets": 0,
      "visible": false
    }]
  });

  function f() {
    var sum = 0;
    table.column(3, {page: 'current'}).data().each(function (value, index) {
      sum += value;
    });
    $('#total_amount').html(sum);
  }

  table.on('processing.dt', f);

});

window.getCookie = function (name) {
  match = document.cookie.match(new RegExp(name + '=([^;]+)'));
  if (match) {
    return match[1];
  }
};