var isShowCheckBox = true;

//map object cache
var mapCache = {
	map: null,
	markersLayer: null,
	vectorLayer: null,
	drawFeaturePoint: null,
	drawFeatureLine: null,
	measurePolyline: null,
	measurePolygon: null,
	popupsArr: [],
	measurePopupsArr: [],
	OverviewMapControl:null//鹰眼
};

//vectory style
var mapStyle = {
	getStyle: function(){
		return {
			pointRadius: 5,
			strokeColor: "",
			strokeOpacity: 1,
			strokeWidth: 3,
			fillColor: "#acd5f6",
			fillOpacity: 0.6,
			externalGraphic: "${basePath}static/js/ol2/img/marker.png",
			graphicWidth: 21,
			graphicHeight: 25,
			graphicXOffset: -10,
			graphicYOffset: -25
		};
	},
	getSelect: function(){
		return {
			pointRadius: 5,
			strokeColor: "",
			strokeOpacity: 1,
			strokeWidth: 3,
			fillColor: "#444",
			fillOpacity: 0.4,
			externalGraphic: "${basePath}static/js/ol2/img/marker.png",
			graphicWidth: 21,
			graphicHeight: 25,
			graphicXOffset: -10,
			graphicYOffset: -25,
			cursor: "pointer"
		};
	},
	getTemporary: function(){
		return {
			pointRadius:5,
			strokeColor:"",
			strokeOpacity:1,
			strokeWidth:3,
			fillColor:"#948EF5",
			fillOpacity:0.4
		};
	}
};

//缓存对象 
var mapDrawCache = (function(){
		var datas = {};
		var markerArr = [];
		var popupArr = [];
		var popupDatas = {};
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
			cachePopup : function (id,obj){
				popupArr.push(obj.popup);
				return popupDatas[id] = obj;
			},
			getPopup : function (id){
				for(var i = 0; i < popupArr.length; i ++){
					if(id == popupArr[i].id){
						return popupArr[i].popup;
					}
				}
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
			aferShowInBounds : $.noop,
			checkLatLng : function(c) {
				//检测监控点坐标是否合法
				return c.latitude > -90 && c.latitude
				< 90 && c.longitude > - 180
						&& c.longitude < 180;
			}
		};
})();

//初始化谷歌地图
function initGoogleMap(){
	if(mapCache.map) $("#" + mapCache.map.getViewport().id).remove();
	mapCache.map = new OpenLayers.Map("map_camera", {
		maxExtent : new OpenLayers.Bounds(-20037508.3427892, -20037508.3427892, 20037508.3427892, 20037508.3427892),
		numZoomLevels : 18,
		maxResolution : 156543.0339,
		units : 'm',
		projection : "EPSG:900913",
		displayProjection : new OpenLayers.Projection("EPSG:4326")
	});
	//谷歌矢量地图
	var gmoverlay = new OpenLayers.Layer.TMS("中国", "", {
		'type': 'png', 
		'getURL': googleGetTileURL,
		'tileOptions': {
			onImageError : function() { // 切片数据加载失败使用空白图片代替
				var img = this.imgDiv;
				if (img.src != null) {
					this.imageReloadAttempts++;
					if (this.imageReloadAttempts <= OpenLayers.IMAGE_RELOAD_ATTEMPTS) {
						this.setImgSrc(this.layer.getURL(this.bounds));
					} else {
						OpenLayers.Element.addClass(img, "olImageLoadError");
						this.events.triggerEvent("loaderror");
						img.src = basePath+"static/images/novideo.png";
						this.onImageLoad();
					}
				}
		   }
		}
	});

	mapCache.map.addLayer(gmoverlay);

	mapCache.map.addControl(new OpenLayers.Control.Scale());
	mapCache.map.addControl(new OpenLayers.Control.MousePosition());
	mapCache.map.addControl(new OpenLayers.Control.LayerSwitcher());
	//设置中心点
	var cenLonLat = new OpenLayers.LonLat(mapCenterLongitude, mapCenterLatitude).transform(mapCache.map.displayProjection, mapCache.map.getProjectionObject());
	mapCache.map.setCenter(cenLonLat, 13);
	//初始化样式和 控件
	initGisStyleAndControl();
}

