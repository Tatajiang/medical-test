//初始化地图
var mapWidget ={ 
		polygonDrawFeature:null,//多边形框选
		markerArr:[],//框选的点位集合
		currentSelectMarker:null, //当前选则的点位
		baseLayer:null,
		view:null,
		map:null,
		isSector:false,//时候是扇形 
		mapCenterLon:null,
		mapCenterLat:null,
		clusters:null,
		boxDraw :null,//长方形框选
		shapes:[],//多边形
		polygonDraw:null,
		circularDraw:null,//点选
		popup:null,
		vectorSource:null,//多边形source
		vectorLayer:null,//多边形layer
		trackLayer:null,//轨迹的layer
		pointsLayer:null,//点layer
		pathFeature:[],//排查轨迹
		drawOverlay : null,
		selectPointerMove : null //鼠标经过交互
};
var  feature ;
//聚合图样式
var earthquakeFill = new ol.style.Fill({
  color: 'rgba(255, 153, 0, 0.8)'
});
var earthquakeStroke = new ol.style.Stroke({
  color: 'rgba(255, 204, 0, 0.2)',
  width: 1
});
var textFill = new ol.style.Fill({
  color: '#fff'
});
var textStroke = new ol.style.Stroke({
  color: 'rgba(0, 0, 0, 0.6)',
  width: 3
});
/**
 * 初始化地图  
 * 
 * isPgis false为加载Google离线瓦片地图  true为加载pgis地图
 * 注：使用Google离线地图是需将坐标转为火星坐标系  LatLonTransform.gcj_encrypt(纬度,经度)
 */
