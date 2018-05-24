window.getCookie = function (name) {
  match = document.cookie.match(new RegExp(name + '=([^;]+)'));
  if (match) {
    return match[1];
  }
};
$(document).ready(function () {

  var buttonSubscribe = $('<button />', {
    "class": 'btn btn-success',
    text: "Subscribe"
  }).get(0).outerHTML;

  var durInput = $('<input />', {
    class: 'form-control'
  }).get(0).outerHTML;

  var total = $('<div />', {
    id: "total",
    text: 0
  }).get(0).outerHTML;

  var langJson;
  var locale = getCookie("local");
  if (locale === "ru") {
    langJson = '/json/datatable_ru.json'
  } else {
    langJson = '/json/datatable_en.json'
  }

  var table = $('#catalog').DataTable({
    "processing": true,
    "serverSide": true,
    "ajax": "getCatalogData",
    "columns": [
      {"data": "id", sortable: false, searchable: false},
      {
        "data": "name",
        sortable: false,
        searchable: true,
        className: 'text-center'
      },
      {
        "data": "publicationDate",
        sortable: false,
        searchable: false,
        className: 'text-center'
      },
      {
        "data": "publisher.name",
        sortable: false,
        searchable: true,
        className: 'text-center'
      },
      {
        "data": "price",
        sortable: true,
        searchable: false,
        className: 'text-center'
      },
      {sortable: false, searchable: false, className: 'text-center'},
      {sortable: false, searchable: false, className: 'text-center'},
      {sortable: false, searchable: false, className: 'text-center'}
    ],
    language: {
      url: langJson
    },
    "columnDefs": [{
      "targets": 5,
      "data": null,
      "defaultContent": durInput
    }, {
      "targets": 0,
      "visible": false
    }, {
      "targets": 7,
      "data": null,
      "defaultContent": buttonSubscribe
    }, {
      "targets": 6,
      "defaultContent": total
    }]
  });
  $('#catalog tbody').on('click', 'button', function () {
    var magazineId = table.cell($(this).closest('tr'), 0).data();
    var totalPrice = table.cell($(this).closest('tr'), 6).data();
    var input = $(this).closest('tr').find('td').eq(4).find('input');
    if (isNaN(totalPrice) || totalPrice < 1) {
      input.addClass('is-invalid');
      return;
    }
    $.ajax({
      type: "POST",
      url: "subscribe",
      data: {
        magazineId: magazineId,
        duration: input.val()
      }, success: function (data) {
        window.location = "catalog";
      }
    });
  }).on('change', 'input', function () {
    var price = table.cell($(this).closest('tr'), 4).data();

    if (isNaN($(this).val())) {
      $(this).addClass("is-invalid");
      return;
    } else {
      var duration = parseInt($(this).val(), 10);
      if (duration >= 1) {
        $(this).addClass("is-valid");
      } else {
        $(this).addClass("is-invalid");
        return;
      }
    }
    table.cell($(this).closest('td').next('td')).data(duration * price);
  });

});
