
var cameraPointArray = [];// 保存全部的点位信息
var Playocx = null;
var centerPoint = null;//中心点位
var mapLevel = 0;
var showCameraLevel = 18;//显示点位的地图级数
var isFirstInit = true;
var isFirstPlay = true ;
var isPlayerInit = false;
var isHistorySream = false ;
var isRealStream = false;
var CAMERA_STATUS_ONLINE = '0';
var cameraAreaArr = [];//保存左边选择的点位数组
var markerInArea =[];
var currentRealplayAdd ='';//当前实时播放的camera  add
var currentRecordplayAdd ='';//历史播放的camera add
var ALL_CHIRDREN_CHECKED  = 2;
var DEPART_CHIRDREN_CHECKED = 1;
var NOCHIRDRENCHECKED = 0;
var camerasNodes =[];//单独勾选的点位,没有选择区域
var selectCameraName='';//过滤点位名称
var  isclrea = true;

var mapCache = {
	 	map: null,
	 	markersLayer: null,
	 	vectorLayer: null,
	 	drawFeaturePoint: null,
	 	drawFeatureLine: null,
	 	measurePolyline: null,
	 	measurePolygon: null,
	 	popupsArr: [],
	 	measurePopupsArr: []
	 };
var mapDrawCache = (function(){
	var datas = {};
	var markerArr = [];
	return {
		toArr : function() {
			var arr = [];
			for ( var k in datas)
				arr.push(datas[k]);
			return arr;
		},
		toMarkerArr : function(){
			return markerArr;
		},
		updateMarkerArr : function(marker){
			for(var i = 0; i < markerArr.length; i ++){
				if(marker.dto.id == markerArr[i].dto.id){
					markerArr[i] = marker;
				}
			}
		},
		updateDtoRemarkFromArr : function(id, remark){
			for(var i = 0; i < markerArr.length; i ++){
				if(id == markerArr[i].dto.id){
					markerArr[i].dto.remark = remark;
				}
			}
		},
		removeMarkerArr : function(id){
			for(var i = 0; i < markerArr.length; i ++){
				if(id == markerArr[i].dto.id){
					markerArr.splice(i, 1);
					return;
				}
			}
		},
		getMarkerFromArr : function(id) {
			for(var i = 0; i < markerArr.length; i ++){
				if(id == markerArr[i].dto.id){
					return markerArr[i];
				}
			}
		},
		hasCached : function(dto) {
			return !!datas[dto.id];
		},
		cache : function(id, obj) {
			markerArr.push(obj.marker);
			return datas[id] = obj;
		},
		getMarker : function(id) {
			return datas[id] && datas[id].marker;
		},
		getDto : function(id) {
			return datas[id] && datas[id].dto;
		},
		setDto : function(dto) {
			return datas[dto.id].dto = dto;
		},
		clearDto : function(){
			datas = {};
			markerArr = [];
		},
		//aferShowInBounds : $.noop,
		checkLatLng : function(c) {
			//检测监控点坐标是否合法
			return c.latitude > -90 && c.latitude
			< 90 && c.longitude > - 180
					&& c.longitude < 180;
		}
	};
})();


/**
 * 设置中心点和地图的level
 */
function mapConfig() {
	centerPoint = mapCache.map.getCenter();
	centerPoint = centerPoint.transform(mapCache.map.projection,mapCache.map.displayProjection);
	mapLevel = mapCache.map.getZoom();
}
//清空地图上的marker
function clearMarkers()
{
	var markerArr = mapDrawCache.toMarkerArr();
	$.each(markerArr, function() {
		var marker = this;
		mapCache.markersLayer.removeMarker(marker);
		marker.destroy();
	});
	mapDrawCache.clearDto();
}
//地图的事件注册
function registerMapEvents() {
	mapCache.map.events.register("zoomend", mapCache.map, function(e) {
		mapConfig();
		clearPopup();
		if(isclrea){
			clearMarkers();
		}
		if (mapLevel < showCameraLevel) {
			statisticsInbounds(mapLevel,false,"zoomend");
			return;
		}
		
		queryCamersInBoundsByAreas();
	});
	mapCache.map.events.register("moveend", mapCache.map, function(e) {
		mapConfig();
		if (mapLevel < showCameraLevel){
			statisticsInbounds(mapLevel,false,"moveend");
			return;
		}
		queryCamersInBoundsByAreas();
	});

}


/**
 * 区域内的点位聚合统计
 * @param mapLevel
 * @param isRemoveMarkOutOfArea
 * @param optionType
 * @returns
 */
