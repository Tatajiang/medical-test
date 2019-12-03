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
                <button class="btn btn-primary"  type="button" data-toggle="modal" data-target="#editModal" ><i class="fa fa-plus-square" aria-hidden="true" style="margin-right:5px;"></i>添加</button>&nbsp;
                <button class="btn btn-danger"  type="button" id="delete"><i class="fa fa-minus-square" aria-hidden="true" style="margin-right:5px;"></i>批量删除</button>&nbsp;
            </div>
        </div>

        <table id="dataList" class="table table-striped jambo_table"></table>

    </div>
</div>

<!--编辑模态框start-->
<div class="modal fade" id="editModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header"><button class="close" type="button" onclick="hideEditForm();">x</button>
                <h4>编辑体检项目</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form" id="editForm" >
                    <input type="hidden" name="id" />
                    <div class="form-group">
                        <label class="col-sm-2 control-label">项目名称：</label>
                        <div class="col-sm-10">
                            <input type="text" name="name" class="form-control" required data-required="required" data-bv-notempty-message="项目名称不能为空" />
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">项目描述：</label>
                        <div class="col-sm-10">
                            <textarea rows="6" class="form-control" name="description" style="outline:medium;border-width:1px;" textarea data-textarea="textarea" data-bv-notempty-message="描述信息不能为空" ></textarea>
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
<!--编辑角色模态框end-->


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
                Modal.confirm({ message: "确定要删除吗?(注意：如果删除的项目已分配到套餐中，将会删除套餐中的该项目!)",icon:"fa fa-exclamation-circle"}).on(function (e) {
                    if (e) {
                        var array = new Array(row.length);
                        for(var i = 0;i < row.length; i++){
                            array[i] = row[i].id;
                        }
                        $.ajax({
                            url:$ctx+ '/service/rest/platform.operation.ItemService/collection/deletes?ids='+array,
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
                return serializeTableQueryParams(params, {});
            },
            columns : [
                {field : 'checked', checkbox : true, visible: true},
                {field : "id",align:"center",visible:false},
                {field : "name" ,title : "项目名称" ,align:"center" ,width : 80},
                {field : "description" ,title : "项目描述" ,align:"center" ,width : 80},
                {field : "createTime" ,title : "创建时间" ,align:"center" ,sortable : true ,width : 80},
                {field : "operate", title : "操作", align:"center",width : 80,
                    formatter: function(value,row,index){
                        var html = '';
                        html += '<a class="btn btn-primary" data-toggle="modal" data-target="#editModal" onclick="update(\''+row.id+'\')">编辑信息</a>';
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
    function update(id){
        var row = $('#dataList').bootstrapTable('getRowByUniqueId', id);
        $('#editForm').loadJson(row);

    }

    // 保存对象
    function save(){
        //获取表单对象
        var bootstrapValidator = $('#editForm').data('bootstrapValidator');
        //手动触发验证
        bootstrapValidator.validate();
        if(bootstrapValidator.isValid()){
            Modal.confirm({ message: "确定保存?",icon:"fa fa-exclamation-circle"}).on(function (r) {
                if(r){
                    var params = $('#editForm').serializeObject();
                    if(!params.id){
                        var url = $ctx + '/service/rest/platform.operation.ItemService/collection/create';
                    }else{
                        var url = $ctx + '/service/rest/platform.operation.ItemService/collection/update';
                    }
                    $.postExtend(url, params, function(data){
                        if(data.code == 1){
                            Modal.alert({message: data.description, icon:'fa fa-check'}).on(function(){
                                hideEditForm();
                                $('#dataList').bootstrapTable('refresh');
                            });
                        }else{
                            Modal.alert({message: data.description, icon:'fa fa-error'});
                        }
                    });
                }
            });
        }
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