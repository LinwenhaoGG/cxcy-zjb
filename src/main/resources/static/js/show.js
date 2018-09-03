/*
* @Author: Administrator
* @Date:   2018-08-30 11:10:42
* @Last Modified by:   Administrator
* @Last Modified time: 2018-09-01 00:50:04
*/
$(document).ready(function() {
	Date.prototype.toLocaleString = function(){
		if((this.getMonth()+1)<10){
			var month = this.getMonth()+1;
			month = "0"+month;
		}else{
			var month = this.getMonth()+1;
		}
		if(this.getDate()<10){
			var date = this.getDate();
			date = "0"+date;
		}else{
			var date = this.getDate();
		}
		if(this.getHours()<10){
			var h = this.getHours();
			h = "0"+h;
		}else{
			var h = this.getHours();
		}
		if(this.getMinutes()<10){
			var m = this.getMinutes();
			m="0"+m;
		}else{
			var m = this.getMinutes();
		}
		if(this.getSeconds()<10){
			var s = this.getSeconds();
			s="0"+s;
		}else{
			var s = this.getSeconds();
		}
		return this.getFullYear()+'-'+month+"-"+date+" "+
		h+":"+m+":"+s;
	};

    loadComments();


	// 点赞
	$("#praise_container").on('click', '.heart', function() {
		var id = $(this).attr("id");
		var Split = id.split("like");
		var messageID = Split[1];
		var num = parseInt($("#likeCount"+messageID).html());
		var str = $(this).attr("rel");

		if(str == "like"){
			// $("#likeCount"+messageID).html(num+1);
			$(this).addClass('heartAnimation').attr("rel","unlike");
		}
		else{
			// $("#likeCount"+messageID).html(num-1);
			$(this).removeClass('heartAnimation').attr("rel","like");
			$(this).css("background-positon","left");
		}
		// console.log();
		var url2 = '/production/addOrRemoveVote/'+$(".work_data").attr("value");
		// console.log(url);
        $.ajax({
            url:url2,
            type:'get',
            datatype:'json',
            success:function(){
                countVotesAndComments();
            },
            error:function(err){
                console.log(err)
            }

        });


	});
	// 发布评论
	$(".post_comment").on('click', '.comment_summit', function() {
        var comment = $(".comment_box").val();
		var url3 = '/comment/createComment/'+$(".work_data").attr("value");
		console.info(url3);
        $(".comment_box").val("");

        $.ajax({
            url:url3,
			data:{
                "comment":comment
			},
            type:'post',
            success:function(res){
                countVotesAndComments();
                loadComments();
            },
            error:function(err){
                console.log(err)
            }

        });
	});

    // 删除评论
    $(".comments_list").on('click','.delete-btn', function() {
		var id = $(this).parent().parent().attr("id");
        var url3 = '/comment/deleteComment?pId='+$(".work_data").attr("value")+'&cId='+id;
        console.info(url3);
        $.ajax({
            url:url3,
            type:'get',
            success:function(){
            	alert("删除成功");
                countVotesAndComments();
                loadComments();
            },
            error:function(err){
                alert("删除失败");
                console.log(err)
            }

        })


    });

});

function loadComments(){
    $.ajax({

        url:"/comment/listAllComments?pId="+$(".work_data").attr("value"),
        type:'get',
        success:function(res){
            var data = res.data;
            console.log(data);
            var floorNum = 0;
            $(".comments_list").empty();
            $(data).each(function() {
                var u_name = Object.keys(this.map)[0];
                var object = this.map[u_name];
                var Createtime = new Date(object.createTime);
                Createtime = Createtime.toLocaleString();
                floorNum++;

                if(this.flag){
                    var newDom = '<li id="'+object.id+'"><p><span class="floor">#<span class="floor_num">'+floorNum+'</span>楼</span><span class="u_name">'+
                        u_name+'</span></p><p class="details_comment">'+object.content+'</p><p class="comment_time">'+Createtime+'<button class="btn btn-primary  delete-btn">删除</button></p></li>'
                    $(".comments_list").append(newDom);
                }
                else{
                    var newDom = '<li id="'+object.id+'"><p><span class="floor">#<span class="floor_num">'+floorNum+'</span>楼</span><span class="u_name">'+
                        u_name+'</span></p><p class="details_comment">'+object.content+'</p><p class="comment_time">'+Createtime+'</p></li>'
                    $(".comments_list").append(newDom);
                }
            });
        },
        error:function(err){
            console.log(err);
        }

    });
}

function countVotesAndComments() {
	// $("#voteSize").empty();
    $.ajax({

        url:"/production/countVoteAndComment/"+$(".work_data").attr("value"),
        type:'get',
        success:function(res){
            $("#voteSize").html(res.data.voteSize);
            $("#commentSize").html(res.data.commentSize);
        },
        error:function(err){
            console.log(err);
        }

    });
}
function returnHtml(url) {
    window.location.href="/production/center/user"+url;
}