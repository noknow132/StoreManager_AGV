$(function(){
	loadOutLet();//加载出口
	$("#table1 thead").css({"background-color":"#078fff"});
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


//出口条件筛选查询
$("#exportisSeachSelect").unbind().change(function(){
	loadOutLet();
})

//加载出口
function loadOutLet(){
	$.get("../outLet/searchOutLet",{outType:$("#exportisSeachSelect").val()},function(result){
		$("#storeHourse_ul_id").empty();
		for (var i = 0; i < result.length; i++) {
			$("#storeHourse_ul_id").append('<li class="config_message_table_content">'
			+'<div><span class="ml30">'+result[i].outlet_name+'</span></div>'		
			+'<div>'+(result[i].out_type=="0"?"出货口":"分拣口")+'</div>'
			+'<div>'+result[i].out_no+'</div>'
			+'<div>'			
			+(result[i].is_uesd=='0'?'<img class="config_table_diss ml5" src="../image/diss.png"/>':'<img class="config_table_start  ml5" src="../image/start.png"/>')		
			+'</div>'		
			+'<div>'+format(result[i].create_time)+'</div>'
			+'<div>'+(result[i].update_time == null?'':format(result[i].update_time))+'</div>'
			+'<div style="color: #078fff;" class="cursor">'		
			+'<span style="color: #078fff;" class="fl config_disable" dataNo="'+result[i].outlet_id+'">'+(result[i].is_uesd=='0'?"启用":"禁用")+'</span>'		
			+'<span style="color: #078fff;" class="fl config_alert ml20" dataNo="'+result[i].outlet_id+'">修改</span>'		
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
	})
}

//启用/禁用
function isDisAble(id,isUsed){
	$.post("../outLet/updateOutLetIsUsed",
		{
			outletId:id,//$(this).attr("dataNo"),
			isUsed:isUsed
		},function(result){
			debugger
			if (result.stus == "200") {
				loadOutLet();//加载出口
				publicTipMessage("ok","操作成功！");
			} else {
				publicTipMessage("error","操作失败！");
			}
		}
	);
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
			}else if(result.stus == "300"){
				publicTipMessage("error","当前编号已存在！");
			}else{
				publicTipMessage("error","操作失败！");
			}
	})
}




    