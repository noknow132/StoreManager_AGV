var connectFlag = true;
$(function(){
	
	loadPlcConnConfig0();//加载堆垛机plc配置
	loadPlcConnConfig1();//加载输送链plc配置
	
	//获取本地Ip
	$.get("../ConfigParamController/getLocalIp",function(list){		
		if(list.length > 0){	
//			list.splice(0,1);//删除第一个本地ip地址 
			appendLocalIpHTML(list);
			$("#localIp").css("display","block");
			$("#noNet").text("");
		}else{
			$("#localIp").css("display","none");
			$("#noNet").text("暂无网络连接！");
		}

	})

//一键连接PLC
$("#plc_P").click(function(){
	publicTipMessage("busy","请等待...");
	var plcIpArrayFlag = false;//是否提示ip格式错误
	var plcPortArrayFlag = false;//是否提示端口格式错误
	
	var plcIpArray = new Array();//所有PLC配置的ip
	var plcPortArray = new Array();//所有PLC配置的端口
	//检查ip是否合法
	
	var plcIpInput = $("input[name='PLCIP']");//name为PLCIP的input
	var plcPortInput = $("input[name='PLCPort']");//name为PLCPort的input
	
	//获得输入的ip
	for (var i = 0; i < plcIpInput.length; i++) {
		plcIpArray.push($(plcIpInput[i]).val())
	}
	//获得输入的端口
	for (var i = 0; i < plcPortInput.length; i++) {
		plcPortArray.push($(plcPortInput[i]).val())
	}
	
	//循环检查ip是否合法
	for (var i = 0; i < plcIpArray.length; i++) {
		plcIpArrayFlag = f_check_IP(plcIpArray[i]);
		if(!plcIpArrayFlag){
			break;
		}
	}
	//循环检查端口是否合法
	for (var i = 0; i < plcPortArray.length; i++) {
		plcPortArrayFlag = check(plcPortArray[i]);
		if(!plcPortArrayFlag){
			break;
		}
	}
	//ip不合法，给提示
	if(!plcIpArrayFlag){
		publicTipMessage("error","PLC连接地址格式有误，连接失败！");
		return;
	}
	//端口不合法，给提示
	if(!plcPortArrayFlag){
		publicTipMessage("error","连接端口格式错误！");
		return;
	}
	
	setTimeout(function(){
		if(plcIpArrayFlag && plcPortArrayFlag){
		//堆垛机PlC连接
		var ddjLi = $("#duiduojiDiv ul li")//堆垛机div下所有的li
		console.info(ddjLi)
		if(ddjLi != undefined){
			for (var i = 0; i < ddjLi.length; i++) {
				testPLC(ddjLi[i]);//堆垛机PlC连接
			}
		}

		//输送链Plc连接
		var sslLi = $("#shusonglianDiv ul li")//输送链div下所有的li
		if(sslLi != undefined){
			for (var i = 0; i < sslLi.length; i++) {
				testPLC1($(sslLi[i]));//输送链PlC连接
			}
		}
		}
		
		if(connectFlag){
			$(".plc_error").hide();
			publicTipMessage("ok","连接成功！");
		}else{
			$(".plc_error").show();
			publicTipMessage("error","连接失败！");

		}
	}, 500)
});

})


//拼接本地IP
function appendLocalIpHTML(list){
	$("#localIp").empty();
	for (var i = 0; i < list.length; i++) {
		$("#localIp").append('<option value="'+list[i]+'">'+list[i]+'</option>');
	}
}



