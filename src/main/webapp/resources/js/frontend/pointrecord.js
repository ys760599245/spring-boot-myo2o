$(function () {
    var loading = false;
    var maxItems = 20;
    var pageSize = 10;
//获取该用户的奖品临朐记录列表的URL
    var listUrl = '/o2o/frontend/listuserawardmapsbycustomer';

    var pageNum = 1;
    var productName = '';

//按照查询条件获取奖品兑换记录列表
    function addItems(pageSize, pageIndex) {
        // 生成新条目的HTML
        var url = listUrl + '?shopId=1&' + 'pageIndex=' + pageIndex
            + '&pageSize=' + pageSize + '&productName=' + productName;
        loading = true;
        $.getJSON(url, function (data) {
            if (data.success) {
                maxItems = data.count;
                var html = '';
                data.userAwardMapList.map(function (item, index) {
                    var status = "";
                    //根据userstatus 显示是否已经在实体店领取过该商品
                    if (item.usedStatus == 0) {
                        status = "未领取";
                    } else if (item.usedStatus == 1) {
                        status = "已领取";
                    }
                    html += '' + '<div class="card" data-award-id='
                        + item.userAwardId + '>'
                        + '<div class="card-header">' + item.shop.shopName
                        + '<span class="pull-right">' + status
                        + '</span></div>' + '<div class="card-content">'
                        + '<div class="list-block media-list">' + '<ul>'
                        + '<li class="item-content">'
                        + '<div class="item-inner">'
                        + '<div class="item-subtitle">' + item.awardName
                        + '</div>' + '</div>' + '</li>' + '</ul>'
                        + '</div>' + '</div>' + '<div class="card-footer">'
                        + '<p class="color-gray">'
                        + new Date(item.createTime).Format("yyyy-MM-dd")
                        + '</p>' + '<span>消费积分:' + item.point + '</span>'
                        + '</div>' + '</div>';
                });
                $('.list-div').append(html);
                var total = $('.list-div .card').length;
                if (total >= maxItems) {
                    // 加载完毕，则注销无限加载事件，以防不必要的加载
                    $.detachInfiniteScroll($('.infinite-scroll'));
                    // 删除加载提示符
                    $('.infinite-scroll-preloader').remove();
                }
                pageNum += 1;
                loading = false;
                $.refreshScroller();
            }
        });
    }

    addItems(pageSize, pageNum);
    // 绑定卡片点击的事件 如点击卡片 则进入讲评临朐的详情页面
    //顾客凭借详情页面 的二维码到实体类给电源扫描领取事物奖品
    $(".list-div").on('click', '.card', function (e) {
        var userAwardId = e.currentTarget.dataset.userAwardId;
        window.location.href = '/o2o/frontend/myawarddetail?userAwardId' + userAwardId;
    })


    $(document).on('infinite', '.infinite-scroll-bottom', function () {
        if (loading)
            return;
        addItems(pageSize, pageNum);
    });

    $('#search').on('input', function (e) {
        productName = e.target.value;
        $('.list-div').empty();
        pageNum = 1;
        addItems(pageSize, pageNum);
    });

    $('#me').click(function () {
        $.openPanel('#panel-left-demo');
    });
    $.init();
});
