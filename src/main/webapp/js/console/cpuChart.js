// JavaScript Document
var cpu = function(){
    var num=50;
    var seriesData = [];
    var cpuChart = echarts.init(document.getElementById('cpuChart'));
    
    cpuChart.setOption({
        title:{
            text:'cpu',
            left:45
        },
        tooltip:{
            trigger:'axis',
            axisPointer:{
                type:'cross',
                animation:false
            }
        },
        axisPointer:{
            snap:true
        },
        toolbox:{
            show:true,
            feature:{
                magicType:{
                    type:['stack','tiled']
                },
                dataZoom:{
                    yAxisIndex:false
                }
            }
        },
        grid:{
            left:0,
            right:0,
            top:50,
            bottom:0
        },
        xAxis:{
            type:'category',
            boundaryGap:false,
            data:new Array(num)
        },
        yAxis:{
            type:'value',
            min:0,
            position:'right'
        }
    });
    
    var set_info = function(info){
        var opion = {legend:{}};
        info.forEach(function(element, index, arr){
            seriesData.push({
                name:element, 
                data:new Array(num), 
                type:'line', 
                showSymbol: false, 
                hoverAnimation:false, 
                areaStyle:{}
            });
        });
        opion.legend.data = info;
        opion.series=seriesData;
        cpuChart.setOption(opion);
        
        var xAxisData = new Array(num);
        var addData = function () {
            $.get(
                '/console/cpu_status',
                function (data) {
                    xAxisData.shift();
                    var date = new Date(data.data.time);
                    xAxisData.push((date.getMonth()+1)+'月'+date.getDate()+'日'
                        +' '+date.getHours()+':'+date.getMinutes()+':'+date.getSeconds()
                        +' '+date.getMilliseconds());
                    seriesData.forEach(function (element) {
                        element.data.shift();
                        element.data.push(data.data.status[element.name].toFixed(3));
                    });
                    cpuChart.setOption({
                        xAxis: {data: xAxisData},
                        series: seriesData
                    });
                    setTimeout(addData, 500);
                }
            );
        };
        // var count = 0;
        // var addData = function(){
        //     count++;
        //     seriesData.forEach(function(element, index, arr){
        //         element.data.shift();
        //         element.data.push(Math.random()*100);
        //     });
        //     xAxisData.shift();
        //     xAxisData.push(count);
        //     cpuChart.setOption({
        //         xAxis: {data:xAxisData},
        //         series: seriesData
        //     });
        //     setTimeout(addData, 500);
        // };
        addData();
    };

    $.get(
        '/console/cpu_info',
        function (data) {
            set_info(data.data);
        }
    );
    // var info = ['cpu0', 'cpu1', 'cpu2', 'cpu3'];
    // set_info(info);
};