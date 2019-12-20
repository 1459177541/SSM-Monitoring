// JavaScript Document
var desktop = function () {
    var flush = function () {
        $('#desktop_img').attr('src', '/console/desktop?t='+Math.random());
        setTimeout(flush, 500);
    };
    flush();
};