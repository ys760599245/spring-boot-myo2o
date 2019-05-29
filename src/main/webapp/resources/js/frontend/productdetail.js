$(function () {
    var productId = getQueryString('productId');
    //商品信息的url
    var productUrl = '/o2o/frontend/listproductdetailpageinfo?productId='
        + productId;
//访问后台获取该商品信息并且渲染
    $.getJSON(
        productUrl,
        function (data) {
            if (data.success) {
                //获取商品信息
                var product = data.product;
                //商品缩略图
                $('#product-img').attr('src', product.imgAddr);
                //商品更新时间
                $('#product-time').text(
                    new Date(product.lastEditTime)
                        .Format("yyyy-MM-dd"));
                //上平名称
                $('#product-name').text(product.productName);
                //商品简介
                $('#product-desc').text(product.productDesc);
                if (product.point != undefined) {
                    $("#promotion-point").text(product.point);
                }


                var imgListHtml = '';
                /*          product.productImgList.map(function (item, index) {
                              imgListHtml += '<div> <img src="'
                                  + item.imgAddr + '"/></div>';
                          });*/
                // 生成购买商品的二维码供商家扫描
                /*        imgListHtml += '<div> <img src="/o2o/frontend/generateqrcode4product?productId='
                            + product.productId + '"/></div>';
                        $('#imgList').html(imgListHtml);*/
            } else if () {
                //如果原价不为空而现价为空则只是展示原价
                $("#price").show();
                $("#promotionPrice").text('$' + product.promotionPrice);
            } else if (product.normalPrice == undefined && product.promotionPrice != undefined) {
                //如果现价不为空而原价为空则展示现价
                $("#promotionPrice").text('$' + product.promotionPrice);
            }

            //遍历商品详情图片列表 并且生成img标签
            product.productImgList.map(function (item, index) {
                imgListHtml += '<div> <img src="'
                    + item.imgAddr
                    + '" width="100%"/></div>';
            });
            if (data.needQRCode) {
                //若顾客登录 则生成购买商品的二维码 供商家扫描
                imgListHtml += '<div> <img src="/o2o/frontend/generateqrcode4product?productId='
                    + product.productId
                    + '"width="100%"/></div>';
            }
            $('#imgList').html(imgListHtml);
        });
    //点击后打开右侧栏
    $('#me').click(function () {
        $.openPanel('#panel-left-demo');
    });
    $.init();
});
