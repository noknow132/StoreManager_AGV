$(function(){
	loadWorkType();//加载作业类型
	
//	新建出口
	$(".type_add").click(function(){
		$(".zhezhao").addClass("block").removeClass("none");
	});
//取消
	$(".type_button_no").click(function(){
		$(".zhezhao").addClass("none").removeClass("block");
		clearSaveWorkTypeData();//清空新建类型模态框数据
	});
//确认
	$(".type_button_ok").click(function(){
		$(".zhezhao").addClass("none").removeClass("block");
		saveWorkType();//新增或者修改作业类型
	});
//关闭
	$(".type_close").click(function(){
		$(".zhezhao").addClass("none").removeClass("block");
		clearSaveWorkTypeData();//清空新建类型模态框数据
	});
	
		
	 //1.初始化Table
    var oTable = new TableInit();
    oTable.Init();


});


var TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#worktype_table').bootstrapTable({
            url: '../WorkTypeController/searchWorkTypeBootstrap',         //请求后台的URL（*）
            method: 'post',                      //请求方式（*）
            toolbar: '#toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            sortable: false,                     //是否启用排序
            sortOrder: "asc",                   //排序方式
            queryParams: oTableInit.queryParams,//传递参数（*）
            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       //初始化加载第一页，默认第一页
            pageSize: 10,                       //每页的记录行数（*）
            pageList: [10, 25, 50, 100],        //可供选择的每页的行数（*）
            search: true,                       //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: true,
            showColumns: true,                  //是否显示所有的列
            showRefresh: true,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: true,                //是否启用点击选中行
            height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "ID",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true
            }, {
                field: 'typeName',
                title: '名字'
            }, {
                field: 'typeStatue',
                title: '状态'
            }, {
                field: 'createTime',
                title: '创建时间'
                
            }, {
                field: 'updateTime',
                title: '最近修改时间'
            },{
                field: 'Desc2',
                title: '操作'
            }, ]
        });
    };

    //得到查询的参数
    oTableInit.queryParams = function (params) {
        var temp = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            limit: params.limit,   //页面大小
            offset: params.offset //页码
        };
        return temp;
    };
    return oTableInit;
};










//加载作业类型
function loadWorkType(){
	$("#storeHourse_ul_id").empty();
	$.get("../WorkTypeController/searchWorkType",function(result){
		//console.info(result)
		for (var i = 0; i < result.length; i++) {
			$("#storeHourse_ul_id").append('<li class="type_message_table_content">'+
					'<div><span class="ml80">'+result[i].typeName+'</span></div>'+
					'<div>'+(result[i].typeStatue=='0'?'<img class="type_table_diss ml5" src="../image/diss.png"/>':'<img class="type_table_start ml5" src="../image/start.png"/>')+'</div>'+
					'<div>'+format(result[i].createTime)+'</div>'+
					'<div>'+(result[i].updateTime == null?'':format(result[i].updateTime))+'</div>'+
					'<div style="color: #078fff;" class="cursor center">'+
					'<span style="color: #078fff;margin-left:45%;" class="fl type_disable" dataNo="'+result[i].typeId+'">'+(result[i].typeStatue=='0'?'启用':'禁用')+'</span>'+
					'<span style="color: #078fff;" class="fl type_alert ml20" dataNo="'+result[i].typeId+'">修改</span>'+
					'</div>'+
					'</li>');
		}
		//启用或禁用
		$(".type_disable").click(function(){
			var isUsed = "1";//默认启用
			if($(this).text() == "禁用"){
				isUsed = "0";//禁用
			}
			$.post("../WorkTypeController/updateWorkTypeById",{
				workTypeId:$(this).attr("dataNo"),
				isUsed:isUsed
				},function(result){
					if (result.stus == "200") {
						loadWorkType();//加载作业类型
						publicTipMessage("ok","操作成功！");
					} else {
						publicTipMessage("error","操作失败！");
					}
			})
		});
		
		//修改type_alert
		$(".type_alert").click(function(){
			var doc = document.documentElement;  
			var relHeight = (doc.clientHeight > doc.scrollHeight) ? doc.clientHeight : doc.scrollHeight;//获取屏幕高度和当前页面高度中的较大值  
			document.getElementById('zhezhao').style.height = relHeight+'px';//id为about_zhezhao的div就是我页面中的遮罩层 
			$(".zhezhao").addClass("block").removeClass("none");
			$(".type_tan_").text("修改");
			$.post("../WorkTypeController/searchWorkTypeById",{workTypeId:$(this).attr("dataNo")},function(result){
				$("#typeId").val(result.typeId);
				$("[name='typeName']").val(result.typeName);//名称
			})
		});
	})
}

//操作出口 新增或修改
function saveWorkType(){
	$.post("../WorkTypeController/saveWorkType",{
		typeName:$("[name='typeName']").val(),
		typeId:$("#typeId").val()
		},function(result){
		if(result.stus == "200"){
			publicTipMessage("ok","操作成功！");
			$(".zhezhao").addClass("none").removeClass("block");
			loadWorkType();//加载作业类型
			clearSaveWorkTypeData();//清空新建类型模态框数据
		}else{
			publicTipMessage("error","操作失败！");
		}
	})
}

//清空新建类型模态框数据
function clearSaveWorkTypeData(){
	$("[name='typeName']").val("");//清空出口名
	$("#typeId").val("");
}