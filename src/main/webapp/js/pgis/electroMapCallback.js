//ocx  一键功能回调方法
var frameTime = null;
function OcxCallback(msg) {
		
		//消息
		if(msg){
			var operType = '';
			var arr = msg.split(",");
			var index = arr[1].indexOf(":");
			
			if(index != -1){
				operType = arr[1].substr(0,index);
			}else{
				operType = arr[1];
			}
			switch (operType) {
			case 'CapFile'://抓图
				var path = arr[1].replace("CapFile:", "");
				if(isHistorySream){
					frameTime = arr[3].substr(arr[3].indexOf(":") + 1);
				}else{
					frameTime = $.timeUtils.getDateByTime(new Date());
				}
				if(path){
					path = ftpPath+"/" + path.substr(path.lastIndexOf("/") + 1, path.length);
					$(".onekeymapCon").show();
					$("#img_snapshot").attr({"src": basePath+"rest/getFtpFileStream?path=" + path, "imgUrl": path,'occurTime':frameTime});
					var cameraAdd = arr[2].split(":")[1];
					searchCameraByAddr(cameraAdd,function(data){
						$("#cameraId").val(data.camera.id);
					});
				}
				break;
			case 'OneKeySummery'://摘要
				var devId = arr[2].substr(arr[2].indexOf(":") + 1);
				if(isHistorySream){
					frameTime = arr[3].substr(arr[3].indexOf(":") + 1);
				}
				searchCameraByAddr(devId, function(data){
					var cameraName = data.camera.name;
					if(cameraName && cameraName.length > 15){
						cameraName = cameraName.substring(0,15) + "...";
					}
					$(".selected_camera_point_zy").empty().html('<span class="camera_item" style="margin-right:0px;" id="'+ data.camera.id +'" address="'+ devId +'" cameraname="'+ data.camera.name +'">'+ cameraName +'</span>');
				});
				$(".onekeyZaiyaoCon").show();
				//获取当前时间为摘要终止时间，往前推30分钟为摘要开始时间
				var sd = new Date();
				var endTime = $.timeUtils.getDateByTime(sd);
				sd.setTime(sd.getTime() - 1000 * 60 *30);
				var startTime = $.timeUtils.getDateByTime(sd);
				$("#zystartTime").val(startTime);
				$("#zyendTime").val(endTime);
				break;
			case 'OneKeySearch'://检索
				var devId = arr[2].substr(arr[2].indexOf(":") + 1);
				if(isHistorySream){
					frameTime = arr[3].substr(arr[3].indexOf(":") + 1);
				}
				searchCameraByAddr(devId, function(data){
					var cameraName = data.camera.name;
					if(cameraName && cameraName.length > 15){
						cameraName = cameraName.substring(0,15) + "...";
					}
					$(".selected_camera_point_js").empty().html('<span class="camera_item" style="margin-right:0px;" id="'+ data.camera.id +'" address="'+ devId +'" cameraname="'+ data.camera.name +'">'+ cameraName +'</span>');
				});
				$(".onekeySearchCon").show();
				//获取当前时间为摘要终止时间，往前推30分钟为摘要开始时间
				var sd = new Date();
				var endTime = $.timeUtils.getDateByTime(sd);
				sd.setTime(sd.getTime() - 1000 * 60 *30);
				var startTime = $.timeUtils.getDateByTime(sd);
				$("#jsstartTime").val(startTime);
				$("#jsendTime").val(endTime);
				break;
			/*case 'OneKeySearchHumen'://一键意图搜人
				var devId = arr[3].substr(arr[3].indexOf(":") + 1);
				var path = arr[2].replace("CapFile:", "");
				frameTime = arr[4].substr(arr[4].indexOf(":") + 1);
				if(path){
					path = path.substr(path.lastIndexOf("/") + 1, path.length);
					var picUrl = ftpPath+ "/" + path;
					searchCameraByAddr(devId, function(data){
						var url = basePath+"rest/videoAnalysis/toVideoRelayTrack?moudle=relaytrack&picUrl=" + picUrl + "&cameraId=" + data.camera.id+ "&lon=" + data.camera.longitude+ "&lat=" + data.camera.latitude+"&traceTime=" + frameTime;
						window.open(url);
						
					});
				}
				break;*/
			/*case 'OneKeySearchHumenByPic'://人脸搜索
				var devId = arr[3].substr(arr[3].indexOf(":") + 1);
				var path = arr[2].replace("CapFile:", "");
				$(".onekeyFaceCon").css("display", "");
				 if(path){
					path = path.substr(path.lastIndexOf("/") + 1, path.length);
					var faceUrl = "/ftp/" + path;
					$(".onekeyFaceCon img").attr({"src": basePath + "rest/getFtpFileStream?path=" + faceUrl});
					$('#cutimg').val(faceUrl);
					//window.open("${basePath}rest/face/toFaceSnapshot?faceUrl=" + faceUrl);
					initJcrop();
					//$("#cuttarget").Jcrop();
					//jcrop_api.enable();
				}
				break;
			case 'OneKeySearchCar'://以车搜车
				var devId = arr[3].substr(arr[3].indexOf(":") + 1);
                var path = arr[2].replace("CapFile:", "");
                 if(path){
                    path = path.substr(path.lastIndexOf("/") + 1, path.length);
                    var imgUrl = "/ftp/" + path;
                    window.open(basePath + "rest/car/imageSearch?imgUrl=" + imgUrl);
                }
                break;
			case 'OneKeySearchCarByPic'://特征搜车
                var devId = arr[3].substr(arr[3].indexOf(":") + 1);
                var path = arr[2].replace("CapFile:", "");
                 if(path){
                    path = path.substr(path.lastIndexOf("/") + 1, path.length);
                    var imgUrl = "/ftp/" + path;
                    window.open(basePath + "rest/car/featureSearch?imgUrl=" + imgUrl);
                }
	            break;*/
			default:
				break;
			}
			
		}
    }

