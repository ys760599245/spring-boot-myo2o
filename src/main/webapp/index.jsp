<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
</head>
<body>
<h2>Hello World!</h2>
<div class="form-group">
    <label for="j_captcha" class="t">验证码：</label> <input id="j_captcha"
                                                         name="j_captcha" type="text" class="form-control x164 in"/>
    <img
            id="captcha_img" alt="点击更换" title="点击更换"
            onclick="changeVerifyCode(this)" src="Kaptcha" class="m"/>
    <button onclick="kaihuo()">click me</button>
</div>
<script type="text/javascript"
        src="resources/js/shop/base/jquery-1.11.2.min.js"></script>
<script type="text/javascript">
    function changeVerifyCode(img) {
        img.src = "Kaptcha?" + Math.floor(Math.random() * 100);
    }

    function kaihuo() {
        var a = "bb";
        $.ajax({
            url: "shop/test",
            type: "get",
            dataType: 'json',
            success: function (data) {
                alert(data);
            }
        });
    }
</script>
</body>
</html>
