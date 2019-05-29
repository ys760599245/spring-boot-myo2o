$(function () {
    var shopId = 1;
    //列出该店铺下所有授权信息的URL
    var listUrl = '/o2o/shop/listshopauthmapsbyshop?pageIndex=1&pageSize=9999&shopId=' + shopId;
    //删除授权信息的URL
    var deleteUrl = '/o2o/shop/removeshopauthmap';

    function getList() {
        $.getJSON(listUrl, function (data) {
            if (data.success) {
                var shopauthList = data.shopAuthMapList;
                var tempHtml = '';
                shopauthList.map(function (item, index) {
                    var textOp = "恢复";
                    var contraryStatus = 0;
                    if (item.enableStatus == 1) {
                        //若状态值为1 表明是授权生效
                        textOp = "删除";
                        contraryStatus = 0;
                    } else {
                        contraryStatus = 1;
                    }

                    tempHtml
                        += ''
                        + '<div class="row row-shopauth">'
                        + '<div class="col-40">' + item.name + '</div>'
                        + '<div class="col-20">' + item.title + '</div>'
                        + '<div class="col-40">'
                        + '<a href="#" class="edit" data-employee-id="' + item.employeeId + '" data-auth-id="' + item.shopAuthId + '" data-status="' + item.enableStatus + '">编辑</a>'
                        + '<a href="#" class="delete" data-employee-id="' + item.employeeId + '" data-auth-id="' + item.shopAuthId + '" data-status="' + item.enableStatus + '">删除</a>'
                        + '</div>'
                        + '</div>';
                });
                $('.shopauth-wrap').html(tempHtml);
            }
        });
    }

    getList();

    function deleteItem(id) {
        $.confirm('确定么?', function () {
            $.ajax({
                url: deleteUrl,
                type: 'POST',
                data: {
                    shopAuthId: id,
                },
                dataType: 'json',
                success: function (data) {
                    if (data.success) {
                        $.toast('删除成功！');
                        getList();
                    } else {
                        $.toast('删除失败！');
                    }
                }
            });
        });
    }

//给A标签的click事件绑定上对应的方法 即可点击带有edit的A标签就会跳转到授权编辑界面
    //点击带有status的A标签就回去更新该授权信息的状态
    $('.shopauth-wrap').on('click', 'a', function (e) {
        var target = $(e.currentTarget);
        if (target.hasClass('edit')) {
            window.location.href = '/o2o/shop/shopauthedit?shopauthId=' + e.currentTarget.dataset.authId;
        } else if (target.hasClass('delete')) {
            deleteItem(e.currentTarget.dataset.authId);
        } else if (target.hasClass('status')) {
            changeStatus(e.currentTarget.dataset.authId, e.currentTarget.dataset.status)
        }
    });

    // $('#new').click(function () {
    //     window.location.href = '/o2o/shop/shopauthedit';
    // });
    function changeStatus(id, status) {
        var shopAuth = {};
        shopAuth.shopAuthId = id;
        shopAuth.enableStatus = status;
        $.confirm('确定么', function () {
            $.ajax({
                url: modifyUrl,
                type: 'POST',
                data: {
                    //将json参数转化为字符串
                    shopAuthMapstr: JSON.stringify(shopAuth),
                    statusChange: true,
                },
                dataType: 'json',
                success: function (data) {
                    if (data.success()) {
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