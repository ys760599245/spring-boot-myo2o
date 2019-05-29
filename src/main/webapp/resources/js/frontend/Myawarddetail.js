$(function () {
    var userAwardId = getQueryString('userAwardId');
    //根据userAwardId获取用户奖品映射信息
    var productUrl = '/o2o/frontend/getawardbyuserawardid?userAwardId='
        + userAwardId;
//访问后台获取该商品信息并且渲染
    $.getJSON(
        productUrl,
        function (data) {
            if (data.success) {
                //获取奖品信息
                var award = data.award;
                //商品缩略图
                $('#award-img').attr('src', getContextPath, award.awardImg);
                //商品更新时间
                $('#create-time').text(
                    new Date(data.userAwardMap.createTime())
                        .Format("yyyy-MM-dd"));
                //上平名称
                $('#award-name').text(award.awardName);
                //商品简介
                $('#award-desc').text(award.awardDesc);
                var imgListHtml = '';
                if (data.usedStatus == 0) {
                    //若未去实体类去兑换实体奖品 生成兑换礼品的二维码 供商家扫码
                    imgListHtml += '<div> <img src="/o2o/frontend/generateqrcode4award?userAwardId='
                        + userAwardId
                        + '"width="100%"/></div>';
                    $('#imgList').html(imgListHtml);
                }
            }

        });
    //点击后打开右侧栏
    $('#me').click(function () {
        $.openPanel('#panel-left-demo');
    });
    $.init();
});