function initOLMap(){
	
	console.log(111);
    
	if(!isPgis){//谷歌地图
		  //瓦片格式转换函数
	   	  var pad = function pad(num, n) {
	       	var len = num.toString().length;
	       	while (len < n) {
	       		num = "0" + num;
	       		len++;
	       	}
	       	return num;
	      }
	   	  //创建底图瓦片图层
	   	  mapWidget.baseLayer= new ol.layer.Tile({
	           useInterimTilesOnError: false,
	           source : new ol.source.XYZ({
	               tileUrlFunction: function(tileCoord, pixelRatio, projection){
	               	var template = "/Gis_0001/static_map/{z}/{y}-{x}.png";
	               	var zRegEx = /\{z\}/g;
	               	var xRegEx = /\{x\}/g;
	               	var yRegEx = /\{y\}/g;
	               	if (!tileCoord) {
	                       return undefined;
	                     } else {
	                       return template.replace(zRegEx, tileCoord[0].toString())
	                           .replace(xRegEx, function(){
	                           	var curSize = Math.pow(2, tileCoord[0]);
	                           	var x = pad((tileCoord[1] % curSize), 6);
	                           	return x.toString()
	                           })
	                           .replace(yRegEx, function() {
	                             var curSize = Math.pow(2, tileCoord[0]);
	                             var y = pad(((-tileCoord[2] - 1) % curSize), 6);
	                             return y.toString();
	                           });
	                     }
	               }
	           })
	      });
	   	  mapWidget.baseLayer.set("layername", "baseLayer");
		   
			var center=null;
			var point = LatLonTransform.gcj_encrypt(parseFloat(mapCenterLatitude),parseFloat(mapCenterLongitude));//84坐标系转换成火星坐标系
			center = ol.proj.transform([point.lon, point.lat], "EPSG:4326", "EPSG:3857");
	      //创建地图对象
	      mapWidget.view = new ol.View({
         	  minZoom: 3,
         	  maxZoom: 21,
              zoom: 16,
			  projection: "EPSG:3857",
              center: center
	      });
	      mapWidget.map= new ol.Map({
	           target: 'map_camera',
	           view: mapWidget.view,
	           logo: null
	      });
		  //添加基础图层
	      mapWidget.map.addLayer(mapWidget.baseLayer);
	      mapWidget.vectorSource= new ol.source.Vector({wrapX: false});
	  	  mapWidget.vectorLayer = new ol.layer.Vector({
	        source: mapWidget.vectorSource
	      });
	  	mapWidget.map.addLayer(mapWidget.vectorLayer);
	}else if(isPgis){//PGIS 地图
		//创建底图瓦片图层
		mapWidget.baseLayer = new ol.layer.Tile({
	        useInterimTilesOnError: false,
	        source : new ol.source.XYZ({
	        	projection: 'EPSG:4326',
	        	tileGrid: new ol.tilegrid.TileGrid({
	                origin: [0, 0],
	                maxZoom: 21,
	            	minZoom: 1,
	                resolutions: [
	               	   2, 1, 0.5,
	                   0.25, 0.125, 0.0625,
	                   0.03125, 0.015625, 0.0078125,
	                   0.00390625, 0.001953125, 0.0009765625,
	                   0.00048828125, 0.000244140625, 0.0001220703125,
	                   0.00006103515625, 0.000030517578125, 0.0000152587890625,
	                   0.00000762939453125, 0.000003814697265625, 0.0000019073486328125
	                ],
	                tileSize: [256, 256]
	            }),
	            tileUrlFunction: function(tileCoord, pixelRatio, projection){
	            	var crosPath = $ctx + '/service/rest/app.AppBasicService/collection/getHttpCorsStream?path=';
	            	var template = titeServePath+"/Maps/default/EzMap?Service=getImage&Type=RGB&Col={x}&Row={y}&Zoom={z}&V=0.3";
	            	var zRegEx = /\{z\}/g;
	            	var xRegEx = /\{x\}/g;
	            	var yRegEx = /\{y\}/g;
	            	if (!tileCoord) {
	                    return undefined;
	                  } else {
	                    return crosPath + encodeURIComponent(template.replace(zRegEx, tileCoord[0].toString())
	                        .replace(xRegEx, function(){
	                        	var x = tileCoord[1];
	                        	return x.toString()
	                        })
	                        .replace(yRegEx, function() {
	                          var y = tileCoord[2];
	                          return y.toString();
	                        }));
	                  }
	            }
	        })
	    });
		mapWidget.baseLayer.set("layername", "baseLayer");
		//创建影像瓦片图层
		var yxLayer = new ol.layer.Tile({
	        useInterimTilesOnError: false,
	        source : new ol.source.XYZ({
	        	projection: 'EPSG:4326',
	        	tileGrid: new ol.tilegrid.TileGrid({
	                origin: [0, 0],
	                maxZoom: 21,
	            	minZoom: 14,
	                resolutions: [
	               	   2, 1, 0.5,
	                   0.25, 0.125, 0.0625,
	                   0.03125, 0.015625, 0.0078125,
	                   0.00390625, 0.001953125, 0.0009765625,
	                   0.00048828125, 0.000244140625, 0.0001220703125,
	                   0.00006103515625, 0.000030517578125, 0.0000152587890625,
	                   0.00000762939453125, 0.000003814697265625, 0.0000019073486328125
	                ],
	                tileSize: [256, 256]
	            }),
	            tileUrlFunction: function(tileCoord, pixelRatio, projection){
	            	var crosPath = $ctx + '/service/rest/app.AppBasicService/collection/getHttpCorsStream?path=';
	            	var template = titeServePath+"/Maps/default/EzMap?Service=getImage&Type=RGB&Col={x}&Row={y}&Zoom={z}&V=0.3";
	            	var zRegEx = /\{z\}/g;
	            	var xRegEx = /\{x\}/g;
	            	var yRegEx = /\{y\}/g;
	            	if (!tileCoord) {
	                    return undefined;
	                  } else {
	                    return crosPath + encodeURIComponent(template.replace(zRegEx, tileCoord[0].toString())
	                        .replace(xRegEx, function(){
	                        	var x = tileCoord[1];
	                        	return x.toString()
	                        })
	                        .replace(yRegEx, function() {
	                          var y = tileCoord[2];
	                          return y.toString();
	                        }));
	                  }
	            }
	        })
	    });
		mapWidget.baseLayer.set("layername", "yxLayer");
		//mapWidget.baseLayer.set("layername", "clusters");
	    //创建地图对象
		var lon,lat;
    	lon = parseFloat(mapCenterLongitude);
    	lat = parseFloat(mapCenterLatitude);
	    mapWidget.view = new ol.View({
            zoom: 16,
            projection: "EPSG:4326",
            center: [lon, lat]
    	});
		mapWidget.map  = new ol.Map({
	        target: 'map_camera',
	        view: mapWidget.view,
	        logo: null
	    });
	    //添加基础图层
		//mapWidget.map.addLayer(yxLayer);
		mapWidget.map.addLayer(mapWidget.baseLayer);
	}

	mapWidget.vectorSource= new ol.source.Vector({wrapX: false});
	mapWidget.vectorLayer = new ol.layer.Vector({
      source: mapWidget.vectorSource
    });
	mapWidget.map.addLayer(mapWidget.vectorLayer);
	//创建聚合点图层
	mapWidget.pointsLayer= new ol.layer.Vector({
    	source: new ol.source.Vector()
    }); 
	mapWidget.pointsLayer.set("layername", "points");
    mapWidget.map.addLayer(mapWidget.pointsLayer);//添加点图层
    
    var isInit = true;//是否是地图初始化
    var preZoom = mapWidget.map.getView().getZoom();
    mapWidget.map.on('moveend',function(e){
    	if(mapType == 'trajectory' || mapType == 'dynamicFace') return;
    	
    	var zoom = mapWidget.map.getView().getZoom();
    	var ex = /^\d+$/;
    	if (ex.test(zoom) && !isInit) {
    		if(zoom >= 15 || (preZoom > 15 && zoom <= 15)){
    			mapWidget.map.removeLayer(mapWidget.clusters);
    			mapWidget.clusters = null;
    			delete mapWidget.clusters;
            	//显示点位信息
    			queryCurrentViewCameraData();
    		}
    		if(zoom <= 5){
    			mapWidget.map.getView().setZoom(5);
    		}
    		if(zoom >= 20){
    			mapWidget.map.getView().setZoom(20);
    		}
    		preZoom = zoom;
    	}
    	isInit = false;
    	
    });
    
    mapWidget.selectPointerMove = new ol.interaction.Select({
        condition: ol.events.condition.pointerMove,
        style: styleFunction
    });
    
    //鼠标移动事件交互
    mapWidget.map.addInteraction(mapWidget.selectPointerMove);
    mapWidget.selectPointerMove.on('select', function(e) {
    	
    	mapPointSelect(e);
    });
    
    mapWidget.map.on("click", function(evt) {
    	if(window.evt) 
    		evt.cancelBubble = true;
    	else 
    		evt.stopPropagation();
    	if(mapType == 'case'){
    		mapPointClick(evt);
    	}else if(mapType == 'trajectory'){
    		mapCarPointView(evt);
    	}else if(mapType == 'dynamicFace'){
    		mapFacePointView(evt);
    	}else{
    		mapPointCilck(evt);
    	}
	});
    container = document.getElementById('popup');
	mapWidget.popup = new ol.Overlay(({
        element: container,
        autoPan: true,
        autoPanAnimation: {
          duration: 250
        }
	}));
    mapWidget.map.addOverlay(mapWidget.popup);
 // 绘制好的标绘符号，添加到FeatureOverlay显示。
    mapWidget.drawOverlay = new ol.layer.Vector({
		source : new ol.source.Vector()
	});
}

/**
 * 自定义地图鹰眼
 * @returns {ol.Map}
 */
