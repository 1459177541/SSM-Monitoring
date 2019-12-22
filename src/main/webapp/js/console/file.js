// JavaScript Document
var anchors=[];
var file = function () {
    $('#path_input').click(function () {
        if ($('#path_input').val() === '') {
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
    const opened_file = [];
    let open_id = 0;
    const select = [];
    const open_url_log = [];
    const remove_select = function (id) {
        $('#file_open_dir_new_' + id).hide();
        $('#file_open_dir_this_' + id).hide();
        $('#file_download_' + id).hide();
        $('#file_delete_' + id).hide();
        $('#file_rename_' + id).hide();
        $(select[id]).removeClass('select_file');
        select[id] = undefined;
    };
    const update_files = function (url, id) {
        $('#file_main_' + id).empty();
        remove_select(id);
        const addOne = function (id, cid, element) {
            $('#file_main_' + id).append(
                '<div id="file_' + id + '_' + cid + '" class="file_file">' +
                '<div class="file_image">' +
                (element.suffix ? element.suffix : '') + '' +
                '</div>' + element.name +
                '</div>'
            );
            $('#file_' + id + '_' + cid).click(function () {
                this.file_info = element;
                if (select[id] && this.id === select[id].id) {
                    remove_select(id);
                    return;
                }
                $('#' + this.id).addClass('select_file');
                if (select[id]) {
                    $('#' + select[id].id).removeClass('select_file');
                }
                select[id] = this;
                if (opened_file[id].child[cid].file) {
                    $('#file_open_dir_new_' + id).hide();
                    $('#file_open_dir_this_' + id).hide();
                    $('#file_download_' + id).show();
                } else {
                    $('#file_open_dir_new_' + id).show();
                    $('#file_open_dir_this_' + id).show();
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
                let cid = 0;
                const open = {url: url, child: []};
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
                $('#file_uploading_' + id).hide();
                opened_file[id] = open;
            }
        });
    };
    const add_dialog = function (id, name, input, f) {
        $('#file_' + id).append(
            '<div id="file_' + name + '_div_' + id + '" class="file_' + name + ' file_dialog">' +
            input +
            '<div id="file_' + name + '_button_ok_' + id + '" class="button">确认</div>' +
            '<div id="file_' + name + '_button_close_' + id + '" class="button">取消</div>' +
            '</div>'
        );
        $('#file_' + name + '_div_' + id).hide();
        $('#file_' + name + '_' + id).click(function () {
            $('#file_' + name + '_div_' + id).show();
        });
        $('#file_' + name + '_button_ok_' + id).click(f);
        $('#file_' + name + '_button_close_' + id).click(function () {
            $('#file_' + name + '_div_' + id).hide();
        });

    };
    const watch_list = {};
    const remove_watch = function (url) {
        watch_list[url]--;
        $.ajax({
            url: 'console/file_remove_watch',
            type: 'POST',
            cache: false,
            data: {url: url},
            success: function (data) {
                console.log(data.data);
            }
        })
    };
    const addPath = function (url) {
        $('#loading').show();
        const id = open_id;
        open_id++;
        open_url_log[id] = [];
        fullpage_api.destroy('all');
        $('#file').append(
            '<div id="file_' + id + '" class="slide">' +
            '<div id="file_title_' + id + '" class="file_title">' + url + '</div>' +
            '<div class="file_op">' +
            '<div id="file_back_' + id + '" class="button">返回</div>' +
            '<div id="file_mkdir_' + id + '" class="button">新建文件夹</div>' +
            '<div id="file_upload_' + id + '" class="button">上传</div>' +
            '<div id="file_download_' + id + '" class="button">下载</div>' +
            '<div id="file_open_dir_new_' + id + '" class="button">在新页面打开</div>' +
            '<div id="file_open_dir_this_' + id + '" class="button">在本页面打开</div>' +
            '<div id="file_delete_' + id + '" class="button">删除</div>' +
            '<div id="file_rename_' + id + '" class="button">重命名</div>' +
            '<div id="file_close_' + id + '" class="button">关闭</div>' +
            '</div>' +
            '<div id="file_main_' + id + '" class="file_main"></div>' +
            '</div>'
        );

        $('#file_back_' + id).hide();
        $('#file_open_dir_new_' + id).hide();
        $('#file_open_dir_this_' + id).hide();
        $('#file_download_' + id).hide();
        $('#file_delete_' + id).hide();
        $('#file_rename_' + id).hide();

        update_files(url, id);

        add_dialog(id,
            'upload',
            '<input id="file_upload_file_' + id + '" type="file" multiple="multiple" class="file_input"/>',
            function () {
                $('#file_uploading_' + id).show();
                $('#file_upload_div_' + id).hide();
                const formData = new FormData();
                formData.append('url', opened_file[id].url);
                for (let i = 0; i < $('#file_upload_file_' + id)[0].files.length; i++) {
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
            }
        );
        add_dialog(id,
            'rename',
            '<div class="input"><font>新文件名</font><input id="file_rename_input_' + id + '" type="text"/></div>',
            function () {
                $('#file_rename_div_' + id).hide();
                $.ajax({
                    url: 'console/file_rename',
                    type: 'POST',
                    cache: false,
                    data: {
                        url: select[id].file_info.url,
                        name: $('#file_rename_input_' + id).val()
                    },
                    success: function (data) {
                        if (data.data) {
                            update_files(url, id);
                        }
                        $('#file_rename_input_' + id).val('');
                    }
                });
            }
        );
        add_dialog(id,
            'mkdir',
            '<div class="input"><font>文件夹名</font><input id="file_mkdir_input_' + id + '" type="text"/></div>',
            function () {
                $('#file_mkdir_div_' + id).hide();
                $.ajax({
                    url: 'console/file_mkdir',
                    type: 'POST',
                    cache: false,
                    data: {
                        url: url,
                        name: $('#file_mkdir_input_' + id).val()
                    },
                    success: function (data) {
                        if (data.data) {
                            update_files(url, id);
                        }
                        $('#file_mkdir_input_' + id).val('');
                    }
                });
            }
        );
        $('#file_download_' + id).click(function () {
            $('<form></form>')
                .attr('action', 'console/file_download/' + encodeURI(select[id].file_info.name))
                .attr('method', 'post')
                .attr('target', '_blank')
                .append(
                    $('<input/>')
                        .attr('name', 'url')
                        .attr('value', encodeURI(select[id].file_info.url))
                        .attr('type', 'hidden')
                )
                .appendTo('body').submit().remove();
        });
        $('#file_open_dir_new_' + id).click(function () {
            addPath(select[id].file_info.url);
        });
        $('#file_open_dir_this_' + id).click(function () {
            open_url_log[id].push(url);
            $('#file_back_' + id).show();
            $('#file_title_' + id).text(select[id].file_info.url);
            url = select[id].file_info.url;
            update_files(select[id].file_info.url, id);
        });
        $('#file_back_' + id).click(function () {
            const n_url = open_url_log[id].pop();
            if (open_url_log[id].length === 0) {
                $('#file_back_' + id).hide();
            }
            $('#file_title_' + id).text(n_url);
            update_files(n_url, id);
            url = n_url;
        });
        $('#file_delete_' + id).click(function () {
            $.ajax({
                url: 'console/file_delete',
                type: 'POST',
                cache: false,
                data: {url: select[id].file_info.url},
                success: function (data) {
                    if (data.data) {
                        update_files(url, id);
                    }
                }
            })
        });
        $('#file_close_' + id).click(function () {
            $('#loading').show();
            fullpage_api.destroy('all');
            remove_watch(url);
            $('#file_' + id).remove();
            $('#fullpage').fullpage({
                anchors: anchors,
                menu: '#menu'
            });
            setTimeout(function () {
                $('#loading').hide();
            }, 1000);
        });
        $('#fullpage').fullpage({
            anchors: anchors,
            menu: '#menu'
        });
        const watch = function (id, url) {
            $.ajax({
                url: 'console/file_watch',
                type: 'POST',
                cache: false,
                data: {url: url},
                success: function (data) {
                    if (data.data) {
                        update_files(url, id);
                    }
                    setTimeout(function () {
                        if (watch_list[url] > 0) {
                            watch(id, url);
                        }
                    }, 1000);
                }
            });
        };
        $.ajax({
            url: 'console/file_add_watch',
            type: 'POST',
            cache: false,
            data: {url: url},
            success: function (data) {
                if (data.data) {
                    update_files(url, id);
                }
                watch_list[url] = 1;
                setTimeout(function () {
                    watch(id, url);
                }, 1000);
            }
        });
        setTimeout(function () {
            $('#loading').hide();
        }, 1000);
        return id;
    };
    $('#path_button').click(function () {
        addPath($('#path_input').val());
    });
    window.onbeforeunload = function () {
        opened_file.forEach(function (value) {
            remove_watch(value.url);
        });
    }
};