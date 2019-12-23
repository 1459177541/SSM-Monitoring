// JavaScript Document
$(document).ready(function(){
    $('#sign_in_button').click(function(){
        $.ajax({
            url: "/sign_in",
            type: 'POST',
            data: {
                id: $("#uid").val(),
                password: $("#pwd").val()
            },
            success: function (result) {
                if (result.success) {
                    window.location.href = result.data;
                } else {
                    $('#err').text(result.data);
                }
            }
        });
    })
});