function statisticsInbounds(mapLevel,isRemoveMarkOutOfArea,optionType){
	var cameraAreaStr = '';
	var singerCheckedCameraStr ='';
	$.each(cameraAreaArr,function(){
		cameraAreaStr += (","+this);
	});
	$.each(camerasNodes,function(){
		singerCheckedCameraStr += (","+this.id);
	});
	if(!cameraAreaStr && !singerCheckedCameraStr){
		clearMap();
		return;
	}
	var requestPointStr = getGridBoundsStr();
	if( !requestPointStr )return;
	$.ajax({
		url : basePath + 'rest/camera/statisticsInbounds',
		type : "POST",
		dataType : 'json',
		data:{"points":requestPointStr,"cameraArea":cameraAreaStr,"singerCheckedCameraIds":singerCheckedCameraStr,"cameraName":$("#selectCameraName").val()},
		success : function(data) {
			if(optionType == 'zoomend'){
				//clearMap();//修正地图在快速切换放大缩小的时候，异步加载未完成又再次触发zoomend 事件，导致前一次加载的marker 任然显示在界面上，因此多加一次清理矫正异步数据页面刷新
			}
			if (data && data.state) {
				var points = data.points;
				markerInArea = [];
				$.each(points,function(){
					var path = basePath + 'static/images/cluster.png';
					var icon = new OpenLayers.Icon(path, new OpenLayers.Size(40, 52),new OpenLayers.Pixel(0, -55));
					var lon = this.longitude;
					var lat = this.latitude;
					if( mapLevel > 10 ){// this.id 是用的中心点作为id,格式( lon:lat )
						 lon = this.id.split(":")[0];
						 lat = this.id.split(":")[1];
					}
					var pointlonlat;
					if(isPgis){
						pointlonlat = new OpenLayers.LonLat(lon, lat).transform(mapCache.map.displayProjection, mapCache.map.getProjectionObject());
					}else{
						var point = LatLonTransform.gcj_encrypt(lat,lon);
						pointlonlat = new OpenLayers.LonLat(point.lon, point.lat).transform(mapCache.map.displayProjection, mapCache.map.getProjectionObject());
					}
					var marker = new OpenLayers.Marker(pointlonlat, icon);
					var dto ={"id":this.id};
					markerInArea.push(marker);
					if(mapDrawCache.hasCached(dto))return true;
					mapCache.markersLayer.addMarker(marker);
					mapDrawCache.cache(this.id, {
						marker : marker,
						dto :dto
					});// 加入缓存
					var contentstr ='<span style="background: red;padding: 4px; color: rgba(255, 255, 255, 1); display: inline-block;">'+this.onlineCount+"/"+this.allCameraCount+'</span>';
					var popup =new OpenLayers.Popup("Popup_"+mapLevel+"_"+this.id, pointlonlat, null,contentstr, false);
					popup.isAlphaImage = true;
					mapCache.map.addPopup(popup);
					mapCache.popupsArr.push(popup);
					$(".olPopup").css({"width":"130px","height":"40px","background-color":"none;"});//background-color: none;
					$(".olPopupContent").css({"width":"100%","height":"100%"});//background: red; padding: 4px; color: rgba(255, 255, 255, 1); display: inline-block;">
					
				});
				if(isRemoveMarkOutOfArea){
					removeMarkOutOfArea(markerInArea);
				}
			} else {
			
			}
			
		},
		failure : function() {
			layer.alert('操作超时!');

		}
	});
	
	
}


function getGridBoundsStr(){
	var gridboundsStr ='';
	var TMS_layer = mapCache.map.getLayersByName("矢量图")[0];//[0].getExtent();
	var gridLineArr = TMS_layer.grid;//获取grid 的行数数组
	$.each(gridLineArr ,function(i){
		var titleArr = this;
		$.each(titleArr,function(j){
			var title = this;
			var bounds = title.bounds;
			var centerPoint2 = bounds.getCenterLonLat();
			// data0 保存边界的中心点  其他四个点是边界点
			var testStr='';
			testStr += ( centerPoint2.lon +":" +centerPoint2.lat +",");
			testStr += ( bounds.left +":" + bounds.top +"," );
			testStr += ( bounds.left +":" +bounds.bottom +"," );
			testStr += ( bounds.right +":" +bounds.top +"," );
			testStr += ( bounds.right +":" +bounds.bottom +";");
			gridboundsStr += testStr;
			
		});
	});
	return gridboundsStr;
}
/**
 * 移除超出区域范围的marker
 * @param markerInAreaArr 区域内的marker
 */
