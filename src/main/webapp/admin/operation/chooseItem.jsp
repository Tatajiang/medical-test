<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../inc/defines.jsp" %>
<div class="container-fluid">
	<div class="x_panel">
		<form role="form"  id="searchItem" >
			<div class="container">
				<div class="row">
					<div class="form-group col-xs-3">
						<div class="input-group">
							<span class="input-group-addon">项目名称</span>
							<input name="name" class="form-control" type="text">
						</div>
					</div>
					<div class="form-group col-xs-3">
						<button class="btn btn-primary" type="button" id="searchEmployee"><i class="fa fa-search" aria-hidden="true" style="margin-right:5px;"></i>查询</button>&nbsp;
						<button class="btn btn-default" type="button" id="resetEmployee">重置</button>
					</div>
				</div>
			</div>
		</form>

		<table id="dataListItem" class="table table-striped jambo_table"></table>
	</div>
</div>

<script type="text/javascript">
	<%
       String medicalId = request.getParameter("medicalId");
  	%>
	var medicalId = "<%=medicalId%>";
	$(document).ready(function(){
		//搜索按钮
		$('#searchEmployee').click(function(){
			var params = $('#searchItem').serializeObject();
			params.absenceMedical = medicalId;
			$('#dataListItem').bootstrapTable("refreshOptions", {
				queryParams:function(p){
					return serializeTableQueryParams(p, params);
				}
			});
		});

		// 重置
		$('#resetEmployee').click(function(){
			$('#searchItem').clearForm();
			var params = $('#searchItem').serializeObject();
			params.absenceMedical = medicalId;
			$('#dataListItem').bootstrapTable("refreshOptions", {
				queryParams:function(p){
					return serializeTableQueryParams(p, params);
				}
			});
		});


		// 初始化表格
		$('#dataListItem').bootstrapTable({
			toolbar:'#toolbar',
			uniqueId:'id',	// 指定主键列
			method:'post',
			url: $ctx + '/service/rest/platform.operation.ItemService/collection/query',
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
			sidePagination: "server",
			pageList: [10, 25, 50, 100],
			// 得到查询的参数
			queryParams : function (params) {
				return serializeTableQueryParams(params, {absenceMedical:medicalId});
			},
			columns : [
				{field : 'checked', checkbox : false, visible: false},
				{field : "id",align:"center",visible:false},
				{field : "name" ,title : "项目名称" ,align:"center" ,width : 80},
				{field : "description" ,title : "项目描述" ,align:"center" ,width : 80},
				{field : "createTime" ,title : "创建时间" ,align:"center" ,sortable : true ,width : 80}
			],
			onLoadSuccess: function () {// 数据加载成功

			},
			onLoadError: function () {// 数据加载失败

			},
			onClickRow: function (row) {//单击表格行事件
                var url = $ctx + '/service/rest/platform.operation.RelevanceService/collection/create';
                var params = {
					itemId: row.id,
					medicalId:medicalId
				}
				$.postExtend(url, params, function(data){
					if(data.code == 1){
						Modal.alert({message: data.description, icon:'fa fa-check'}).on(function(){
							//移除模态框
							$('body').find($("#dialog_choose_item")).remove();
							$('#dataList').bootstrapTable('refresh');
						});
					}else{
						Modal.alert({message: data.description, icon:'fa fa-error'});
					}
				});
			}
		});
	});
</script>