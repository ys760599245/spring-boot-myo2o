$(function () {
    //绑定账号
    var bindUrl = '/o2o/local/bindlocalauth';
    //从地址栏的url里获取usertype
    //usertype=1则为前端展示系统，其余则为店家管理系统
    var usertype = getQueryString('usertype');
    $('#submit').click(function () {
        //获取输入的账号
        var username = $('#username').val();
        //获取输入的密码
        var password = $('#pwd').val();
        //获取输入的验证码
        var verifyCodeActual = $('#j_captcha').val();
        var needVerify = false;
        if (!verifyCodeActual) {
            $.toast('请输入验证码');
            return;
        }
        //访问后台
        $.ajax({
            url: bindUrl,
            async: false,
            cache: false,
            type: "post",
            dataType: 'json',
            data: {
                username: username,
                password: password,
                verifyCodeActual: verifyCodeActual
            },
            success: function (data) {
                if (data.success) {
                    $.toast('绑定成功！');
                    if (usertype == 1) {
                        //若用户在前端展示系统页面则自动退回前端展示系统首页
                        window.location.href = '/o2o/frontend/index';
                    } else {
                        window.location.href = '/o2o/shop/shoplist';
                    }
                } else {
                    $.toast('绑定失败！' + data.errMsg);
                    $('#captcha_img').click();
                }
            }
        });
    });
})