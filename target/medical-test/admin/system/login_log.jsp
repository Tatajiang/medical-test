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
		                        <span class="input-group-addon">用户昵称</span>
		                        <input name="userName" class="form-control" type="text">
		                    </div>
		                </div>
		                <div class="form-group col-xs-2.5">
		                        <button class="btn btn-primary" type="button" id="search"><i class="fa fa-search" aria-hidden="true" style="margin-right:5px;"></i>查询</button>&nbsp;
								<button class="btn btn-default" type="button" id="reset">重置</button>	
		                </div>
		            </div>
		        </div>
			</form>
			
			<div class="form-group col-lg-10">
			    <div class="input-group">
			  	  	<!-- 在此处放业务按钮 -->
			    </div>
			</div>
			
			<table id="dataList" class="table table-striped jambo_table"></table>
			
		</div>
	</div>
	
	<!--查看模态框start-->
	<div class="modal fade" id="editModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"> 
		<div class="modal-dialog">
			<div class="modal-content"> 
				<div class="modal-header"><button class="close" type="button" onclick="hideEditForm();">x</button>
	             	<h4>编辑信息</h4>
				</div>
	            <div class="modal-body">
	             	<form class="form-horizontal" role="form" id="editForm" >
	             		<input type="hidden" name="id" />
	             		<div class="form-group">
	             			<label class="col-sm-2 control-label">会员昵称：</label>
	             			<div class="col-sm-10">
	             				<input type="text" name="userName" class="form-control" readonly="readonly" />
	             			</div>
	             		</div>
	             		<div class="form-group">
	             			<label class="col-sm-2 control-label">性别：</label>
	             			<div class="col-sm-10">
	             				<input type="text" name="genderName" class="form-control" readonly="readonly"  />
	             			</div>
	             		</div>
	             		<div class="form-group">
	             			<label class="col-sm-2 control-label">电话：</label>
	             			<div class="col-sm-10">
	             				<input type="text" name="phone" class="form-control" readonly="readonly"  />
	             			</div>
	             		</div>
	             		<div class="form-group">
	             			<label class="col-sm-2 control-label">登录时间：</label>
	             			<div class="col-sm-10">
	             				<input type="text" name="createTime" class="form-control" readonly="readonly"  />
	             			</div>
	             		</div>
	             		<div class="form-group">
	             			<label class="col-sm-2 control-label">登录地址：</label>
	             			<div class="col-sm-10">
	             				<input type="text" name="ip" class="form-control" readonly="readonly"  />
	             			</div>
	             		</div>
					</form>
             	</div>
             	<div class="modal-footer">
					<button type="button" class="btn btn-default" onclick="hideEditForm();">关闭</button>
				</div>
			</div>
		</div>
	</div>
	<!--查看角色模态框end-->
	
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
			    url: $ctx + '/service/rest/user.LoginLogService/collection/query',
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
					{field : "id",align:"center",visible:false},
					{field : "userName" ,title : "用户昵称" ,align:"center" ,width : 80},
					{field : "genderName" ,title : "性别" ,align:"center" ,width : 80},
					{field : "phone" ,title : "联系电话" ,align:"center" ,width : 80,sortable : true},
					{field : "ip" ,title : "登录地址" ,align:"center" ,width : 80},
					{field : "createTime" ,title : "登录时间" ,align:"center" ,width : 80},
					{field : "operate", title : "操作", align:"center",width : 150, 
		                formatter: function(value,row,index){
		                	var html = '';
		                	html += '<a class="btn btn-primary" data-toggle="modal" data-target="#editModal" onclick="detail(\''+row.id+'\')">查看详情</a>';
		                	return html;
		                }
					}
				],
				onLoadSuccess: function () {// 数据加载成功
					
                },
                onLoadError: function () {// 数据加载失败
                	
                }
			});
		});
		
		
		// 修改
		function detail(id){
			var row = $('#dataList').bootstrapTable('getRowByUniqueId', id);
			$('#editForm').loadJson(row);
			
		}
		
		function hideEditForm(){
			hideModal('editModal');
			$('#editForm')[0].reset();
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