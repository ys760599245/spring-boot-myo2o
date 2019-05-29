$(function () {
    var shopId = 1;
    var awardName = '';

//获取积分兑换记录的url
    function getList() {
        var listUrl = '/o2o/shop/listuserawardmapsbyshop?pageIndex=1&pageSize=9999&shopId='
            + shopId + '&awardName=' + awardName;
        $.getJSON(listUrl, function (data) {
            if (data.success) {
                var userAwardMapList = data.userAwardMapList;
                var tempHtml = '';
                userAwardMapList.map(function (item, index) {
                    tempHtml += '' + '<div class="row row-awarddeliver">'
                        + '<div class="col-33">' + item.awardName
                        + '</div>'
                        + '<div class="col-33 awarddeliver-time">'
                        + new Date(item.createTime).Format("yyyy-MM-dd HH:mm:ss")
                        + '</div>' + '<div class="col-33">' + item.userName
                        + '</div>' + '</div>';
                });
                $('.awarddeliver-wrap').html(tempHtml);
            }
        });
    }

//搜索绑定 获取并且按照奖品名模糊查询
    $('#search').on('input', function (e) {
        awardName = e.target.value;
        $('.awarddeliver-wrap').empty();
        getList();
    });

    getList();
});