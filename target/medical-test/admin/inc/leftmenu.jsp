<%@ page language="java" pageEncoding="UTF-8" %>
<script type="text/javascript">
	$(function(){
		$("#a_home").click(function(){
			$("li[role=presentation]").removeClass("active");
			$("#tab_home").addClass("active");
		});
	});
	//初始化菜单
	generateMemus();
	
	function generateMemus() {
		var url = $ctx + '/service/rest/system.OperationService/collection/getMemus';
		$.post(url , {} , function(result) {
			if(result.code == 1) {
				var str = '';
				var data = result.result;
				for(var i = 0 ; i < data.length ; i++) {
					var obj = data[i];
					//菜单块结束
					if(i > 0 && obj.level == 1){
						str += '	</ul>';
						str += '</li>';
					}
					if(obj.level == 1){
						str += '<li data-count="0">';
						str += '	<a><i class="' + obj.iconCls + '"></i>' + obj.name + '<span class="fa fa-chevron-down"></span></a>';
						str += '	<ul class="nav child_menu">';
					}else{
						str += '		<li><a tab-rel="' + obj.id + '"  url="' + $ctx + obj.url + '"  title="' + obj.name + '">' + obj.name + '</a></li>';
					}
					//如果是最后一个
					if(i == data.length - 1){
						str += '	</ul>';
						str += '</li>';
					}
				}
				$('#sidebar-menu .side-menu').append(str);
			}
		});
	}
</script>
<div id="sidebar-menu" class="main_menu_side hidden-print main_menu" >
	<div class="menu_section">
		<ul class="nav side-menu" >
			<li class="active">
				<a href="#home" id="a_home" aria-controls="home" children="false" role="tab" data-toggle="tab"><i class="fa fa-home"></i> 首页 </a>
			</li>
		</ul>
	</div>
</div>