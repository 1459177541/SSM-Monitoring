var user = function () {
    $('#user_main').append(
        '<div id="user_title">用户管理</div>' +
        '<div id="user_list_main">' +
        '<div id="user_list_left">' +
        '<div id="user_info_title">' +
        '<div class="user_info_id">ID</div>' +
        '<div class="user_info_name">用户名</div>' +
        '<div class="user_info_power">处理器</div>' +
        '<div class="user_info_power">内存</div>' +
        '<div class="user_info_power">网络</div>' +
        '<div class="user_info_power">磁盘</div>' +
        '<div class="user_info_power">文件</div>' +
        '<div class="user_info_power">桌面</div>' +
        '<div class="user_info_power">用户管理</div>' +
        '</div>' +
        '<div id="user_list"></div>' +
        '</div>' +
        '<div id="user_op">' +
        '<div id="user_flush" class="button">刷新</div>' +
        '<div id="user_add_user" class="button">新建用户</div>' +
        '<div id="user_reset_password" class="button unable">重置密码</div> ' +
        '<div id="user_modify_power" class="button unable">修改权限</div>' +
        '</div>' +
        '</div>' +
        '<div id="user_add_user_div">' +
        '<div class="input"><font>用户名</font><input id="user_add_user_name" type="type"/></div>' +
        '<div class="input"><font>密码</font><input id="user_add_user_pwd1" type="password"/></div>' +
        '<div class="input"><font>确认密码</font><input id="user_add_user_pwd2" type="password"/></div>' +
        '<div id="user_add_user_ok" class="button">确认</div>' +
        '<div id="user_add_user_close" class="button">取消</div>' +
        '</div>' +
        '<div id="user_reset_password_div">' +
        '<div class="input"><font>密码</font><input id="user_reset_password_pwd1" type="password"/></div>' +
        '<div class="input"><font>确认密码</font><input id="user_reset_password_pwd2" type="password"/></div>' +
        '<div id="user_reset_password_ok" class="button">确认</div>' +
        '<div id="user_reset_password_close" class="button">取消</div>' +
        '</div>' +
        '<div id="user_div">' +
        'ID:<div id="user_div_id"></div>' +
        '用户名:<div id="user_div_name"></div><hr/>' +
        '权限:' +
        '<div id="user_div_cpu" class="button">处理器</div>' +
        '<div id="user_div_mem" class="button">内存</div>' +
        '<div id="user_div_net" class="button">网络</div>' +
        '<div id="user_div_disk" class="button">磁盘</div>' +
        '<div id="user_div_file" class="button">文件</div>' +
        '<div id="user_div_desktop" class="button">桌面</div>' +
        '<div id="user_div_user" class="button">用户管理</div><hr/>' +
        '<div id="user_ok" class="button">确认</div>' +
        '<div id="user_close" class="button">取消</div>' +
        '</div>'
    );
    $('#user_div').hide();
    $('#user_reset_password_div').hide();
    $('#user_add_user_div').hide();
    let select;
    const add_user = function (id, element) {
        $('#user_list').append(
            '<div id="user_info_' + id + '" class="user_info">' +
            '<div class="user_info_id">' + element.id + '</div>' +
            '<div class="user_info_name">' + element.name + '</div>' +
            '<div class="user_info_power">' + (element.cpu ? 'Y' : 'N') + '</div>' +
            '<div class="user_info_power">' + (element.mem ? 'Y' : 'N') + '</div>' +
            '<div class="user_info_power">' + (element.net ? 'Y' : 'N') + '</div>' +
            '<div class="user_info_power">' + (element.disk ? 'Y' : 'N') + '</div>' +
            '<div class="user_info_power">' + (element.file ? 'Y' : 'N') + '</div>' +
            '<div class="user_info_power">' + (element.desktop ? 'Y' : 'N') + '</div>' +
            '<div class="user_info_power">' + (element.user ? 'Y' : 'N') + '</div>' +
            '</div>'
        );
        let doc = $('#user_info_' + id);
        doc.user_info = element;
        doc.click(function () {
            if (select && select === doc) {
                $('#user_modify_power').addClass('unable');
                $('#user_reset_password').addClass('unable');
                $('#user_info_' + id).removeClass('select');
                select = undefined;
                return;
            }
            doc.addClass('select');
            if (select) {
                select.removeClass('select');
            }
            select = doc;
            $('#user_modify_power').removeClass('unable');
            $('#user_reset_password').removeClass('unable');
        });
    };
    const update_user_list = function () {
        $('#user_list').empty();
        $.ajax({
            url: 'console/user_list',
            method: 'GET',
            cache: false,
            success: function (data) {
                let id = 1;
                data.data.forEach(function (element) {
                    add_user(id, element);
                    id++;
                });
            }
        });
    };
    update_user_list();
    $('#user_add_user').click(function () {
        $('#user_add_user_div').show();
    });
    $('#user_add_user_ok').click(function () {
        if ($('#user_add_user_pwd1').val() !== ''
            && $('#user_add_user_pwd1').val() === $('#user_add_user_pwd2').val()) {
            $.ajax({
                url:'/sign_up',
                type:'POST',
                cache: false,
                data:{
                    name:$('#user_add_user_name').val(),
                    password:$('#user_add_user_pwd1').val()
                },
                success:function (data) {
                    if (data.success) {
                        update_user_list();
                    }
                }
            })
        }
    });
    $('#user_add_user_close').click(function () {
        $('#user_add_user_div').hide();
    });
    $('#user_reset_password').click(function () {
        if (!select) {
            return;
        }
        $('#user_reset_password_div').show();
    });
    $('#user_reset_password_ok').click(function () {
        if ($('#user_reset_password_pwd1').val() !== ''
            && $('#user_reset_password_pwd1').val() === $('#user_reset_password_pwd2').val()) {
            $.ajax({
                url: 'console/user_reset_password',
                type: 'POST',
                cache: false,
                data: {
                    id: select.user_info.id,
                    password: $('#user_reset_password_pwd1').val()
                },
                success: function (data) {
                    if (data.data) {
                        update_user_list();
                    }
                    $('#user_reset_password_div').hide();
                }
            });
            $('#user_reset_password_pwd1').val('');
            $('#user_reset_password_pwd2').val('');
        }
    });
    $('#user_reset_password_close').click(function () {
        $('#user_reset_password_div').hide();
    });
    const modify_button = function (divElement) {
        if (divElement.hasClass('able')) {
            divElement.removeClass('able');
            divElement.addClass('unable');
        } else {
            divElement.removeClass('unable');
            divElement.addClass('able');
        }
    };
    $('#user_div_cpu').click(function () {
        modify_button($('#user_div_cpu'));
    });
    $('#user_div_mem').click(function () {
        modify_button($('#user_div_mem'));
    });
    $('#user_div_net').click(function () {
        modify_button($('#user_div_net'));
    });
    $('#user_div_disk').click(function () {
        modify_button($('#user_div_disk'));
    });
    $('#user_div_file').click(function () {
        modify_button($('#user_div_file'));
    });
    $('#user_div_desktop').click(function () {
        modify_button($('#user_div_desktop'));
    });
    $('#user_div_user').click(function () {
        modify_button($('#user_div_user'));
    });
    $('#user_modify_power').click(function () {
        if (!select) {
            return;
        }
        $('#user_div_id').text(select.user_info.id);
        $('#user_div_name').text(select.user_info.name);
        $('#user_div_cpu')
            .removeClass('able')
            .removeClass('unable')
            .addClass(select.user_info.cpu ? 'able' : 'unable');
        $('#user_div_mem')
            .removeClass('able')
            .removeClass('unable')
            .addClass(select.user_info.mem ? 'able' : 'unable');
        $('#user_div_net')
            .removeClass('able')
            .removeClass('unable')
            .addClass(select.user_info.net ? 'able' : 'unable');
        $('#user_div_disk')
            .removeClass('able')
            .removeClass('unable')
            .addClass(select.user_info.disk ? 'able' : 'unable');
        $('#user_div_file')
            .removeClass('able')
            .removeClass('unable')
            .addClass(select.user_info.file ? 'able' : 'unable');
        $('#user_div_desktop')
            .removeClass('able')
            .removeClass('unable')
            .addClass(select.user_info.desktop ? 'able' : 'unable');
        $('#user_div_user')
            .removeClass('able')
            .removeClass('unable')
            .addClass(select.user_info.user ? 'able' : 'unable');
        $('#user_div').show();
    });
    $('#user_ok').click(function () {
        $.ajax({
            url: 'console/user_modify_power',
            type: 'POST',
            data: {
                id: select.user_info.id,
                name: select.user_info.name,
                cpu: $('#user_div_cpu').hasClass('able'),
                mem: $('#user_div_mem').hasClass('able'),
                net: $('#user_div_net').hasClass('able'),
                disk: $('#user_div_disk').hasClass('able'),
                file: $('#user_div_file').hasClass('able'),
                desktop: $('#user_div_desktop').hasClass('able'),
                user: $('#user_div_user').hasClass('able'),
            },
            success: function (data) {
                if (data.data) {
                    update_user_list();
                }
                $('#user_div').hide();
                $('#user_modify_power').addClass('unable');
                select = undefined;
            }
        });
    });
    $('#user_close').click(function () {
        $('#user_div').hide();
    });
    $('#user_flush').click(function () {
        update_user_list();
    });

};