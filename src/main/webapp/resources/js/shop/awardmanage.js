$(function () {
    var shopId = 1;
    //获取该店铺下的奖品列表URL
    var listUrl = '/o2o/shop/listawardsbyshop?pageIndex=1&pageSize=9999&shopId='
        + shopId;
    //设置奖品的课件状态
    var deleteUrl = '/o2o/shop/modifyaward';

    function getList() {
        //访问后台 获取奖品列表
        $.getJSON(listUrl, function (data) {
            if (data.success) {
                var awardList = data.awardList;
                var tempHtml = '';
                //遍历每条奖品的信息 拼接成一行显示 列信息包含
                //奖品名称 优先级 上架|下架 编辑按钮 包含 awardId
                //预览 awardid
                awardList.map(function (item, index) {
                    var textOp = "下架";
                    var contraryStatus = 0;
                    if (item.enableStatus == 0) {
                        textOp = "上架";
                        contraryStatus = 1;
                    } else {
                        //若状态值为0 优先级 标明是已经下架的奖品 操作 变为 上架  就是点击上架按钮 上架 相关
                        contraryStatus = 0;
                    }
                    //拼接每件奖品的行信息
                    tempHtml += '' + '<div class="row row-award">'
                        + '<div class="col-30">'
                        + item.awardName
                        + '</div>'
                        + '<div class="col-20">'
                        + item.priority
                        + '</div>'
                        + '<div class="col-50">'
                        + '<a href="#" class="edit" data-id="'
                        + item.awardId
                        + '" data-status="'
                        + item.enableStatus
                        + '">编辑</a>'
                        + '<a href="#" class="delete" data-id="'
                        + item.awardId
                        + '" data-status="'
                        + contraryStatus
                        + '">'
                        + textOp
                        + '</a>'
                        + '<a href="#" class="preview" data-id="'
                        + item.awardId
                        + '" data-status="'
                        + item.enableStatus
                        + '">预览</a>'
                        + '</div>'
                        + '</div>';
                });
                $('.award-wrap').html(tempHtml);
            }
        });
    }

    getList();

    function deleteItem(awardId, enableStatus) {
        var award = {};
        award.awardId = awardId;
        award.enableStatus = enableStatus;
        $.confirm('确定么?', function () {
            $.ajax({
                url: deleteUrl,
                type: 'POST',
                data: {
                    awardStr: JSON.stringify(award),
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

    $('.award-wrap')
        .on(
            'click',
            'a',
            function (e) {
                var target = $(e.currentTarget);
                if (target.hasClass('edit')) {
                    //如果有class edit 则点击就进入奖品 编辑界面  并且带有 awardid参数
                    window.location.href = '/o2o/shop/awardedit?awardId='
                        + e.currentTarget.dataset.id;
                } else if (target.hasClass('delete')) {
                    ///如果有 class status 则调用后台功能 上下架 相关奖品 并且带有productid
                    deleteItem(e.currentTarget.dataset.id,
                        e.currentTarget.dataset.status);
                } else if (target.hasClass('preview')) {
                    //如果 class preview 则去前台展示系统改讲评详情页预览奖品 情况
                    window.location.href = '/o2o/frontend/awarddetail?awardId='
                        + e.currentTarget.dataset.id;
                }
            });
//给新增按钮点击绑定点击事件
    $('#new').click(function () {
        window.location.href = '/o2o/shop/awardedit';
    });

    function changeItem(awardId, enableStatus) {
        //定义 award json 对象并且添加 awardId 以及状态 上架 下架
        var award = {};
        award.awardId = awardId;
        award.enableStatus = enableStatus;
        $.confirm("确定么", function () {
            //上架相关奖品
            $.ajax({
                url: deleteUrl,
                type: "POST",
                data: {
                    awardStr: JSON.stringify(data()),
                    statusChange: true
                },
                dataType: "json",
                success: function (data) {
                    if (data.success) {
                        $.toast("操作成功");
                        getList();
                    } else {
                        $.toast("操作失败");
                    }

                }
            })
        })

    }
});