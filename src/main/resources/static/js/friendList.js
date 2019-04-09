/*页面加载完成*/
$(function () {
    var userId;

    $.ajax({
        type:'post',
        url:'/user/person',
        dataType:'json',
        async: false,
        data:{},
        success:function (data) {
            userId = data.id;
        }
    })

    $.ajax({
        type:'post',
        url:'/user/message',
        dataType:'json',
        async: false,
        data:{"userId":userId},
        success:function (data) {
            var totalRedDot = 0 ;
            var arr = data.object;
            $(arr).each(function () {
                var per = $(this)[0];
                var img = "/img/"+per.userImage;
                $("#showmessage").append("<span class=\"innerspan\"><a href=\"/user/inchat?userId="+per.userId+"\"><img src=\""+img+"\" class=\"imgClass\"><span>"+per.userName+"</span>    未读消息:<span  style=\"color: red;\">"+per.messageNum+"</span> </a></span>");
                totalRedDot = totalRedDot+per.messageNum;
            });
            // $("#totalRedDot").html(totalRedDot);
            $(".noread").html(totalRedDot);

        }
    })

})
