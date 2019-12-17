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
        var option = {legend:{}};
        info.forEach(function(element){
            seriesData.push({
                name:element, 
                data:new Array(num), 
                type:'line', 
                showSymbol: false, 
                hoverAnimation:false, 
                areaStyle:{}
            });
        });
        option.legend.data = info;
        option.series=seriesData;
        cpuChart.setOption(option);
        
        var xAxisData = new Array(num);
        var addData = function () {
            $.ajax({
                url: '/console/cpu_status',
                method: 'GET',
                success: function (data) {
                    xAxisData.shift();
                    var date = new Date(data.data.time);
                    xAxisData.push((date.getMonth() + 1) + '月' + date.getDate() + '日'
                        + ' ' + date.getHours() + ':' + date.getMinutes() + ':' + date.getSeconds()
                        + ' ' + date.getMilliseconds());
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
            });
        };
        addData();
    };

    $.ajax({
        url: '/console/cpu_info',
        method: 'GET',
        success: function (data) {
            set_info(data.data);
        }
    });
};