function faceSearch(){
	var faceUrl = $('#cutimg').val();
	var featurFollowarea = '';
	var x = $('#x').val();
    var y = $('#y').val();
    var w = $('#w').val();
    var h = $('#h').val();
    if(x){
    	featurFollowarea = x + "," + y + "," + w + "," + h;
    }
	window.open(basePath + "rest/face/toFaceSnapshot?faceUrl=" + faceUrl + "&featurFollowarea=" + featurFollowarea);
}

var jcrop_api;
function initJcrop()
{
	if(jcrop_api){
        jcrop_api.release();
    }
    
    $('#cuttarget').Jcrop({
        onSelect: selectCheck
    },function(){
        jcrop_api = this;
     });
};

function selectCheck(c)
{
  $('#x').val(c.x);
  $('#y').val(c.y);
  $('#w').val(c.w);
  $('#h').val(c.h);
};

function checkCoords()
{
    if (parseInt($('#w').val())>0) return true;
    alert('请在图片上尽行截取！');
    return false;
};

    //根据id查询名称
function searchCameraByAddr(address, callBack){
	$.ajax({
		url:  basePath+'rest/camera/getCameraByAddress',
		data: {
			"address": address
		},
		method: 'POST',
		dataType: 'JSON',
		success: function(data){
			if (data.state) {
				if(callBack){
					callBack(data);
				}
			}
		}
	});
}

//选择上传线索
function uploadclue(type){
	
	var base64Str = '';
	if(canvasFlag){
		base64Str = $("#wPaint").wPaint("image");
	}
	
	if($("._wPaint_menu ._wPaint_line"))$("._wPaint_menu ._wPaint_line").click();//图片标注重置为默认
	//遍历图片到线索
	$('.img_list_content img').each(function(index){
		var imgUrl = $(this).attr('imgUrl');
		var occurTime = $('#img_snapshot').attr('occurTime');
		$.ajax({
			url:  basePath + 'rest/lawCase/saveClue',
			data: {
				"lawcaseId" :'',
				"clueType" : "0",
				"imgUrl" : imgUrl,
				"cameraId" : $("#cameraId").val(),
				"occurTime" : occurTime,
				"base64Str" : base64Str.split(',')[1]
			},
			method: 'POST',
			dataType: 'JSON',
			success: function(data){
				if (data.state) {
					alert('保存成功!');
					closeOneKeyMap();
				}else{
					alert(data.msg);
				}
			}
		});
	});
}

