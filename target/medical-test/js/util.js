/***
 * 把form的serialize转换成json
 * 用法：$(form).serializeObject()
 * @returns json
 */
$.fn.serializeObject = function(){
   var o = {};
   var a = this.serializeArray();
   $.each(a, function() {
       if (o[this.name]) {
           if (!o[this.name].push) {
               o[this.name] = [o[this.name]];
           }
           o[this.name].push(this.value || '');
       } else {
           o[this.name] = this.value || '';
       }
   });
   return o;
};

/**
 * 把json数据加载到form
 * 用法：$(form).loadJson(json);
 */
$.fn.loadJson = function(jsonValue) {
    var obj = this;
    $.each(jsonValue, function(name, ival) {
        var $oinput = obj.find(":input[name=" + name + "]");
        if ($oinput.attr("type") == "radio" || $oinput.attr("type") == "checkbox") {
            $oinput.each(function() {
                if (Object.prototype.toString.apply(ival) == '[object Array]') {//是复选框，并且是数组         
                    for (var i = 0; i < ival.length; i++) {
                        if ($(this).val() == ival[i])
                            $(this).attr("checked", "checked");
                    }
                } else {
                    if ($(this).val() == ival)
                        $(this).attr("checked", "checked");
                }
            });
        } else if ($oinput.attr("type") == "textarea") {//多行文本框            
            obj.find("[name=" + name + "]").html(ival);
        } else {
            obj.find("[name=" + name + "]").val(ival);
        }
    });
};

/**
 * 清空表单
 * 用法：$(form).clearForm();
 */
$.fn.clearForm = function(){
	$(':input', this).not(':button,:submit,:reset').val('').removeAttr('checked');
	$('textarea', this).val('');
};

/***
 * jquery 扩展
 * @author lq
 */
jQuery.extend({
	/**
	 * 重构jquery post 函数
	 * @param {} url		
	 * @param {} param
	 * @param {} callback	成功后回调函数
	 */
	postExtend:function(url,params,callback){
		//显示加载动画
		$.showLoading();
		jQuery.post(url,params,function(data){
			//隐藏加载动画
			$.hideLoading();
			if(data.redirect){
				if(data.ret){
					window.location.href = $ctx + '/login.jsp?service=' + escape(document.URL) ;
					return;
				}
				$.messager.alert(data.tips,data.messager,'error',function(){
					window.location.href = document.URL;
				});
			}else{
				callback(data);
			}
		}, 'json');
	},
	
	/**
	 * 重构jquery ajax 函数
	 * @param {} url		
	 * @param {} param
	 * @param {} isAsync	是否同步执行(false 同步;true 不同步)
	 * @param {} callback	成功后回调函数
	 */
	ajaxPost:function(url,params,isAsync,callback){
		jQuery.ajax({
		    type: 'POST',
		    url: url,
		    data:params,
		    dataType: 'json',
		    async: isAsync,
		    success:function(data){
		    	//隐藏加载动画
		    	$.hideLoading();
		    	if(data.redirect){
		    		if(data.ret){
						window.location.href = $ctx + '/login.jsp?service=' + escape(document.URL) ;
						return;
					}
					$.messager.alert(data.tips,data.messager,'error',function(){
						window.location.href = document.URL;
					});
				}else{
					callback(data);
				}
		    },
		    beforeSend:function(){
		    	//显示加载动画
		    	$.showLoading();
		    }
		});
	}
});

/**
 * 加载效果
 */
jQuery.extend({
	showLoading:function(){
//		var html = '';
//		html += '<div class="modal fade" id="loadingModal">';
//		html += '	<h5><div class="spinner"></div>正在加载...</h5>';
//		html += '</div>';
//		if($('#loadingModal').length == 0){
//			$('body').append(html);
//		}
		$('body').append('<div class="spinner"></div>');
		//$("#loadingModal").modal('show');
	},
	hideLoading:function(){
		$('body').find('.spinner').remove();
		//$("#loadingModal").modal('hide');
	}
});

/**
 * 序列化列表的参数以及本身的参数
 * @param old
 * @param params
 * @returns
 */
function serializeTableQueryParams(old, params){
	old.page = (old.offset / old.limit + 1) + '';
	old.rows = old.limit + '';
	old.pageSize = old.pageSize;  //页面大小
	old.pageNumber = old.pageNumber; //页码
	if(!old.sort){
		old.order = null;	//排序方式
		old.sort = null;	//排序字段
	}
	return $.extend(params, old);
}
