
$(".imp_btnGroup a").hide();
//确认按钮
$("#sure").click(function(){
	$.post("../ConfigParamController/createRegFile",{
		regSchool:"申请学校："+$("[name='name']").val(),
		principal:"联系人："+$("[name='principal']").val(),
		email:"邮箱："+$("[name='email']").val(),
		tel:"电话："+$("[name='tel']").val()
		},function(result){
		if(result.stus == 200){
			$("#sure").hide();
			$(".imp_btnGroup a").show();
				$(".imp_btnGroup a").attr("href","../storeRegedit.txt");

		}else if(result.stus == 300){
			publicTipMessage("error","注册码不正确！");
		}else{
			publicTipMessage("error","系统出错，请联系管理员！");
		}
		
	})
})

//测试连接（连接正常，到任务页，不正常，到配置页）
function testPLC(){
    $.ajax({
    	url : "../ConfigParamController/testConnect",
    	type : "GET",
    	success : function(map) {  		
    		if(map.stus == true){
    			window.location = "../currentTask/currentTask.html";
    		}else{
    			//plc连接失败
    			window.location = "../Configuration/configuration.html";
    		}
    	}
    	})
}