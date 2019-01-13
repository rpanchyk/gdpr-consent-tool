$(document).ready(function () {
    // parser
    $('#parser-form').submit(function (event) {
        var consent = $('#consent').val();
        if ($.trim(consent) !== '') {
            $.ajax({
                type: 'GET',
                url: '/api?consent=' + consent,
                dataType: "json",
                beforeSend: function (xhr) {
                    $('#parser-result').html('');
                    $('#parser-error').html('');
                },
                success: function (data) {
                    console.log(data);

                    $('#parser-result').html('<pre>' + JSON.stringify(data, null, 2) + '</pre>');
                },
                error: function (data) {
                    var json = data.responseJSON;
                    console.log(json);

                    $('#parser-error').html(JSON.stringify(json, null, 2));
                }
            });
        }
        event.preventDefault();
        return null;
    });

    // composer
    //...
});
