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

        .formatterPic {
            width: 200px;
            height: 150px;
            align-content: center!important;
        }
    </style>
</head>
<body style="background: #f7f7f7;" class="nav-md">
<div class="container-fluid">
    <div class="x_panel">
        <form role="form"  id="searchForm" >
            <div class="container">
                <div class="row">
                    <div class="form-group col-xs-2">
                        <div class="input-group">
                            <span class="input-group-addon">体检套餐</span>
                            <input name="medicalName" class="form-control" type="text">
                        </div>
                    </div>
                    <div class="form-group col-xs-2">
                        <div class="input-group">
                            <span class="input-group-addon">合适性别</span>
                            <select name="gender" class="form-control">
                                <option value="Male">男</option>
                                <option value="Woman">女</option>
                                <option value="Both">两者</option>
                            </select>
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
                <h4>编辑体检套餐</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form" id="editForm" >
                    <input type="hidden" name="id" />
                    <div class="form-group">
                        <label class="col-sm-2 control-label">套餐名称：</label>
                        <div class="col-sm-10">
                            <input type="text" name="medicalName" class="form-control" required data-required="required" data-bv-notempty-message="项目名称不能为空" />
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">合适性别：</label>
                        <div class="col-sm-10">
                            <select name="suitableSex" class="form-control">
                                <option value="Male">男</option>
                                <option value="Woman">女</option>
                                <option value="Both">两者</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">合适年龄：</label>
                        <div class="col-sm-4">
                            <input type="number" name="ageMin" class="form-control" age data-age="age" data-bv-notempty-message="该内容不能为空"/>
                        </div>
                        <span class="col-sm-2" style="text-align: center">______</span>
                        <div class="col-sm-4">
                            <input type="number" name="ageMax" class="form-control" age data-age="age" data-bv-notempty-message="该内容不能为空" />
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">指标意义：</label>
                        <div class="col-sm-10">
                            <textarea rows="6" class="form-control" name="meaning" style="outline:medium;border-width:1px;" textarea data-textarea="textarea" data-bv-notempty-message="指标意义不能为空" ></textarea>
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


<!--上传头像start-->
<div class="modal fade" id="uploadPicModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header"><button class="close" type="button" onclick="hideEditForm();">x</button>
                <h4>上传套餐图片</h4>
            </div>
            <div class="modal-body">
            <form class="form-horizontal" role="form" id="uploadPicForm">
                <input type="hidden" name="id" />
                <div class="form-group">
                    <label class="col-sm-2 control-label">套餐图片：</label>
                    <div class="col-sm-10">
                        <input id="showImg" name="showImg" type="file" accept="image/*" />
                    </div>
                </div>
            </form>
        </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" onclick="uploadPic();">上传</button>
                <button type="button" class="btn btn-default" onclick="hideEditForm();">取消</button>
            </div>
        </div>
    </div>
</div>
<!--上传头像end-->


<jsp:include page="../inc/bottom.jsp" />
<script src="<%=path %>/js/bootstrap-validator/custom-validator.js"></script>
<script type="text/javascript">

    var imgPath = '<%=path%>/service/rest/tk.File/';

    $(function(){
        fishFileInput($("#showImg"), "url", ['png','jpg']);
    })


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
                            url:$ctx+ '/service/rest/platform.operation.MedicalItemsService/collection/deletes?ids='+array,
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
            url: $ctx + '/service/rest/platform.operation.MedicalItemsService/collection/query',
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
                {field : "medicalName" ,title : "套餐名称" ,align:"center" ,width : 80},
                {field : "showImg",title : "套餐图片", align:"center" ,width : 200,height :150
                    ,formatter:function(value,row,index){
                        if (!value) {
                            return '暂未上传';
                        } else {
                            return '<img  class="formatterPic" src="'+ imgPath + value + '"/>';
                        }
                    }},
                {field : "suitableSexName" ,title : "合适性别" ,align:"center" ,width : 80},
                {field : "ageMin" ,title : "合适年龄（最小）" ,align:"center" ,width : 80},
                {field : "ageMax" ,title : "合适年龄（最大）" ,align:"center" ,width : 80},
                {field : "itemNames" ,title : "指标" ,align:"center" ,width : 80},
                {field : "meaning" ,title : "指标意义" ,align:"center" ,width : 80},
                {field : "createTime" ,title : "创建时间" ,align:"center" ,sortable : true ,width : 80},
                {field : "operate", title : "操作", align:"center",width : 80,
                    formatter: function(value,row,index){
                        var html = '';
                        html += '<button type="button" class="btn btn-success"  onclick="uploadPicView(\''+row.id+'\')" >上传头像</button>'
                        html += '<a class="btn btn-primary" data-toggle="modal" data-target="#editModal" onclick="update(\''+row.id+'\')">编辑信息</a>';
                        html += '<a class="btn btn-primary" onclick="items(\''+row.id+'\',\''+row.medicalName+'\')">编辑套餐</a>';
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
                        var url = $ctx + '/service/rest/platform.operation.MedicalItemsService/collection/create';
                    }else{
                        var url = $ctx + '/service/rest/platform.operation.MedicalItemsService/collection/update';
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

    function items(id,name) {
        //打开新的Tab页面
        addTabs({id: name+id, title: name+"-详情", url: $ctx + "/admin/operation/relevance.jsp?medicalId="+id , close: true});
    }


    //上传职员头像（唤起弹出层）
    function uploadPicView(id){
        showModal("uploadPicModal");
        debugger;
        var row = $('#dataList').bootstrapTable('getRowByUniqueId', id);
        // 清空div内容并设置背景图
        $(".file-drop-zone-title").html('');
        $(".file-drop-zone-title").css("cssText","background-image:url('"+imgPath+row.showImg+"')!important");
        $(".file-drop-zone-title").css("background-size","100% 100%");
        row.showImg = "";
        $('#uploadPicForm').loadJson(row);
    }


    //上传职员头像
    function uploadPic(){
        var params = $('#uploadPicForm').serializeObject();
        var formData = new FormData($("#uploadPicForm")[0]);
        $.ajax({
            type:'post',
            url:$ctx + '/admin/upload_json.jsp?dir=image',
            data:formData,
            dataType:'json',
            processData:false,
            contentType:false,
            cache: false,
            beforeSend:function(){},
            success:function(data){
                var result = JSON.parse(data.data);
                if(data.code == "0"){
                    var upPicParams= {id:params.id,showImg:result.fileId};
                    //保存
                    $.postExtend($ctx + '/service/rest/platform.operation.MedicalItemsService/collection/uploadHeadImg', upPicParams, function(data){
                        if(data.code == 1){
                            Modal.alert({message: data.description, icon:'fa fa-times'}).on(function(){
                                hideEditForm();
                                $('#dataList').bootstrapTable('refresh');
                            });
                        }else{
                            Modal.alert({message: data.description, icon:'fa fa-error'});
                        }
                    });
                }else{
                    Modal.alert({message: data.message, icon:'fa fa-error'});
                }
            },
            complete:function(){},
            error:function(){
                Modal.alert({message: "请求失败",icon:"fa fa-times"});
            }
        })

    }


    function hideEditForm(){
        hideModal('editModal');
        $('#editForm')[0].reset();
        $("#editForm").data('bootstrapValidator').resetForm();//清除当前验证的关键之处

        //隐藏用户上传头像弹出层
        hideModal('uploadPicModal');
        $('#uploadPicForm')[0].reset();
        $('#uploadPicForm').clearForm();
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