$(function () {
    var layer;
    layui.use(['layer', 'form', 'element', 'laydate'], function () {
        layer = layui.layer,
            form = layui.form,
            element = layui.element,
            laydate = layui.laydate;

        laydate.render({
            elem: '#edu', //指定元素
            type: 'year',
            format: 'yyyy年',
            value:  (new Date()).getFullYear()+"年",
            isInitValue: true,
            btns: [ 'confirm'],
            max: (new Date()).valueOf()
        });


    });
    displayDiv();
    studentSubmit();
    teacherSubmit();
    companyTeacher();
    imageUploda();
    adminSubmit();
    // 56个民族数据
    var nations = [
        {id: 1, name: '汉族'},
        {id: 2, name: '蒙古族'},
        {id: 3, name: '回族'},
        {id: 4, name: '藏族'},
        {id: 5, name: '维吾尔族'},
        {id: 6, name: '苗族'},
        {id: 7, name: '彝族'},
        {id: 8, name: '壮族'},
        {id: 9, name: '布依族'},
        {id: 10, name: '朝鲜族'},
        {id: 11, name: '满族'},
        {id: 12, name: '侗族'},
        {id: 13, name: '瑶族'},
        {id: 14, name: '白族'},
        {id: 15, name: '土家族'},
        {id: 16, name: '哈尼族'},
        {id: 17, name: '哈萨克族'},
        {id: 18, name: '傣族'},
        {id: 19, name: '黎族'},
        {id: 20, name: '傈僳族'},
        {id: 21, name: '佤族'},
        {id: 22, name: '畲族'},
        {id: 23, name: '高山族'},
        {id: 24, name: '拉祜族'},
        {id: 25, name: '水族'},
        {id: 26, name: '东乡族'},
        {id: 27, name: '纳西族'},
        {id: 28, name: '景颇族'},
        {id: 29, name: '柯尔克孜族'},
        {id: 30, name: '土族'},
        {id: 31, name: '达翰尔族'},
        {id: 32, name: '么佬族'},
        {id: 33, name: '羌族'},
        {id: 34, name: '布朗族'},
        {id: 35, name: '撒拉族'},
        {id: 36, name: '毛南族'},
        {id: 37, name: '仡佬族'},
        {id: 38, name: '锡伯族'},
        {id: 39, name: '阿昌族'},
        {id: 40, name: '普米族'},
        {id: 41, name: '塔吉克族'},
        {id: 42, name: '怒族'},
        {id: 43, name: '乌孜别克族'},
        {id: 44, name: '俄罗斯族'},
        {id: 45, name: '鄂温克族'},
        {id: 46, name: '德昂族'},
        {id: 47, name: '保安族'},
        {id: 48, name: '裕固族'},
        {id: 49, name: '京族'},
        {id: 50, name: '塔塔尔族'},
        {id: 51, name: '独龙族'},
        {id: 52, name: '鄂伦春族'},
        {id: 53, name: '赫哲族'},
        {id: 54, name: '门巴族'},
        {id: 55, name: '珞巴族'},
        {id: 56, name: '基诺族'},

    ];

    /*学生*/

    //动态添加民族
    for (var i = 0; i < nations.length; i++) {
        (function (i) {
            $("select[name='nation']").append("<option value=" + nations[i].name + ">" + nations[i].name + "<option>");
        })(i)
    }
    ;

    //学号不能为空
    $("#number").bind({
        blur:function(){
            if ($("#number").val() == "") {
                $("#number1").show();
                $("#number2").hide();
            } else {
                $("#number2").show();
                $("#number1").hide();
            }
        },
        input:function(){
            if ($("#number").val() == "") {
                $("#number1").show();
                $("#number2").hide();
            } else {
                $("#number2").show();
                $("#number1").hide();
            }
        }
    })
    //姓名不能为空
    $("#name").bind({
        blur:function(){
            if ($("#name").val() == "") {
                $("#name1").show();
                $("#name2").hide();
            } else {
                $("#name2").show();
                $("#name1").hide();
            }
        },
        input:function(){
            if ($("#name").val() == "") {
                $("#name1").show();
                $("#name2").hide();
            } else {
                $("#name2").show();
                $("#name1").hide();
            }
        }
    })


    //专业不能为空
    $("#classes").bind({
        blur:function(){
            if ($("#classes").val() == "") {
                $("#class1").show();
                $("#class2").hide();
            } else {
                $("#class2").show();
                $("#class1").hide();
            }
        },
        input:function(){
            if ($("#classes").val() == "") {
                $("#class1").show();
                $("#class2").hide();
            } else {
                $("#calss2").show();
                $("#class1").hide();
            }
        }
    })



    //简介不能为空
    $("#credential").bind({
        blur:function(){
            if ($("#credential").val() == "") {
                $("#cred1").show();
                $("#cred2").hide();
            } else {
                $("#cred2").show();
                $("#cred1").hide();
            }
        },
        input:function(){
            if ($("#credential").val() == "") {
                $("#cred1").show();
                $("#cred2").hide();
            } else {
                $("#cred2").show();
                $("#cred1").hide();
            }
        }
    })
    //重置
    $("#resetBtn").click(function(){
        $("#number1").hide();
        $("#number2").hide();

        $("#name1").hide();
        $("#name2").hide();

        $("#class1").hide();
        $("#class2").hide();

        $("#cred1").hide();
        $("#cred2").hide();
    });
    /*教师*/

//教师工号不能为空
    $("#Tnumber").bind({
        blur:function(){
            if($("#Tnumber").val()==""){
                $("#Tnumber1").show();
                $("#Tnumber2").hide();
            }else{
                $("#Tnumber2").show();
                $("#Tnumber1").hide();
            }
        },
        input:function(){
            if($("#Tnumber").val()==""){
                $("#Tnumber1").show();
                $("#Tnumber2").hide();
            }else{
                $("#Tnumber2").show();
                $("#Tnumber1").hide();
            }
        }
    });

//教师名字不能为空
    $("#Tname").bind({
        blur:function(){
            if($("#Tname").val()==""){
                $("#Tname1").show();
                $("#Tname2").hide();
            }else{
                $("#Tname2").show();
                $("#Tname1").hide();
            }
        },
        input:function(){
            if($("#Tname").val()==""){
                $("#Tname1").show();
                $("#Tname2").hide();
            }else{
                $("#Tname2").show();
                $("#Tname1").hide();
            }
        }
    });

//在职学校不能为空
    $("#Tschool").bind({
        blur:function(){
            if($("#Tschool").val()==""){
                $("#Tschool1").show();
                $("#Tschool2").hide();
            }else{
                $("#Tschool2").show();
                $("#Tschool1").hide();
            }
        },
        input:function(){
            if($("#Tschool").val()==""){
                $("#Tschool1").show();
                $("#Tschool2").hide();
            }else{
                $("#Tschool2").show();
                $("#Tschool1").hide();
            }
        }
    });

//教师学院不能为空
    $("#Tcollege").bind({
        blur:function(){
            if ($("#Tcollege").val() == "") {
                $("#Tcollege1").show();
                $("#Tcollege2").hide();
            } else {
                $("#Tcollege2").show();
                $("#Tcollege1").hide();
            }
        },
        input:function(){
            if ($("#Tcollege").val() == "") {
                $("#Tcollege1").show();
                $("#Tcollege2").hide();
            } else {
                $("#Tcollege2").show();
                $("#Tcollege1").hide();
            }
        }
    });

//教师专业领域不能为空
    $("#Tspecially").bind({
        blur:function(){
            if($("#Tspecially").val()==""){
                $("#Tspecial1").show();
                $("#Tspecial2").hide();
            }else{
                $("#Tspecial2").show();
                $("#Tspecial1").hide();
            }
        },
        input:function(){
            if($("#Tspecially").val()==""){
                $("#Tspecial1").show();
                $("#Tspecial2").hide();
            }else{
                $("#Tspecial2").show();
                $("#Tspecial1").hide();
            }
        }
    });

//职业不能为空
    $("#Tposition").bind({
        blur:function(){
            if($("#Tposition").val()==""){
                $("#Tpos1").show();
                $("#Tpos2").hide();
            }else{
                $("#Tpos2").show();
                $("#Tpos1").hide();
            }
        },
        input:function(){
            if($("#Tposition").val()==""){
                $("#Tpos1").show();
                $("#Tpos2").hide();
            }else{
                $("#Tpos2").show();
                $("#Tpos1").hide();
            }
        }
    });
    //教师简介不能为空
    $("#Tcredential").bind({
        blur:function(){
            if ($("#Tcredential").val() == "") {
                $("#Tcred1").show();
                $("#Tcred2").hide();
            } else {
                $("#cred2").show();
                $("#cred1").hide();
            }
        },
        input:function(){
            if ($("#Tcredential").val() == "") {
                $("#Tcred1").show();
                $("#Tcred2").hide();
            } else {
                $("#Tcred2").show();
                $("#Tcred1").hide();
            }
        }
    });
    //教师重置
    $("#TresetBtn").click(function(){
        $("#Tnumber1").hide();
        $("#Tnumber2").hide();

        $("#Tname1").hide();
        $("#Tname2").hide();

        $("#Tcred1").hide();
        $("#Tcred2").hide();

        $("#Tpos1").hide();
        $("#Tpos2").hide();

        $("#Tspecial1").hide();
        $("#Tspecial2").hide();

        $("#Tcollege1").hide();
        $("#Tcollege2").hide();

        $("#Tschool1").hide();
        $("#Tschool2").hide();
    });

/*企业*/
    //企业名字不能为空
    $("#companyName").bind({
        blur:function(){
            if( $("#companyName").val()==""){
                $("#cname1").show();
                $("#cname2").hide();
            }else{
                $("#cname2").show();
                $("#cname1").hide();
            }
        },
        input:function(){
            if( $("#companyName").val()==""){
                $("#cname1").show();
                $("#cname2").hide();
            }else{
                $("#cname2").show();
                $("#cname1").hide();
            }
        }
    });

    //企业社会代码
    $("#Cnumber").bind({
        blur:function(){
            if($("#Cnumber").val()==""){
                $("#cnumber1").show();
                $("#cnumber2").hide();
            }else{
                $("#cnumber2").show();
                $("#cnumber1").hide();
            }
        },
        input:function(){
            if($("#Cnumber").val()==""){
                $("#cnumber1").show();
                $("#cnumber2").hide();
            }else{
                $("#cnumber2").show();
                $("#cnumber1").hide();
            }
        }
    });

    //法定人不能为空
    $("#Cboss").bind({
        blur:function(){
            if($("#Cboss").val()==""){
                $("#cboss1").show();
                $("#cboss2").hide();
            }else{
                $("#cboss2").show();
                $("#cboss1").hide();
            }
        },
        input:function () {
            if($("#Cboss").val()==""){
                $("#cboss1").show();
                $("#cboss2").hide();
            }else{
                $("#cboss2").show();
                $("#cboss1").hide();
            }
        }
    });

    //公司类型不能为空
    $("#Ctype").bind({
        blur:function(){
            if($("#Ctype").val()==""){
                $("#ctype1").show();
                $("#ctype2").hide();
            }else{
                $("#ctype2").show();
                $("#ctype1").hide();
            }
        },
        input:function(){
            if($("#Ctype").val()==""){
                $("#ctype1").show();
                $("#ctype2").hide();
            }else{
                $("#ctype2").show();
                $("#ctype1").hide();
            }
        }
    });

    //联系人不能为空
    $("#Ccontacts").bind({
        blur:function(){
            if($("#Ccontacts").val()==""){
                $("#ccontact1").show();
                $("#ccontact2").hide();
            }else{
                $("#ccontact2").show();
                $("#ccontact1").hide();
            }
        },
        input:function () {
            if($("#Ccontacts").val()==""){
                $("#ccontact1").show();
                $("#ccontact2").hide();
            }else{
                $("#ccontact2").show();
                $("#ccontact1").hide();
            }
        }
    });

    //真实姓名
    $("#Cname").bind({
        blur:function(){
            if($("#Cname").val()==""){
              $("#Cname1").show();
              $("#Cname2").hide();
            }else{
                $("#Cname2").show();
                $("#Cname1").hide();
            }
        },
        input:function(){
            if($("#Cname").val()==""){
                $("#Cname1").show();
                $("#Cname2").hide();
            }else{
                $("#Cname2").show();
                $("#Cname1").hide();
            }
        }
    });

    //联系电话不能为空且格式要正确
    $("#Cphone").bind({
        blur:function(){
            //正则
            var reg = /(1[3-9]\d{9}$)/;
            if($("#Cphone").val()==""){
                $("#cphone1").show();
                $("#cphone2").hide();
                $(".phone-tip").html("必填");
            }else if(!reg.test($("#Cphone").val())){
                $("#cphone1").show();
                $("#cphone2").hide();
                $(".phone-tip").html("请输入正确格式的手机号码");
            }else{
                $("#cphone2").show();
                $("#cphone1").hide();
                $(".phone-tip").html("");
            }
        },
        input:function(){
            var reg = /(1[3-9]\d{9}$)/;
            if($("#Cphone").val()==""){
                $("#cphone1").show();
                $("#cphone2").hide();
                $(".phone-tip").html("必填");
            }else if(!reg.test($("#Cphone").val())){
                $("#cphone1").show();
                $("#cphone2").hide();
                $(".phone-tip").html("请输入正确格式的手机号码");
            }else{
                $("#cphone2").show();
                $("#cphone1").hide();
                $(".phone-tip").html("");
            }
        }
    });
    //企业重置
    $("#brestBtn").click(function(){
        $("#cname1").hide();
        $("#cname2").hide();

        $("#cphone2").hide();
        $("#cphone1").hide();
        $(".phone-tip").html("");

        $("#Cname1").hide();
        $("#Cname2").hide();

        $("#ccontact1").hide();
        $("#ccontact2").hide();

        $("#ctype1").hide();
        $("#ctype2").hide();

        $("#cboss1").hide();
        $("#cboss2").hide();

        $("#cnumber1").hide();
        $("#cnumber2").hide();

        $("#cname1").hide();
        $("#cname2").hide();
    })

})


