<%@page import="com.jiang.medical.platform.system.domain.SysRoles.Level"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../inc/defines.jsp" %>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
	<jsp:include page="../inc/resource.jsp" />
	<script src="<%=path %>/js/jquery-3.3.1.min.js"></script>
	<style type="text/css">
	#setOperationForm ul{list-style: none;}
	#setOperationForm ul li{float: left;margin: 10px;}
	</style>
</head>
<body style="background: #f7f7f7;" class="nav-md">
	<div class="container-fluid">
		<div class="x_panel">
			<form role="form"  id="searchRolesForm" >
				<div class="container">
		            <div class="row">
		           		<div class="form-group col-xs-3">
		                    <div class="input-group">
		                        <span class="input-group-addon">角色名称</span>
		                        <input id="name" name="name" class="form-control" type="text">
		                    </div>
		                </div>
		                <div class="form-group col-xs-3">
		                    <div class="input-group">
		                        <span class="input-group-addon">级别</span>
		                        <select name="level" class="form-control">
		                        	<option value="">全部</option>
	             					<%for(Level level:Level.values()){ %>
	             					<option value="<%=level.name()%>"><%=level.getName() %></option>
	             					<%} %>
	             				</select>
		                    </div>
		                </div>
		                <div class="form-group col-xs-2.5">
	                        <button class="btn btn-primary" type="button" id="searchRoles"><i class="fa fa-search" aria-hidden="true" style="margin-right:5px;"></i>查询</button>&nbsp;
							<button class="btn btn-default"  type="button" id="clearRoles">清空</button>&nbsp;		
		                </div>
		            </div>
		        </div>
			</form>
			<div class="form-group col-lg-10">
			    <div class="input-group">
			   		 <button class="btn btn-primary"  type="button" data-toggle="modal" data-target="#addRolesInfoModal"><i class="fa fa-plus-square" aria-hidden="true" style="margin-right:5px;"></i>添加</button>&nbsp;
			    </div>
			</div>
		
			<table id="rolesDataList" class="table table-striped jambo_table"></table>
		</div>
	</div>
	
	<!--新增角色信息start-->
	<div class="modal fade" id="addRolesInfoModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"> 
		<div class="modal-dialog">
			<div class="modal-content"> 
				<div class="modal-header"><button class="close" type="button" onclick="hideEditForm();">x</button>
	             	<h4>新增角色信息</h4>
				</div>
	            <div class="modal-body">
	             	<form class="form-horizontal" role="form" id="addForm">
	             		<input type="hidden" name="id" />
	             		<div class="form-group">
	             			<label class="col-sm-2 control-label">角色名称：</label>
	             			<div class="col-sm-10">
	             				<input type="text" name="name" required data-bv-notempty-message="角色名称不能为空" class="form-control" />
	             			</div>
	             		</div>
						<div class="form-group">
	             			<label class="col-sm-2 control-label">级别：</label>
	             			<div class="col-sm-10">
	             				<select name="level" class="form-control">
	             					<%for(Level level:Level.values()){ %>
	             					<option value="<%=level.name()%>"><%=level.getName() %></option>
	             					<%} %>
	             				</select>
	             			</div>
	             		</div>
	             		<div class="form-group">
	             			<label class="col-sm-2 control-label">备注：</label>
	             			<div class="col-sm-10">
	             				<input type="text" name="description" class="form-control" />
	             			</div>
	             		</div>
					</form>
             	</div>
             	<div class="modal-footer">
					<button type="button" class="btn btn-primary" onclick="save();">保存</button>
					<button type="button" class="btn btn-default" onclick="hideEditForm();">取消</button>
				</div>
			</div>
		</div>
	</div>
	<!--新增角色信息end-->
	
	
	<!--修改角色信息start-->
	<div class="modal fade" id="updateRolesInfoModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"> 
		<div class="modal-dialog">
			<div class="modal-content"> 
				<div class="modal-header"><button class="close" type="button" onclick="hideEditForm();">x</button>
	             	<h4>修改角色信息</h4>
				</div>
	            <div class="modal-body">
	             	<form class="form-horizontal" role="form" id="updateForm">
	             		<input type="hidden" name="id" />
	             		<div class="form-group">
	             			<label class="col-sm-2 control-label">角色名称：</label>
	             			<div class="col-sm-10">
	             				<input type="text" name="name" required data-bv-notempty-message="角色名称不能为空" class="form-control" />
	             			</div>
	             		</div>
						<div class="form-group">
	             			<label class="col-sm-2 control-label">级别：</label>
	             			<div class="col-sm-10">
	             				<select id="level" name="level" class="form-control">
	             					<%for(Level level:Level.values()){ %>
	             					<option value="<%=level.name()%>"><%=level.getName() %></option>
	             					<%} %>
	             				</select>
	             			</div>
	             		</div>
	             		<div class="form-group">
	             			<label class="col-sm-2 control-label">备注：</label>
	             			<div class="col-sm-10">
	             				<input type="text" name="description" class="form-control" />
	             			</div>
	             		</div>
					</form>
             	</div>
             	<div class="modal-footer">
					<button type="button" class="btn btn-primary" onclick="update();">保存</button>
					<button type="button" class="btn btn-default" onclick="hideEditForm();">取消</button>
				</div>
			</div>
		</div>
	</div>
	<!--修改角色信息end-->
	
	<!--设置权限start-->
	<div class="modal fade" id="operationModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"> 
		<div class="modal-dialog">
			<div class="modal-content"> 
				<div class="modal-header"><button class="close" type="button" onclick="hideEditForm();">x</button>
	             	<h4>分配菜单</h4>
				</div>
	            <div class="modal-body">
	             	<form class="form-horizontal" role="form" id="setOperationForm">
	             		
					</form>
             	</div>
             	<div class="modal-footer">
					<button type="button" class="btn btn-primary" onclick="getAll()">保存</button>
					<button type="button" class="btn btn-default" onclick="hideEditForm();">取消</button>
				</div>
			</div>
		</div>
	</div>
	<!--设置权限end-->
	
	<jsp:include page="../inc/bottom.jsp" />
	<script src="<%=path %>/js/bootstrap-validator/custom-validator.js"></script>
	
	<script type="text/javascript">
		$(document).ready(function(){
			//搜索按钮
			$('#searchRoles').click(function(){
				var params = $('#searchRolesForm').serializeObject();
				$('#rolesDataList').bootstrapTable("refreshOptions", {
		    		queryParams:function(p){
		    			return serializeTableQueryParams(p, params);
		    		}
				});
			});
			
			// 清空
			$("#clearRoles").click(function(){
				$('#searchRolesForm').clearForm();
				var params = $('#searchRolesForm').serializeObject();
				$('#rolesDataList').bootstrapTable("refreshOptions", {
		    		queryParams:function(p){
		    			return serializeTableQueryParams(p, params);
		    		}
				}); 
			})
			
			// 初始化表格
			$('#rolesDataList').bootstrapTable({
				toolbar:'#toolbar',
				uniqueId:'id',	// 指定主键列
				method:'post',
			    url: $ctx + '/service/rest/system.SysRolesService/collection/query',
			    striped: true,
			    pagination: true,
			    pageNumber: 1,
	            pageSize: 15,
	            showRefresh: true,
	            showToggle: true,
	            showColumns: true,
	            cardView: false,	
	            striped: true,
	            showHeader: true,
	            sidePagination: "server",
	            pageList: [10, 25, 50, 100],
	          	// 得到查询的参数
	            queryParams : function (params) {
	    			return serializeTableQueryParams(params, {});
	            },
				columns : [
					{field : "name",title : "角色名称",align:"center",width : 50,sortable : true},
					{field : "flage",title : "状态",align:"center",width : 50,sortable : true},
					{field : "level",title : "级别",align:"center",width : 50,sortable : true},
					{field : "description",title : "备注",align:"center",width : 100},
					{field : 'operate',title: '操作',align: 'center', width : 100,
		                formatter: function(value,row,index){
		                	var html = '';
		                	html += '<button type="button" class="btn btn-info" data-toggle="modal" data-target="#operationModal" onclick="setOperation(\''+row.id+'\')">设置权限</button>';
		                	html += '<button type="button" class="btn btn-primary" data-toggle="modal" data-target="#updateRolesInfoModal" onclick="updateRoles(\''+row.id+'\')"><i class="fa fa-pencil-square-o" aria-hidden="true" style="margin-right:5px;"></i>修改</button>';
		                	if(row.flage=="账户已启用"){
		                		html += '<button type="button" class="btn btn-danger" onclick="forbiddenRoles(\''+row.id+'\')"><i class="fa fa-minus-square" aria-hidden="true" style="margin-right:5px;"></i>禁用</button>';
		                	}else if(row.flage=="账户已禁用"){
		                		html += '<button type="button" class="btn btn-info" onclick="useRoles(\''+row.id+'\')"><i class="fa fa-plus-square" aria-hidden="true" style="margin-right:5px;"></i>启用</button>';
		                	}
		                	
		                	return html;
		                }
					}
				]
			});
		});
		
		// 保存对象
		function save(){
			// 获取表单对象
		    var bootstrapValidator = $('#addForm').data('bootstrapValidator');
		    // 手动触发验证
		    bootstrapValidator.validate();
		    if(bootstrapValidator.isValid()){
				Modal.confirm({ message: "确定保存?",icon:"fa fa-exclamation-circle"}).on(function (r) {
					if(r){
						var url = $ctx + '/service/rest/system.SysRolesService/collection/update';
						var params = $('#addForm').serializeObject();
						$.postExtend(url, params, function(data){
							if(data.code == 1){
								Modal.alert({message: data.description, icon:'fa fa-check'}).on(function(){
									hideEditForm();
									$('#rolesDataList').bootstrapTable('refresh');
								});
							}else{
								Modal.alert({message: data.description, icon:'fa fa-error'});
							}
						});
					}
				});
		    }
		}

		// 修改对象
		function update(){
			// 获取表单对象
		    var bootstrapValidator = $('#updateForm').data('bootstrapValidator');
		    // 手动触发验证
		    bootstrapValidator.validate();
		    if(bootstrapValidator.isValid()){
				Modal.confirm({ message: "确定保存?",icon:"fa fa-exclamation-circle"}).on(function (r) {
					if(r){
						var url = $ctx + '/service/rest/system.SysRolesService/collection/update';
						var params = $('#updateForm').serializeObject();
						$.postExtend(url, params, function(data){
							if(data.code == 1){
								Modal.alert({message: data.description, icon:'fa fa-check'}).on(function(){
									hideEditForm();
									$('#rolesDataList').bootstrapTable('refresh');
								});
							}else{
								Modal.alert({message: data.description, icon:'fa fa-error'});
							}
						});
					}
				});
		    }
		}
		
		// 修改
		function updateRoles(id){
			var row = $('#rolesDataList').bootstrapTable('getRowByUniqueId', id);
			$('#updateForm').loadJson(row);
			if(row.level == "系统管理员"){
				$('#level').val("admin");
			}
			if(row.level == "其他用户"){
				$('#level').val("others");
			}
		}
		
		function hideEditForm(){
			hideModal('addRolesInfoModal');
			$('#addForm')[0].reset();
			hideModal('updateRolesInfoModal');
			$('#updateForm')[0].reset();
			hideModal('operationModal');
			$('#setOperationForm')[0].reset();
		}
		
		function showModal(id){
			$('#' + id).modal('show');
		}
		
		function hideModal(id){
			$('#' + id).modal('hide');
		}
		
		// 禁用
		function forbiddenRoles(id){
			Modal.confirm({ message: '确定禁用？',icon:"fa fa-exclamation-circle"}).on(function (r) {
				if(r){
					var url = $ctx + '/service/rest/system.SysRolesService/collection/delete';
					$.postExtend(url, {id:id}, function(data){
						if(data.code == 1){
							Modal.alert({message: data.description, icon:'fa fa-check'}).on(function(){
								$('#rolesDataList').bootstrapTable('refresh');
							});
						}else{
							Modal.alert({message: data.description, icon:'fa fa-error'});
						}
					});
				}
			});
		}
		
		// 启用
		function useRoles(id){
			Modal.confirm({ message: '确定启用？',icon:"fa fa-exclamation-circle"}).on(function (r) {
				if(r){
					var url = $ctx + '/service/rest/system.SysRolesService/collection/use';
					$.postExtend(url, {id:id}, function(data){
						if(data.code == 1){
							Modal.alert({message: data.description, icon:'fa fa-check'}).on(function(){
								$('#rolesDataList').bootstrapTable('refresh');
							});
						}else{
							Modal.alert({message: data.description, icon:'fa fa-error'});
						}
					});
				}
			});
		}
		
		// 根据角色id查询出对应的资源
		function setOperation(roleId){
			var operationIds = [];
			var url = $ctx + '/service/rest/system.OperationService/collection/query';
			var params = {roleId:roleId};
			$.postExtend(url, params, function(result){
				if(result.code ==1) {
					 var data = result.result;
					 if(data != null) {	
						 for(var i = 0 ; i < data.length ; i++) {
							 operationIds.push(data[i].id);
						 }
					 }
					 assignOperationView(operationIds.join(','),roleId);
				 }
			});
		}
		
		/**
		  * 显示所有权限，并自动选中已有权限
		  * @param operationIdsStr 已有权限id
		  */
		 function assignOperationView(operationIdsStr,roleId){
			 var row = $('#rolesDataList').bootstrapTable('getRowByUniqueId', roleId);
			 
			 var url = $ctx + '/service/rest/system.OperationService/collection/queryAll';
			 var params = {roleId:roleId};
			 $.postExtend(url , params , function(result) {
				 var str = '<h6>当前角色：' + row.name + '</h6>';
				 str += '<div class="operationList" data-roleid="' + roleId + '">';
				 if(result.code == 1) {
					 var data = result.result;
					 for(var i = 0 ; i < data.length ; i++) {
						//判断是否是根结点
						
						if(data[i].level == 1){
							str += '<div style = "width:auto;height:auto;"><dl style = "background:#5098eb;color:#ffffff;margin: 20px 0 0 0;"><dt><label><input type = "checkbox" value = "'+data[i].id+'" onclick = "checkAll(this)">&nbsp;'+data[i].name+'</label></dt></dl>';
						}else {
							str += '<dd style = "display:inline;">';
							if(operationIdsStr.indexOf(data[i].id) == -1){	//判断是否被选中,不选中
								str += '<label style = "display:inline;margin-right:50px;"><input type="checkbox" name="operation" onchange = "checknav(this)" value="'+data[i].id+'" />&nbsp;'+data[i].name+'</label>';
							}else {
								str += '<label style = "display:inline;margin-right:50px;"><input type="checkbox" name="operation" onchange = "checkItem(this)" checked = "checked" value="'+data[i].id+'" />&nbsp;'+data[i].name+'</label>';
							}
							str += '</dd>';
							if(i < data.length-2){	//防止数组下标越界
								if(data[i+1].level == 1) { //本组权限分配完毕
									str +='</div>';
								}
							}
						}
					 }
					 str += '</div>';
					 $('#setOperationForm').html(str);
					//初始化所有分类导航复选框
					var div = $('#setOperationForm div div');
					for(var i = 0 ; i < div.length ; i++) {				
						checklist(div[i]);
					}
				 }
			 });
		 }
		
		 /**
		  * 分类导航复选框部分选中，全选，全不选状态
		  * @param div:分类导航复选框所在div
		  */
		 function checklist(div) {
			 var first = $(div).find('dl').find('input');
			 if(checkedAll(div)) {	//全选
				first[0].indeterminate = false;
				first[0].checked = true;
			 }else if(checkedNone(div)) {	//全不选
				first[0].indeterminate = false;
				first[0].checked = false;
			 }else {	//部分选中
			 	first[0].indeterminate = true;
			 	first[0].checked = false;
			 }
		 }
		 
		 function checkItem(el){
			 var first = $(el).parent().parent().parent().find('dl').find('input');
			 var div = $(el).parent().parent().parent();
			 if(checkedAll(div)) {	//全选
				first[0].indeterminate = false;
				first[0].checked = true;
			 }else if(checkedNone(div)) {	//全不选
				first[0].indeterminate = false;
				first[0].checked = false;
			 }else {	//部分选中
			 	first[0].indeterminate = true;
			 	first[0].checked = false;
			 }
		 }
		 
		 /**
		  * 判断是否全选
		  * @param el:被选中的子项
		  * @return true:全选 false：存在没有被选中的input
		  */
		 function checkedAll(div){
			var has = false;
			$(div).find('dd').find('input').each(function() {
				if(!this.checked) {	//存在没有被选中
					has = true;
					return false;
				}
			});
			return !has;
		 }
		 
		 function checkAll(el) {//全选/全不选
			 var div = $(el).parent().parent().parent().parent();
			 var checked = el.checked;
			 if(navigator.appName.indexOf("Microsoft Internet Explorer")!=-1 && document.all && !checkedAll(div) && !checkedNone(div)) {	//如果是IE浏览器
				checked = !checked;
			 	el.checked = checked;
			 }
			 $(el).parent().parent().parent().parent().find('dd').find('input').each(function() {
			 	this.checked = checked;
			 });
		 }
		 /**
		  * 分类导航复选框部分选中，全选，全不选状态
		  * @param el:复选框对象
		  */
		 function checknav(el) {
			 checklist($(el).parent().parent().parent());
		 }
		 /**
		  * 判断是否全不选
		  * @param el:被选中的子项
		  * @return true:全不选 false：存在被选中的input
		  */
		 function checkedNone(div){
			var has = false;
			$(div).find('dd').find('input').each(function() {
				if(this.checked) {	//存在被选中
					has = true;
					return false;
				}
			});
			return !has;
		 }
		 
		//保存节点
		function saveNode(operationId) {
			var url = $ctx + '/service/rest/system.OperationService/collection/setRoleOperation';
			var params = {
				'roleId':$('.operationList').data('roleid'),
				'operationId':operationId
			};
			$.postExtend(url , params , function(result) {
				if(result.code == 1){
					Modal.alert({message: '保存成功!', icon:'fa fa-check'}).on(function(){
						$('#rolesDataList').bootstrapTable('refresh');
						hideModal('operationModal');
					});
				}else {
					Modal.alert({message: result.description, icon:'fa fa-error'});
				}
			});
		}
		 
		//遍历记录页面上所有的选中状态的菜单资源
		function getAll() {
			var operationIds = {
				init : function() {
					this.grid = [];
				},
				insert : function(str) {
					this.grid.push(str);
				},
				toString : function() {
					return this.grid.join(',');
				}
			};
			var div = $('#setOperationForm').find('div').find('div');
			operationIds.init();
			for (var i = 0; i < div.length; i++) {
				var has = false;
				$(div[i]).find('dd').find('input').each(function() {
					if (this.checked) {
						has = true;
						operationIds.insert(this.value);
					}
				});
				if (has) {
					var first = $(div[i]).find('dl').find('input').val();
					operationIds.insert(first);
				}
			}
			var str = operationIds.toString();
			saveNode(str);
			operationIds.grid = null; //释放资源
			operationIds = null; //释放
		}
	</script>
</body>
</html>