function googleGetTileURL(bounds) {
	var res = this.map.getResolution();
	var tileOriginY = this.map.getMaxExtent().top;
	var tileOriginX = this.map.getMaxExtent().left;
	var x = Math.round((bounds.left - tileOriginX)
			/ (res * this.tileSize.w));
	var y = Math
			.round((tileOriginY - bounds.top) / (res * this.tileSize.h));
	var z = this.map.getZoom();
	var mapPicDir = "/Gis_0001/static_map/";
	var curSize = Math.pow(2, z);
	var path = mapPicDir + z + "/" + pad((y % curSize), 6) + "-"
			+ pad((x % curSize), 6) + ".png";
	return path;
}

function pad(num, n) {
	var len = num.toString().length;
	while (len < n) {
		num = "0" + num;
		len++;
	}
	return num;
}

function initPGisMap() {
	if(mapCache.map) $("#" + mapCache.map.getViewport().id).remove();
	//var titeServePath = '${titeServePath}';
	//创建地图
	mapCache.map = new OpenLayers.Map('map_camera', {
		units: "degrees",//度量单位
		projection: new OpenLayers.Projection("EPSG:4326"),//900913以米作为xy，4326以经纬度作为xy //投影规则
		displayProjection: new OpenLayers.Projection("EPSG:4326"),//显示的投影规则
		minzoom: 9,
		maxzoom: 21
	});
	
	//重载地图方法，用于限制图层缩放级别
	mapCache.map.isValidZoomLevel = function(zoomLevel) {
		return ((zoomLevel != null) && (zoomLevel >= 9) && (zoomLevel <= 21));
	};
	
	//PGIS图层 TMS Markers Vector
	var tmsoverlay = new OpenLayers.Layer.TMS("矢量图", titeServePath + "/Maps/default/EzMap", {
		type : 'png',
		getURL : pgisGetTileURL,
		alpha : true,
		resolutions: [
	        2, 1, 0.5,
	        0.25, 0.125, 0.0625,
	        0.3125, 0.015625, 0.0078125,
	        0.00390625, 0.001953125, 0.0009765625,
	        0.00048828125, 0.000244140625, 0.0001220703125,
	        0.00006103515625, 0.000030517578125, 0.0000152587890625,
	        0.00000762939453125, 0.000003814697265625, 0.0000019073486328125
       ],
		tileOrigin: new OpenLayers.LonLat(0, 0),
		isBaseLayer : true
	});
	
	//PGIS图层 TMS Markers Vector 
	var yxoverlay = new OpenLayers.Layer.TMS("卫星图", titeServePath + "/Maps/yx/EzMap", {
		type : 'png',
		getURL : pgisGetTileURL,
		alpha : true,
		resolutions: [
	        2, 1, 0.5,
	        0.25, 0.125, 0.0625,
	        0.3125, 0.015625, 0.0078125,
	        0.00390625, 0.001953125, 0.0009765625,
	        0.00048828125, 0.000244140625, 0.0001220703125,
	        0.00006103515625, 0.000030517578125, 0.0000152587890625,
	        0.00000762939453125, 0.000003814697265625, 0.0000019073486328125
       ],
	   tileOrigin: new OpenLayers.LonLat(0, 0),
	   isBaseLayer : true
	});
	//add layers
	mapCache.map.addLayers([tmsoverlay, yxoverlay]);
	//center
	mapCache.map.setCenter(new OpenLayers.LonLat(mapCenterLongitude, mapCenterLatitude), 17, true, true);
	var iconPath = basePath+'static/images/cluster.png';
	var icon = new OpenLayers.Icon(iconPath, new OpenLayers.Size(40, 52), new OpenLayers.Pixel(-10, -25));
	var lonlat =new OpenLayers.LonLat(mapCenterLongitude, mapCenterLatitude).transform(  
            new OpenLayers.Projection("EPSG:4326"), mapCache.map.getProjectionObject());
	//创建点图层 设置中心点marker
	var deStyle = new OpenLayers.Style(mapStyle.getStyle());
	var styleMap = new OpenLayers.StyleMap({"default": deStyle, "select": mapStyle.getSelect(), "temporary": mapStyle.getTemporary()});
	mapCache.markersLayer = new OpenLayers.Layer.Markers("点视图", {styleMap: styleMap});
	mapCache.map.addLayer(mapCache.markersLayer);
	//var marker = new OpenLayers.Marker(lonlat,icon);
	//mapCache.markersLayer.addMarker(marker);
	//初始化样式和 控件
	initGisStyleAndControl();
}