function removeMarkOutOfArea(markerInAreaArr){
	var drawedMarkArr = mapDrawCache.toArr();//把当前的marker缓存保存下来
	var poppupArr = mapCache.popupsArr;//popup 缓存保存
	//mapCache.popupsArr = [];//清理popup缓存
	mapDrawCache.clearDto();//清理marker缓存
	$.each(drawedMarkArr,function(){
		var drawedMarkLonLat = this.marker.lonlat;
		var isExist = false;
		$.each(markerInAreaArr,function(){
			var markerInAreaLonLat = this.lonlat;
			if(markerInAreaLonLat.lon == drawedMarkLonLat.lon && markerInAreaLonLat.lat == drawedMarkLonLat.lat){
				isExist = true;
			}
		});
		if(!isExist){//如果不存在就移除
			mapCache.markersLayer.removeMarker(this.marker);//移除marker
			this.marker.destroy();
			//移除popup
			var markerLon = this.marker.lonlat.lon;
			var markerLat = this.marker.lonlat.lat;
			$.each(poppupArr,function(){
				var popup = this;
				var popupLon = this.lonlat.lon;
				var popupLat = this.lonlat.lat;
				if( markerLon == popupLon &&  markerLat == popupLat ){
					mapCache.map.removePopup(popup);
				}
			});
		}else{
			//重新组装缓存
			mapDrawCache.cache(this.dto.id, {
				marker : this.marker,
				dto :this.dto
			});// 加入缓存
		}
	});
	
}
/**
 * 查询附近范围内的点位数据
 * @param lon
 * @param lat
 * @param distance
 * @param mapLevel
 */
function queryCameraByRadius(lon,lat,distance,mapLevel){
	$.ajax({

	/*	url : basePath + 'rest/camera/queryCameraByRadius',
		type : "POST",
		data :{
			lon : lon,
			lat :lat,
			distance : distance
		},*/
		url:  'http://43.4.112.113:8080/service/rest/app.AppMapService/collection/queryByScope',
		type : "POST",
        data :{
            longitude : lon,
            latitude :lat,
            raidus : distance
        },
		dataType : 'json',
		success : function(data) {
			if (data && data.data) {
				var cameras = JSON.parse(data.data);
			//	var cameras = data.cameras;
				if ( cameras && cameras.length > 0 ) {
					drawCamerasOnMap(cameras);
				}
			} else {
				queryCameraByRadius(lon,lat,distance,mapLevel);
			}
			
		},
		failure : function() {
			layer.alert('操作超时!');

		}
	});
}

/**
 * 区域范围内的点位标注
 * @returns
 */
function  queryCamersInBoundsByAreas(){
	
	var cameraAreaStr = '',singerCheckedCameraStr = '';
	$.each(cameraAreaArr,function(){
		cameraAreaStr += (","+this);
	});
	$.each(camerasNodes,function(){
		singerCheckedCameraStr += (","+this.id);
	});
	if( !cameraAreaStr && !singerCheckedCameraStr){
		//clearMap();
		return;
	}
	var TMS_layer = mapCache.map.getLayersByName("矢量图")[0];//[0].getExtent();
	var bounds = TMS_layer.getTilesBounds();//获取grid 的行数数组
	var centerPoint = bounds.getCenterLonLat();
	var gridboundsStr ="";
	// data0 保存边界的中心点  其他四个点是边界点
	gridboundsStr += ( bounds.left +":" + bounds.top +"," );
	gridboundsStr += ( bounds.left +":" +bounds.bottom +"," );
	gridboundsStr += ( bounds.right +":" +bounds.top +"," );
	gridboundsStr += ( bounds.right +":" +bounds.bottom );
	if( !gridboundsStr )return;
	
	
	$.ajax({
		url : basePath + 'rest/camera/queryCamersInBoundsByAreas',
		type : "POST",
		data :{
			"cameraAreaStr" : cameraAreaStr,
			"gridboundsStr" :gridboundsStr,
			"singerCheckedCameraIds":singerCheckedCameraStr,
			"cameraName":$("#selectCameraName").val()
		},
		dataType : 'json',
		success : function(data) {
			if (data && data.state) {
				var cameras = data.cameras;
				if ( cameras && cameras.length > 0 ) {
					drawCamerasOnMap(cameras);
				}
			}
			
		},
		failure : function() {
			layer.alert('操作超时!');

		}
	});
}

