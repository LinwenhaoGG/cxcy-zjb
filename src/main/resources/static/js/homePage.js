$(function () {
    /*//获取id
    var address = window.location.href;
    var index = address.indexOf("=");
    var id = address.substring(index+1, address.length); */
    $.ajax({
        url: '/user/personMessage',
        dataType:'json',
        type:'post',
        success:function (data) {
           $("#message").html(data.msg);
        }

    });



})