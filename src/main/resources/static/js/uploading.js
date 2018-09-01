/*
* @Author: Administrator
* @Date:   2018-08-29 21:48:31
* @Last Modified by:   Administrator
* @Last Modified time: 2018-08-31 12:16:23
*/
$(document).ready(function() {

	// 获取视频绝对路径
	function getFileURL(file){
		var getUrl = null;
		if(window.createObjectURL!=undefined){
			getUrl = window.createObjectURL(file);
		}
		else if(window.URL!=undefined){
			getUrl = window.URL.createObjectURL(file);
		}
		else if(window.webkitURL!=undefined){
			getUrl=window.webkitURL.createObjectURL(file);
		}
		return getUrl;
	}
    var selectVideo = function(){
		var zore = 0;
        var url = getFileURL($(this).files[zore]);
        if(url){
            $(".videoBox source").src = url;
        }
    }
	// 判断上传文件的后缀名是否符合要求
	function fileSuffix(fileName){
		// 获取后缀
		var suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        if(suffix=="doc"||suffix=="docx"){
        	return true;
        }else{
        	return false;
        }
	}



	// 限制简介字数
	$("#note").keyup(function() {
		var len = $(this).val().length;
		$("#text-count").html(len);

	});

	// 选择类型
	/*// 方向
	$(".dropdown1").on('click', '.lis', function() {
		
		var val = $(this).text();

		$(".typeselect1").text(val);

	});*/

	// 分类
	$(".dropdown2").on('click', '.lis', function() {
		// id
		var cid = $(this).val();
		// 选项值(
		var val = $(this).text();

		$(".typeselect2").text(val);
		$(".typeselect2").attr({
			"value": cid
		});
	});

	// 类别
	$(".dropdown3").on('click', '.lis', function() {
		//type
		var type = $(this).val();
		// 选项值
		var val = $(this).text();

		$(".typeselect3").text(val);
		$(".typeselect3").attr({
			"value":type
		});
	});
		
	// 显示选择的文件不带路径的文件名
	$(".file_choose input").change(function() {
		$(".details-warning").hide();
		
		// 获取文件名并显示
		var file_url = $(this).val().split("\\");
		let s = fileSuffix(file_url[file_url.length-1]);
		if(s){
			$(".selected").show();
			$(".file_name").text(file_url[file_url.length-1]);
		}
		else{
			$(".details-warning").show();
		}
		
	});

    // 预览视频
    $(".video_choose input").change(function(){
        // 显示视频框
        $(".videoBox").show();
        // 给路径
        var url = getFileURL(this.files[0]);
        if(url){
            $("video").attr({
                "src": url
            });
        }
        // selectVideo();
    });

	// 关闭提示
	$(".desc").on('click', '.close-desc-warning', function(){
		$(".desc-warning").hide();
	});

	$(".desc textarea").focus(function() {
		$(".desc-warning").hide();
	});

	$(".file_intro").on('click', '.close-details-warning', function() {
		$(".details-warning").hide();
	});

	//检查并提交
	$(".page").on('click', '.summit_btn', function() {
		// 获取分类id
		var cid = $(".typeselect2").attr("value");
		// 获取类别id
		var sort = $(".typeselect3").attr("value");
		// 获取标题
		var title = $(".word_title").val();
		// 获取作品简介
		var desc = $(".desc textarea").val();
		// 获取文件名
		var file_url = $(".file_choose input").val();
		// 后缀名是否正确
		let file_suffix=$(".file_choose input").val().split("\\");
		let ss = fileSuffix(file_suffix[file_suffix.length-1]);
		ss=!ss;

		// 获取文件
		var wf = document.getElementById("wordFile").files[0];

		// 获取视频
		var vf = document.getElementById("videoFile").files[0];

		
			let fd = new FormData($('#myForm')[0]);
			fd.append('Catagorys',cid);
			fd.append('pSort',sort);
			fd.append('pTitle',title);
			fd.append('pSummary',desc);
			fd.append('pContent',wf);
			fd.append('videoFile',vf);
			console.log(fd);
	
		// 判断标题是否为空
		if(title==""){
			// 显示标题不能为空
			$(".word_title").attr({
				placeholder: '作品标题不能为空'
			
			});
			// 判断作品简介是否为空
			if(desc==""){
				$(".desc-warning").show();
			}
			if(ss){
				$(".details-warning").show();
			}
			// 判断是否有上传介绍文件
			if(file_url==""){
				$(".details-warning").show();
			}
		}
		else if(desc==""){
			$(".desc-warning").show();
			// 判断是否有上传介绍文件
			if(file_url==""){
				$(".details-warning").show();
			}
			if(ss){
				$(".details-warning").show();
			}
		}
		else if(ss){
			$(".details-warning").show();
			if(file_url==""){
				$(".details-warning").show();
			}
		}
		else if(file_url==""){
			$(".details-warning").show();
		}
		else{
			
			$.ajax({
				url:'/production/zpr/saveProduction',
				type:'post',
				datatype:'json',
				data:fd,
				cache:false,
				traditional:true,
				contentType:false,
				processData:false,
				success:function(){
					alert("上传成功")
				},
				error:function(err){
					console.log(err)
				}

			})

		}

	});
	

});