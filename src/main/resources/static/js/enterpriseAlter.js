$(function () {
    //头像
    var img1 = "";
    //营业执照
    var img = "";
    //上传图片
    uploadImg();
    //更新用户信息
    updateStudent();

})

//更新用户信息
function updateStudent() {
    $("#submit").click(function () {
        var location = window.location.href;
        var index = location.lastIndexOf("=");
        var id = location.substring(index + 1, location.length);
        var avatar = img1;
        var license = img;
        var companyName = $("#companyName").val();
        var email = $("#email").val();
        var number = $("#number").val();
        var boss = $("#boss").val();
        var type = $("#type").val();
        var contacts = $("#contacts").val();
        var phone = $("#phone").val();
        $.ajax({
            url: "/admins/updateCompany",
            type: "post",
            dataType: "json",
            data: {
                "avatar": avatar,
                "license": license,
                "companyName": companyName,
                "email": email,
                "number": number,
                "boss": boss,
                "type": type,
                "contacts": contacts,
                "phone":phone,
                "id": id,
                "state": 1
            },
            success: function (data) {
                alert(data.msg);
            }
        })
    });
}

//上传图片
function uploadImg() {
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
                console.log(data)
                if (code == 1) {
                    $("#imgId").prop("src", "/img/" + data.msg);
                    img = data.msg;
                } else {
                    layer.alert(data.msg, {icon: 2});
                }
            },
            error: function (data) {
                layer.alert('图片太大啦，不能超过2M', {icon: 2});
            }
        });


    });

    $("#file1").change(function () {
        var form = new FormData($("#imageUpload1")[0]);
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
                console.log(data)
                if (code == 1) {
                    $("#imgId1").prop("src", "/img/" + data.msg);
                    img1 = data.msg;
                } else {
                    layer.alert(data.msg, {icon: 2});
                }
            },
            error: function (data) {
                layer.alert('图片太大啦，不能超过2M', {icon: 2});
            }
        });


    });

}

