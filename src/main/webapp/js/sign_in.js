// JavaScript Document
$(document).ready(function(){
    $('#sign_in_button').click(function(){
        $.post(
            "/sign_in",
            {
                id:$("#uid").val(), 
                password:$("#pwd").val()
            },
            function(result, status){
                if(result.success){
                    $('#err').text(result.message);
                    window.location.href = result.data;
                }else{
                    $('#err').text(result.data);
                }
            },
            'json'
        )
    })
});