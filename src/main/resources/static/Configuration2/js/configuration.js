$(function(){
//一键初始化弹出框
	//添加点击事件
	$(".one_key").click(function(){
		$("#one_key_del").modal({
			backdrop : false,
		});

		$("#delChangeStoreId").attr("changeIdData",$(this).attr("changeIdData"));	
	});
//配置导航切换样式
	$(".config_con_ul li").click(function(){
		$(this).addClass("current").siblings().removeClass("current");
	});
//配置系统名称
	$(".config_system_buuton").click(function(){
		$.get("../ConfigParamController/selectConfigParamOne",function(result){
			if(result == null || result == undefined){
				publicTipMessage("error","请添加参数配置！");
				//alert("请添加参数配置！")
			}else if(result.programName == null || result.programName ==""){
				$(".config_zhe").addClass("block").removeClass("none");
			}else{
				publicTipMessage("error","系统名确认后不可更改，请联系管理员！");
				//alert("系统名确认后不可更改，请联系管理员")
			}
		})
	});
	

//确认配置系统名称
	$(".config_zhe_que").click(function(){
		if($("#systemName").val() == ""){
			publicTipMessage("error","请填写名称！");
			return;
		}
		$.post("../ConfigParamController/updateConfigParamName",{name:$("#systemName").val()},function(result){
			if(result.stus == "200"){
				publicTipMessage("ok","操作成功！");
				//alert("操作成功！")
				$(".add_top_min .add_concent_name").text(result.systemName);//刷新名称
				$(".config_zhe").addClass("none").removeClass("block");
			}else if(result.stus == "201"){
				publicTipMessage("error","名称已被修改过，不能再次修改！！！");
				$(".config_zhe").addClass("none").removeClass("block");
			}else{
				publicTipMessage("error","操作失败，请联系管理员！");
			}
		})
	});	
	
	//一键初始化操作
	$("#one_key_del_ok").click(function(){
		debugger
		$.post("../ConfigParamController/initByOnekey",function(result){
			debugger
			if(result.stus == 200 ){
				publicTipMessage("ok","操作成功！");
				$('#one_key_del').modal('hide');//隐藏仓库选择模态框

			}else if(result.stus == 500){
				publicTipMessage("error","操作失败！");
			}
		})
	});
	
});