function setOverview(){
	
	var source,view;
	
	if(!isPgis){
		var overviewcenter=null;
		if(param.orgLat && param.orgLon){
			overviewcenter = LatLonTransform.gcj_encrypt(parseFloat(param.orgLat),parseFloat(param.orgLon));
			overviewcenter = ol.proj.transform([overviewcenter.lon, overviewcenter.lat], "EPSG:4326", "EPSG:3857");
			//center = ol.proj.transform([parseFloat(param.orgLon), parseFloat(param.orgLat)], "EPSG:4326", "EPSG:3857")
		}else{
			var point = LatLonTransform.gcj_encrypt(parseFloat(mapWidget.mapCenterLat),parseFloat(mapWidget.mapCenterLon));//84坐标系转换成火星坐标系
			overviewcenter = ol.proj.transform([point.lon, point.lat], "EPSG:4326", "EPSG:3857");
		}
		
		source = new ol.source.XYZ({
            tileUrlFunction: function(tileCoord, pixelRatio, projection){
               	var template = "/Gis_0001/static_map/{z}/{y}-{x}.png";
               	var zRegEx = /\{z\}/g;
               	var xRegEx = /\{x\}/g;
               	var yRegEx = /\{y\}/g;
               	if (!tileCoord) {
                       return undefined;
                     } else {
                       return template.replace(zRegEx, tileCoord[0].toString())
                           .replace(xRegEx, function(){
                           	var curSize = Math.pow(2, tileCoord[0]);
                           	var x = pad((tileCoord[1] % curSize), 6);
                           	return x.toString()
                           })
                           .replace(yRegEx, function() {
                             var curSize = Math.pow(2, tileCoord[0]);
                             var y = pad(((-tileCoord[2] - 1) % curSize), 6);
                             return y.toString();
                           });
                     }
               }
           });
		
		view = new ol.View({  
            projection: 'EPSG:3857',  
            center:overviewcenter,
            zoom:12
        });
	}else{
		
		var lon,lat;
		if(param.orgLat && param.orgLon){
		    lon = parseFloat(param.orgLon);
		    lat = parseFloat(param.orgLat);
		}else{
	    	lon = parseFloat(mapWidget.mapCenterLon);
	    	lat = parseFloat(mapWidget.mapCenterLat);
	    }
		
		source = new ol.source.XYZ({
        	projection: 'EPSG:4326',
        	tileGrid: new ol.tilegrid.TileGrid({
                origin: [0, 0],
                maxZoom: 21,
            	minZoom: 1,
                resolutions: [
               	   2, 1, 0.5,
                   0.25, 0.125, 0.0625,
                   0.03125, 0.015625, 0.0078125,
                   0.00390625, 0.001953125, 0.0009765625,
                   0.00048828125, 0.000244140625, 0.0001220703125,
                   0.00006103515625, 0.000030517578125, 0.0000152587890625,
                   0.00000762939453125, 0.000003814697265625, 0.0000019073486328125
                ],
                tileSize: [256, 256]
            }),
            tileUrlFunction: function(tileCoord, pixelRatio, projection){
            	var template = titeServePath+"/Maps/default/EzMap?Service=getImage&Type=RGB&Col={x}&Row={y}&Zoom={z}&V=0.3";
            	var zRegEx = /\{z\}/g;
            	var xRegEx = /\{x\}/g;
            	var yRegEx = /\{y\}/g;
            	if (!tileCoord) {
                    return undefined;
                  } else {
                    return template.replace(zRegEx, tileCoord[0].toString())
                        .replace(xRegEx, function(){
                        	var x = tileCoord[1];
                        	return x.toString()
                        })
                        .replace(yRegEx, function() {
                          var y = tileCoord[2];
                          return y.toString();
                        });
                  }
            }
        });
		
		view = new ol.View({  
            projection: 'EPSG:4326',  
            center:[lon, lat],
            zoom:12
        });
	}
	
	return new ol.Map({
    	interactions: ol.interaction.defaults().extend([new app.Drag()]),
        target: 'overview',  
        layers:[new ol.layer.Tile({
            useInterimTilesOnError: false,
            source : source
        })],  
        view: view,
        logo: null
    });
}

/**
 * 点位信息弹窗
 * @param feature
 */
function setPointPosition(feature,type2){
	var coordinate = feature.getGeometry().getCoordinates();
	var name = feature.values_.name;
	var id = feature.values_.id;
	var address = feature.values_.cameraDeptName;
	var direction = feature.values_.direction;
	var type = feature.values_.type;
	var html = '';
	var coordinate = feature.getGeometry().getCoordinates();
	if(!type2||type2=='camera'){

		html += '<div>';
		html += '	<p>监控点名称：</p>';
		html += '	<p>'+ name +'</p>';
		html += '	<p>监控点编号：</p>';
		html += '	<p>'+ address +'</p>';
		html += '	<p>所属区域：'+ (direction == null?"":direction) +'</p>';
		//html += '	<p>'+ (direction == null?"":direction) +'</p>';
		html += '	<p>类型：'+ (type == 0?"球机":"枪机") +'</p>';
		html += '	<div style="height: 36px;width: 90%;border: 1px solid #ddd;margin:0 auto;">';
		html += '		<div class="pop_player"><span onclick="realtimePlayer(\''+address+'\',\''+name+'\');">播放</span></div>';
		html += '		<div class="history_player"><span onclick="historyStreamPlay1(\''+address+'\',\''+name+'\');">历史回放</span></div>';
		html += '		<div class="pop_addcheck" onclick="selectCamera('+"'"+address+"'"+","+"'"+name+"',"+"'"+id+"'"+')"><span>加入已选</span></div>';
		html += ' </div>';
	}else if (type2=='poi'){
		html += '<div style="margin-left:10px;">';
		html += '	<p>' + name +'</p>';
		html += '</div>';
		html += '<p class="pointIcon"><i class="iconfont icon-zuobiao"></i></p>';
	}else if(type2 == 'case'){
		html += '<p class="pointIcon" style="font-size:30px;"><i class="fa fa-map-marker"></i></p>';
	}else if(type2 == 'trace'){
		
	}
	$("#popup-content").html(html);
	mapWidget.popup.setPosition(coordinate);
	
	$("#popup-closer").on('click',function(){
		mapWidget.popup.setPosition(undefined);
		$("#popup-closer").blur();
		return false;
	});
}

