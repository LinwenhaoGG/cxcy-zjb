$(function () {
    var layer;
    layui.use(['layer', 'form', 'element'], function () {
        layer = layui.layer
            , form = layui.form
            , element = layui.element
    });
    displayDiv();
    studentSubmit();
    teacherSubmit();
    companyTeacher();
    imageUploda();
    adminSubmit();
})

//上传图片
function imageUploda() {
    $("#file").change(function () {
        var form = new FormData($("#imageUpload")[0]);
        $.ajax({
            url: '/user/imageUpload',
            data: form,
            type: 'post',
            scriptCharset: 'UTF8',
            processData: false,
            contentType: false,
            async: false,
            success: function (data) {
                var code = data.code;
                if (code == 1){
                    $("#imageShow").css("display","block");
                    $("#spanImg").prop("src","/img/"+data.msg);
                }else{
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
                /*"license":license,*/
                "type": type,
                "contacts": contacts,
                "phone": phone
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