/**
 * 选择案件 type 1 视频分析  2 视频浓缩全快照
 * @returns
 */
function changeCase(type){
	if($("._wPaint_menu ._wPaint_line"))$("._wPaint_menu ._wPaint_line").click();//图片标注重置为默认
	layer.open({
		type : 2,
		title : '案件选择',
		shadeClose : true,
		offset: '10px',
		shade : 0.1,// 遮罩背景效果
		area : [ '1080px', '650px' ],
		shift : 0,// 动画效果 有0~6选
		content : basePath+'rest/cameraMedia/openMycase?fromModule=analysis',
		success : function(layero, index) {
			var body = layer.getChildFrame('body', index);
			var iframeWin = window[layero.find('iframe')[0]['name']]; // 得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
		}
	});
}
//保存线索至当前案件
function saveClue(){
	
	var base64Str = '';
	if(canvasFlag){
		base64Str = $("#wPaint").wPaint("image");
	}
	
	var imgUrl = $("#img_snapshot").attr('imgUrl');
	var cameraId = $("#cameraId").val();
	var occurTime = $('#img_snapshot').attr('occurTime');
	$.ajax({
		url: basePath + 'rest/lawCase/saveClue',
		data: {
			"lawcaseId" : $("#caseId").val(),
			"clueType" : "0",
			"cameraId":cameraId,
			"imgUrl" : imgUrl,
			"occurTime" : occurTime,
			"base64Str" : base64Str.split(',')[1]
		},
		method: 'POST',
		dataType: 'JSON',
		success: function(data){
			$("#cameraId").val('');
			alert('保存成功!');
			closeOneKeyMap();
		}
	});
}
//提交检索联网视频分析
function submitJSAnalysisTask(type){
	var taskName = '检索人车';
	/*if(!$.trim($("#jsTimeRange").val())){
		alert('请输入检索时长');
		return;
	}
	var jsTime = parseInt($("#jsTimeRange").val());
	var  sd = null;
	if(frameTime){
		var frameTimeStr = frameTime.replace(/-/g,'/');
		sd = new Date(frameTimeStr);
	}else{
		sd = new Date();
	}
	sd.setTime(sd.getTime() - 1000 * 60 *jsTime);
	var startTime = $.timeUtils.getDateByTime(sd);
	sd.setTime(sd.getTime() + 1000 * 60 *jsTime);
	var endTime = $.timeUtils.getDateByTime(sd);
	var startNum = parseInt(startTime.replace(/-/g,'').replace(/:/g,'').replace(' ',''));
	var endNum = parseInt(endTime.replace(/-/g,'').replace(/:/g,'').replace(' ',''));*/
	var sd = new Date();
	var startTime = $("#jsstartTime").val();
	var endTime = $("#jsendTime").val(); 
	var DateDiff = GetDateDiff(startTime,endTime,"second");
 	if(!$.trim(startTime)||!$.trim(endTime)){
		alert('请输入检索时段');
		return;
	}
 	if(endTime>$.timeUtils.getDateByTime(sd)){
		alert('终止时间不能大于当前时间');
		return;
	}
 	if(startTime>endTime){
 		alert('起始时间不能大于终止时间');
		return;
 	}
 	if(DateDiff>43200){
		alert('摘要时段不能超过12小时');
		return;
	}
	var spanList = $(".selected_camera_point_js .camera_item");
	var cameraList = [];
	var cameraIds = [];
	var cameraNames = [];
	$.each(spanList,function(i){
		var cameraId = this.id;
		var address = $(this).attr("address");
		var cameraName = $(this).attr("cameraname");
		if(cameraId){
			cameraList.push(address);
			cameraIds.push(cameraId);
			cameraNames.push(cameraName);
		}
	});
	var obj = {};
	obj.cameraList = cameraList.toString();
	obj.cameraIds = cameraIds.toString();
	obj.cameraNames = cameraNames.toString();
	obj.startTime = startTime;
	obj.endTime = endTime;
    obj.taskName = taskName;
    obj.taskType = "1";
    
	var obj1 = {};
	obj1["type"] = 3;
	obj1["taskName"] = taskName;
	obj1["coatColor"] = "";
	obj1["pantsColor"] = "";
	obj1["startFrame"] = "0";
	obj1["endFrame"] = "-1";
	obj.userData = JSON.stringify(obj1);
    
    $.ajax({ 
		url : basePath + 'rest/videoAnalysis/addWebVideoAnalysis',
        type : "POST" ,
        dataType:'json',
        data : obj,
        success : function(data) {
			if (data.state) {
				layer.msg('提交成功');
				closeOneKeyMap();
				//window.open(basePath + 'rest/videoAnalysis/toVideoAnalysisNew');
				window.open(basePath + 'rest/u2sAnalysis/tovideoAnalysis');
			} else {
				alert('提交失败');
			}
		},
		failure : function() {
			layer.alert('操作超时!');
		}
     });
}
//计算时间差
function GetDateDiff(startTime, endTime, diffType) {
    //将xxxx-xx-xx的时间格式，转换为 xxxx/xx/xx的格式 
    startTime = startTime.replace(/\-/g, "/");
    endTime = endTime.replace(/\-/g, "/");
    //将计算间隔类性字符转换为小写
    diffType = diffType.toLowerCase();
    var sTime =new Date(startTime); //开始时间
    var eTime =new Date(endTime); //结束时间
    //作为除数的数字
    var timeType =1;
    switch (diffType) {
        case"second":
            timeType =1000;
        break;
        case"minute":
            timeType =1000*60;
        break;
        case"hour":
            timeType =1000*3600;
        break;
        case"day":
            timeType =1000*3600*24;
        break;
        default:
        break;
    }
    return parseInt((eTime.getTime() - sTime.getTime()) / parseInt(timeType));
}

