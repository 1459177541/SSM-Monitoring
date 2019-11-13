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
        var opion = {legend:{data:[]}};
        info.forEach(function(element, index, arr){
            seriesData.push({
                name:element.name,
                data:new Array(num), 
                type:'line', 
                showSymbol: false, 
                hoverAnimation:false, 
                areaStyle:{}
            });
            opion.legend.data.push(element.name);
        });
        opion.series=seriesData;
        memChart.setOption(opion);

        var xAxisData = new Array(num);
        var addData = function () {
            $.get(
                '/console/mem_status',
                function (data) {
                    xAxisData.shift();
                    var date = new Date(data.data.time);
                    xAxisData.push((date.getMonth()+1)+'月'+date.getDate()+'日'
                        +' '+date.getHours()+':'+date.getMinutes()+':'+date.getSeconds()
                        +' '+date.getMilliseconds());
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
            );
        };
        // var count = 0;
        // var addData = function(){
        //     count++;
        //     seriesData.forEach(function(element, index, arr){
        //         element.data.shift();
        //         element.data.push(Math.random()*8*1024*1024);
        //     });
        //     xAxisData.shift();
        //     xAxisData.push(count);
        //     memChart.setOption({
        //         xAxis: {data:xAxisData},
        //         series: seriesData
        //     });
        //     setTimeout(addData, 500);
        // };
        addData();
    };

    $.get(
        '/console/mem_info',
        function (data) {
            set_info(data.data);
        }
    );
    // var info = [
    //     {
    //         type:'value',
    //         name:'memery',
    //         min:0,
    //         max:8*1024*1024
    //     },{
    //         type:'value',
    //         name:'swap',
    //         min:0,
    //         max:8*1024*1024
    //     }
    // ];
    // set_info(info);
};