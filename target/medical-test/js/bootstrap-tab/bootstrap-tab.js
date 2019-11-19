/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var addTabs = function (obj) {
    id = obj.id;
    
    $("#tabs-nav li",parent.document).each(function(){
    	if($(this).attr("class") == "active"){
    		$(this).removeClass("active");
    	}
    })
    
    $("#tabs-content div",parent.document).each(function(){
    	if($(this).attr("class") == "tab-pane active"){
    		$(this).removeClass("active");
    	}
    })
     
    //如果TAB不存在，创建一个新的TAB
    if (!$("#" + id, parent.document)[0]) {
        //固定TAB中IFRAME高度,根据需要自己修改
        mainHeight = parent.document.body.offsetHeight - 130;
        //创建新TAB的title
        title = '<li role="presentation" id="tab_' + id + '"><a href="#' + id + '" aria-controls="' + id + '" role="tab" data-toggle="tab"><i class="'+obj.icon+'"></i>' + obj.title;
        //是否允许关闭
        if (obj.close) {
            title += ' <i class="close-tab glyphicon glyphicon-remove-sign" tabclose="' + id + '"></i>';
        }
        title += '</a></li>';
        //是否指定TAB内容
        if (obj.content) {
            content = '<div role="tabpanel" class="tab-pane" id="' + id + '">' + obj.content + '</div>';
        } else {//没有内容，使用IFRAME打开链接
            content = '<div role="tabpanel" class="tab-pane" id="' + id + '"><iframe src="' + obj.url + '" width="100%" height="' + mainHeight +
                    '" frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="yes" allowtransparency="yes"></iframe></div>';
        }
        //加入TABS
        $("#tabs-nav",parent.document).append(title);
        $("#tabs-content",parent.document).append(content);
    }else{
    	if($("#" + id, parent.document)[0].getElementsByTagName('iframe')[0].src != 'http://'+window.location.host+'/gfyj/'+obj.url){
    		$("#" + id, parent.document)[0].getElementsByTagName('iframe')[0].src = obj.url;
    	}
    }

    //激活TAB
    $("#tab_" + id,parent.document).addClass('active');
    $("#" + id,parent.document).addClass("active");
};

var closeTab = function (id) {
	//如果关闭的是当前激活的TAB，激活他的前一个TAB
	$("#tabs-nav",parent.document).children("li").each(function(){
		if($(this).attr("class") == "active"){
			if($(this).attr("id") == "tab_" + id){
				 $("#tab_" + id,parent.document).prev().addClass('active');
			     $("#" + id,parent.document).prev().addClass('active');
			}
		}
	});
    //关闭TAB
    $("#tab_" + id,parent.document).remove();
    $("#" + id,parent.document).remove();
};

$(function () {
	$("#a_home").click(function(){
		$("div[role=tabpanel]").each(function(){
			$(this).removeClass("active");
		})
		if($("#home").attr("class") == "tab-pane"){
			$("#home").addClass("active");
		}
	})
	
    $("[tab-rel]").click(function () {
        addTabs({id: $(this).attr("tab-rel"), title: $(this).attr('title'), url: $(this).attr('url'), close: true});
    });
    
    $(".nav-tabs").on("click", "[tabclose]", function (e) {
        id = $(this).attr("tabclose");
        closeTab(id);
    });
});