//上传图片
function imageUploda() {
    $("#file").change(function () {
        // var form = new FormData($("#imageUpload")[0]);
        // var form = new FormData($(".upload")[0]);
        var fileInput = document.getElementById("file");
        var form = new FormData();
        form.append("file",fileInput.files[0]);
        console.log(fileInput);
        $.ajax({
            url: '/user/imageUpload',
            data: form,
            type: 'post',
            scriptCharset: 'UTF8',
            processData: false,
            contentType: false,
            async: false,
            success: function (data) {
                console.log(data);
                var code = data.code;
                if (code == 1){
                    $("#imageShow").css("display","block");
                    $("#spanImg").prop("src","/img/"+data.msg);
                }else{
                    $("#imageShow").css("display","none");
                    layer.alert(data.msg, {icon: 2});
                }
            },
            error: function (data) {
                 layer.alert('图片太大啦，不能超过2M', {icon: 2});
            }
        });
    });


}
//管理员提交表单
function adminSubmit() {
    $("#adminSubmit").click(function () {
        var name = $("#Aname").val();
        alert("name为"+name);
        $.ajax({
            url: "/user/saveAdmin",
            data: {
                "name": name,
            },
            dataType: "json",
            type: "post",
            success: function (data) {
                if (data.code == 1) {
                    window.location.href = "/login-success";
                }
            }
        })
    });
}