// ocx销毁
window.onbeforeunload = function() {
	if (null != Playocx) {
		unitPlayer();
	}

};
// 设置marker数据
function setMarkerData(cameraPointVar) {
	var dto = {
		id : cameraPointVar.id,
		type : cameraPointVar.type,
		level : mapLevel,
		name : cameraPointVar.name,
		longitude : cameraPointVar.lon,
		latitude : cameraPointVar.lat,
		icon : basePath + 'static/js/ol2/img/iconfont-dingwei.png',
		remark : "",
		createTime : "",
		status : cameraPointVar.status,
		address : cameraPointVar.address
	};
	var haveCache = mapDrawCache.hasCached(dto);
	if (!haveCache) {
		addMarker(dto);
	}
}
// 添加marker
function addMarker(dto) {
	if(mapDrawCache.hasCached(dto))return;
	var status = dto.status;
	var type = dto.type;
	var path ='';
	if( CAMERA_STATUS_ONLINE == status ){
		path = (type == '1'?(basePath + 'static/images/camera_1_online .png'):(basePath + 'static/images/camera_0_online.png'));
	}else{
		path = (type == '1' ?(basePath + 'static/images/camera_1_offline.png'):(basePath + 'static/images/camera_0_offline.png'));
	}
	//var path = basePath + 'static/googlemap/images/camera-select.png';
	var icon = new OpenLayers.Icon(path, new OpenLayers.Size(30, 30),
			new OpenLayers.Pixel(-10, -25));
	var pointlonlat;
	if(isPgis){
		pointlonlat = new OpenLayers.LonLat(dto.longitude, dto.latitude).transform(mapCache.map.displayProjection, mapCache.map.getProjectionObject());
	}else{//谷歌地图需要先转换成火星坐标系再转成墨卡托坐标
		var point = LatLonTransform.gcj_encrypt(Number(dto.latitude),Number(dto.longitude));
		pointlonlat = new OpenLayers.LonLat(point.lon, point.lat).transform(mapCache.map.displayProjection, mapCache.map.getProjectionObject());
	}
	var marker = new OpenLayers.Marker(pointlonlat, icon);
	marker.dto = dto;
	mapCache.markersLayer.addMarker(marker);
	registerMarkerEvent(marker);
	mapDrawCache.cache(dto.id, {
		dto : dto,
		marker : marker
	});// 加入缓存
}
//清空地图上的popup
function clearPopup(){
	//清理地图上的popup
	$.each(mapCache.popupsArr,function(){
		mapCache.map.removePopup(this);
	});
	mapCache.popupsArr = [];
}


//初始化实时播放器ocx
function initPlayer() {
	try {
		Playocx = document.getElementById("PlatFormOcx");// 定义在jsp页面中，公共调用initplayer
		Playocx.Init(platIp, "1");
		Playocx.RegCallback(window, "OcxCallback");
		Playocx.Login(platIp, port, uid, pwd, "");// 流媒体服务器ip 端口 用户名 密码
		Playocx.SetFTPServer(fileService);
		Playocx.ConfigurePlayWin("1");
		isPlayerInit = true;
	} catch (err) {
		//alert("初始化播放器失败：err--"+err);
	}
}
//反初始化
function unitPlayer(){
	try {
		Playocx.Uninit();
		isPlayerInit = false;
	} catch (e) {
		alert("反初始化失败,err:"+e);
	}
}
//实时视频播放事件
function realtimePlayer(address,name){
	videoFrameAddress = address;
	videoFrameName = name;
	$('.video_player_time').hide();
	$('.history_play_btn').css('background-color','none');
	$('.real_play_btn').css('background-color','#4e9bcf');
	
	$("#camera_name").html(name);
	$("#div_camera_point").css("height","505px").show();
	RealPlay(address);
	return false;
}

function historyStream(add,name){
	$("#div_camera_point").css("height","625px");
	$("#camera_point_list>ul").empty();
	$("#div_camera_point, #div_seach").show();
	$("#camera_name").html(name);
	$("#playAddrr").val(add);
	if(currentRecordplayAdd){
		Playocx.StopRecordPlay(currentRecordplayAdd);
		currentRealplayAdd ='';
	}
	if(currentRealplayAdd){
		Playocx.StopRealPlay(currentRealplayAdd);
		currentRealplayAdd ='';
	}
	
}

