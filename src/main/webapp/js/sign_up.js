// JavaScript Document
$(document).ready(function(){
    $('#dialog').hide();
    $('#console_button').click(function () {
        window.location.href = '/console';
    });
    $('#sign_up_button').click(function(){
        if($('#pwd').val()===''||$('#pwd2').val()===''){
            $('#err').text('密码为空');
        } else if($("#pwd").val()!==$("#pwd2").val()){
            $("#err").text('两次输入密码不匹配');
        } else {
            $.ajax({
                url: "sign_up",
                type: 'POST',
                data: {
                    name: $('#name').val(),
                    password: $("#pwd").val()
                },
                success: function (result) {
                    if (result.success) {
                        // window.location.href = result.data;
                        $('#message').text("您的ID为：" + result.data);
                        $("#dialog").show();
                    } else {
                        $('#err').text(result.data);
                    }
                }
            });
        }
    })
});