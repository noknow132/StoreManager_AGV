$(function(){
	//1.初始化Table
    var oTable = new TableInit();
    oTable.Init();

//    //2.初始化Button的点击事件
//    var oButtonInit = new ButtonInit();
//    oButtonInit.Init();
	
	loadOutLet();//加载出口
//	新建出口
	$(".config_add").click(function(){
		$(".zhezhao").addClass("block").removeClass("none");
	});
//取消
	$(".export_button_no").click(function(){
		$(".zhezhao").addClass("none").removeClass("block");
		clearInsertOutLetData();//清空添加出口模态框数据
	});
//确认
	$(".export_button_ok").click(function(){
		if(!OutType()){
			return;
		}
		if(!OutNo()){
			return;
		}
		saveOutLet();
		 
	});
//叉叉关闭
	$(".export_close").click(function(){
		$(".zhezhao").addClass("none").removeClass("block");
		clearInsertOutLetData();//清空添加出口模态框数据
	});

});

var TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#tb_departments').bootstrapTable({
            url: '../outLet/searchOutLet1',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            sortable: false,                     //是否启用排序
            sortOrder: "asc",                   //排序方式
            queryParams: queryParams,//传递参数（*）
            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       //初始化加载第一页，默认第一页
            pageSize: 10,                       //每页的记录行数（*）
            pageList: [10, 25, 50, 100],        //可供选择的每页的行数（*）
            search: false,                       //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
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
                field: 'outlet_name',
                title: '出口名称'
            }, {
                field: 'out_type',
                title: '出口类型',
                formatter : function(value, row, index){ 
                	var html = row.out_type == 0?"出货口":"分拣口";
                	return  html
                	}
            }, {
                field: 'out_no',
                title: '出口编号'
            }, {
                field: 'is_uesd',
                title: '状态',
                formatter : function(value, row, index){ 
                	var html = row.is_uesd == 0?'<img class="config_table_diss ml5" src="../image/diss.png"/>':'<img class="config_table_start  ml5" src="../image/start.png"/>';
                	return  html
                	}
                	
            }, {
                field: 'create_time',
                title: '创建时间',
                formatter : function(value, row, index){ return format(value) }
            },{
                field: 'update_time',
                title: '最近修改时间',
                formatter : function(value, row, index){ return format(value) }
            },{
                field: 'operate',
                title: '操作',
                formatter : function(value, row, index){ 
                	var html = '<span style="color: #078fff;" class="fl config_disable" dataNo="'+row.outlet_id+'">'+(row.is_uesd=='0'?"启用":"禁用")+'</span>';
                	html +='<span style="color: #078fff;" class="fl config_alert ml20" dataNo="'+row.outlet_id+'">修改</span> ';
//                	return '<span style="color: #078fff;" class="fl config_disable" dataNo="'+row.outlet_id+'">'+(row.is_uesd=='0'?"启用":"禁用");
                	return  html
                	}
            },]
        });
    };
    return oTableInit;
};

//得到查询的参数
var queryParams = function () {
    var temp = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
    		outType:$("#exportisSeachSelect").val()
    };
    return temp;
};

//出口条件筛选查询
$("#exportisSeachSelect").unbind().change(function(){
	loadOutLet();
	$('#tb_departments').bootstrapTable('refresh', {queryParams: queryParams});//刷新table
})

