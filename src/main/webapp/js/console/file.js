// JavaScript Document
var anchors=[];
var file = function () {
    $('#path_input').click(function () {
        if ($('#path_input').val()==='') {
            $('#path').empty();
            $.get(
                '/console/root_path',
                function (data) {
                    data.data.forEach(function (element) {
                        $('#path').append('<option value="' + element + '">');
                    })
                }
            );
        }
    });
    $('#path_button').click(function () {
        fullpage_api.destroy('all');
        $('#file').append('<div class="slide">'+$('#path_input').val()+'</div>');
        $('#fullpage').fullpage({
            anchors: anchors,
            menu: '#menu'
        });
    });
};