//初始化样式和 控件
function initGisStyleAndControl(){
	//style
	var deStyle = new OpenLayers.Style(mapStyle.getStyle());
	var styleMap = new OpenLayers.StyleMap({"default": deStyle, "select": mapStyle.getSelect(), "temporary": mapStyle.getTemporary()});
	//vector layer
	mapCache.vectorLayer = new OpenLayers.Layer.Vector('线路视图', {styleMap: styleMap});//标注点线面图层
	mapCache.map.addLayer(mapCache.vectorLayer);
	//draw feature
	mapCache.drawFeaturePoint = new OpenLayers.Control.DrawFeature(mapCache.vectorLayer, OpenLayers.Handler.Point);
	mapCache.map.addControl(mapCache.drawFeaturePoint);
	mapCache.drawFeatureLine = new OpenLayers.Control.DrawFeature(mapCache.vectorLayer, OpenLayers.Handler.Path);
	mapCache.map.addControl(mapCache.drawFeatureLine);
	//measure
	mapCache.measurePolyline = new OpenLayers.Control.Measure(OpenLayers.Handler.Path, {
		persist: true,
		handlerOptions: {
			layerOptions:{styleMap: styleMap}
	    }
	});
	mapCache.measurePolygon = new OpenLayers.Control.Measure(OpenLayers.Handler.Polygon, {
		persist: true,
		handlerOptions: {
			layerOptions:{styleMap: styleMap}
	    }
	});
	mapCache.map.addControl(mapCache.measurePolyline);
	mapCache.map.addControl(mapCache.measurePolygon);
	//放置点图层
	mapCache.markersLayer = new OpenLayers.Layer.Markers("点视图", {styleMap: styleMap});//放置点图层
	mapCache.map.addLayer(mapCache.markersLayer);
	
	var zoomControl = mapCache.map.getControlsByClass("OpenLayers.Control.Zoom")[0];
	mapCache.map.removeControl(zoomControl);
	mapCache.map.addControl(new OpenLayers.Control.Navigation());//鼠标导航
	mapCache.map.addControl(new OpenLayers.Control.KeyboardDefaults());//键盘
	mapCache.OverviewMapControl = new OpenLayers.Control.OverviewMap();
	mapCache.OverviewMapControl.maximized = true;//设置鹰眼展开显示
	mapCache.OverviewMapControl.minRectSize = 5;
	mapCache.OverviewMapControl.autoPan = true;
	mapCache.map.addControl(mapCache.OverviewMapControl);//鹰眼 右下
	mapCache.map.addControl(new OpenLayers.Control.ScaleLine());//比例尺
	mapCache.map.addControl(new OpenLayers.Control.LayerSwitcher());//图层切换 右上
	//mapCache.map.addControl(new OpenLayers.Control.MousePosition());//显示鼠标所在位置坐标  右下
	
	//mapCache.map.addControl(new OpenLayers.Control.PanZoomBar());//平移缩放工具条 左上
	//mapCache.map.addControl(new OpenLayers.Control.Graticule());
	 //声明并初始化一个矢量图层  
	//实例化 Geolocate 对象 中心点位置绑定不变化
	var geolocate = new OpenLayers.Control.Geolocate({
	    bind:false
	});
    mapCache.map.addControl(geolocate); 
    geolocate.activate();
      
}

function pgisGetTileURL(bounds) {
   bounds = this.adjustBounds(bounds);
   var res = this.getServerResolution();
   var x = Math.round((bounds.left - this.tileOrigin.lon) / (res * this.tileSize.w));
   var y = Math.round((bounds.bottom - this.tileOrigin.lat) / (res * this.tileSize.h));
   var z = this.serverResolutions != null ?
       OpenLayers.Util.indexOf(this.serverResolutions, res) :
       this.getServerZoom();
   if (this.levelSequence === 0) {
			z = this.levelMax - z;
		}
   var path = "?Service=getImage&Type=RGB&Col=" + x + "&Row=" + y
				+ "&Zoom=" + z + "&V=0.3"; 
	 if (this.ezZoomOffset != 0) {
		path = path + "&ZoomOffset=" + this.ezZoomOffset;
	 }
   var url = this.url;
   if (OpenLayers.Util.isArray(url)) {
       url = this.selectUrl(path, url);
   }
   return url + path;
}