/**
 * 创建单个点的聚合样式
 * @param feature
 * @returns {ol.style.Style}
 */
function createEarthquakeStyle(feature) {
    var name = feature.get('name');
    var magnitude = 6;
    var radius = 5;
    var type = feature.get('type');
    var imgsrc = $ctx +'/js/img/camera_ball_online.png';
    if(type == 1){
    	imgsrc = basePath+'static/images/camera_icon/camera_ptz_online.png';
    }
    if(type == 2){
    	imgsrc = $ctx + '/js/img/up.png';
    }
    if(type == 3){
    	imgsrc = $ctx + '/js/img/down.png';
    }
    return new ol.style.Style({
      geometry: feature.getGeometry(),
      image: new ol.style.Icon({
    	  anchor : [0.5,0.96],
			crossOrigin: 'anonymous',
			opacity : 1,
	       src: imgsrc
      })
    }); 
}

function changeExtentToPoints(extent){
	var points='';
	points+=(extent[0]+":"+extent[1]);
	points+=(","+extent[0]+":"+extent[3]);
	points+=(","+extent[2]+":"+extent[1]);
	points+=(","+extent[2]+":"+extent[3]);
	return points;
}
/**
 * 查看当前视图监控点数据
 */
function queryCurrentViewCameraData(){
	var size =mapWidget.map.getSize();
	var extent = mapWidget.view.calculateExtent(size);
	var points='';
	if(!isPgis){
		extent = ol.proj.transformExtent(extent,ol.proj.get("EPSG:3857"),ol.proj.get("EPSG:4326"));
	}
	var tleft = ol.extent.getTopLeft(extent);
	var tright =ol.extent.getTopRight(extent);
	var bright= ol.extent.getBottomRight(extent);
	var bleft = ol.extent.getBottomLeft(extent);
	if(!isPgis){
		tleft[0]= LatLonTransform.gcj_decrypt(tleft[1],tleft[0]).lon;
		tleft[1]= LatLonTransform.gcj_decrypt(tleft[1],tleft[0]).lat;
		tright[0]= LatLonTransform.gcj_decrypt(tright[1],tright[0]).lon;
		tright[1]= LatLonTransform.gcj_decrypt(tright[1],tright[0]).lat;
		bright[0]= LatLonTransform.gcj_decrypt(bright[1],bright[0]).lon;
		bright[1]= LatLonTransform.gcj_decrypt(bright[1],bright[0]).lat;
		bleft[0]= LatLonTransform.gcj_decrypt(bleft[1],bleft[0]).lon;
		bleft[1]= LatLonTransform.gcj_decrypt(bleft[1],bleft[0]).lat;
	}
	
	//points = changeExtentToPoints(extent);
	points += (tleft[0]+":"+tleft[1]);
	points+=(","+tright[0]+":"+tright[1]);
	points+=(","+bright[0]+":"+bright[1]);
	points+=(","+bleft[0]+":"+bleft[1]);
	if(mapType == 'camera'){
		queryCongruentPointData(points);
	}
}

/**
 * 查询聚合点的点位数据
 * @param polygon
 */
function queryCongruentPointData(polygon){
	var url = $ctx + '/service/rest/app.AppMapService/collection/queryCamerasInPolygon';
	$.ajax({
		url: url,
        type: "POST", 
        dataType:'json',
        data:{
        	"polygon":polygon
        },
        success : function(data){
            if(data.code == 0){
             var cameras = JSON.parse(data.data);
                 if(cameras.length == 0){
                     	return;
                   }
                   renderPointView(cameras);
            }
        },
		failure : function() {
			layer.alert('操作超时!');
		}
	});
}

var maxFeatureCount;
/**
 * 计算聚合圆大小信息
 */
function calculateClusterInfo(resolution) {
	maxFeatureCount = 0;
	var features = mapWidget.clusters.getSource().getFeatures();
	var feature, radius;
	for (var i = features.length - 1; i >= 0; --i) {
	  feature = features[i];
	  var originalFeatures = feature.get('features');
	  var extent = ol.extent.createEmpty();
	  var j, jj;
	  for (j = 0, jj = originalFeatures.length; j < jj; ++j) {
	    ol.extent.extend(extent, originalFeatures[j].getGeometry().getExtent());
	  }
	 
	  maxFeatureCount = Math.max(maxFeatureCount, jj);
	  radius = 0.25 * (ol.extent.getWidth(extent) + ol.extent.getHeight(extent)) /
	      resolution;
	  feature.set('radius', radius);
	}
}

/**
 * 渲染聚合视图
 * @param data
 */
