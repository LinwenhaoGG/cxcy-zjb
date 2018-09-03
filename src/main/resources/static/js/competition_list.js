$(function () {
    $('.del_compotition').click(function () {
        if (confirm('确定要删除该比赛吗？删除后不可恢复！')) {
            //发送删除请求
            return true;
        }
        return false;
    })
})