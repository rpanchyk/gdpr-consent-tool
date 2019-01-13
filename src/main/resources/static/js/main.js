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

                    $('#parser_result').html('<pre>' + jsonPrettify(data) + '</pre>');
                },
                error: function (error) {
                    var data = error.responseJSON;
                    console.log(data);

                    $('#parser_error').html(jsonPrettify(data));
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

                $('#composer_result').html('<pre>' + jsonPrettify(data) + '</pre>');
            },
            error: function (error) {
                var data = error.responseJSON;
                console.log(data);

                $('#composer_error').html(jsonPrettify(data));
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

    var jsonPrettify = function (json) {
        if (typeof json !== 'string') {
            json = JSON.stringify(json, undefined, 2);
        }
        json = json.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
        return json.replace(/("(\\u[a-zA-Z0-9]{4}|\\[^u]|[^\\"])*"(\s*:)?|\b(true|false|null)\b|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?)/g, function (match) {
            var cls = 'number';
            if (/^"/.test(match)) {
                if (/:$/.test(match)) {
                    cls = 'key';
                } else {
                    cls = 'string';
                }
            } else if (/true|false/.test(match)) {
                cls = 'boolean';
            } else if (/null/.test(match)) {
                cls = 'null';
            }
            return '<span class="' + cls + '">' + match + '</span>';
        });
    };
});