function renderPointView(data){
	//初始化地图
	var features = [];
	$.each(data, function(i){
		var lat = this.latitude;
		var lon = this.longitude;
		if(lat && lon){
			if(!isPgis){
				var latlon = LatLonTransform.gcj_encrypt(parseFloat(lat), parseFloat(lon));
				features[i] = new ol.Feature(new ol.geom.Point(ol.proj.transform([latlon.lon, latlon.lat], "EPSG:4326", "EPSG:3857")));
			}else{
				features[i] = new ol.Feature(new ol.geom.Point([parseFloat(lon), parseFloat(lat)]));
			}
			features[i].set('name', this.name);
			features[i].set('sign','point_render');
			features[i].setProperties(this);
			if(mapType == 'trajectory'){
				features[i].set('sign','point_trace');
			}
		}
	});
	//聚合图
	var clusterSource =null;
	if(!isPgis){
		clusterSource = new ol.source.Cluster({
		  distance: 35,
		  projection: ol.proj.get("EPSG:3857"),//"EPSG:4326", "EPSG:3857"
		  source: new ol.source.Vector({
			  features: features
		  })
		});
	}else{
		var distance = 30;
		if(mapWidget.map.getView().getZoom() >= 16){
			distance = 0;
		}
		clusterSource = new ol.source.Cluster({
		  distance: distance,
		  projection: ol.proj.get("EPSG:4326"),
		  source: new ol.source.Vector({
			  features: features
		  })
		});
	}
	
	
	//遍历地图所有图层, 存在热力图层则添加数据，没有则创建
    var layers =mapWidget.map.getLayers(), f = true;
    layers.forEach(function(layer, index, array){
    	var layername = layer.get("layername");
    	if(layername == "clusters"){
    		layer.setSource(clusterSource);
    	//	mapWidget.view.setZoom(mapWidget.view.getZoom() - 1);
    		f = false;
    	}else{
    	  //  mapWidget.map.removeLayer(layer);
    	}
    }, this);
    
    //清除覆盖物
    /*var overlays=  mapWidget.map.getOverlays();
    overlays.forEach(function(overlay, index, array){
    	overlay.setPosition(undefined);
    }, this);*/
    //创建聚合点图层
    if(f){
        mapWidget.clusters = new ol.layer.Vector({
          source: clusterSource,
          style: styleFunction
        }); 
        mapWidget.clusters.set("layername", "clusters");
        mapWidget.map.addLayer(mapWidget.clusters);
    }
    
}


/**
 * 聚合图样式
 */
var currentResolution;
function styleFunction(feature, resolution) {
	if (resolution != currentResolution) {
		calculateClusterInfo(resolution);
		currentResolution = resolution;
	}
	var style, size,zoom;
	zoom = mapWidget.map.getView().getZoom();
	if (feature && feature.get('features')) {
		size = feature.get('features').length;
	}else{
		return;
	}
	if (zoom < 16 && size >0) {
		var radius = feature.get('radius');
		if(!radius){
			calculateClusterInfo(resolution);
			currentResolution = resolution;
		}
		style = new ol.style.Style({
			image : new ol.style.Circle({
				radius : radius,
				fill : new ol.style.Fill({
					color : [ 255, 153, 0,
							Math.min(0.8, 0.4 + (size / maxFeatureCount)) ]
				})
			}),
			text : new ol.style.Text({
				text : size.toString(),
				fill : textFill,
				stroke : textStroke
			})
		});
	}else if (zoom >= 16 && size >1) {
		style = new ol.style.Style({
			image: new ol.style.Icon({
				anchor : [0.5,0.96],
				crossOrigin: 'anonymous',
				opacity : 1,
          		src: $ctx +'/js/img/polo.png'
        	})
		});
	}
	else {
		if (feature) {
			var originalFeature = feature.get('features')[0];
			style = createEarthquakeStyle(originalFeature);
		}
	}
	return style;
}

/**
 * 地图点位鼠标经过事件
 * @param features
 */
var popupTimeout = 0;
function mapPointSelect(event){
	event.stopPropagation();
	if(event.selected.length > 0){
		var features = event.selected[0].get('features');
		if(features){
			var html = '';
			var coordinate = event.mapBrowserEvent.coordinate;
			$.each(features,function(){
				
				var camera = {};
				camera.id = this.values_.id;
				camera.name = this.values_.name;
				camera.type = this.values_.type;
				camera.address = this.values_.address;
				camera.direction = this.values_.direction;
				camera.longitude = this.values_.longitude;
				camera.latitude = this.values_.latitude;
				html += '<p style="cursor: pointer;" onclick="/*drawPointAndShowPointDetaill('+ JSON.stringify(camera).replace(/\"/g,"\'") +');*/">'+ this.values_.name +'</p>';
			});
			$(".pointmove_tip").html(html);
			$(".pointmove_tip").show();
			var endx = window.event.pageX - 15;   //计算div的最终位置
			var endy = window.event.pageY - 25;
			var boxy = $("#map_camera").offset().top;
			var boxx = $("#map_camera").offset().left;
			var boxwidth = $("#map_camera").width();
			var boxheight = $("#map_camera").height();
			var tipwidth = $(".pointmove_tip").width();
			var tipheight = $(".pointmove_tip").height();
			
			endx = endx - boxx;
			endy = endy - boxy + 45;
			
			if((endx+tipwidth + 25)>(boxwidth + boxx)){
				endx = endx - tipwidth - 25;
				endx = endx + boxx;
			}
			
			if((endy+tipheight + 25)>(boxheight + boxy)){
				endy = endy - tipheight;
				endy = endy + boxy;
			}
			
			$(".pointmove_tip").css("left",endx+"px").css("top",endy +"px");
		}
	}else{
		window.clearTimeout(popupTimeout);
		popupTimeout = setTimeout(function(){
			$(".pointmove_tip").hide();
		},2000);
	}
}

function mapPointClick(event){
	var longitude = event.coordinate[0];
	var latitude = event.coordinate[1];
	var camera = {
		longitude:longitude,
		latitude:latitude,
		type:0,
		type2:'case'
	};
	
	var url = $ctx + '/service/rest/search.CaseSearchService/collection/selectPoliceComprehensiveByRadius';
	$.ajax({
		url: url,
        type: "POST", 
        dataType:'json',
        data:{
        	lon:longitude,
        	lat:latitude,
        	distance:5
        },
        success : function(data){
        	
        	loadTableData(data);

        	if(data.length == 0){
                return;
            }
            
            clearMapFeatures();
            
            renderPointView(data);

            drawPointAndShowPointDetaill(camera);
        },
		failure : function() {
			layer.alert('操作超时!');
		}
	});
}

