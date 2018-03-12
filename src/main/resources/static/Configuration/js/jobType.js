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

});

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