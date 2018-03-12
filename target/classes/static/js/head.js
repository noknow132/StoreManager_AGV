
//Created by DELL on 2018/1/5

var warnCode = 0;//报警状态
var roleLevel = 0;//0是管理员 1操作员
var isLinkFlag=false;
var loginUser;//登录人信息

$(function() {
	//alert("head");
	userHead('../userHead/head.html');
    checkLogin();//检查登录session
    setSystemName();//系统名称设置
    reset();//入口复位按钮点击事件
    //退出点击
	$('.clic').click(function() {
		$.get("../UserInfoController/initSession",function(){
			window.location = "../login/login.html";
		})
		
	});
	//报警每隔一秒 刷新一次
	setInterval(function(){
		//报警
		$.get("../ConfigParamController/warn",function(result){
			var code = result.warnCode;
			if(code != "0"){
				if(code == "1"){
					$("#ChangeVal").text("取货位无货")
				}else if(code == "2"){
					$("#ChangeVal").text("放货位有货")
				}else if(code == "3"){
					$("#ChangeVal").text("启动时堆垛机有货")
				}else if(code == "4"){
					$("#ChangeVal").text("货物歪斜")
				}else if(code == "5"){
					$("#ChangeVal").text("安全刹车报警")
				}
				warnCode = code;
				$("#MustDo").val(result.warnRecordId)//报警记录的id
				//显示报警框
				$("#head_cho").modal({
					backdrop : false,
				});
			} 
		});
	}, 5000)
	
	//报警确认
	$("#MustDo").click(function(){
		$.post("../ConfigParamController/solveWarn",
				{
			warnCode:warnCode,
			warnRecordId:$("#MustDo").val()
			}
		,function(result){ 
			if(result.stus= "200"){
				publicTipMessage("ok", "解除报警！");
				$('#head_cho').modal('hide')//隐藏增加模态框
			}else{
				publicTipMessage("error", "解除报警时出现异常，解除报警失败！");
			}
		});
	})
	

});

function userHead(url){
    $.ajax({
    	url : url,
    	contentType : "application/json;charset=UTF-8",
    	type : "GET",
    	async:false,
    	success : function(data) {
            $(".add_top").html(data);
    	}
    	})
}

//获取url参数
function GetRequest() {
  var url = location.search; // 获取url中"?"符后的字串
  var theRequest = new Object();
  if (url.indexOf("?") != -1) {
    var str = url.substr(1);
    strs = str.split("&");
    for (var i = 0; i < strs.length; i++) {
      theRequest[strs[i].split("=")[0]] = unescape(strs[i].split("=")[1]);
    }
  }
  return theRequest;
}


//检查登录信息
function checkLogin(){
	$.ajax({
		url : "../UserInfoController/checkLoginInfo",
		contentType : "application/json;charset=UTF-8",
		type : "POST",
		async:false,
		success : function(result) {
			console.info(typeof result);
			if(typeof result =="string"){
				window.location = "../login/login.html";
			}else{
				loginUser = result.userBase;
				if(loginUser!=undefined && loginUser != null && loginUser != ""){
					roleLevel = loginUser.roleLevel;
					$(".head_auth_name").text(loginUser.userName);
					 if(roleLevel == 0){  //判断是管理员登陆拼接账户管理
						 $(".add_nav_ul li").eq(6).show();
						 $(".one_key").show();
						 $(".head_auth").text("管理员:");
						return
					}
				}else{
					publicTipMessage("error","登录过期，请重新登录！");
					window.location = "../login/login.html";
				}
			}
			
			
		}
			
	})
	$(".li_a").click(function(){
		$(".li_a").removeClass("nav_select");
		$(this).addClass("nav_select");
	});
}

//系统名称
function setSystemName(){
	$.get("../ConfigParamController/selectConfigParamOne",function(result){
		var titlename = "";
		if(result != null && result != undefined && result.programName != ""){
			titlename = result.programName;
		}else{
			titlename = "益课立库管理系统";
		}
		$(".add_top_min .add_concent_name").text(titlename);
		$("title").text(titlename);
	})
}

////入口复位按钮点击事件（非PLC操作）
//function  reset(){
//	$(".add_nav_ul_right").click(function(){
//		$.get("../ConfigParamController/reset",function(result){
//			console.info(result)
//			if(result.stus == 200){
//				if(result.resetValue == 0){
//					publicTipMessage("ok","开始复位！");
//				}else if(result.resetValue == 1){
//					publicTipMessage("ok","复位进行中，不要重复点击！");
//				}else{
//					publicTipMessage("error","当前无需复位");
//				}
//			}else if(result.stus == 500){
//				publicTipMessage("error","复位出错，请联系管理员");
//			}
//		})
//	})
//
//}
//入口复位按钮点击事件（PLC操作）
function  reset(){
	$("#reset").click(function(){
		//查询是否有正在执行中的任务
		$.post("../workStep/searchWorkStepByStatue",{status:1},function(result){
			if(result.length == 0){
				//PLC复位
				$.get("../ConfigParamController/resetPlace",function(result){
					if(result){
						publicTipMessage("ok","复位成功！");
					}else{
						publicTipMessage("error","复位出错，请联系管理员！");
					}
				})
			}else{
				publicTipMessage("error","存在正在执行的任务，请结束后在复位！");
			}
		})

	})
}

//启动 连接堆垛机和输送链两个PLC
function connectPlcConn(){
	var map ;
    $.ajax({
    	url : "../PlcConnConfigController/plcConn",
    	contentType : "application/json;charset=UTF-8",
    	type : "GET",
    	async:false,
    	success : function(data) {
    		map = data;
    	}
    	})
    	return map;
}

//判断网络状态
function isNetStateConnect(){
	var flag = false;//是否存在网络
    $.ajax({
    	url : "../ConfigParamController/isNetStateConnect",
    	contentType : "application/json;charset=UTF-8",
    	type : "GET",
    	async:false,
    	success : function(data) {
    		flag = data;  		
    	}
    	})
    	return flag;
}


//时间戳格式化 yyyy-MM-dd HH:ss:mm
function add0(m){return m<10?'0'+m:m }//保证年月日时分秒 至少是两位数，一位数会自动补0
function format(shijianchuo)
{
//shijianchuo是整数，否则要parseInt转换
var time = new Date(shijianchuo);
var y = time.getFullYear();
var m = time.getMonth()+1;
var d = time.getDate();
var h = time.getHours();
var mm = time.getMinutes();
var s = time.getSeconds();
return y+'-'+add0(m)+'-'+add0(d)+' '+add0(h)+':'+add0(mm)+':'+add0(s);
}