function mapCarPointView(event){
	mapWidget.map.forEachFeatureAtPixel(event.pixel, function(feature) {
		var coordinate = feature.getGeometry().getCoordinates();

		var obj = feature.values_.features[0].values_;
		var name = obj.name;
		var occurTime = obj.upTime || obj.downTime;
		var html = '';
		html += '<div style="margin:10px;">';
		html += '	<p>点位信息:<code>' + name +'</code></p>';
		html += '	<p style="margin-top:15px;">目标出现时间:<code>' + occurTime +'</code></p>';
		html += '</div>';
		$("#popup-content").html(html);
		mapWidget.popup.setPosition(coordinate);
		$("#popup-closer").on('click',function(){
    		mapWidget.popup.setPosition(undefined);
            $("#popup-closer").blur();
            return false;
		});
	});
}

function mapFacePointView(event){
	mapWidget.map.forEachFeatureAtPixel(event.pixel, function(feature) {
		var coordinate = feature.getGeometry().getCoordinates();
		
		var html = '';
		if(feature.values_.features.length > 1){
			var coordinate = feature.getGeometry().getCoordinates();
			var features = feature.values_.features;
			html += '<div style="margin:10px;">';
			$.each(features,function(){
				html += '	<p>点位信息:<code>' + this.values_.name +'</code></p>';
			});
			html += '</div>';
		}else{
			var obj = feature.values_.features[0].values_;
			var name = obj.name;
			html += '<div style="margin:10px;">';
			html += '	<p>点位信息:<code>' + name +'</code></p>';
			html += '</div>';
		}
		$("#popup-content").html(html);
		mapWidget.popup.setPosition(coordinate);
		$("#popup-closer").on('click',function(){
    		mapWidget.popup.setPosition(undefined);
            $("#popup-closer").blur();
            return false;
		});
	});
}

/**
 * 地图点位鼠标点击事件
 * @param features
 */
function mapPointCilck(event){
  //  var long = event.coordinate[0];
 //   var lati = event.coordinate[1];
 // queryCameraByRadius(long,lati,1000,mapLevel);
	mapWidget.map.forEachFeatureAtPixel(event.pixel, function(feature) {
		
		//点击查看点位信息
		if(feature && feature.values_ && feature.values_.sign =='point_sign'){
  		  setPointPosition(feature);
		}
		//目标出现点
		if(feature && feature.values_ && feature.values_.sign =='point_trace'){
			var coordinate = feature.getGeometry().getCoordinates();
			var name = feature.values_.name;
			var occurTime = feature.values_.occurTime;
			var html = '<p>点位信息:</p><code>' + name +'</code>';
			html += '<p style="margin-top:15px;">目标出现时间:</p><code>' + occurTime +'</code>';
			$("#popup-content").html(html);
			mapWidget.popup.setPosition(coordinate);
			$("#popup-closer").on('click',function(){
  	    		mapWidget.popup.setPosition(undefined);
  	            $("#popup-closer").blur();
  	            return false;
			});
		}
		
		//单个点位显示点位详情  多个点位显示点位列表
		if(feature && feature.values_ && feature.values_.features && feature.values_.features.length ==1 && feature.values_.features[0].values_.sign == 'point_render'){
			setPointPosition(feature.values_.features[0]);
		}else if(feature && feature.values_ && feature.values_.features && feature.values_.features.length > 1){
  		  
			var html = '';
			var coordinate = feature.getGeometry().getCoordinates();
			var features = feature.values_.features;
			$.each(features,function(){
				var camera = {};
				camera.id = this.values_.id;
				camera.name = this.values_.name;
				camera.type = this.values_.type;
				camera.address = this.values_.address;
				camera.direction = this.values_.direction;
				camera.longitude = this.values_.longitude;
				camera.latitude = this.values_.latitude;
				html += '<p style="cursor: pointer;" onclick="/*drawPointAndShowPointDetaill('+ JSON.stringify(camera).replace(/\"/g,"\'") +');*/">'+ this.values_.name +'</p>';
			});
	  		$(".pointmove_tip").html(html);
			$(".pointmove_tip").show();
			var endx = window.event.pageX - 15;   //计算div的最终位置
			var endy = window.event.pageY - 25;
			var boxy = $("#map_camera").offset().top;
			var boxx = $("#map_camera").offset().left;
			var boxwidth = $("#map_camera").width();
			var boxheight = $("#map_camera").height();
			var tipwidth = $(".pointmove_tip").width();
			var tipheight = $(".pointmove_tip").height();
			
			endx = endx - boxx;
			endy = endy - boxy;
			
			if((endx+tipwidth + 25)>(boxwidth + boxx)){
				endx = endx - tipwidth - 25;
				endx = endx + boxx;
			}
			
			if((endy+tipheight + 25)>(boxheight + boxy)){
				endy = endy - tipheight;
				endy = endy + boxy;
			}
			
			$(".pointmove_tip").css("left",endx+"px").css("top",endy +"px");
		}
		return feature;
	});
}

/**
 * 添加点位并显示点位详情
 */