//添点
var sceneArr = [], sceneObjArr = [];
function addPointTool(obj){
	 var dto = {
		id: obj.id,
		type: obj.imgKey,
		level: obj.level,
		name: obj.name,
		longitude: obj.lng, 
		latitude: obj.lat,
		icon: basePath + 'static/js/ol2/img/iconfont-dingwei.png',
		remark:obj.remark,
		createTime: obj.dstr,
		status : obj.status,
		address : obj.address
	 };
	 var path = basePath + 'static/googlemap/images/camera-select.png';
	 var icon = new OpenLayers.Icon(path, new OpenLayers.Size(26, 35), new OpenLayers.Pixel(-10, -25));
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
	 //event
	 addDrafEventListener(marker);
	 /*//放入到缓存
	 mapDrawCache.cache(dto.id, {
		dto : dto,
		marker : marker
	 });
	 sceneArr.push(dto);
	 sceneObjArr.push({id: obj.dstr, obj: marker});*/
}

function addDrafEventListener(marker){
	marker.events.register("click", marker, function(evt){
		clearMeasureData();
		var dto = marker.dto;
		 var p = new OpenLayers.Popup(null, new OpenLayers.LonLat(dto.longitude, dto.latitude).transform(  
		            new OpenLayers.Projection("EPSG:4326"), mapCache.map.getProjectionObject()), new OpenLayers.Size(0, 20), "", true);
		 p.autoSize = true;
		 var contentStr = '';
         //_obj = feature.get('features')[0];
         contentStr += '<p>点位名称：' + dto.name + '</p>';
         contentStr += '<p>状态：' + (dto.status==0?"在线":"下线") + '</p>';
         //contentStr += '<p>address：' + dto.address + '</p>';
		 p.setContentHTML("<div style='border:#999 solid 1px;padding:3px;'>"+ contentStr +"</div>");
		 mapCache.map.addPopup(p);
		 mapCache.popupsArr.push(p);
		 
		 currentCamera["id"] = dto.id;
		$("#cameraName").empty();
		$("#cameraName").append('<span><b>当前点位：'+dto.name+'</b></span>');
		$("#currentCameraId").val(dto.id);
		//searchVideoByCamera(dto.id,true);
		//popupContent(dto.id, dto.name);
   });
}

//clear measure
function clearMeasureData(){
	 $(mapCache.popupsArr).each(function(){
		mapCache.map.removePopup(this);
	 });
	 mapCache.measurePolyline.deactivate();
	 mapCache.measurePolygon.deactivate();
}

//清理地图
function clearMapTool(){
	sceneObjArr = [];
	sceneArr = [];
	
	mapCache.vectorLayer.removeAllFeatures();
	mapCache.markersLayer.clearMarkers();
	//popupsArr
	for(var i = 0; i < mapCache.popupsArr.length; i++){
		var p =  mapCache.popupsArr[i];
		try{mapCache.map.removePopup(p);}catch(ex){}
	}
	mapCache.popupsArr = [];
	//measurePopupsArr
	for(var i = 0; i < mapCache.measurePopupsArr.length; i++){
		var p =  mapCache.measurePopupsArr[i];
		try{mapCache.map.removePopup(p);}catch(ex){}
	}
	mapCache.measurePopupsArr = [];
	//close window
	$(".layui-layer-close").trigger('click');
}

