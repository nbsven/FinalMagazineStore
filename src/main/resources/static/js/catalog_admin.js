window.getCookie = function (name) {
  match = document.cookie.match(new RegExp(name + '=([^;]+)'));
  if (match) {
    return match[1];
  }
};

var buttonDelete = $('<button />', {
  "class": 'btn btn-danger',
  text: "Delete"
});

var buttonEdit = $('<button />', {
  "class": 'btn btn-success btn-edit',
  text: "Edit"
});

var div = $('<div />', {}
).append(buttonEdit).append(buttonDelete).get(0).outerHTML;

$(document).ready(function () {

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
      {sortable: false, searchable: false, className: 'text-center'}
    ],
    language: {
      url: langJson
    },
    "columnDefs": [{
      "targets": 5,
      "data": null,
      "defaultContent": div
    }, {
      "targets": 0,
      "visible": false
    }]
  });

  $('#catalog tbody').on('click', 'button', function () {
    if ($(this).hasClass('btn-edit')) {
      var id = table.cell($(this).closest('tr'), 0).data();
      window.location = "/modification?id=" + id;
      return;
    }
    var data = table.cell($(this).closest('tr'), 0).data();
    $.ajax({
      type: "POST",
      url: "delete",
      data: {
        magazineId: data
      }, success: function (data) {
        window.location = "catalog";
      }
    });
  });

});
