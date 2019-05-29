$(function () {
    var loading = false;
    //分页允许返回的最大条数 超过次数禁止访问后台
    var maxItems = 999;
    //一页返回的最大条数
    var pageSize = 10;
    //获取店铺列表的URL
    var listUrl = '/o2o/frontend/listshops';
    //获取店铺类别列表以及区域列表的URL
    var searchDivUrl = '/o2o/frontend/listshopspageinfo';
    //页码
    var pageNum = 1;
    //从地址栏URL获取 Parent Shop CategoryId
    var parentId = getQueryString('parentId');
    var areaId = '';
    var shopCategoryId = '';
    var shopName = '';

    function getSearchDivData() {
        如果传入parentId
        则取出一级类别下面的所有的二级类别
        var url = searchDivUrl + '?' + 'parentId=' + parentId;
        $
            .getJSON(
                url,
                function (data) {
                    if (data.success) {
                        //获取后台返回过来的店铺类别列表
                        var shopCategoryList = data.shopCategoryList;
                        var html = '';
                        html += '<a href="#" class="button" data-category-id=""> 全部类别  </a>';
                        shopCategoryList
                            .map(function (item, index) {
                                html += '<a href="#" class="button" data-category-id='
                                    + item.shopCategoryId
                                    + '>'
                                    + item.shopCategoryName
                                    + '</a>';
                            });
                        $('#shoplist-search-div').html(html);
                        var selectOptions = '<option value="">全部街道</option>';
                        var areaList = data.areaList;
                        areaList.map(function (item, index) {
                            selectOptions += '<option value="'
                                + item.areaId + '">'
                                + item.areaName + '</option>';
                        });
                        $('#area-search').html(selectOptions);
                    }
                });
    }

//渲染出店铺类别列表以及区域列表以供搜索
    getSearchDivData();

    /**
     *  获取分页展示的店铺列表信息
     * @param pageSize
     * @param pageIndex
     */
    function addItems(pageSize, pageIndex) {
        // 生成新条目的HTML
        //拼接出查询的URL 赋空值默认就是去掉这个条件的限制 有值就代表按照这个条件去查询
        var url = listUrl + '?' + 'pageIndex=' + pageIndex + '&pageSize='
            + pageSize + '&parentId=' + parentId + '&areaId=' + areaId
            + '&shopCategoryId=' + shopCategoryId + '&shopName=' + shopName;
        //设定加载符若还在后台取数据 则不能再次访问后台避免多次重复加载
        loading = true;
        //访问后台获取相应查询条件下的店铺列表
        $.getJSON(url, function (data) {
            if (data.success) {
                //获取当前查询条件下的店铺总数
                maxItems = data.count;
                var html = '';
                //遍历店铺列表 拼接出卡片集合
                data.shopList.map(function (item, index) {
                    html += '' + '<div class="card" data-shop-id="'
                        + item.shopId + '">' + '<div class="card-header">'
                        + item.shopName + '</div>'
                        + '<div class="card-content">'
                        + '<div class="list-block media-list">' + '<ul>'
                        + '<li class="item-content">'
                        + '<div class="item-media">' + '<img src="'
                        + item.shopImg + '" width="44">' + '</div>'
                        + '<div class="item-inner">'
                        + '<div class="item-subtitle">' + item.shopDesc
                        + '</div>' + '</div>' + '</li>' + '</ul>'
                        + '</div>' + '</div>' + '<div class="card-footer">'
                        + '<p class="color-gray">'
                        + new Date(item.lastEditTime).Format("yyyy-MM-dd")
                        + '更新</p>' + '<span>点击查看</span>' + '</div>'
                        + '</div>';
                });
                //将卡片的集合添加到Html组件上
                $('.list-div').append(html);
                //获取目前为止已经显示的卡片的总数 包含之前已经加载的
                var total = $('.list-div .card').length;
                //若总数已经达到跟按照此查询条件列出来的总数一致 则停止后台的加载
                if (total >= maxItems) {
                    // 加载完毕，则注销无限加载事件，以防不必要的加载
                    $.detachInfiniteScroll($('.infinite-scroll'));
                    // 删除加载提示符
                    $('.infinite-scroll-preloader').remove();
                }
                //否则页码加1 继续Load出新的店铺
                pageNum += 1;
                //加载结束 可以再次加载
                loading = false;
                //刷新界面 显示新加载的界面
                $.refreshScroller();
            }
        });
    }

    // 预先加载20条
    addItems(pageSize, pageNum);
//下滑屏幕自动进行分页搜索
    $(document).on('infinite', '.infinite-scroll-bottom', function () {
        if (loading)
            return;
        addItems(pageSize, pageNum);
    });
//点击店铺的卡片进入该店铺的详情页面
    $('.shop-list').on('click', '.card', function (e) {
        var shopId = e.currentTarget.dataset.shopId;
        window.location.href = '/o2o/frontend/shopdetail?shopId=' + shopId;
    });
//选择新的店铺类别之后 重置页码清空原先的店铺列表 按照新的类别进行查询
    $('#shoplist-search-div').on(
        'click',
        '.button',
        function (e) {
            if (parentId) {// 如果传递过来的是一个父类下的子类
                shopCategoryId = e.target.dataset.categoryId;
                //若之前已经选择别的category 则移除其选定效果 改成新添加的
                if ($(e.target).hasClass('button-fill')) {
                    $(e.target).removeClass('button-fill');
                    shopCategoryId = '';
                } else {
                    $(e.target).addClass('button-fill').siblings()
                        .removeClass('button-fill');
                }
                //由于查询条件的改变清空店铺列表在进行查询
                $('.list-div').empty();
                //重置页码
                pageNum = 1;
                addItems(pageSize, pageNum);
            } else {// 如果传递过来的父类为空，则按照父类查询
                parentId = e.target.dataset.categoryId;
                if ($(e.target).hasClass('button-fill')) {
                    $(e.target).removeClass('button-fill');
                    parentId = '';
                } else {
                    $(e.target).addClass('button-fill').siblings()
                        .removeClass('button-fill');
                }
                $('.list-div').empty();
                pageNum = 1;
                addItems(pageSize, pageNum);
                parentId = '';
            }

        });

    $('#search').on('input', function (e) {
        shopName = e.target.value;
        $('.list-div').empty();
        pageNum = 1;
        addItems(pageSize, pageNum);
    });

    $('#area-search').on('change', function () {
        areaId = $('#area-search').val();
        $('.list-div').empty();
        pageNum = 1;
        addItems(pageSize, pageNum);
    });

    $('#me').click(function () {
        $.openPanel('#panel-left-demo');
    });

    $.init();
});
