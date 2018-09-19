$(function () {
    //数据总条数
    var totalPage = 0;
    //每页条数
    // var limitNum = 5;
    certifiedAdmin(1);
    page();
    //查询已认证老师
    find();
})
//查询已认证老师,带关键字
 function  find(){
    $("#findId").click(function () {
        var selectState =  $("select option:selected").val();
        var keywork = $("#keywork").val();
        $.ajax({
            url:"/admins/findAdmin",
            data:{
               "selectState":selectState,
                "keywork":keywork
            },
            dataType:"json",
            type:"post",
            success:function (data) {
                console.log(data)
                if (data.code == 0){
                    alert(data.msg);
                }
                var data = data.object;
                totalPage = data[0];
                if (totalPage == 0){
                    alert("无法查询到该用户");
                }
                page();
                //遍历数据
                $("#content").html("");
                for (var i = 1 ; i < data.length ; i++){
                    var arr = data[i];
                var name = arr.name;//管理员姓名
                var avatar = arr.avatar; //头像
                var email = arr.email;//邮箱
                var telephone = arr.telephone; // 手机号码
                var username = arr.username; // 账号名
                var state = '已认证'; //账号状态
                var id = arr.id; //id

                var content = "<tr><td class='id' style='display:none'>"+id+"</td><td>"+name+"</td><td><img src='/img/"+avatar+"' alt='' style='width: 80px;height: 80px;'></td><th>"+email+"</th>"+
                            "<th>"+telephone+"</th><th>"+username+"</th><th>"+state+"</th><th><a href='/admins/toAlterAdmin?id="+id+"' th:href='@{/admins/toAlterAdmin?id="+id+"}'>修改</a></th></tr>";

                $("#content").append(content);
                }

            }

        })


    });

 }

//分页
function page() {
    $("#demo2").css("text-align","center");
    layui.use(['laypage', 'layer'], function() {
				var laypage = layui.laypage,
					layer = layui.layer;
				laypage.render({
					elem: 'demo2',
					count: totalPage,
					limit: 5,
					theme: '#1E9FFF',
                    jump: function(obj, first){
                    //obj包含了当前分页的所有参数，比如：
                    // console.log(obj.curr); //得到当前页，以便向服务端请求对应页的数据。
                    // console.log(obj.limit); //得到每页显示的条数

                    //首次不执行
                    if(!first){
                        // 查询对应页的数据
                        $("#content").html("");
                        certifiedAdmin(obj.curr);
                        // page();
                    }
                  }
				});
	});
}
//查询已认证老师列表，不带关键字
function certifiedAdmin(data) {
    var page = data;
    var size = 5;
    $.ajax({
        url:"/admins/certifiedAdmin",
        data:{
            "page":page,
            "size":size
        },
        type:"post",
        async:false,
        dataType:"json",
        success:function (data) {
            var data = data.object;
            //总页数
            totalPage  = data[0];
            //List集合
            var data = data[1];
            for (var i = 0 ; i < data.length ; i++){
                var arr = data[i];
                var name = arr.name;//管理员姓名
                var avatar = arr.avatar; //头像
                var email = arr.email;//邮箱
                var telephone = arr.telephone; // 手机号码
                var username = arr.username; // 账号名
                var state = '已认证'; //账号状态
                var id = arr.id; //id

                var content = "<tr><td class='id' style='display:none'>"+id+"</td><td>"+name+"</td><td><img src='/img/"+avatar+"' alt='' style='width: 80px;height: 80px;'></td><th>"+email+"</th>"+
                            "<th>"+telephone+"</th><th>"+username+"</th><th>"+state+"</th><th><a href='/admins/toAlterAdmin?id="+id+"' th:href='@{/admins/toAlterAdmin?id="+id+"}'>修改</a></th></tr>";

                $("#content").append(content);

            }

        }

    })
}


