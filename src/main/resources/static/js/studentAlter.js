$(function () {
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
        var avatar = img;
        var name = $("#name").val();
        var email = $("#email").val();
        var telephone = $("#telephone").val();
        var number = $("#number").val();
        var edu = $("#edu").val();
        var college = $("#college").val();
        var classes = $("#classes").val();
        var nation = $("#nation").val();
        var politicsstatus = $("#politicsstatus").val();
        var credential = $("#credential").val();
        $.ajax({
            url: "/admins/updateStudent",
            type: "post",
            dataType: "json",
            data: {
                "avatar": avatar,
                "name": name,
                "email": email,
                "telephone": telephone,
                "number": number,
                "edu": edu,
                "college": college,
                "classes": classes,
                "nation": nation,
                "politicsstatus": politicsstatus,
                "credential": credential,
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

}