function searchVideoByCamera(cameraId,all){
	var startTime = $("#startTime").val();
	var endTime = $("#endTime").val(); 
	if(startTime == ''){
		layer.alert("请输入开始时间！");
		return;
	}
	if(all){
		startTime = '';
	}
	$.ajax({
		url : basePath + 'rest/ctrlUnitFile/queryVideoByCamera',
		type : 'POST',
		data : {
			cameraId : cameraId,
			startTime : startTime,
			endTime : endTime
		},
		dataType : 'JSON',
		success : function(data, textStatus, jqXhr) {
			
			if(data.state){
				var result = data.videoList;
				$('#cameraVideoTree1').empty();
				$('#videoList1').empty();
				if (result.length > 0) {
					var cameraName = result[0].cameraName;
					if(cameraName && cameraName.length > 20){
						cameraName = cameraName.substring(0,20) + "...";
					}
                    $('.right-video-list .chenk_all').prop("checked", false);
                    var treeList = '<dt class="clearfix" cameraId="' + cameraId + '" startTime="' + startTime + '" endTime="' + endTime + '">'
                        + '<span class="cameraName-span">'
                        + '<i class="fa fa-folder" aria-hidden="true" title="'+ result[0].cameraName +'"></i>'
                        + cameraName
                        + '</span><span class="camera_handle_span load_more_video pull-right">'
                        + '<i class="glyphicon glyphicon-plus-sign" title="load more"></i>'
                        + '</span><span class="camera_handle_span remove_camera_list pull-right">'
                        + '<i class="glyphicon glyphicon-remove-sign" title="remove"></i>'
                        + '</span></dt>'
                        + '<dd><ul class="cs_videoUl" cameraId="' + cameraId + '" page="1"></ul></dd>';
                    $('#cameraVideoTree1').prepend(treeList);

                }else {
                	$('#cameraVideoTree1').prepend("没有找到数据...");
                	if(!all){
                		layer.msg('没有找到数据!', {
							icon : 1,
							time : 1500
						});
                	}
                    return;
                }
				
				var html ='';
				for ( var i=0;i<result.length;i++) {
	                  var item = result[i];
	                  var transcodingId = item.transcodingId;
	                  var name = item.fileName;
	                  /* var timeLong = $.timeUtils.getTimeLong(item.timeLong); */
	                  var cameraInfoId = item.cameraId;
	                  var cameraInfoName = item.cameraName;
	                  var thumbnailUrl = item.filePathafterupload;
	                  var originalUrl = item.filePathafterupload;
	                  /* var hideStyle = isSingle ? 'style="display:none;"' : ''; */
	                  var videoUrl = item.filePathafterupload;
	                  var fileId = item.id;
	                  var createTime = item.createTime;
	                  html += '<li style="margin-right: 5px;">';
	                	  html +=  '<div class="cs_videoPic">';
	                	 html +=  '<div class="cs_case_img" style="padding:10px 0 10px 10px;">';
	                	html +=  '<div class="box_cont col-md-6" videoId="' + transcodingId + '" originalUrl="' + originalUrl + '" videoUrl="' + videoUrl + '" style="height:110px;padding:0;margin:0px;">';
	                	html +=  '<div class="box_img" ><img width="110" height="90" src="'+basePath+'static/images/nongsuov.jpg"></div>';
	                    if(isShowCheckBox){
	                    	html +=  '<input type="checkbox" fileId="'+fileId+'" videoId="' + transcodingId + '"  hasObjectStructured="true"   videoName="' + name + '" cameraInfoName="' + cameraInfoName + '" cameraInfoId="' + cameraId + '" class="cs_videocheck" name="videoOri">';
	                    }
	                	html += '<div class="box_bg" onclick="playCameraVideo(\''+ transcodingId +'\');" style="cursor: pointer;"></div>';
	                	html += '<div  >';
	                      /* + '<img width="110" height="90" src="${basePath}static/images/common/bf.png" style="cursor: pointer;">'
	                       */
	                	html += '</div>';
	                	html += '</div>';
	                	html += '<div class="col-md-6" style="padding:0;">';
	                	html += '<p title="'+name+'">名称:';
	                	html += name;
	                	html += '</p>';
	                	html += '<p title="'+ (new Date(createTime)).format("yyyy-MM-dd hh:mm:ss") +'">时间:';
	                   html += (new Date(createTime)).format("yyyy-MM-dd hh:mm:ss");
	                   html += '</p>';
	                      /* + '<p title="'+item.startTime+'">开始:'
	                      + item.startTime
	                      + '</p>'
	                      + '<p title="'+item.endTime+'">结束:'
	                      + item.endTime
	                      + '</p>' */
	                    html += '</div>';
	                    html += '</div>';
	                    html += '</div>' ;
	                    html += '</li>';
	                }
	                $('.cs_videoUl1').append(html);
				
			}
		}
	});
}

