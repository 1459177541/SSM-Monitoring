// JavaScript Document
$(document).ready(function(){
    $('#sign_up_button').click(function(){
        if($('#pwd').val()===''||$('#pwd2').val()===''){
            $('#err').text('密码为空');
        } else if($("#pwd").val()!==$("#pwd2").val()){
            $("#err").text('两次输入密码不匹配');
        } else {
            $.post(
                "register", 
                {
                    id:$("#uid").val(),
                    name:$('#name').val(),
                    password:$("#pwd").val()
                },
                function(result){
                    if(result.success){
                        window.location.href = result.data;
                    }else{
                        $('#err').text(result.data);
                    }
                }
            )
        }
    })
});