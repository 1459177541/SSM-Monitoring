// JavaScript Document
var start = function(power){
    power.forEach(function(element){
        $("#menu").append(powerMap[element].menu);
        anchors.push(powerMap[element].anchor);
        $('#fullpage').append(powerMap[element].doc);
    });
    $('#fullpage').fullpage({
        anchors: anchors,
        menu: '#menu'
    });
    power.forEach(function(element){
       powerMap[element].method?powerMap[element].method():null; 
    });
    $("#menu").append('<li id="sing_out"><a href="/sign_out">登出</a></li>');
    $('#loading').remove();
};
$(document).ready(function(){
   $.get(
     '/console/power',
     function (result) {
         var power = result.data.sort(function (a, b) {
             var ao = powerMap[a]?powerMap[a].order:0;
             var bo = powerMap[b]?powerMap[b].order:0;
             return ao-bo;
         });
         start(power);
     }
   );
//     start(['index', 'cpu', 'mem', 'file', 'user']);
});