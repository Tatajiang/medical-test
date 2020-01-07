<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../inc/defines.jsp" %>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
	<jsp:include page="../inc/resource.jsp" />
</head>
<body style="background: #f7f7f7;" class="nav-md">
	<div class="container-fluid">
		<div class="x_panel">
			<form role="form"  id="searchForm" >
				<div class="container">
		            <div class="row">
		                <div class="form-group col-xs-2">
		                    <div class="input-group">
		                        <span class="input-group-addon">姓名</span>
		                        <input name="likeNickName" class="form-control" type="text">
		                    </div>
		                </div>
		                <div class="form-group col-xs-2.5">
		                        <button class="btn btn-primary" type="button" id="search"><i class="fa fa-search" aria-hidden="true" style="margin-right:5px;"></i>查询</button>&nbsp;
								<button class="btn btn-default" type="button" id="reset">重置</button>	
		                </div>
		            </div>
		        </div>
			</form>
			<table id="dataList" class="table table-striped jambo_table"></table>
		</div>
	</div>
	
	<!--编辑基本信息start-->
	<div class="modal fade" id="setRoleModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"> 
		<div class="modal-dialog">
			<div class="modal-content"> 
				<div class="modal-header"><button class="close" type="button" onclick="hideEditForm();">x</button>
	             	<h4>分配角色</h4>
				</div>
	            <div class="modal-body">
	            	<form id="setRoleForm">
	            		<input type="hidden" name="userId">
	             		<dl>
		             		
	             		</dl>
	             	</form>
             	</div>
             	<div class="modal-footer">
					<button type="button" class="btn btn-primary" onclick="saveRole()">保存</button>
					<button type="button" class="btn btn-default" onclick="hideEditForm();">取消</button>
				</div>
			</div>
		</div>
	</div>
	<!--编辑基本信息窗口end-->
	
	<jsp:include page="../inc/bottom.jsp" />
	<script type="text/javascript">
		$(document).ready(function(){
			//搜索按钮
			$('#search').click(function(){
				var params = $('#searchForm').serializeObject();
				$('#dataList').bootstrapTable("refreshOptions", {
		    		queryParams:function(p){
		    			return serializeTableQueryParams(p, params);
		    		}
				});
			});
			
			// 重置
			$('#reset').click(function(){
				$('#searchForm').clearForm();
				var params = $('#searchForm').serializeObject();
				$('#dataList').bootstrapTable("refreshOptions", {
		    		queryParams:function(p){
		    			return serializeTableQueryParams(p, params);
		    		}
				});
			});
			
			// 初始化表格
			$('#dataList').bootstrapTable({
				toolbar:'#toolbar',
				uniqueId:'id',	// 指定主键列
				method:'post',
			    url: $ctx + '/service/rest/user.UserService/collection/query',
			    striped: true,
			    pagination: true,
			    pageNumber: 1,
	            pageSize: 10,
	            showRefresh: true,
	            showToggle: true,
	            showColumns: true,
	            cardView: false,	
	            striped: true,
	            sortable: true,
	            sortName: "createTime",
	            sortOrder: "desc",
	            sidePagination: "server",
	            pageList: [10, 25, 50, 100],
	          	// 得到查询的参数
	            queryParams : function (params) {
	    			return serializeTableQueryParams(params, {});
	            },
				columns : [
					{field : "nickname",title : "姓名",align:"center",width : 80},
					{field : "loginId",title : "登录名",align:"center",width : 50},
					{field : "genderName",title : "性别",align:"center",width : 20},
					{field : "phone",title : "联系电话",align:"center",width : 50},
					{field : "roleNames",title : "相关角色",align:"center",width : 80,sortable : true},
					{field : "id", title : "操作", align:"center", width : 100, formatter:function(val, row, index){
						return '<a href="javascript:;" class="btn btn-primary" data-toggle="modal" data-target="#setRoleModal" onclick="setRole(\'' + val + '\');">绑定角色</a>';
					}}
				]
			});
		});
		
		function setRole(id){
			//var row = $('#dataList').bootstrapTable('getRowByUniqueId', id);
			$('#setRoleForm').loadJson({userId:id});
			var url = $ctx + '/service/rest/user.UserService/collection/getRolesByUser';
			$.post(url, {userId:id}, function(data){
				if(data.code == 1){
					var html = '';
					for(var i = 0; i < data.result.length; i ++){
						var role = data.result[i];
						
						html += '<dd style="display:inline;">';
						html += '	<label style="display:inline;margin-right:50px;">';
						html += '		<input type="checkbox" name="roleIds" ' + role.checked + ' value="' + role.id + '">' + role.name;
						html += '	</label>';
						html += '</dd>';
					}
					$('#setRoleForm dl').html(html);
				}
			});
		}
		//保存角色
		function saveRole(){
			var roleIds = [];
			$('#setRoleForm input[type=checkbox]').each(function(i, el){
				if(el.checked){
					roleIds.push(el.value);
				}
			});
			console.log(roleIds.join(','));
			
			var formData = $('#setRoleForm').serializeObject();
			var url = $ctx + '/service/rest/user.UserService/collection/setRolesByUser';
			var params = {
				userId:formData.userId,
				roleIds:roleIds.join(',')
			}
			$.post(url, params, function(data){
				if(data.code == 1){
					Modal.alert({message: data.description, icon:'fa fa-check'}).on(function(){
						$('#setRoleModal').modal('hide');
						$('#dataList').bootstrapTable('refresh');
					});
				}else{
					Modal.alert({message: data.description, icon:'fa fa-error'});
				}
			});
		}
		
		function hideEditForm(){
			hideModal('setRoleModal');
			$('#setRoleForm')[0].reset();
		}
		function showModal(id){
			$('#' + id).modal('show');
		}
		
		function hideModal(id){
			$('#' + id).modal('hide');
		}
	</script>
</body>
</html>