function historyStreamPlay(){
	var selCaId = $("#playAddrr").val();
	var startTimeStr,endTimeStri;
	var startTime = $('#startTimeStr').datetimebox('getValue');
	var endTime = $('#endTimeStr').datetimebox('getValue');
	if( startTime && endTime ){
		startTimeStr = formateHistroyTime(startTime);
		endTimeStri = formateHistroyTime(endTime);
		try{
			Playocx.RecordPlay(selCaId,startTimeStr,endTimeStri);
			isRealStream = false;
			isHistorySream = true;
			currentRecordplayAdd = selCaId;
		}catch(e){
			alert("历史视频播放err:"+e);
		}
	}
	
}


/**
 * 摄像头点位 
 * @param lat
 * @param lon
 * @param id
 * @param name
 * @param address
 * @param status
 */
function cameraPoint(lat, lon, id, name, address, status,type) {
	this.lat = lat;
	this.lon = lon;
	this.id = id;
	this.name = name;
	this.address = address;
	this.status = status;
	this.type = type;
}


function clearPlayerList(){
	$("#div_seach").hide();
	$("#camera_point_list>ul").empty();
}
function tabCameraClick(addr , name){
	clearPlayerList();
	realtimePlayer(addr,name);
}

function configWinNum(){
	try{
		Playocx.ConfigurePlayWin("1");
	}catch(err){
		layer.alert("设置单屏失败,err:"+err);
	}
}


//初始化点位数组
function drawCamerasOnMap(data) {
	$.each(data, function(i) {
		if (this.latitude) {
			var cameraPointVar = new cameraPoint(this.latitude, this.longitude, this.id,
					this.name, this.address, this.status,this.type);
			//if (mapLevel >= showCameraLevel) {
				setMarkerData(cameraPointVar);
			//}
		}
	});

}

/**
 * 组装子节点的区域编号 及 组装子节点的单个点位
 * @param parentNode
 */
function pushChirdrenRegion(parentNode){
	var children = parentNode.children;
	var data =[];
	// 全部已经勾选的子节点
	for (var int = 0; int < children.length; int++) {
		var chirld = children[int];
		if( chirld.checked ){
			data.push(chirld);
		}
	}
	if(data.length > 0){
		for (var int = 0; int < data.length; int++) {
			var child = data[int];
			if( child.check_Child_State == DEPART_CHIRDREN_CHECKED ){
				pushChirdrenRegion(child);
			}else{
				if( child.isCamera == '1'){
					camerasNodes.push(child);
				}else{
					cameraAreaArr.push(child.region);
				}
			}
		}
	}
}

/**
 * 添加checkBox选中的点位
 */
function drawCheckedAreaMark(){
	cameraAreaArr = [];//所选的区域id
	camerasNodes =[];//保存单个点位
	var rootNode = treeObj.getNodesByParam("level",0)[0];
	if(rootNode.checked){
		if( rootNode.check_Child_State == ALL_CHIRDREN_CHECKED ){
			cameraAreaArr.push(rootNode.region);
		}else if(rootNode.check_Child_State == DEPART_CHIRDREN_CHECKED ){
			pushChirdrenRegion(rootNode);
		}
		
	}
	if(cameraAreaArr.length == 0){
		clearMap();
	}else{
		if (mapLevel < showCameraLevel) {
			statisticsInbounds(mapLevel,true,"");
		}
	}
	$.each(camerasNodes,function(){
		var node = this;
		var lon =node.longitude;
		var lat = node.latitude;
		if(!lon || !lat)return true;
		var camera = {
				id : node.id,
				name : node.name,
				type : node.type,
				status : node.status,
				level : mapLevel,
				longitude : lon,
				latitude : lat,
				address : node.address
		};
		addMarker(camera);
	});
}
/**
 * checkbox 选择事件
 * @param e
 * @param treeId
 * @param treeNode
 */
function zTreeonCheck(e,treeId,treeNode)
{
	isclrea = true;
	drawCheckedAreaMark();
}



function formateHistroyTime(timeStr)
{
	var timeStrArr=timeStr.split(" ");
	var ymdStr="" ,hmsStr="";
	$.each(timeStrArr[0].split("-"),function(){
		ymdStr+=this;
	});
	$.each(timeStrArr[1].split(":"),function(){
		hmsStr+=this;
	});
	return ymdStr+"T"+hmsStr+"Z";
}



function  clearMap(){
	if (isclrea) {
		clearPopup();
		clearMarkers();
	}
}

//关闭当前页面
function closeThisThis(this_){
	$(this_).parent().hide("show");
	if(currentRealplayAdd){
		Playocx.StopRealPlay(currentRealplayAdd);
		currentRealplayAdd ='';
	}
	if(currentRecordplayAdd){
		Playocx.StopRecordPlay(currentRecordplayAdd);
		currentRecordplayAdd ='';
	}
}