//加载出口
function loadOutLet(){
	$.get("../outLet/searchOutLet1",{outType:$("#exportisSeachSelect").val()},function(result){
		$("#storeHourse_ul_id").empty();
		for (var i = 0; i < result.rows.length; i++) {
			$("#storeHourse_ul_id").append('<li class="config_message_table_content">'
			+'<div><span class="ml30">'+result.rows[i].outlet_name+'</span></div>'		
			+'<div>'+(result.rows[i].out_type=="0"?"出货口":"分拣口")+'</div>'
			+'<div>'+result.rows[i].out_no+'</div>'
			+'<div>'			
			+(result.rows[i].is_uesd=='0'?'<img class="config_table_diss ml5" src="../image/diss.png"/>':'<img class="config_table_start  ml5" src="../image/start.png"/>')		
			+'</div>'		
			+'<div>'+format(result.rows[i].create_time)+'</div>'
			+'<div>'+(result.rows[i].update_time == null?'':format(result.rows[i].update_time))+'</div>'
			+'<div style="color: #078fff;" class="cursor">'		
			+'<span style="color: #078fff;" class="fl config_disable" dataNo="'+result.rows[i].outlet_id+'">'+(result.rows[i].is_uesd=='0'?"启用":"禁用")+'</span>'		
			+'<span style="color: #078fff;" class="fl config_alert ml20" dataNo="'+result.rows[i].outlet_id+'">修改</span>'		
			+'</div>'		
			+'</li>');
		}
		//修改出口
		$(".config_alert").unbind().click(function(){
			var doc = document.documentElement;  
			var relHeight = (doc.clientHeight > doc.scrollHeight) ? doc.clientHeight : doc.scrollHeight;//获取屏幕高度和当前页面高度中的较大值  
			document.getElementById('zhezhao').style.height = relHeight+'px';//id为about_zhezhao的div就是我页面中的遮罩层 
			$(".zhezhao").addClass("block").removeClass("none");
			$(".export_tan_").text("修改");
			$.post("../outLet/searchOutLetById",{outletId:$(this).attr("dataNo")},function(result){
				$("#outletId").val(result.outletId);
				$("[name='outletName']").val(result.outletName);//出口名
				if(result.outType == 0){
					$("#radio-1-2").prop("checked",true);
				}else if(result.outType == 1){
					$("#radio-1-3").prop("checked",true);
				}
				$("[name='outNo']").val(result.outNo);//编号
			})
		});
		
	//禁用或启用
	$(".config_disable").unbind().click(function(){
		var isUsed = "1";//默认启用
		if($(this).text() == "禁用"){
			isUsed = "0";//禁用
		}
		$.post("../outLet/updateOutLetIsUsed",{
			outletId:$(this).attr("dataNo"),
			isUsed:isUsed
			},function(result){
				if (result.stus == "200") {
					loadOutLet();//加载出口
					publicTipMessage("ok","操作成功！");
				} else {
					publicTipMessage("error","操作失败！");
				}
			})
		});
	
	//table 禁用事件
	//off() 方法移除用.on()绑定的事件处理程序。
	$("#tb_departments").off("click",".config_disable").unbind().on("click",".config_disable",function(){
		var isUsed = "1";//默认启用
		if($(this).text() == "禁用"){
			isUsed = "0";//禁用
		}
		$.post("../outLet/updateOutLetIsUsed",{
			outletId:$(this).attr("dataNo"),
			isUsed:isUsed
			},function(result){
				if (result.stus == "200") {
					//刷新table
					$('#tb_departments').bootstrapTable('refresh', {queryParams: queryParams});
					publicTipMessage("ok","操作成功！");
				} else {
					publicTipMessage("error","操作失败！");
				}
			})
	})
	//table 修改事件
	//off() 方法移除用.on()绑定的事件处理程序。
	$("#tb_departments").off("click",".config_alert").on("click",".config_alert",function(){
		var doc = document.documentElement;  
			var relHeight = (doc.clientHeight > doc.scrollHeight) ? doc.clientHeight : doc.scrollHeight;//获取屏幕高度和当前页面高度中的较大值  
			document.getElementById('zhezhao').style.height = relHeight+'px';//id为about_zhezhao的div就是我页面中的遮罩层 
			$(".zhezhao").addClass("block").removeClass("none");
			$(".export_tan_").text("修改");
			$.post("../outLet/searchOutLetById",{outletId:$(this).attr("dataNo")},function(result){
				$("#outletId").val(result.outletId);
				$("[name='outletName']").val(result.outletName);//出口名
				if(result.outType == 0){
					$("#radio-1-2").prop("checked",true);
				}else if(result.outType == 1){
					$("#radio-1-3").prop("checked",true);
				}
				$("[name='outNo']").val(result.outNo);//编号
			})
	})
	
	
	})
}

//清空添加出口模态框数据
function clearInsertOutLetData(){
	$("[name='outletName']").val("");//清空出口名
	$("#radio-1-2").prop("checked",false);//取消出口选中
	$("#radio-1-3").prop("checked",false);//取消分拣口选中
	$("[name='outNo']").val("");//清空编号
	$("#outletId").val("");
}

//验证编号非空且只能为数字
function OutNo(){
	var outNoS=$("#outNo").val();
	var re = /^\d+$/
	if(outNoS===""){
		publicTipMessage("error","编号不能为空！");
	return false;
	}else if(!re.test(outNoS)){
		publicTipMessage("error","编号只能为数字！");
	return false;
	}
	return true;
	}

//验证类型是否选中
function OutType(){
	if($("input[name='outType']:checked").length == 0){
		publicTipMessage("error","类型不能为空！");
		return false;
	}
	return true;
}

//操作出口 新增或修改
function saveOutLet(){
	var outType = $("#radio-1-2").prop("checked")?0:1;
	$.post("../outLet/saveOutLet",{
		outletName:$("[name='outletName']").val(),
		outType:outType,
		outNo:$("#outNo").val(),
		outletId:$("#outletId").val()
		},function(result){
			if(result.stus == "200"){
				publicTipMessage("ok","操作成功！");
				$(".zhezhao").addClass("none").removeClass("block");
				loadOutLet();//加载出口
				clearInsertOutLetData();//清空添加出口模态框数据
				$('#tb_departments').bootstrapTable('refresh', {queryParams: queryParams});//刷新table
			}else if(result.stus == "300"){
				publicTipMessage("error","当前编号已存在！");
			}else{
				publicTipMessage("error","操作失败！");
			}
			//loadOutLet();//加载出口
	})
}




    