$(function () {
    var shopId = 1;
    var listUrl = '/o2o/shop/listproductsbyshop?pageIndex=1&pageSize=9999&shopId='
        + shopId;
    var deleteUrl = '/o2o/shop/modifyproduct';

    /**
     *获取此店铺下面的商品列表
     */
    function getList() {
        //从后台获取此店铺的商品列表
        $.getJSON(listUrl, function (data) {
            if (data.success) {
                var productList = data.productList;
                var tempHtml = '';
                //遍历每条商品信息，拼接成一行 列信息包含
                //  商品名称 优先级 上架 下架 包含ProductId  编辑按钮ProductId
                //预览 ProductId
                productList.map(function (item, index) {
                    var textOp = "下架";
                    var contraryStatus = 0;
                    if (item.enableStatus == 0) {
                        //若状态值为0 表示是已经下架商品 操作变为上架操作 即点击上架按钮上架相关操作
                        textOp = "上架";
                        contraryStatus = 1;
                    } else {
                        contraryStatus = 0;
                    }
                    //拼接每件商品的行信息
                    tempHtml += '' + '<div class="row row-product">'
                        + '<div class="col-30">'
                        + item.productName
                        + '</div>'
                        + '<div class="col-20">'
                        + item.priority
                        + '</div>'
                        + '<div class="col-50">'
                        + '<a href="#" class="edit" data-id="'
                        + item.productId
                        + '" data-status="'
                        + item.enableStatus
                        + '">编辑</a>'
                        + '<a href="#" class="delete" data-id="'
                        + item.productId
                        + '" data-status="'
                        + contraryStatus
                        + '">'
                        + textOp
                        + '</a>'
                        + '<a href="#" class="preview" data-id="'
                        + item.productId
                        + '" data-status="'
                        + item.enableStatus
                        + '">预览</a>'
                        + '</div>'
                        + '</div>';
                });
                //将拼接好的信息赋值进Html控件
                $('.product-wrap').html(tempHtml);
            }
        });
    }

    getList();

    function deleteItem(id, enableStatus) {
        var product = {};
        product.productId = id;
        product.enableStatus = enableStatus;
        $.confirm('确定么?', function () {
            $.ajax({
                url: deleteUrl,
                type: 'POST',
                data: {
                    productStr: JSON.stringify(product),
                    statusChange: true
                },
                dataType: 'json',
                success: function (data) {
                    if (data.success) {
                        $.toast('操作成功！');
                        getList();
                    } else {
                        $.toast('操作失败！');
                    }
                }
            });
        });
    }

//将Class为product-wrap里面的a标签 绑定上点击事件
    $('.product-wrap')
        .on(
            'click',
            'a',
            function (e) {
                var target = $(e.currentTarget);
                if (target.hasClass('edit')) {
                    //如果有class edit 则点击就进入店铺信息编辑界面 并且带ProductId参数
                    window.location.href = '/o2o/shop/productedit?productId='
                        + e.currentTarget.dataset.id;
                } else if (target.hasClass('delete')) {
                    deleteItem(e.currentTarget.dataset.id,
                        e.currentTarget.dataset.status);
                    //如果有class preview则去前台展示系统该商品详情页面预览商品信息情况
                } else if (target.hasClass('preview')) {
                    window.location.href = '/o2o/frontend/productdetail?productId='
                        + e.currentTarget.dataset.id;
                }
            });

    $('#new').click(function () {
        window.location.href = '/o2o/shop/productedit';
    });
});