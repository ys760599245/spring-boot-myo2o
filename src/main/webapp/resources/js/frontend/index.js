$(function () {
    //定义访问后台 获取头条列表以及以及一级类别列表的URL
    var url = '/o2o/frontend/listmainpageinfo';
//获取头条列表以及以及一级类别列表
    $.getJSON(url, function (data) {
        if (data.success) {
            //获取后台传过来的头条列表
            var headLineList = data.headLineList;
            console.log("=============1111111111111111:" + headLineList.toString());
            var swiperHtml = '';
            //遍历头条列表 以及凭借轮播图组合
            headLineList.map(function (item, index) {
                console.log("=============:" + item.toString());

                /* swiperHtml += '<div class="swiper-slide">'
                     + '<a href="' + item.lineLink + '" external><img class ="banner-img" src="' + getContextPath() + item.lineImg + '" alt="' + item.lineName + '"></a>'
                     + '</div>';*/

                swiperHtml += '<div class="swiper-slide">'
                    + '<a href="' + item.lineLink + '" external><img class ="banner-img" src="' + item.lineImg + '" alt="' + item.lineName + '"></a>'
                    + '</div>';

            });
            //将轮播图组赋值给前端的Html组件
            $('.swiper-wrapper').html(swiperHtml);
            //设定轮播图组合轮换时间为1秒
            $(".swiper-container").swiper({
                autoplay: 1000,
                //用户对轮播图进行操作的时候 是否自动停止
                autoplayDisableOnInteraction: false
            });
            //获取后台传递过来的大类列表
            var shopCategoryList = data.shopCategoryList;
            var categoryHtml = '';
            //遍历大类列表 拼接处 两两一行的类别
            shopCategoryList.map(function (item, index) {
                categoryHtml += ''
                    + '<div class="col-50 shop-classify" data-category=' + item.shopCategoryId + '>'
                    + '<div class="word">'
                    + '<p class="shop-title">' + item.shopCategoryName + '</p>'
                    + '<p class="shop-desc">' + item.shopCategoryDesc + '</p>'
                    + '</div>'
                    + '<div class="shop-classify-img-warp">'
                    + '<img class="shop-img" src="' + item.shopCategoryImg + '">'
                    + '</div>'
                    + '</div>';
            });
            //将拼接好的类别赋值给前端的html控件进行展示
            $('.row').html(categoryHtml);
        }
    });
//点击我 显示侧栏位
    $('#me').click(function () {
        $.openPanel('#panel-left-demo');
    });


    $('.row').on('click', '.shop-classify', function (e) {
        var shopCategoryId = e.currentTarget.dataset.category;
        var newUrl = '/o2o/frontend/shoplist?parentId=' + shopCategoryId;
        window.location.href = newUrl;
    });

    function getContextPath() {
        return "/o2o/";
    }

});