function searchVideoByCameraName(cameraName){
	$.ajax({
		//url : basePath + 'rest/cameraMedia/getVideoByCameraName',
		url : basePath + 'rest/ctrlUnitFile/getVideoByCameraName',
		type : 'POST',
		data : {
			cameraName : cameraName
		},
		dataType : 'JSON',
		success : function(data, textStatus, jqXhr) {
			if(data.state){
				var result = data.videoList;
				$('#cameraVideoTree1').empty();
				$('#videoList1').empty();
				if (result.length > 0) {
					for(var i = 0; i < result.length; i++){
						var item  = result[i];
						var ctrlUnitFileList = item.ctrlUnitFileList;
						var cameraName = item.cameraName;
						if(cameraName && cameraName.length > 20){
							cameraName = cameraName.substring(0,20) + "...";
						}
						var treeList = '<dt class="clearfix" cameraId="' + item.cameraId + '">'
	                        + '<span class="cameraName-span">'
	                        + '<i class="fa fa-folder" aria-hidden="true" title="'+ item.cameraName +'"></i>'
	                        + cameraName
	                        + '</span><span class="camera_handle_span load_more_video pull-right">'
	                        + '<i class="glyphicon glyphicon-plus-sign" title="load more"></i>'
	                        + '</span><span class="camera_handle_span remove_camera_list pull-right">'
	                        + '<i class="glyphicon glyphicon-remove-sign" title="remove"></i>'
	                        + '</span></dt>'
	                        + '<dd><ul class="cs_videoUl" id="ul_'+ item.cameraId +'" cameraId="' + item.cameraId + '" page="1"></ul></dd>';
	                    $('#cameraVideoTree1').prepend(treeList);
	                    var html = '';
	                    for(var j = 0; j < ctrlUnitFileList.length; j++){
	                    	var ctrlUnitFile = ctrlUnitFileList[j];
	                    	html += '<li style="margin-right: 5px;">';
	                    	html += '<div class="cs_videoPic" >';
	                    	html += '<div class="cs_case_img" style="padding:10px 0 10px 10px;">';
	                    	html += '<div class="box_cont col-md-6" videoId="' + ctrlUnitFile.transcodingId + '" originalUrl="' + ctrlUnitFile.filePathafterupload + '" videoUrl="' + ctrlUnitFile.filePathafterupload + '" style="height:110px;padding:0;margin:0px;">';
	                    	html += '<div class="box_img"><img width="110" height="90" src="'+basePath+'static/images/nongsuov.jpg"></div>';
	                    	if(isShowCheckBox){
	                    		html += '<input type="checkbox" fileId="'+ctrlUnitFile.id+'" videoId="' + ctrlUnitFile.transcodingId + '"  hasObjectStructured="true"   videoName="' + ctrlUnitFile.fileName + '" cameraInfoName="' + item.cameraName + '" cameraInfoId="' + item.cameraId + '" class="cs_videocheck" name="videoOri">';
	                    	}
	                    	html += '<div class="box_bg" onclick="playCameraVideo(\''+ ctrlUnitFile.transcodingId +'\');" style="cursor: pointer;"></div>';
	                    	html += '<div>';
	                    	html += '</div>';
	                    	html += '</div>';
	                    	html += '<div class="col-md-6" style="padding:0;">';
	                    	html += '<p title="'+ctrlUnitFile.fileName+'">名称:';
	                    	html += ctrlUnitFile.fileName;
	                    	html += '</p>';
	                    	html += '<p>时间:';
	                    	html += (new Date(ctrlUnitFile.createTime)).format("yyyy-MM-dd hh:mm:ss");
			                html += '</p>';
			                html += '</div>';
			                html += '</div>';
			                html += '</div>';
			                html += '</li>';
			            }
			            $("#ul_"+item.cameraId).append(html);
					}

                }else {
                	$('#cameraVideoTree1').prepend("没有找到数据...");
            		layer.msg('没有找到数据!', {
						icon : 1,
						time : 1500
					});
                    return;
                }
				initSlideToggleRight();
			}
		}
	});
}

function searchCamera(){
	var cameraName = $("#cameraName").val();
	if(cameraName == ''){
		layer.msg('请输入搜索的内容！!', {
			icon : 2,
			time : 1000
		//1.5s后自动消失
		});
		//layer.alert("请输入搜索的内容！");
		return; 
	}
	searchVideoByCameraName(cameraName);
}

var currentCamera= {};
function showSearchModal(cameraId,cameraName,marker){
	popupContent(cameraId, cameraName);
	searchVideoByCamera(cameraId,true);
}

