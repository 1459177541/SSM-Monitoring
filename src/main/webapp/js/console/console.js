// JavaScript Document
var powerMap = {
    index:{
        menu:'<li data-menuanchor="index_anchor" class="active"><a href="#index_anchor">主页</a></li>',
        anchor:'index_anchor',
        dom:'<div id="index" class="section">INDEX</div>',
        order:1
    },
    cpu:{
        menu:'<li data-menuanchor="cpu_anchor"><a href="#cpu_anchor">处理器</a></li>',
        anchor:'cpu_anchor',
        dom:'<div id="cpu" class="section"><div id="cpuChart"></div></div>',
        method:cpu,
        order:2
    },
    mem:{
        menu:'<li data-menuanchor="mem_anchor"><a href="#mem_anchor">内存</a></li>',
        anchor:'mem_anchor',
        dom:'<div id="men" class="section">MEM</div>',
        order:3
    },
    file:{
        menu:'<li data-menuanchor="file_anchor"><a href="#file_anchor">文件</a></li>',
        anchor:'file_anchor',
        dom:'<div id="file" class="section">FILE</div>',
        order:4
    },
    user:{
        menu:'<li data-menuanchor="user_anchor"><a href="#user_anchor">用户</a></li>',
        anchor:'user_anchor',
        dom:'<div id="user" class="section">USER</div>',
        order:5
    }
};
var start = function(power){
    var anchors = [];
    power.forEach(function(element){
        $("#menu").append(powerMap[element].menu);
        anchors.push(powerMap[element].anchor);
        $('#fullpage').append(powerMap[element].dom);
    });
    $('#fullpage').fullpage({
        anchors: anchors,
        menu: '#menu'
    });
    power.forEach(function(element){
       powerMap[element].method?powerMap[element].method():null; 
    });
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
});