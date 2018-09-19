$(function () {
    //数据总条数
    var totalPage = 0;
    //每页条数
    // var limitNum = 2;
    companyIdentification(1);
    pass();
    refuse();
    page();

})
//分页
function page() {
    $("#demo2").css("text-align","center");
    layui.use(['laypage', 'layer'], function() {
				var laypage = layui.laypage,
					layer = layui.layer;
				laypage.render({
					elem: 'demo2',
					count: totalPage,
					limit: 2,
					theme: '#1E9FFF',
                    jump: function(obj, first){
                    //obj包含了当前分页的所有参数，比如：
                    // console.log(obj.curr); //得到当前页，以便向服务端请求对应页的数据。
                    // console.log(obj.limit); //得到每页显示的条数

                    //首次不执行
                    if(!first){
                        // 查询对应页的数据
                        $("#content").html("");
                        companyIdentification(obj.curr);
                        // page();
                    }
                  }
				});
	});
}
//查询未认证企业列表
function companyIdentification(data) {
    var page = data;
    var size = 2;
    $.ajax({
        url:"/admins/companyIdentification",
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
                console.log(arr)
                var name = arr.name;//企业公司名称
                var boss = arr.boss;//企业法定代表人
                var contacts = arr.contacts; // 联系人
                var phone = arr.phone; //联系电话
                var number = arr.number; //企业社会代码
                var username = arr.username; //账号名
                var email = arr.email; //企业(公司)邮箱
                var state = '待认证'; //账号状态
                var avatar = arr.avatar;//头像
                var id = arr.id; //id
                var content = "<div class='panel panel-primary mb-30'>     <div class='studentId' style='display: none;'>"+id+"</div>    <div class='panel-header'><span class='mr-30'>企业公司名称："+name+"</span><span class='mr-30'>企业法定代表人："+boss+"</span>"+
"<span class='mr-30'>联系人："+contacts+"</span> <span class='mr-30'>联系电话："+phone+"</span><span class='mr-30'>企业社会代码："+number+"</span>"+
            "</div><div class='panel-body clearfix'><div class='head f-l'><img class='avatar size-XXXL radius' src='/img/"+avatar+"'></div><div class='f-l'>"+
                    "<p class='f-l mr-40'>账号名: "+username+"</p><p class='f-l mr-40'>企业(公司)邮箱: "+email+"</p><p class='f-l mr-40'>认证状态: "+state+"</p><input class='pass btn btn-success radius f-r ml-30' type='button' value='通过认证'><input class='refuse btn btn-default radius f-r' type='button'  value='拒绝通过'>"+
                     "</div></div></div>";

                $("#content").append(content);

            }

        }

    })
}

//拒绝学生认证
function refuse() {
    $("#content").on("click",".refuse", function() {
        // $(this)为按钮那个位置
        // var id = $(this).parent().parent().parent().find(".studentId").html();
        var id = $(this).parent().parent().siblings(".studentId").html();
        $.ajax({
            url:"/admins/rejectPass",
            data:{"id":id},
            dataType:"json",
            type:"post",
            success:function (data) {
                if (data.code == 1){
                    alert(data.msg);
                    //刷新页面
                    $("#content").html("");
                    companyIdentification();
                    page();
                }else{
                    alert(data.msg);
                }
            }
        })

    })

}
//通过学生认证
function pass() {
    $("#content").on("click",".pass", function() {
        // $(this)为按钮那个位置
        // var id = $(this).parent().parent().parent().find(".studentId").html();
        var id = $(this).parent().parent().siblings(".studentId").html();
        $.ajax({
            url:"/admins/pass",
            data:{"id":id},
            dataType:"json",
            type:"post",
            success:function (data) {
                if (data.code == 1){
                    alert(data.msg);
                    //刷新页面
                    $("#content").html("");
                    companyIdentification();
                    page();
                }else{
                    alert(data.msg);
                }
            }
        })
    })
}
