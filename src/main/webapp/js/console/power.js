// JavaScript Document
var powerMap = {
    index:{
        menu:'<li data-menuanchor="index_anchor" class="active"><a href="#index_anchor">主页</a></li>',
        anchor:'index_anchor',
        doc:'<div id="index" class="section">' +
                '<div class="main">' +
                    '<h1>主页</h1>' +
                    '<h2>选择左边一项查看相应项目</h2>' +
                    '<h2>或者滑动鼠标滚轮切换</h2>' +
                '</div>' +
            '</div>',
        order:1
    },
    cpu:{
        menu:'<li data-menuanchor="cpu_anchor"><a href="#cpu_anchor">处理器</a></li>',
        anchor:'cpu_anchor',
        doc:'<div id="cpu" class="section"><div id="cpuChart" class="chart"></div></div>',
        method:cpu,
        order:2
    },
    mem:{
        menu:'<li data-menuanchor="mem_anchor"><a href="#mem_anchor">内存</a></li>',
        anchor:'mem_anchor',
        doc:'<div id="men" class="section"><div id="memChart" class="chart"></div></div>',
        method:mem,
        order:3
    },
    file:{
        menu:'<li data-menuanchor="file_anchor"><a href="#file_anchor">文件</a></li>',
        anchor:'file_anchor',
        doc:'<div class="section" id="file">' +
                '<div id="file_index" class="slide">' +
                    '<div class="main">'+
                        '<h1>文件</h1>' +
                        '<h2>选择或输入目录路径以监听</h2>' +
                        '<div class="input">' +
                            '<font>路径</font>' +
                            '<input id="path_input" list="path">' +
                            '<datalist id="path"></datalist>' +
                        '</div>' +
                        '<div id="path_button" class="button">确认</div>' +
                    '</div>'+
                '</div>' +
            '</div>',
        method: file,
        order:4
    },
    user:{
        menu:'<li data-menuanchor="user_anchor"><a href="#user_anchor">用户</a></li>',
        anchor:'user_anchor',
        doc:'<div id="user" class="section">USER</div>',
        order:5
    }
};