var stompClient = null;
/*页面加载完成*/
$(function () {
    getUserMessage();
    connect();
    UnreceivedInfomation();
    $("#submitComment").click(function () {
        sendMessage();
    });
    updateMessage();
    $("#submitComment").mouseenter(function(){
        $("#submitComment").removeClass("layui-btn-primary")
    });
    $("#submitComment").mouseleave(function(){
        $("#submitComment").addClass("layui-btn-primary")
    });
    $("#messageDiv").click(function(){
        $("#content").focus();
    })
})
//更新信息
function updateMessage() {
    $("#content").click(function () {
        var senderId = $("#senderId").val();
        var receiverId = $("#receiverId").val();

        $.ajax({
            type:'post',
            url:'/user/updateMessage',
            dataType:'json',
            data:{"senderId":senderId,"receiverId":receiverId},
            async:true,
            success:function (data) {
                alert(data[0].msg);
            }
        })
    });
}

//获取未接收的消息
function UnreceivedInfomation() {
    var senderId = $("#senderId").val();
    var receiverId = $("#receiverId").val();

    $.ajax({
        type:'post',
        url:'/user/unreceivedInfo',
        dataType:'json',
        data:{"senderId":senderId,"receiverId":receiverId},
        async:true,
        success:function (data) {
            var image = $("#receiverImage")[0].src;
            $(data.object).each(function () {
                var per = $(this)[0];
                $("#tableId").append("<tr><td style=\"width:1%\"><img src="+image+" style=\"width: 40px;height: 40px;\" class=\"receiver\"/></td><td><span>"+per.content+"</span></td><td></td></tr>");

            })
        }

    })

}

//与基站进行连接
function connect() {
    var socket  = new SockJS("/endpoint-websocket");
    stompClient = Stomp.over(socket);
    stompClient.connect({},function (frame) {
        console.log('Connected:'+frame);
        var senderId = $("#senderId").val();
        stompClient.subscribe('/channel/single/chat/'+senderId,function (result) {
            //显示返回的消息
            showContent(JSON.parse(result.body));
        })
    });
}

//显示内容
function showContent(data) {
    var image = $("#receiverImage")[0].src;
    var content = data.content;
    var receiverId = $("#receiverId").val();
    var senderId = data.senderId;
    if (receiverId == senderId){
        $("#tableId").append("<tr><td style=\"width:1%\"><img src="+image+" style=\"width: 40px;height: 40px;\" class=\"receiver\"/></td><td><span>"+content+"</span></td><td></td></tr>");
    }
     //设置滚动条拉到最下面
    $('#scrollDiv').scrollTop( $('#scrollDiv')[0].scrollHeight );
}

//获取用户信息
function getUserMessage(){
    //获取路径上的用户id
    var id = $("#userId").val();
    //利用ajax去请求该用户的信息
    $.ajax({
        //请求方式
        type:'POST',
        //发送请求的地址
        url:'/user/chat',
        //服务器返回的数据类型
        dataType:'json',
        //发送到服务器的数据，对象必须为key/value的格式，jquery会自动转换为字符串格式
        data:{"userId":id},
        async:false,
        success:function(data){
            //请求成功函数内容
            // console.info(data.msg);
            $("#imgId").prop("src","/img/"+data.object[0].avatar);
            $("b").html(data.object[0].username);
            $("#receiverId").val(data.object[0].id);
            $("#receiverImage").prop("src","/img/"+data.object[0].avatar);

            $("#senderId").val(data.object[1].id);
            $("#senderImage").prop("src","/img/"+data.object[1].avatar);

        },
        error:function(jqXHR){
            //请求失败函数内容
            console.info("出问题了！");
        }
    })
}

//发送信息
function sendMessage(){
    var content = $("#content").val();
    content = $.trim(content);
    if (content != '') {
        content = htmlEncodeJQ(content);
        var senderId = $("#senderId").val();
        var receiverId = $("#receiverId").val();
        var image = $("#senderImage")[0].src;
        $("#tableId").append("<tr><td></td><td style=\"text-align:right\" ><span class='send'>" + content + "</span></td><td style=\"width:1%\"><img src=" + image + " style=\"width: 40px;height: 40px;\" class=\"sender\"/></td></tr>");
        $("#content").val("");
        stompClient.send("/app/user/single/chat", {},
            JSON.stringify({'content': content, 'senderId': senderId, 'receiverId': receiverId}));
        //设置滚动条拉到最下面
        $('#scrollDiv').scrollTop( $('#scrollDiv')[0].scrollHeight );
    }
}

/*回车发送消息*/
$(document).keydown(function(e){
    if(!e) var e = window.event;
    if(e.keyCode==13){
        sendMessage();
    }
});

/*解决js脚本注入*/
function htmlEncodeJQ ( str ) {
    return $('<span/>').text( str ).html();
}



