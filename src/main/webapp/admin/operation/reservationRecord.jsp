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
                    <div class="form-group col-xs-2.5">
                        <button class="btn btn-primary" type="button" id="search"><i class="fa fa-search" aria-hidden="true" style="margin-right:5px;"></i>查询</button>&nbsp;
                        <button class="btn btn-default" type="button" id="reset">重置</button>
                    </div>
                </div>
            </div>
        </form>

        <div class="form-group col-lg-10">
            <div class="input-group">
                <%--<button class="btn btn-primary"  type="button" data-toggle="modal" data-target="#editModal" ><i class="fa fa-plus-square" aria-hidden="true" style="margin-right:5px;"></i>添加</button>&nbsp;--%>
                <button class="btn btn-danger"  type="button" id="delete"><i class="fa fa-minus-square" aria-hidden="true" style="margin-right:5px;"></i>批量删除</button>&nbsp;
            </div>
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


        //批量删除
        $("#delete").click(function(){
            var row = $("#dataList").bootstrapTable('getSelections', function (row) {return row});
            if(row == ""){
                Modal.alert({message: "请先选择至少一条记录",icon:"fa fa-check"});
            }else{
                Modal.confirm({ message: "确定要删除吗?(注意：删除该项目后，预约过该项目的用户依旧可以查看该项目信息!)",icon:"fa fa-exclamation-circle"}).on(function (e) {
                    if (e) {
                        var array = new Array(row.length);
                        for(var i = 0;i < row.length; i++){
                            array[i] = row[i].id;
                        }
                        $.ajax({
                            url:$ctx+ '/service/rest/platform.operation.ReservationRecordService/collection/deletes?ids='+array,
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
            url: $ctx + '/service/rest/platform.operation.ReservationRecordService/collection/query',
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
                {field : "userName" ,title : "用户姓名" ,align:"center" ,width : 80},
                {field : "medicalName" ,title : "预约套餐" ,align:"center" ,width : 80},
                {field : "userCard" ,title : "身份证号" ,align:"center" ,width : 80},
                {field : "reservationTime" ,title : "预约时间" ,align:"center" ,width : 80},
                {field : "createTime" ,title : "创建时间" ,align:"center" ,width : 80},
                {field : "operate", title : "操作", align:"center",width : 80,
                    formatter: function(value,row,index){
                        var html = '';
                        if (row.isDispose){
                            html += '<a class="btn btn-primary" disabled="disabled">已生成记录</a>';
                        }else {
                            html += '<a class="btn btn-primary" onclick="createInspect(\''+row.id+'\')">生成体检记录</a>';
                        }
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

    // 生成体检记录
    function createInspect(id){
        Modal.confirm({ message: "确定生成体检记录吗?",icon:"fa fa-exclamation-circle"}).on(function (r) {
            if(r){
                var url = $ctx + '/service/rest/platform.operation.ReservationRecordService/collection/createInspect';
                $.postExtend(url, {id:id}, function(data){
                    if(data.code == 1){
                        Modal.alert({message: data.description, icon:'fa fa-check'}).on(function(){
                            $('#dataList').bootstrapTable('refresh');
                        });
                    }else{
                        Modal.alert({message: data.description, icon:'fa fa-error'});
                    }
                });
            }
        });
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