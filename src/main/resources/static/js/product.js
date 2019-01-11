$("#grantMoneyListDataGrid").datagrid({
    columns : [[
        //列定义
        {field : 'id',          title : '编号',     align:"left", halign:"left",width : 10},
        {field : 'reportId',    title : '报表id',   align:"left", halign:"left",width : 10},
        {field : 'sqlWhere',    title : '查询条件', align:"left", halign:"left",width : 10},
        {field : 'state',       title : '查询状态', align:"left", halign:"left",width : 10},
        {field : 'startTime',   title : '开始时间', align:"left", halign:"left",width : 10},
        {field : 'endTime',     title : '结束时间', align:"left", halign:"left",width : 10},
        {field : 'requestUrl',  title : '查看excel', align:"left", halign:"left",width : 10}
    ]],
    rownumbers : true,
    singleSelect:true,
    collapsible:false,
    isCanQuery:false,
    fitColumns:true,
    remoteSort:true,
    showFooter: true,
    fit:true,
    striped:true,
    url:"/index/getTabData",
    method:"POST",
    loadMsg:null,
    pagination:true,
    noheader:true,
    pageNumber:1,
    pageSize:10,
    pageList:[10,20,30,40,50],
    loader:function(param,success,error){
        var opts = $("#grantMoneyListDataGrid").datagrid("options");
        var isCanQuery = opts.isCanQuery||false;
        if(isCanQuery){
            $("#submitButton,#resetButton,#excelExportButton").linkbutton("disable");
            $.ajaxPackage({
                url:opts.url,
                async:true,
                type:opts.method,
                data:param,
                dataType:"json",
                isShowLoadMask:true,
                success:function(response, textStatus, jqXHR){
                    if(response.resCode == "000000"){
                        success(response.attachment);
                    }else{
                        $.messager.alert("提示", response.resMsg, "error", function(){
                            error.apply(this, arguments);
                        });
                    }
                },
                error:function(response, textStatus, jqXHR){
                    $.messager.alert("提示", "查询失败", "error", function(){
                        error.apply(this, arguments);
                    });
                },
                complete:function(jqXHR,textStatus){
                    $("#submitButton,#resetButton,#excelExportButton").linkbutton("enable");
                }
            });
        }
        return isCanQuery;
    },
    onLoadSuccess:function(data){
        $("#grantMoneyListDataGrid").datagrid("resize");
        var $rownumber = $(".datagrid-body-inner .datagrid-btable .datagrid-td-rownumber .datagrid-cell-rownumber");
        if($rownumber.length>0){
            $($rownumber[$rownumber.length-1]).html("");
        }
    }
});