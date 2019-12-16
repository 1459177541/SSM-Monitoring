// JavaScript Document
var anchors=[];
var opened_file=[];
var open_id=0;
var select = [];
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
        var id = open_id;
        open_id++;
        fullpage_api.destroy('all');
        $('#file').append(  '<div class="slide">' +
                                '<div class="file_menu">'+url+'</div>' +
                                '<div class="file_op">' +
                                    '<div id="file_open_dir_'+id+'" class="button">在新标签页打开</div>' +
                                    '<div id="file_download_'+id+'" class="button">下载</div>' +
                                '</div> '+
                                '<div id="file_'+id+'" class="file_main"></div>' +
                            '</div>');
        $('#fullpage').fullpage({
            anchors: anchors,
            menu: '#menu'
        });
        $.post(
            '/console/file_list',
            {url: url},
            function (data) {
                var cid = 0;
                var open = {url:url, child:[]};
                data.data.forEach(function (element) {
                    open.child[cid] = element;
                    $('#file_'+id).append('<div id="file_'+id+'_'+cid+'" class="file_file">'+
                                        '<div class="file_image">' +
                                        (element.suffix?element.suffix:'')+'' +
                                        '</div>' +
                                        element.name +
                                    '</div>');
                    $('#file_'+id+'_'+cid).click(function () {
                        var ids = this.id.split('_');
                        if (select[ids[1]] && this.id === select[ids[1]].id) {
                            $('#file_open_dir_'+ids[1]).hide();
                            $('#file_download_'+ids[1]).hide();
                            $(select[ids[1]]).removeClass('select_file');
                            select[ids[1]] = undefined;
                            return;
                        }
                        $('#'+this.id).addClass('select_file');
                        if(select[ids[1]]) {
                            $('#' + select[ids[1]].id).removeClass('select_file');
                        }
                        select[ids[1]] = this;
                        if (opened_file[id].child[ids[2]].file) {
                            $('#file_open_dir_'+ids[1]).hide();
                            $('#file_download_'+ids[1]).show();
                        } else {
                            $('#file_open_dir_'+ids[1]).show();
                            $('#file_download_'+ids[1]).hide();
                        }
                    });
                    cid++;
                });
                opened_file[id] = open;
            }
        );
        $('#file_open_dir_'+id).click(function () {
            var ids = select[this.id.split('_')[3]].id.split('_');
            addPath(opened_file[ids[1]].child[ids[2]].url);
        });
    };
    $('#path_button').click(function () {
        addPath($('#path_input').val());
    });
};