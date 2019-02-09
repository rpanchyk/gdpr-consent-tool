$(function () {
    setActiveTab();

    // parser
    $('#parser_form').submit(function (event) {
        event.preventDefault();

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

                    $('#parser_result').html('<pre class="border border-light bg-light">' + jsonPrettify(data) + '</pre>');
                },
                error: function (error) {
                    var data = error.responseJSON;
                    console.log(data);

                    // $('#parser_error').html(jsonPrettify(data));
                    $('#parser_error').html('<textarea class="form-control p-2 mb-2 rounded-1 result-error" disabled>' + data.error + '</textarea>');
                    textAreaAdjust($('#parser_error textarea'));
                }
            });
        }

        return false;
    });

    // composer
    $('#composer_form').submit(function (event) {
        event.preventDefault();

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

                $('#composer_result').html(
                    '<button class="btn-clipboard2" title="Copy to clipboard" onclick="copyToClipboard(\'' + data['consent'] + '\'); $(this).tooltip(\'dispose\');$(this).attr(\'title\',\'Copied!\');$(this).tooltip(\'show\');return false;" data-toggle="tooltip-composer" data-placement="top">'
                    + '<img class="clippy" src="images/clippy.svg" width="14"/>'
                    + '</button>'
                    + '<input type="text" class="form-control text-success" value="' + data['consent'] + '" readonly/>');

                $('[data-toggle="tooltip-composer"]').tooltip();

                // fill parser field to be kind
                var consent = $('#consent');
                if ($.trim(consent.val()) === '') {
                    consent.val(data['consent']);
                }
            },
            error: function (error) {
                var data = error.responseJSON;
                console.log(data);

                // $('#composer_error').html(jsonPrettify(data.error));
                $('#composer_error').html('<textarea class="form-control p-2 mb-2 rounded-1 result-error" disabled>' + data.error + '</textarea>');
                textAreaAdjust($('#composer_error textarea'));
            }
        });

        return false;
    });
});

function serializeForm(form) {
    var result = {};
    $.map(form.serializeArray(), function (n) {
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
}

function jsonPrettify(json) {
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
}

function setActiveTab() {
    var parserTab = $('#nav-parser-tab');
    var composerTab = $('#nav-composer-tab');

    parserTab.click(function () {
        // console.log('nav-parser-tab');
        Cookies.set('navTabState', 'parser', {expires: 365});
    });
    composerTab.click(function () {
        // console.log('nav-composer-tab');
        Cookies.set('navTabState', 'composer', {expires: 365});
    });

    var navTabState = Cookies.get('navTabState');
    // console.log('navTabState: ' + navTabState);

    if (navTabState === 'composer') {
        composerTab.trigger("click")
    } else {
        parserTab.trigger("click")
    }
}

function copyToClipboard(str) {
    var $temp = $("<input>");
    $("body").append($temp);
    $temp.val(str).select();
    document.execCommand("copy");
    $temp.remove();
}

function textAreaAdjust(o) {
    o.css("height", "1px");
    setTimeout(function () {
        o.css("height", (o.prop('scrollHeight') + 2) + "px");
    }, 1);
}
