// JavaScript Document
var mem = function(){
    var num=50;
    var seriesData = [];
    var memChart = echarts.init(document.getElementById('memChart'));
    
    memChart.setOption({
        title:{
            text:'mem',
            left:45
        },
        tooltip:{
            trigger:'axis',
            axisPointer:{
                type:'cross',
                animation:false
            },
            formatter:'{b0}<hr/>{a0}:{c0} KB<br/>{a1}:{c1} KB'
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
        var option = {legend:{data:[]}};
        info.forEach(function(element){
            seriesData.push({
                name:element.name,
                data:new Array(num), 
                type:'line', 
                showSymbol: false, 
                hoverAnimation:false, 
                areaStyle:{}
            });
            option.legend.data.push(element.name);
        });
        option.series=seriesData;
        memChart.setOption(option);

        var xAxisData = new Array(num);
        var addData = function () {
            $.ajax({
                url: '/console/mem_status',
                method: 'GET',
                success: function (data) {
                    xAxisData.shift();
                    var date = new Date(data.data.time);
                    xAxisData.push((date.getMonth() + 1) + '月' + date.getDate() + '日'
                        + ' ' + date.getHours() + ':' + date.getMinutes() + ':' + date.getSeconds()
                        + ' ' + date.getMilliseconds());
                    seriesData.forEach(function (element) {
                        element.data.shift();
                        element.data.push(data.data.status[element.name]);
                    });
                    memChart.setOption({
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
        url: '/console/mem_info',
        method: 'GET',
        success: function (data) {
            set_info(data.data);
        }
    });
};