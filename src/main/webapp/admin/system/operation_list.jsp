<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../inc/defines.jsp" %>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
	<jsp:include page="../inc/resource.jsp" />
	<script src="<%=path %>/js/jquery-3.3.1.min.js"></script>
</head>
<body style="background: #f7f7f7;" class="nav-md">
	<div class="container-fluid">
		<div class="x_panel">	
			<div class="row">
				<div class="col-md-5" id="operationTree"></div>
				
				<div class="col-md-7">
					<fieldset>
						<legend>菜单详情</legend>
						<form id="editForm">
							<div class="form-group col-xs-7">
								<label>菜单名称：</label>
								<input class="form-control" type="text" name="name" style="outline:medium;border-width:0;" readonly="readonly"/>
							</div>
							<div class="form-group col-xs-7">
								<label>菜单链接：</label>
								<input class="form-control" type="text" name="url" style="outline:medium;border-width:0;" readonly="readonly"/>
							</div>
							<div class="form-group col-xs-7">
								<label>资源的深度：</label>
								<input class="form-control" type="text" name="level" style="outline:medium;border-width:0;" readonly="readonly"/>
							</div>
							<div class="form-group col-xs-7">
								<label>显示序号：</label>
								<input class="form-control" type="text" name="showIndex" style="outline:medium;border-width:0;" readonly="readonly"/>
							</div>
							<div class="form-group col-xs-7">
								<label>资源描述：</label>
								<textarea class="form-control" name="description" style="outline:medium;border-width:0;" readonly="readonly"></textarea>
							</div>
						</form>
					</fieldset>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="../inc/bottom.jsp" />
	
	<script type="text/javascript">
		
		var referchDeptTree = function () {
			var url = $ctx + '/service/rest/system.OperationService/collection/getTree';
			$.postExtend(url,{},function(data){
				$('#operationTree').treeview({
		   	          data: data,
		   	          showCheckbox : false,//是否显示复选框
		   	          highlightSelected: true, //高亮选中节点
		   	          nodeIcon:"fa fa-folder",//节点图标
		   	          showIcon:true,//是否显示图标
		   	          levels:5,//展开级别 默认:2
		   	          onNodeSelected: function(event, node){
		   	        	  $('#editForm').loadJson(node);
		   	          }
		   	      });
			});
		};
		
		$(function(){
			referchDeptTree();//刷新树
		});
	</script>
</body>
</html>