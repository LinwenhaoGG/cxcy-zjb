$(function () {
    //点击重新提交
    $("#again").click(function () {
        submitAgain();
    });

})
function submitAgain() {
    $.ajax({
        url: "/user/submitAgain",
        dataType:"json",
        type:"post",
        success:function (data) {
            if (data.code == 1 ){
                var uStyle = data.object;
                window.location.href="/html/validate.html?uStyle="+uStyle;
            }
        }
        
    })
}