$(function () {
    //从Url里面获取Awardid 参数的的值
    var awardId = getQueryString('awardId');
    var shopId = 1;
    //通过awardid获取奖品信息的url
    var infoUrl = '/o2o/shop/getawardbyid?awardId=' + awardId;
    //更新奖品的url
    var awardPostUrl = '/o2o/shop/modifyaward';
    //由于奖品添加和编辑都是同一个页面 该标识符用来标明本次是添加还是编辑
    var isEdit = false;
    if (awardId) {
        // 若有awardid 则为编辑操作
        getInfo(awardId);
        isEdit = true;
    } else {
        awardPostUrl = '/o2o/shop/addaward';
    }

    $("#pass-date").calendar({
        value: ['2017-12-31']
    });


    //  获取需要编辑的奖品信息 并且赋值给表单
    function getInfo(id) {
        $.getJSON(infoUrl, function (data) {
            if (data.success) {
                //从返回的json 获取award对象的信息
                var award = data.award;
                $('#award-name').val(award.awardName);
                $('#priority').val(award.priority);
                $('#award-desc').val(award.awardDesc);
                $('#point').val(award.point);
            }
        });
    }

    $('#submit').click(function () {
        var award = {};
        award.awardName = $('#award-name').val();
        award.priority = $('#priority').val();
        award.awardDesc = $('#award-desc').val();
        award.point = $('#point').val();
        award.awardId = awardId ? awardId : '';
        award.expireTime = $('#pass-date').val();
        console.log(award.expireTime);
        var thumbnail = $('#small-img')[0].files[0];
        var formData = new FormData();
        formData.append('thumbnail', thumbnail);
        formData.append('awardStr', JSON.stringify(award));
        var verifyCodeActual = $('#j_captcha').val();
        if (!verifyCodeActual) {
            $.toast('请输入验证码！');
            return;
        }
        formData.append("verifyCodeActual", verifyCodeActual);
        $.ajax({
            url: awardPostUrl,
            type: 'POST',
            data: formData,
            contentType: false,
            processData: false,
            cache: false,
            success: function (data) {
                if (data.success) {
                    $.toast('提交成功！');
                    $('#captcha_img').click();
                } else {
                    $.toast('提交失败！');
                    $('#captcha_img').click();
                }
            }
        });
    });

});