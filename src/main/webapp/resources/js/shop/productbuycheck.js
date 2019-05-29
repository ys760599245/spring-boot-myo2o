$(function () {
    var shopId = 1;
    var productName = '';

    function getList() {
        var listUrl = '/o2o/shop/listuserproductmapsbyshop?pageIndex=1&pageSize=9999&shopId=' + shopId + '&productName=' + productName;
        $.getJSON(listUrl, function (data) {
            if (data.success) {
                var userProductMapList = data.userProductMapList;
                var tempHtml = '';
                userProductMapList.map(function (item, index) {
                    tempHtml += ''
                        + '<div class="row row-productbuycheck">'
                        + '<div class="col-33">' + item.productName + '</div>'
                        + '<div class="col-33 productbuycheck-time">' + item.createTime + '</div>'
                        + '<div class="col-33">' + item.userName + '</div>'
                        + '</div>';
                });
                $('.productbuycheck-wrap').html(tempHtml);
            }
        });
    }

    $('#search').on('input', function (e) {
        productName = e.target.value;
        $('.productbuycheck-wrap').empty();
        getList();
    });

    getList();


    var myChart = echarts.init(document.getElementById('chart'));

    var option = {
        tooltip: {
            trigger: 'axis',
            axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
            }
        },
        legend: {
            data: ['避孕套', '麻古', '伟哥']
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        xAxis: [
            {
                type: 'category',
                data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
            }
        ],
        yAxis: [
            {
                type: 'value'
            }
        ],
        series: [
            {
                name: '避孕套',
                type: 'bar',
                data: [120, 132, 101, 134, 290, 230, 220]
            },
            {
                name: '麻古',
                type: 'bar',
                data: [60, 72, 71, 74, 190, 130, 110]
            },
            {
                name: '伟哥',
                type: 'bar',
                data: [62, 82, 91, 84, 109, 110, 120]
            }
        ]
    };

    myChart.setOption(option);

    /**
     *获取7天的销量
     */
    function getProductSellDailyList() {
        //获取该店铺商品7天销量的URL
        var listProductSellDailyUrl = "/o2o/shop/listproductselldailyinfobyshop";
        //访问后台 该店铺商品7天的销量的URL
        $.getJSON(listProductSellDailyUrl, function (data) {
            if (data.success) {
                //生成静态的Echart信息的部分
                var myChart = echarts.init(document.getElementById("chart"));
                var option = generateStaticEchartPart();
                //遍历销售统计列表 动态设定echarts的值
                option.legend.data = data.legendData;
                option.xAxis = data.xAxis;
                option.series = data.series;
                myChart.setOption(option);
            }

        })

    }

    /**
     *生成静态的Echart信息的部分
     */
    function generateStaticEchartPart() {
        //逻辑部分
        var option = {
            //提示框 鼠标炫富交互时候的信息提示
            tooltip: {
                trigger: 'axis',
                axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                    type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                }
            },
            //图例 每个图标最多仅有一个图列子
            legend: {},
            //直角坐标系内绘图网格
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            xAxis: [{}],
            yAxis: [
                {
                    type: 'value'
                }
            ],


        };

    }

});