var currentPointFeature = null;
function drawPointAndShowPointDetaill(camera){
	
	
	if(!camera.latitude || !camera.longitude || parseInt(camera.latitude) == 0 || parseInt(camera.longitude) == 0){
		alert('该点位经纬度信息不全，无法定位点位位置！');
		return;
	}
	var type = camera.type;
	var type2 = camera.type2;
	if(type2==null||type2==''||type2==undefined){
		type2='camera';
	}

	if(currentPointFeature){
		mapWidget.drawOverlay.getSource().removeFeature(currentPointFeature);
	}
	var type = camera.type;
    var imgsrc = $ctx +'/js/img/camera_ball_online.png';
    if(type == 1){
    	imgsrc = basePath+'static/images/camera_icon/camera_ptz_online.png';
    }
	if(type2 == "poi"){
	    imgsrc = basePath+'static/images/camera_icon/icon-point.png';
    }
	if(type2 == "case"){
    	imgsrc = basePath+'static/images/camera_icon/icon-point.png';
	}
	var iconStyle = new ol.style.Style({
		image : new ol.style.Icon({
			anchor : [0.5,0.96],
			crossOrigin: 'anonymous',
			opacity : 1,
			src : imgsrc
		})
	});
	var iconFeature;
	if(!isPgis){
		var latlon = LatLonTransform.gcj_encrypt(parseFloat(camera.latitude), parseFloat(camera.longitude));
		iconFeature = new ol.Feature(new ol.geom.Point(ol.proj.transform([latlon.lon, latlon.lat], "EPSG:4326", "EPSG:3857")));
	}else{
		iconFeature = new ol.Feature(new ol.geom.Point([parseFloat(camera.longitude), parseFloat(camera.latitude)]));
	}
	iconFeature.setStyle(iconStyle);
	iconFeature.setProperties(camera);
	iconFeature.set('sign','point_sign');
	/*mapWidget.pathFeature.push(iconFeature);
	mapWidget.drawOverlay.setMap(mapWidget.map);
	mapWidget.drawOverlay.getSource().addFeature(iconFeature);*/
	setPointPosition(iconFeature,type2);
	//currentPointFeature = iconFeature;
	if(type2 != 'case'){
		mapWidget.map.getView().setCenter(iconFeature.getGeometry().getCoordinates());
	}
}

/**
 * 初始化框选
 */
var isBoxActive =false;
function addBoxInteraction(){
	
	if(isPolygonActive || isBoxActive){
		return;
	}
	
	mapWidget.map.removeInteraction(mapWidget.selectPointerMove);
	 var geometryFunction = ol.interaction.Draw.createBox();
	 mapWidget.boxDraw =  new ol.interaction.Draw({
         source: mapWidget.vectorSource,
         type: 'Circle',
         freehand: true,
         geometryFunction: geometryFunction
     });
	 
	 //showPolygonTip('按住鼠标不放，拖动选取范围，放开鼠标完成框选');
/*	 mapWidget.boxDraw.on("drawstart",function(e){
		 hidePolgonTip();
	 });*/
      mapWidget.boxDraw.on("drawend",function(e){
    	  mapWidget.shapes.push(e.feature);
    	  var coordinates = e.feature.getGeometry().getCoordinates()[0];
    	  var points = '';
    	  $.each(coordinates,function(){
	   	  		var coordinate = this;
	   	  		if(!isPgis){
	   	  			coordinate = ol.proj.transform(coordinate, "EPSG:3857","EPSG:4326");
	   	  			var point = LatLonTransform.gcj_decrypt(coordinate[1],coordinate[0]);
	   	  			points+=(','+point.lon+':'+point.lat);
	   	  		}else{
	   	  			points+=(','+coordinate[0]+':'+coordinate[1]);
	   	  		}
	   	  		
   	  	  });
    	  mapWidget.map.removeInteraction(mapWidget.boxDraw);
    	  mapWidget.isSector = false;
    	  isBoxActive = false;
    	  mapWidget.map.addInteraction(mapWidget.selectPointerMove);
    	  //JsInterFace.getPoints(points);
    	  //queryCamerasInPolygon(points);
    	  //获取选中的点位信息
    	  //boxValue(points);
      });
      $(mapWidget.map.getViewport()).on("contextmenu", function(event){
	    	event.preventDefault();
	        mapWidget.map.removeInteraction(mapWidget.boxDraw);
	        isBoxActive = false;
	        // 书写事件触发后的函数
	    });
      mapWidget.map.addInteraction(mapWidget.boxDraw);
      isBoxActive = true;
}

/**
 * 自定义框选
 */
var isPolygonActive =false;
function addPolygonInteraction(){
	if(isPolygonActive || isBoxActive){
		return;
	}
	mapWidget.map.removeInteraction(mapWidget.selectPointerMove);
	mapWidget.polygonDraw =  new ol.interaction.Draw({
        source: mapWidget.vectorSource,
        type: 'Polygon',
        freehand: true
      });
	//showPolygonTip('按住鼠标不放，拖动选取范围，放开鼠标确定范围');
	/* mapWidget.polygonDraw.on("drawstart",function(e){
		 hidePolgonTip();
	 });*/
    mapWidget.polygonDraw.on("drawend",function(e){
    	mapWidget.map.addInteraction(mapWidget.selectPointerMove);
    	mapWidget.shapes.push(e.feature);
   	  	var coordinates = e.feature.getGeometry().getCoordinates()[0];
   	  	var points ='';
   	  	$.each(coordinates,function(){
	   	  	var coordinate = this;
   	  		if(!isPgis){
   	  			coordinate = ol.proj.transform(coordinate, "EPSG:3857","EPSG:4326");
   	  			var point = LatLonTransform.gcj_decrypt(coordinate[1],coordinate[0]);
   	  			points+=(','+point.lon+':'+point.lat);
   	  		}else{
   	  			points+=(','+coordinate[0]+':'+coordinate[1]);
   	  		}
   	  	});
   	  mapWidget.map.removeInteraction(mapWidget.polygonDraw);
   	  mapWidget.isSector = false;
   	  isPolygonActive  = false;
   	  //queryCamerasInPolygon(points);
   	  //JsInterFace.getPoints(points);
   	  //TODO
    });
    $(mapWidget.map.getViewport()).on("contextmenu", function(event){
    	event.preventDefault();
        mapWidget.map.removeInteraction(mapWidget.polygonDraw);
        isPolygonActive = false;
        // 书写事件触发后的函数
    });
    mapWidget.map.addInteraction(mapWidget.polygonDraw);
    isPolygonActive = true;
}

/**
 * 清除地图
 */
