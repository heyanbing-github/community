// (function(){
	// var myChart = echarts.init(document.getElementById("widget-chart-box-1"));
	// var myChart2 = echarts.init(document.getElementById("widget-chart-box-2"));
	
	/*var labelTop = {
		
    normal : {
        label : {
            show : true,
            position : 'center',
            formatter : '{b}',
            textStyle: {
                baseline : 'bottom'
            }
        },
        labelLine : {
            show : false
        }
        
    }
};
var labelFromatter = {
    normal : {
        label : {
            formatter : function (params){
                return 100 - params.value + '%'
            },
            textStyle: {
                baseline : 'center'
            }
        }
    },
}
var labelBottom = {
    normal : {
        color: '#ccc',
        label : {
            show : true,
            position : 'center'
        },
        labelLine : {
            show : false
        }
    },
    emphasis: {
        color: '#ccc'
    }
};
var radius = [40, 35];
option = {
	
	
	
    legend: {
        x : 'center',
        y : 'center',
        
    },
    
    grid: {
    	x:0,
    	y:0,
    	x2:0,
    	y2:0
    },
    
    toolbox: {
        show : true,
        feature : {
            magicType : {
                show: true, 
                type: ['pie', 'funnel'],
                option: {
                    funnel: {
                        width: '20%',
                        height: '30%',
                        itemStyle : {
                            normal : {
                                label : {
                                    formatter : function (params){
                                        return 'other\n' + params.value + '%\n'
                                    },
                                    textStyle: {
                                        baseline : 'middle'
                                    }
                                }
                            },
                        } 
                    }
                }
            }
           
        }
    },
    series : [
        {
            type : 'pie',
            
            radius : radius,
            x: '0%', // for funnel
            itemStyle : labelFromatter,
            data : [
                {name:'other', value:46, itemStyle : labelBottom},
                {name:'', value:54,itemStyle : labelTop}
            ]
        }
        
    ],
    animation :false
};*/
                    
	
	// myChart.setOption(option);
	// myChart2.setOption(option);
// })();




(function(){
    $.ajax({
        url:"http://localhost:8080/community/user/getKindsSort",
        dataType:"json",
        Type:"post",
        success:function (data){
            var myChart = echarts.init(document.getElementById("index-pie-1"));

            option = {
                tooltip : {
                    trigger: 'item',
                    formatter: "{a} <br/>{b} : {c} ({d}%)"
                },
                legend: {
                    orient : 'vertical',
                    x : 'left',
                    data:data.name,
                },
                toolbox: {
                    show : true,
                    feature : {
                        mark : {show: true},
                        dataView : {show: true, readOnly: false},
                        magicType : {
                            show: true,
                            type: ['pie', 'funnel'],
                            option: {
                                funnel: {
                                    x: '25%',
                                    width: '50%',
                                    funnelAlign: 'center',
                                    max: 1548
                                }
                            }
                        },
                        restore : {show: true},
                        saveAsImage : {show: true}
                    }
                },
                calculable : true,
                series : [
                    {
                        name:'帖子种类',
                        type:'pie',
                        radius : ['50%', '70%'],
                        itemStyle : {
                            normal : {
                                label : {
                                    show : false
                                },
                                labelLine : {
                                    show : false
                                }
                            },
                            emphasis : {
                                label : {
                                    show : true,
                                    position : 'center',
                                    textStyle : {
                                        fontSize : '20',
                                        fontWeight : 'bold'
                                    }
                                }
                            }
                        },
                        data:data,
                    }
                ]
            };

            myChart.setOption(option);
        }
    })

})();



(function(){
    $.ajax({
        url:"http://localhost:8080/community/user/getTopGoods",
        dataType:"json",
        Type:"post",
        success:function (data) {
            var myChart = echarts.init(document.getElementById("index-bar-1"));

            var goodsname=new Array(data.length);
            var goodsvalue=new Array(data.length);
            for(var i = 0;i <data.length;i++){
                goodsname[data.length-i-1]=data[i].gname;
            }
            for(var i = 0;i <data.length;i++){
                goodsvalue[data.length-i-1]=data[i].value;
            }
            console.log(goodsname);
            option = {
                title: {
                    text: '前五名',
                },
                tooltip: {
                    trigger: 'axis',
                    axisPointer: {
                        type: 'shadow'
                    }
                },

                grid: {
                    left: '3%',
                    right: '4%',
                    bottom: '3%',
                    containLabel: true
                },
                xAxis: {
                    type: 'value',
                    boundaryGap: [0, 0.01]
                },
                yAxis: {
                    type: 'category',
                    data: goodsname
                },
                series: [
                    {
                        name: '销售量',
                        type: 'bar',
                        data: goodsvalue
                    }
                ]
            };

            myChart.setOption(option);
        }
    })
})();



(function(){
	var myChart = echarts.init(document.getElementById("index-line-1"));
	
	option = {

    tooltip: {
        trigger: 'axis'
    },
    legend: {
        data:['最高气温','最低气温'],
        x: "right"
    },
    
    xAxis:  {
        type: 'category',
        boundaryGap: false,
        data: ['周一','周二','周三','周四','周五','周六','周日']
    },
    yAxis: {
        type: 'value',
        axisLabel: {
            formatter: '{value} °C'
        }
    },
    series: [
        {
            name:'最高气温',
            type:'line',
            data:[11, 11, 15, 13, 12, 13, 10],
            markPoint: {
                data: [
                    {type: 'max', name: '最大值'},
                    {type: 'min', name: '最小值'}
                ]
            },
            markLine: {
                data: [
                    {type: 'average', name: '平均值'}
                ]
            }
        },
        {
            name:'最低气温',
            type:'line',
            data:[1, -2, 2, 5, 3, 2, 0],
            markPoint: {
                data: [
                    {name: '周最低', value: -2, xAxis: 1, yAxis: -1.5}
                ]
            },
            markLine: {
                data: [
                    {type: 'average', name: '平均值'},
                    [{
                        symbol: 'none',
                        x: '90%',
                        yAxis: 'max'
                    }, {
                        symbol: 'circle',
                        label: {
                            normal: {
                                position: 'start',
                                formatter: '最大值'
                            }
                        },
                        type: 'max',
                        name: '最高点'
                    }]
                ]
            }
        }
    ]
};


                    
                    
	
	myChart.setOption(option);
})();

