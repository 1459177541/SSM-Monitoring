// JavaScript Document
var net = function(){
    var num=50;
    var seriesData = [];
    var netChart = echarts.init(document.getElementById('netChart'));
    netChart.setOption({
        title:{
            text:'net',
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
            bottom:20
        },
        xAxis:{
            type:'category',
            boundaryGap:false,
            data:new Array(num)
        },
        yAxis:[{
            type:'value',
            name:'up',
            min:0,
            position:'left'
        },{
            type:'value',
            name:'down',
            min:0,
            position:'right'
        }]
    });

    var set_info = function(info){
        var option = {legend:{}};
        info.forEach(function(element, index, arr){
            seriesData.push({
                name:element,
                data:new Array(num), 
                type:'line',
                lineStyle:'solid',
                showSymbol: false, 
                hoverAnimation:false,
                yAxisIndex:0
            });
            seriesData.push({
                name:element,
                data:new Array(num),
                type:'line',
                lineStyle:'dashed',
                showSymbol: false,
                hoverAnimation:false,
                yAxisIndex:1
            })
        });
        option.legend.data = info;
        option.series=seriesData;
        netChart.setOption(option);
        
        var xAxisData = new Array(num);
        var addData = function () {
            $.get(
                '/console/net_status',
                function (data) {
                    xAxisData.shift();
                    var date = new Date(data.data[0].time);
                    xAxisData.push((date.getMonth()+1)+'月'+date.getDate()+'日'
                        +' '+date.getHours()+':'+date.getMinutes()+':'+date.getSeconds()
                        +' '+date.getMilliseconds());
                    seriesData.forEach(function (element) {
                        element.data.shift();
                        element.data.push(data.data[element.yAxisIndex].status[element.name].toFixed(3));
                    });
                    netChart.setOption({
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
        //     netChart.setOption({
        //         xAxis: {data:xAxisData},
        //         series: seriesData
        //     });
        //     setTimeout(addData, 500);
        // };
        addData();
    };

    $.get(
        '/console/net_info',
        function (data) {
            set_info(data.data);
        }
    );
    // var info = ['net0', 'net1', 'net2', 'net3'];
    // set_info(info);
};