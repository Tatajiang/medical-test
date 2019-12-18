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
                            <span class="input-group-addon">用户姓名:</span>
                            <input name="userName" class="form-control" type="text">
                        </div>
                    </div>
                    <div class="form-group col-xs-2">
                        <div class="input-group">
                            <span class="input-group-addon">套餐名称:</span>
                            <input name="medicalName" class="form-control" type="text">
                        </div>
                    </div>
                    <div class="form-group col-xs-2">
                        <div class="input-group">
                            <span class="input-group-addon">项目名称:</span>
                            <input name="itemName" class="form-control" type="text">
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
            <div class="input-group"></div>
        </div>

        <table id="dataList" class="table table-striped jambo_table"></table>

    </div>
</div>


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
            url: $ctx + '/service/rest/platform.operation.InspectRecordService/collection/query',
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
                return serializeTableQueryParams(params, {});
            },
            columns : [
                {field : 'checked', checkbox : false, visible: false},
                {field : "id",align:"center",visible:false},
                {field : "userName" ,title : "用户姓名" ,align:"center" ,width : 80},
                {field : "medicalName" ,title : "套餐名称" ,align:"center" ,width : 80},
                {field : "itemName" ,title : "项目名称" ,align:"center" ,width : 80},
                {field : "isCompleteName" ,title : "体检状态" ,align:"center" ,width : 80},
                {field : "completeTime" ,title : "完成时间" ,align:"center" ,width : 80},
                {field : "createTime" ,title : "创建时间" ,align:"center" ,width : 80},
            ],
            onLoadSuccess: function () {// 数据加载成功

            },
            onLoadError: function () {// 数据加载失败

            }
        });
    });

    function hideEditForm(){
        hideModal('editModal');
        $('#editForm')[0].reset();
        $("#editForm").data('bootstrapValidator').resetForm();//清除当前验证的关键之处
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