//企业提交表单
function companyTeacher() {
    $("#companySubmit").click(function () {
        var realName = $("#Cname").val();
        var name = $("#companyName").val();
        var number = $("#Cnumber").val();
        var boss = $("#Cboss").val();
        /*var license = $("#Clicense").val();*/
        var type = $("#Ctype").val();
        var contacts = $("#Ccontacts").val();
        var phone = $("#Cphone").val();
        $.ajax({
            url: "/user/saveCompany",
            data: {
                "realName": realName,
                "name": name,
                "number": number,
                "boss": boss,
                // "license":"license",
                "type": type,
                "contacts": contacts,
                "phone": phone
            },
            dataType: "json",
            type: "post",
            success: function (data) {
                if (data.code == 1) {
                    window.location.href = "/login-success";
                }else{
                    layer.alert(data.msg, {icon: 2});
                }
            }
        })
    });
}

//教师提交表单
function teacherSubmit() {
    $("#teacherSubmit").click(function () {
        var Tnumber = $("#Tnumber").val();
        var Tname = $("#Tname").val();
        var Tschool = $("#Tschool").val();
        var Tcollege = $("#Tcollege").val();
        var Tspecially = $("#Tspecially").val();
        var Tposition = $("#Tposition").val();
        var Tnation = $("#Tnation").val();
        var Tpoliticsstatus = $("#Tpoliticsstatus").val();
        var Tcredential = $("#Tcredential").val();
        $.ajax({
            url: "/user/saveTeacher",
            data: {
                "name": Tname,
                "college": Tcollege,
                "school": Tschool,
                "specially": Tspecially,
                "number": Tnumber,
                "nation": Tnation,
                "position": Tposition,
                "politicsstatus": Tpoliticsstatus,
                "credential": Tcredential
            },
            dataType: "json",
            type: "post",
            success: function (data) {
                if (data.code == 1) {
                    window.location.href = "/login-success";
                }else{
                    var msg = data.msg;
                    layer.alert(msg, {icon: 2});
                }
            }
        })
    });
}

