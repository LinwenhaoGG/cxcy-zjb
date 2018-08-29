$(function () {
    layui.use('form', function(){
        var form = layui.form;
        var layer = layui.layer;

    });
    //登录
    $("#login").click(function () {
        var userName = $("#uName").val();
        var userPwd = $("#uPassword").val();
        $.ajax({
            url: "/user/login2",
            data: {"userName": userName, "userPwd": userPwd},
            dataType: 'json',
            type: 'post',
            success: function (data) {
                var code = data.code;
                if (code == 0){
                    var msg = data.msg;
                    layer.alert(msg, {icon: 2});
                }else{
                    //查询是否已经审核
                    var state = data.object.state;
                    if (state == 0){
                        var uStyle = data.object.style;
                        window.location.href="/html/validate.html?uStyle="+uStyle;
                    }else if (state == 1) {
                        window.location.href="/html/homePage.html";
                    } else if (state == 2) {
                        window.location.href="/html/waitTip.html";
                    }else if (state == 3) {
                        window.location.href="/html/failTip.html";
                    }
                }

            }

        })
    })


})