function clearMapFeatures(){
	if(isPolygonActive || isBoxActive){
		mapWidget.map.addInteraction(mapWidget.selectPointerMove);
		//CollectGarbage();
	}
	isBoxActive =false;
	isPolygonActive =false;
	if(mapWidget.boxDraw){
		mapWidget.map.removeInteraction(mapWidget.boxDraw);
		mapWidget.boxDraw = null;
		//CollectGarbage();
	}
	if(mapWidget.polygonDraw){
		mapWidget.map.removeInteraction(mapWidget.polygonDraw);
		mapWidget.polygonDraw = null;
		//CollectGarbage();
	}
	if(mapWidget.shapes.length > 0){
		$.each(mapWidget.shapes,function(){
			mapWidget.vectorLayer.getSource().removeFeature(this);
		});
	}
	
	if(currentPointFeature){
		mapWidget.drawOverlay.getSource().removeFeature(currentPointFeature);
		currentPointFeature = null;
	}
	mapWidget.shapes = [];
	getFeature();
}

/**
 * 遍历图层查找feature，改变图标颜色
 * @param cameraAddress  点位标志
 * @param isShowPosition 是否显示点位信息弹窗
 */
function getFeature(cameraAddress,isShowPosition){  
   var layers = mapWidget.map.getLayers().getArray(); 
   for(var i=0;i<layers.length;i++){
       var source = layers[i].getSource();  
       if(source instanceof ol.source.Vector){
          var features = source.getFeatures();
          if(features.length>0){
             for(var j=0;j<features.length;j++){
            	 var feature = features[j].get('features');
            	 var imgsrc = '';
            	 if( feature && feature.length == 1){
            		 var address = feature[0].values_.address;
            		 var type = feature[0].values_.type;
            		 if(cameraAddress == address){
            			 imgsrc = basePath+'static/images/camera_icon/camera_ball_online_hover.png';
            		     if(type == 1){
            		    	 imgsrc = basePath+'static/images/camera_icon/camera_ptz_online_hover.png';
            		     }
            		     features[j].setStyle(new ol.style.Style({
                 			image: new ol.style.Icon({
                 				anchor : [0.5,0.96],
                 				crossOrigin: 'anonymous',
                 				opacity : 1,
                 				src: imgsrc
                 			})
                 		}));
            		 }
            		 if(!cameraAddress){
            			 imgsrc = $ctx +'/js/img/camera_ball_online.png';
            		     if(type == 1){
            		    	 imgsrc = basePath+'static/images/camera_icon/camera_ptz_online.png';
            		     }
            		     features[j].setStyle(new ol.style.Style({
                 			image: new ol.style.Icon({
                 				anchor : [0.5,0.96],
                 				crossOrigin: 'anonymous',
                 				opacity : 1,
                 				src: imgsrc
                 			})
                 		}));
            		 }
            	 }
            	 
            	 if( feature && feature.length > 1){
            		 $.each(feature,function(k){
            			 var address = feature[k].values_.address;
                		 var type = feature[k].values_.type;
                		 if(cameraAddress == address){
                			 imgsrc = basePath+'static/images/camera_icon/polo_hover.png';
                		     features[j].setStyle(new ol.style.Style({
                     			image: new ol.style.Icon({
                     				anchor : [0.5,0.96],
                     				crossOrigin: 'anonymous',
                     				opacity : 1,
                     				src: imgsrc
                     			})
                     		}));
                		 }
            		 });
            		 if(!cameraAddress){
            			 imgsrc = $ctx +'/js/img/polo.png';
            		     features[j].setStyle(new ol.style.Style({
                 			image: new ol.style.Icon({
                 				anchor : [0.5,0.96],
                 				crossOrigin: 'anonymous',
                 				opacity : 1,
                 				src: imgsrc
                 			})
                 		}));
            		 }
            	 }
            	 if(isShowPosition){
     				var coordinate = features[j].getGeometry().getCoordinates();
         			mapWidget.view.setCenter(coordinate);
     				setPointPosition(features[j].values_.features[0]);
     			 }
             }  
          }
	   }
   }  
}  

/**
 * 绘制线
 * @param cameras
 */
function drawLineFeature(camerasStr){
	var cameras = JSON.parse(camerasStr);
	var style1 = new ol.style.Style({
		stroke: new ol.style.Stroke({
			width: 4,
			color: [200, 0, 0, 1]
		})
	});
	for(var i=0; i< cameras.length-1; i++){
		var lon1 = cameras[i].longitude;
		var lon2 = cameras[i+1].longitude;
		var lat1 = cameras[i].latitude;
		var lat2 = cameras[i+1].latitude;
		var marker1 = LatLonTransform.gcj_encrypt(parseFloat(lat1),parseFloat(lon1));
		var marker2 = LatLonTransform.gcj_encrypt(parseFloat(lat2),parseFloat(lon2));
		if(!isPgis){
			marker1 = ol.proj.transform([marker1.lon, marker1.lat], "EPSG:4326", "EPSG:3857");
			marker2 = ol.proj.transform([marker2.lon, marker2.lat], "EPSG:4326", "EPSG:3857");
		}else{
			marker1 = ol.proj.transform([lon1, lat1], 'EPSG:3857', 'EPSG:900913');
			marker2 = ol.proj.transform([lon2, lat2], 'EPSG:3857', 'EPSG:900913');
		}
		var feature = new ol.Feature({
		     geometry:new ol.geom.LineString(
		         [marker1, marker2])
		});
		/*var dx = marker2.lon - marker1.lon;
        var dy = marker2.lat - marker1.lat;*/
		var dx = lon2 - lon1;
        var dy = lat2 - lat1;
        var rotation = Math.atan2(dy, dx);
		style = new ol.style.Style({
        	geometry: new ol.geom.Point(marker2),
        	image: new ol.style.Icon({
        		src: basePath + 'static/images/arrow.png',
        		anchor: [0.75, 0.5],
        		rotateWithView: true,
        		rotation: -rotation
        	})
        });
		feature.setStyle([style,style1]);
		mapWidget.pathFeature.push(feature);
		mapWidget.drawOverlay.setMap(mapWidget.map);
		mapWidget.drawOverlay.getSource().addFeature(feature);
	}
}