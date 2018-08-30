$(function(){
	var layer;
	layui.use(['layer', 'form', 'element'], function(){
			  layer = layui.layer
			  ,form = layui.form
			  ,element = layui.element 
	});
    //账号不能为空
    $("#uName").blur(function(){
        var value = $("#uName").val();
        if(value.trim()==""){
            $("#uName1").css("display","block");
            $("#uName11").html("账号不能为空");
            $("#uName2").css("display","none");
        }else{
            $("#uName1").css("display","none");
            $("#uName11").html("");
            $("#uName2").css("display","block");
        }
        $.ajax({
            url: "/user/key",
            data:{"uName":value},
            dataType: 'json',
            type: 'get',
            success:function (data) {
                var code = data.code;
                if (code == 1){
                    $("#uName1").css("display","none");
                    $("#uName11").html("");
                    $("#uName2").css("display","block");
                }else{
                    $("#uName1").css("display","block");
                    $("#uName11").html(data.msg);
                    $("#uName2").css("display","none");
                }
            }

        })

    })

    //密码长度不能少于6位
    $("#uPassword").blur(function(){
        var pwd = $("#uPassword").val();
        if(pwd.length < 6){
//			layer.msg('密码长度不能少于6位', {icon: 5});
            $("#pwd1").css("display","block");
            $("#pwd11").html("密码长度不能少于6位");
            $("#pwd2").css("display","none");
        }else{
            $("#pwd1").css("display","none");
            $("#pwd11").html("");
            $("#pwd2").css("display","block");

        }
    })
//重复密码要相同
    $("#repwd").blur(function(){
        var repwd = $("#repwd").val();
        var pwd = $("#uPassword").val();
        if(repwd != pwd){
            /*layer.msg('本次密码与上次输入密码不一致', {icon: 5}); */
            $("#repwd1").css("display","block");
            $("#repwd11").html("本次密码与上次输入密码不一致");
            $("#repwd2").css("display","none");
        } else{
            $("#repwd1").css("display","none");
            $("#repwd11").html("");
            $("#repwd2").css("display","block");
        }
    })
    //真实姓名不能为空
     $("#name").blur(function(){
        var value = $("#name").val();
        if(value.trim()==""){
            $("#name1").css("display","block");
            $("#name11").html("真实姓名不能为空");
            $("#name2").css("display","none");
        }else{
            $("#name1").css("display","none");
            $("#name11").html("");
            $("#name2").css("display","block");
        }
     })

    //点击获取验证码
    $("#codeBtn").click(function(){
        var telephone = $("#uTelephone").val();
        if(telephone.length != 11){
            $("#uTelephone1").css("display","block");
            $("#uTelephone11").html("你的手机号码输入有误！请重新输入");
        }else{
            $("#uTelephone1").css("display","none");
            $("#codeBtn").css("pointer-events","none");
            $("#codeBtn").addClass("layui-btn-disabled");
            var timer = window.setInterval(decreaseValue,1000);

            var num = 60;
            function decreaseValue(){
                $("#codeBtn").html(num+"秒后再次点击");

                if(num ==0){
                    $("#codeBtn").removeClass("layui-btn-disabled");
                    $("#codeBtn").css("pointer-events","all");
                    $("#codeBtn").html("获取验证码");
                    window.clearInterval(timer);

                }
                num--;
            }

            $.ajax({
                url:'/user/sendSms',
                data: {"telephone":telephone },
                dataType:'json',
                /*contentType: "application/json",*//*contentType: "application/json",*/
                type:'post',
                success:function(data){
                }

            })

        }
    })

    $("#uTelephone").blur(function(){
        var telephone = $("#uTelephone").val();
        if(telephone.length != 11){
            $("#uTelephone1").css("display","block");
            $("#uTelephone11").html("你的手机号码输入有误！请重新输入");
        }else{
            $("#uTelephone1").css("display","none");
        }
    })
    //提交表单
    $("#submitForm").click(function () {
        var uName = $("#uName").val();
        var uPassword = $("#uPassword").val();
        var uTelephone = $("#uTelephone").val();
        var uStyle = $("select option:selected").val();
        var code = $("#code").val();
        var name = $("#name").val();
        async: false,
        $.ajax({
            url:"/user/register",
            data:{"username":uName,"password":uPassword,"telephone":uTelephone,"style":uStyle,"code":code,"name":name},
            dataType:'json',
            type:'post',
            success:function (data) {
                var status =data.code;
                if (status == 1){
                    layer.alert('恭喜你，注册成功！', {icon: 1});
                    window.setTimeout(jumpMethod,2000);
                    function jumpMethod() {
                        window.location.href="/html/index.html";
                    }
                }else{
                    var msg = data.msg;
                    layer.alert(msg, {icon: 2});
                }
            }
        });


    })


})