//提交摘要（浓缩）联网视频分析
function submitZYAnalysisTask(){
	var taskName = '浓缩';
	/*if(!$.trim($("#zyTimeRange").val())){
		alert('请输入摘要时长');
		return;
	}
	var zyTime = parseInt($("#zyTimeRange").val());
	var  sd = null;
	if(frameTime){
		var frameTimeStr = frameTime.replace(/-/g,'/');
		sd = new Date(frameTimeStr);
	}else{
		sd = new Date();
	}
	sd.setTime(sd.getTime() - 1000 * 60 *zyTime);
	var startTime = $.timeUtils.getDateByTime(sd);
	sd.setTime(sd.getTime() + 1000 * 60 *zyTime);
	var endTime = $.timeUtils.getDateByTime(sd);
	var startNum = parseInt(startTime.replace(/-/g,'').replace(/:/g,'').replace(' ',''));
	var endNum = parseInt(endTime.replace(/-/g,'').replace(/:/g,'').replace(' ',''));*/
	var sd = new Date();
	var startTime = $("#zystartTime").val();
	var endTime = $("#zyendTime").val();
	var DateDiff = GetDateDiff(startTime,endTime,"second");
	if(!$.trim(startTime)||!$.trim(endTime)){
		alert('请输入摘要时段');
		return;
	}
	if(endTime>$.timeUtils.getDateByTime(sd)){
		alert('终止时间不能大于当前时间');
		return;
	}
	if(startTime>endTime){
 		alert('起始时间不能大于终止时间');
		return;
 	}
	if(DateDiff>43200){
		alert('摘要时段不能超过12小时');
		return;
	}
	var spanList = $(".selected_camera_point_zy .camera_item");
	var cameraList = [];
	var cameraIds = [];
	var cameraNames = [];
	$.each(spanList,function(i){
		var cameraId = this.id;
		var address = $(this).attr("address");
		var cameraName = $(this).attr("cameraname");
		if(cameraId){
			cameraList.push(address);
			cameraIds.push(cameraId);
			cameraNames.push(cameraName);
		}
	});
	
	//监控点列表
	var obj = {};
	obj.cameraList = cameraList.toString();
	obj.cameraIds = cameraIds.toString();
	obj.cameraNames = cameraNames.toString();
	obj.startTime = startTime;
	obj.endTime = endTime;
    obj.taskName = taskName;
    obj.taskType = "2";
    //检索
	var obj1 = {};
	obj1["type"] = 6;
	obj1["taskName"] = taskName;
	obj1["analyseParam"] = "";
	obj1["thickness"] = 10;
	obj1["startFrame"] = "0";
	obj1["endFrame"] = "-1";
	obj1["sensitivity"] = "2";
	obj.userData = JSON.stringify(obj1);
    
    $.ajax({ 
		url : basePath + 'rest/videoAnalysis/addWebVideoAnalysis',
        type : "POST" ,
        dataType:'json',
        data : obj,
        success : function(data) {
			if (data.state) {
				layer.msg('提交成功');
				closeOneKeyMap();
				window.open(basePath + 'rest/u2sAnalysis/tovideoAnalysis?taskType=2');
				//window.open(basePath + 'rest/videoAnalysis/toVideoAnalysisNew');
			} else {
				alert('提交失败');
			}
		},
		failure : function() {
			layer.alert('操作超时!');
		}
     });
}