//测试连接 堆垛机
function testPLC(ddjLi){
	var flge = true;
	var Dflge = true;
	
	var PLCIP = $(ddjLi).find("input[name='PLCIP']").val();//堆垛机ip
	var PLCPort = $(ddjLi).find("input[name='PLCPort']").val(); //堆垛机端口
	var PlcId = $(ddjLi).find("input[name='plcConfigId']").val(); //配置id
	
	flge= f_check_IP(PLCIP); //检查ip是否合法
	Dflge=check(PLCPort);//检查端口是否合法
	    if(flge&&Dflge){

    $.ajax({
    	url : "../PlcConnConfigController/saveIp",
    	data:{
    		"localIp":$("#localIp").val(),
    		"PLCIP":PLCIP,
    		"PLCPort":PLCPort,
    		"PLCId":PlcId
    		},
    	type : "POST",
    	async:false,
    	success : function(map) {  		
    		if(map.stus == true){
    			//check();
    			$(ddjLi).find("img").attr("src","../image/start.png");
//    			$(".plc_success").show();
//    			$(".plc_error").hide();
    		}else{
//    			check(PLCIP);
//    			f_check_IP(PLCPort);
    			$(ddjLi).find("img").attr("src","../image/cha.png");
//    			$(".plc_success").hide();
//    			$(".plc_error").show();
    			connectFlag = false;//连接状态默认为ture
    		}
    	}
    	})
	    }
}
//测试连接 输送链
function testPLC1(sslLi){
	var flge = true;
	var Dflge = true;
	
	var PLCIP = $(sslLi).find("input[name='PLCIP']").val();//输送链ip
	var PLCPort = $(sslLi).find("input[name='PLCPort']").val(); //输送链端口
	var PlcId = $(sslLi).find("input[name='plcConfigId']").val(); //配置id

	flge= f_check_IP(PLCIP); //检查ip是否合法
	Dflge=check(PLCPort);//检查端口是否合法
	    if(flge&&Dflge){

    $.ajax({
    	url : "../PlcConnConfigController/saveIp",
    	data:{
    		"localIp":$("#localIp").val(),
    		"PLCIP":PLCIP,
    		"PLCPort":PLCPort,
    		"PLCId":PlcId
    		},
    	type : "POST",
    	async:false,
    	success : function(map) {  		
    		if(map.stus == true){
    			//check();
    			$(sslLi).find("img").attr("src","../image/start.png");
//    			$(".plc_success1").show();
//    			$(".plc_error1").hide();
    		}else{
    			//check();
//    			f_check_IP(PLCIP);
//    			check(PLCPort);
    			$(sslLi).find("img").attr("src","../image/cha.png");
//    			$(".plc_success1").hide();
//    			$(".plc_error1").show();
    			connectFlag = false;//连接状态默认为true
    			
    		}
    	}
    	})
	    }
}
//验证plc连接ip
function f_check_IP(ip){ 
//	var ip = document.getElementById($regIp).value;
   var re=/^(\d+)\.(\d+)\.(\d+)\.(\d+)$/;//正则表达式   
   if(re.test(ip)){   
       if( RegExp.$1<256 && RegExp.$2<256 && RegExp.$3<256 && RegExp.$4<256) 
       return true;   
   }   
//    $(".plc_success").hide();
//	$(".plc_error").show();
   return false;  

}
//验证连接端口
function check(prot){
	var reg = /^[0-9]{2,4}$/;
	if(!reg.test(prot)){
		return false;
	}
	return true;	
}


//加载堆垛机plc配置
function loadPlcConnConfig0(){
	$.post("../PlcConnConfigController/searchPlcConnConfigByType",{plcType:0},function(result){
		
		for (var i = 0; i < result.length; i++) {
			$("#duiduojiDiv ul").append('<li>'
					+'<div class="mt20 clearfix"  style="display:inline-block;">'
					+'<span class="fl fc_8 fs14 plc_span">PLC连接地址：</span>'
					+'<input type="text" class="plc_input fs14 fc_8 fl" id="reg_ip" name="PLCIP" value="'+result[i].plcIp+'" placeholder=""></input>'
					+'<span class="fl fc_8 fs14 plc_span lh24">连接端口：</span>'
					+'<input type="text" class="plc_input fs14 fc_8 fl" id="PLCPort" name="PLCPort" value="'+result[i].plcPort+'" placeholder="" style="width:100px;"></input>'
					+'<img src="" class="ml30" style="width:16px; margin-top:7px;">'
					+'<input type="hidden" name="plcConfigId" value="'+result[i].picId+'">'
				+'</div>'
			+'</li>')
		}
	})
}

//加载输送链plc配置
function loadPlcConnConfig1(){
	$.post("../PlcConnConfigController/searchPlcConnConfigByType",{plcType:1},function(result){
		
		for (var i = 0; i < result.length; i++) {
			$("#shusonglianDiv ul").append('<li>'
					+'<div class="mt20 clearfix" style="display:inline-block;">'
			+'<span class="fl fc_8 fs14 plc_span">PLC连接地址：</span>'
			+'<input type="text" class="plc_input fs14 fc_8 fl reg_ip"  name="PLCIP" value="'+result[i].plcIp+'" placeholder=""/>'
			+'<span class="fl fc_8 fs14 plc_span lh24">连接端口：</span>'
			+'<input type="text" class="plc_input fs14 fc_8 fl"  name="PLCPort" value="'+result[i].plcPort+'" placeholder="" style="width:100px;"/>'
			+'<img src="" class="ml30" style="width:16px; margin-top:7px;">'
			+'<input type="hidden" name="plcConfigId" value="'+result[i].picId+'">'
			+'</div>'
			+'</li>')
		}
		
	})
}