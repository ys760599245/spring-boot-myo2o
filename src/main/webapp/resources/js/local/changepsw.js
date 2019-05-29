$(function () {
    //修改平台密码的controller url
    var url = '/o2o/local/changelocalpwd';
    //从地址栏的URL里获取usertype
    //usertype=1则为customer,其余为shopowner
    var usertype = getQueryString('usertype');
    $('#submit').click(function () {
        //获取账号
        var username = $('#username').val();
        var password = $('#password').val();
        var newPassword = $('#newpassword').val();
        var confirmPassword = $('#confirmPassword').val();
        if (newPassword != confirmPassword) {
            $.toast('两次输入的新密码不一致');
            return;
        }

        var fromData = new FormData();
        fromData.append('username', username);
        fromData.append('password', password);
        fromData.append('newPassword', newPassword);
        //获取验证码
        var verifyCodeActual = $('#j_captcha').val();
        if (!verifyCodeActual) {
            $.toast('请输入验证码！');
            return;
        }
        fromData.append('verifyCodeActual', verifyCodeActual);

        $.ajax({
            url: url,
            type: 'post',
            data: fromData,
            contentType: false,
            processData: false,
            cache: false,
            success: function (data) {
                if (data.success) {
                    $.toast('提交成功！');
                    if (usertype == 1) {
                        window.location.href = '/o2o/frontend/index';
                    } else {
                        window.location.href = '/o2o/shop/shoplist';
                    }
                } else {
                    $.toast('提交失败!' + data.errMsg);
                    $('#j_captcha').click();
                }
            }
        });
    });

    $('#back').click(function () {
        window.location.href = '/o2o/shop/shoplist';
    });
})