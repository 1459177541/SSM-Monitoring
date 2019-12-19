// JavaScript Document
var anchors=[];
var opened_file=[];
var open_id=0;
var select = [];
var file = function () {
    $('#path_input').click(function () {
        if ($('#path_input').val()==='') {
            $('#path').empty();
            $.ajax({
                url: '/console/root_path',
                method: 'GET',
                success: function (data) {
                    data.data.forEach(function (element) {
                        $('#path').append('<option value="' + element + '">');
                    })
                }
            });
        }
    });
    var update_files = function (url, id) {
        $('#file_main_'+id).empty();
        var addOne = function (id, cid, element) {
            $('#file_main_' + id).append(
                '<div id="file_' + id + '_' + cid + '" class="file_file">' +
                '<div class="file_image">' +
                (element.suffix ? element.suffix : '') + '' +
                '</div>' + element.name +
                '</div>'
            );
            $('#file_' + id + '_' + cid).click(function () {
                if (select[id] && this.id === select[id].id) {
                    $('#file_open_dir_' + id).hide();
                    $('#file_download_' + id).hide();
                    $('#file_delete_' + id).hide();
                    $('#file_rename_' + id).hide();
                    $(select[id]).removeClass('select_file');
                    select[id] = undefined;
                    return;
                }
                $('#' + this.id).addClass('select_file');
                if (select[id]) {
                    $('#' + select[id].id).removeClass('select_file');
                }
                select[id] = this;
                if (opened_file[id].child[cid].file) {
                    $('#file_open_dir_' + id).hide();
                    $('#file_download_' + id).show();
                } else {
                    $('#file_open_dir_' + id).show();
                    $('#file_download_' + id).hide();
                }
                $('#file_delete_' + id).show();
                $('#file_rename_' + id).show();
            });
        };
        $.ajax({
            url: '/console/file_list',
            data: {url: url},
            method: 'POST',
            success: function (data) {
                var cid = 0;
                var open = {url: url, child: []};
                data.data.forEach(function (element) {
                    open.child[cid] = element;
                    addOne(id, cid, element);
                    cid++;
                });
                $('#file_main_' + id).append(
                    '<div id="file_uploading_' + id + '" class="file_file">' +
                        '<div class="file_image">loading</div>' +
                        '正在上传中' +
                    '</div>'
                );
                $('#file_uploading_'+id).hide();
                opened_file[id] = open;
            }
        });
    };
    var addPath = function (url) {
        var id = open_id;
        open_id++;
        fullpage_api.destroy('all');
        $('#file').append(
            '<div id="file_' + id + '" class="slide">' +
            '<div class="file_title">' + url + '</div>' +
            '<div class="file_op">' +
            '<div id="file_open_dir_' + id + '" class="button">在新标签页打开</div>' +
            '<div id="file_download_' + id + '" class="button">下载</div>' +
            '<div id="file_upload_' + id + '" class="button">上传到本目录</div>' +
            '<div id="file_delete_' + id + '" class="button">删除</div>' +
            '<div id="file_rename_' + id + '" class="button">重命名</div> ' +
            '</div>' +
            '<div id="file_main_' + id + '" class="file_main"></div>' +
            '<div id="file_upload_div_' + id + '" class="file_upload">' +
            '<input id="file_upload_file_' + id + '" type="file" multiple="multiple" class="file_input"/>' +
            '<div id="file_upload_button_ok_' + id + '" class="button">确认</div>' +
            '<div id="file_upload_button_close_' + id + '" class="button">取消</div>' +
            '</div>' +
            '<div id="file_rename_div_' + id + '" class="file_rename">' +
            '<div class="input"><font>新文件名</font><input id="file_rename_input_' + id + '" type="text"/></div>' +
            '<div id="file_rename_button_ok_' + id + '" class="button">确认</div>' +
            '<div id="file_rename_button_close_' + id + '" class="button">取消</div>' +
            '</div>' +
            '</div>'
        );
        $('#fullpage').fullpage({
            anchors: anchors,
            menu: '#menu'
        });
        $('#file_upload_div_' + id).hide();
        $('#file_rename_div_' + id).hide();
        $('#file_open_dir_' + id).hide();
        $('#file_download_' + id).hide();
        $('#file_delete_' + id).hide();
        $('#file_rename_' + id).hide();
        update_files(url, id);
        $('#file_open_dir_' + id).click(function () {
            addPath(opened_file[id].child[select[id].id.split('_')[2]].url);
        });
        $('#file_upload_' + id).click(function () {
            $('#file_upload_div_' + id).show();
        });
        $('#file_upload_button_ok_' + id).click(function () {
            $('#file_uploading_' + id).show();
            $('#file_upload_div_' + id).hide();
            var formData = new FormData();
            formData.append('url', opened_file[id].url);
            for (var i = 0; i < $('#file_upload_file_' + id)[0].files.length; i++) {
                formData.append('file', $('#file_upload_file_' + id)[0].files[i]);
            }
            $.ajax({
                type: 'POST',
                url: 'console/file_upload',
                data: formData,
                processData: false,
                contentType: false,
                cache: false,
                success: function (data) {
                    if (data.data === 'success') {
                        update_files(url, id);
                    } else {
                        alert(data.message);
                    }
                    $('#file_uploading_' + id).hide();
                }
            });
        });
        $('#file_upload_button_close_'+id).click(function () {
            $('#file_upload_div_' + id).hide();
        });
        $('#file_delete_' + id).click(function () {
            $.ajax({
                url: 'console/file_delete',
                type: 'POST',
                data: {url: opened_file[id].child[select[id].id.split('_')[2]].url},
                success: function (data) {
                    if (data.data) {
                        update_files(url, id);
                    }
                }
            })
        });
        $('#file_rename_' + id).click(function () {
            $('#file_rename_div_' + id).show();
        });
        $('#file_rename_button_ok_' + id).click(function () {
            $('#file_rename_div_' + id).hide();
            $.ajax({
                url: 'console/file_rename',
                type: 'POST',
                data: {
                    url: opened_file[id].child[select[id].id.split('_')[2]].url,
                    name: $('#file_rename_input_' + id).val()
                },
                success: function (data) {
                    if (data.data) {
                        update_files(url, id);
                    }
                    $('#file_rename_input_' + id).val('');
                }
            });
        });
        $('#file_rename_button_close_' + id).click(function () {
            $('#file_rename_div_' + id).hide();
        });
        return id;
    };
    $('#path_button').click(function () {
        addPath($('#path_input').val());
    });
};