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
        <form role="form"  id="searchForm" >
            <div class="container">
                <div class="row">
                    <div class="form-group col-xs-2">
                        <div class="input-group">
                            <span class="input-group-addon">项目名称</span>
                            <input name="name" class="form-control" type="text">
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
                <button class="btn btn-primary"  type="button" onclick="itemChoose()" ><i class="fa fa-plus-square" aria-hidden="true" style="margin-right:5px;"></i>添加</button>&nbsp;
                <button class="btn btn-danger"  type="button" id="delete"><i class="fa fa-minus-square" aria-hidden="true" style="margin-right:5px;"></i>批量删除</button>&nbsp;
            </div>
        </div>

        <table id="dataList" class="table table-striped jambo_table"></table>

    </div>
</div>


<jsp:include page="../inc/bottom.jsp" />
<script src="<%=path %>/js/bootstrap-validator/custom-validator.js"></script>
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

        //批量删除
        $("#delete").click(function(){
            var row = $("#dataList").bootstrapTable('getSelections', function (row) {return row});
            if(row == ""){
                Modal.alert({message: "请先选择至少一条记录",icon:"fa fa-check"});
            }else{
                Modal.confirm({ message: "确定要删除吗?",icon:"fa fa-exclamation-circle"}).on(function (e) {
                    if (e) {
                        var array = new Array(row.length);
                        for(var i = 0;i < row.length; i++){
                            array[i] = row[i].id;
                        }
                        $.ajax({
                            url:$ctx+ '/service/rest/platform.operation.RelevanceService/collection/deletes?ids='+array,
                            cache:false,		//false是不缓存，true为缓存
                            async:true,			//true为异步，false为同步
                            beforeSend:function(){},
                            success:function(result){
                                if(result.code == "1"){
                                    Modal.alert({message: result.description,icon:"fa fa-check"});
                                    $('#dataList').bootstrapTable('refresh');
                                }else{
                                    Modal.alert({message: result.description,icon:"fa fa-check"});
                                }
                            },
                            complete:function(){},
                            error:function(){
                                Modal.alert({message: "请求失败",icon:"fa fa-check"});
                            }
                        })
                    }
                    return;
                });
            }
        })

        // 初始化表格
        $('#dataList').bootstrapTable({
            toolbar:'#toolbar',
            uniqueId:'id',	// 指定主键列
            method:'post',
            url: $ctx + '/service/rest/platform.operation.RelevanceService/collection/query',
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
                {field : 'checked', checkbox : true, visible: true},
                {field : "id",align:"center",visible:false},
                {field : "name" ,title : "项目名称" ,align:"center" ,width : 80},
                {field : "description" ,title : "项目描述" ,align:"center" ,width : 80},
                {field : "createTime" ,title : "创建时间" ,align:"center" ,sortable : true ,width : 80}
            ],
            onLoadSuccess: function () {// 数据加载成功

            },
            onLoadError: function () {// 数据加载失败

            }
        });
    });

    //选择项目
    function itemChoose(){
        <%
           String medicalId = request.getParameter("medicalId");
        %>
        Modal.dialog({id:"dialog_choose_item",title:"选择体检项目",url:$ctx + "/admin/operation/chooseItem.jsp?medicalId=<%=medicalId%>",width:1200,height:800});
    }

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