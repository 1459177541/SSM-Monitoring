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
    var addPath = function (url) {
        var id = 'file_main_'+url.replace(/\//g, '_');
        $('#file').append(  '<div class="slide">' +
                                '<div class="file_menu">'+url+'</div>' +
                                '<div class="file_op">' +
                                '</div> '+
                                '<div id="'+id+'" class="file_main"></div>' +
                            '</div>');
        $.post(
            '/console/file_list',
            {url: url},
            function (data) {
                var jq_id = '#'+id;
                data.data.forEach(function (element) {
                    $(jq_id).append('<div id=\"file_'+element.url.replace(/\//g, '_')+'\" class="file_file">'+
                                        '<div class=\"file_image\">' +
                                        (element.suffix?element.suffix:'')+'' +
                                        '</div>' +
                                        element.name +
                                    '</div>');
                })
            }
        )
    };
    $('#path_button').click(function () {
        fullpage_api.destroy('all');
        addPath($('#path_input').val());
        $('#fullpage').fullpage({
            anchors: anchors,
            menu: '#menu'
        });
    });
};