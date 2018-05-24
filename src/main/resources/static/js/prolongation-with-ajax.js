jQuery(document).ready(function () {
  jQuery('.ajax').click(function () {
    doAjax(this)
  });

  function doAjax(btn) {
    var prolongation = jQuery(btn).parent().find('.prolongation').val();
    var id = jQuery(btn).parent().find('.id').val();
    console.log(prolongation);
    console.log(id);

    jQuery.ajax({
      url: 'prolongate',
      type: 'POST',
      // dataType: 'json',
      // contentType: 'application/json',
      // mimeType: 'application/json',
      data: {
        duration: prolongation,
        id: id
      }
    }).done(function (newDate) {
      jQuery(btn).parent().parent().find('.end_date').text(newDate);
    }).fail(function (e) {
      console.log(e);
    });
  }
});