function initMapContentDiv(){
	var html = '<div id="mapDiv" style="margin-top:-20px;">';
	html += '<div class="gisPannelDiv" style="top:10px;z-index:99999999999">';
	html += '<div class="gisSearchDiv">';
//		html += '<form id="cameraPointSearchForm" autocomplete="off">';
	html += '<div class="input-group">';
	html += '<span class="input-group-btn">';
	html += '<span class="input-group-btn" style="padding: 6px 12px;"><i class="fa fa-navicon"></i></span>';
	html += '</span>';
	html += '<input class="searchInput form-control" type="text" id="cameraName" name="cameraName" placeholder="请输入搜索内容..."/>';
	html += '<span onclick="searchCamera();" class="input-group-btn searchInputBtn">';
	//html += '<button id="searchBtnOut"><i class="fa fa-search searchBtn"></i></button>';
	html += '<button id="searchBtnOut"><i class="fa fa-search searchBtn"></i></button>';
	html += '</span>';
	html += '</div>';
//		html += '</form>';
   html += '</div>';
	html += '<div class="gisSearchList">';
	html += '<div class="camera-search-result" style="display:block;">';
	html += '<div class="camera-search-result-list">';
	html += '<ul class="camera-search-list">';
	html += '</ul>';
	html += '<div class="btn-group page-btn-group">';
	html += '<button class="btn btn-default last-camera">上一页</button>';
	html += '<button class="btn btn-default next-camera">下一页</button>';
	html += '</div>';
	html += '</div>';
	html += '</div>';
	html += '<div class="camera-detail-info" style="display:none;">';
	html += '</div>';
	html += '</div>';
	html += '</div>';
	html += '</div>';
	html += '<div id="gisPopupDiv"  class="ol-popup"><div id="popup-content"></div></div>';
	html += '<div id="nameTooltip" class="tooltip">  </div>';
	
	$('.cs_map').append(html);
	
	/* var csMapHeight = $('.cs_map').height();
	if(csMapHeight > 0){
		$('#mapDiv').height(csMapHeight);
	} */
	
	/* nameTooltip = $('#nameTooltip');
    nameTooltip.tooltip({
       animation: false,
       trigger: 'manual',
       placement: 'bottom'
    }); */
}

function popupContent(id, name){
    var str="<div class='gisPopup'><div class='close-btn'><span onclick='closePopup();' class='glyphicon glyphicon-remove'></span></div>";
	     str+="<table class='popTable'>"+
	          "<tbody>";
	 var cameraName = '';
	 if(name.length>8){
		 for(var i=0;i<8;i++){
			 cameraName += name[i];
		 }
		 cameraName +='...';
	 }else{
		 cameraName = name;
	 }
	 str+="<tr><td>点位名称</td><td title='"+name+"'>"+cameraName+"</td></tr>";
	 str+="<tr><td colspan='2'>查询点位视频:</td></tr><tr>"+"<td width='60px' nowrap>开始时间</td>"+
	 "<td><div class='input-group input-append date form_datetime'>"+
	 '<input readonly="readonly" placeholder="请输入时间" class="laydate-icon form-control" style="padding: 0 0 0 10px" name="startTime" id="startTime" onclick="laydate({istime: true, max:laydate.now(),format: \'YYYY-MM-DD hh:mm:ss\'})">'+
	 /* "<span class='input-group-addon'><i id='"+id+"' class='fa fa-calendar'></i>"+ */
	 "</span></div></td>"+"</tr>";
	  
	 str+="<tr>"+"<td width='60px' nowrap>结束时间</td>"+
	 "<td><div class='input-group input-append date form_datetime'>"+
	 '<input readonly="readonly" placeholder="请输入时间" class="laydate-icon form-control" style="padding: 0 0 0 10px" name="endTime" id="endTime" onclick="laydate({istime: true, max:laydate.now(),format: \'YYYY-MM-DD hh:mm:ss\'})">'+
	 /* "<span class='input-group-addon'><i class='fa fa-calendar'></i>"+ */
	 "</span></div></td>"+"</tr>";
	  
	 str+="</tbody></table>";
	  
	 str+="<div class='text-center form-group bottom-btn-group'>" + 
	 '<button type="button" class="btn btn-success btn-sm" id="searchVideoByCameraBtn" onclick="searchVideoByCamera(\''+id+'\')"><i class="fa fa-search"></i><span> </span>查询</button><span> </span>'+
	 "</div>";
	   
	 str+="</div>";
	 $(".gisSearchList ul").empty().append(str);
	 var date = new Date();
	 $("#endTime").val(date.format("yyyy-MM-dd hh:mm:ss"));
	 date.setHours(date.getHours()-2);
	 $("#startTime").val(date.format("yyyy-MM-dd hh:mm:ss"));
	 return str;
}