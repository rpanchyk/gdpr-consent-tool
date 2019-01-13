$(document).ready(function () {
    // parser
    $('#parser_form').submit(function (event) {
        var consent = $('#consent').val();
        if ($.trim(consent) !== '') {
            $.ajax({
                type: 'GET',
                url: '/api?consent=' + consent,
                dataType: "json",
                beforeSend: function (xhr) {
                    $('#parser_result').html('');
                    $('#parser_error').html('');
                },
                success: function (data) {
                    console.log(data);

                    $('#parser_result').html('<pre>' + JSON.stringify(data, null, 2) + '</pre>');
                },
                error: function (data) {
                    var error = data.responseJSON;
                    console.log(error);

                    $('#parser_error').html(JSON.stringify(error, null, 2));
                }
            });
        }
        event.preventDefault();
        return false;
    });

    // composer
    $('#composer_form').submit(function (event) {
        var formData = JSON.stringify(serializeForm($(this)));
        $.ajax({
            type: 'POST',
            url: '/api',
            dataType: "json",
            data: formData,
            beforeSend: function (xhr) {
                $('#composer_result').html('');
                $('#composer_error').html('');
            },
            success: function (data) {
                console.log(data);

                $('#composer_result').html('<pre>' + JSON.stringify(data, null, 2) + '</pre>');
            },
            error: function (data) {
                var error = data.responseJSON;
                console.log(error);

                $('#composer_error').html(JSON.stringify(error, null, 2));
            }
        });
        event.preventDefault();
        return false;
    });

    var serializeForm = function ($form) {
        var result = {};
        $.map($form.serializeArray(), function (n) {
            if (n['name'] === 'allowed_purpose_ids' || n['name'] === 'allowed_vendor_ids') {
                var arr = [];
                n['value'].split(',').forEach(function (item) {
                    arr.push($.trim(item));
                });
                n['value'] = arr;
            }
            result[n['name']] = n['value'];
        });
        return result;
    };
});