//ie8下隐藏图片标注
var browser=navigator.appName 
var b_version=navigator.appVersion 
var version=b_version.split(";"); 
var trim_Version=version[1].replace(/[ ]/g,""); 
if(browser=="Microsoft Internet Explorer" && trim_Version=="MSIE8.0"){
	$("#editImage").hide();
}
var canvasFlag = false;
var imagewidth;
var imageheight;
//获取图片原始大小
function LoadImage(imgSrc, callback){	
	var image = new Image();
	image.src = imgSrc;
	if (image.complete) {
		callback(image);
		image.onload=function(){};
	} else {
		image.onload = function() {
	  		callback(image);
	  		// clear onLoad, IE behaves erratically with animated gifs otherwise
	  		image.onload=function(){};
		}
		image.onerror = function() {
	    	alert("Could not load image.");
		}
	}
}
function AlertImageSize(image) {
	imagewidth=image.width;
	imageheight=image.height;
}

//图片标记
function editImg(this_){
	canvasFlag = true;
	var canvasWrap='<div id="wPaint" style="position:relative; width:1920px; height:1080px; background:#dceefc;">'
    	+'<canvas></canvas>'
    	+'</div>';
    var imgSrc=$(this_).parent().siblings("#canvasWrap").find("img").attr("src");  //获取图片地址
    $(this_).parent().siblings("#canvasWrap").find(".snapshot_content_info").hide();  //隐藏截图
    $(this_).parent().siblings("#canvasWrap").append(canvasWrap);						//添加画布
  //获得原始图片尺寸
    LoadImage(imgSrc, AlertImageSize);
  
    setTimeout(function(){
        if(imagewidth && imageheight){
			 $("#wPaint").css({"width":imagewidth,"height":imageheight});
		} 
		var wp = $("#wPaint").wPaint({
			image: imgSrc
		}).data('_wPaint');
		$(this_).siblings("#equiteditImage").show();
		$(this_).hide();
    },200);
}
//取消编辑图片
function equiteditImage()
{
	canvasFlag = false;
	$("#canvasWrap").find("#wPaint").remove();
	$("#canvasWrap").siblings("._wPaint_menu").remove();
	$("#equiteditImage").hide();
	$("#canvasWrap").find(".snapshot_content_info").show();
	$("#editImage").show();
	//获取图片src
    //var imageData = $("#wPaint").wPaint("image");
   
}