//学生提交表单
function studentSubmit() {
    $("#studentSubmit").click(function () {
        var number = $("#number").val();
        var name = $("#name").val();
        var edu = $("#edu").val();
        var college = $("#college").val();
        var classes = $("#classes").val();
        var nation = $("#nation").val();
        var politicsstatus = $("#politicsstatus").val();
        var credential = $("#credential").val();
        $.ajax({
            url: "/user/saveStudent",
            data: {
                "number": number,
                "name": name,
                "edu": edu,
                "college": college,
                "classes": classes,
                "nation": nation,
                "politicsstatus": politicsstatus,
                "credential": credential
            },
            dataType: "json",
            type: "post",
            success: function (data) {
                if (data.code == 1) {
                    window.location.href = "/login-success";
                }else{
                    var msg = data.msg;
                    layer.alert(msg, {icon: 2});
                }
            }
        })
    });

}

//显示需要填写的信息框
function displayDiv() {
    var uStyle = $("#uStyle").val();
    if (uStyle == 1) {
        $("#student").css("display", "block");
    } else if (uStyle == 2) {
        $("#teacher").css("display", "block");
    } else if (uStyle == 3 ) {
        $("#company").css("display", "block");
    } else if (uStyle == 4 ) {
        $("#admin").css("display", "block");
    }

}

