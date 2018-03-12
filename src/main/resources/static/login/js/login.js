/**
 * Created by DELL on 2018/1/11.
 */
$(function(){
    var height =  $(window).height();
    $(".login_bg").css("height",height);
//新增或修改 账号
    $('#login_form').bootstrapValidator({
        container: 'tooltip',
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
        	loginName: {
                validators: {
                    notEmpty: {
                        message: '姓名不能为空！'
                    },
                    /*  stringLength: {
                     min: 6,
                     max: 18,
                     message: '用户名长度必须在6到18位之间'
                     }, */

                }
            },
            password: {
                validators: {
                    notEmpty: {
                        message: '密码不能为空！'
                    },
                    /*  stringLength: {
                     min: 6,
                     max: 18,
                     message: '用户名长度必须在6到18位之间'
                     }, */

                }
            },


        }
    }).on('success.form.bv', function(e) {
        // Prevent form submission
        e.preventDefault();
        var $form = $(e.target);
        $.post("../UserInfoController/userLogin", $form.serialize(), function(data) {
			if(data.statu == "200"){
//				window.location = data.location;
				autoRegEdit();//判断注册码是否存在
				//publicTipMessage("ok","登录成功！");
			}else if(data.statu == "300"){		
				publicTipMessage("error","此账号已被禁用，请联系管理员！");
			}else if(data.statu == "350"){		
				publicTipMessage("error","登录失败，本机暂未授权，请联系管理员！");
			}else if(data.statu == "500"){		
				$("#login_form").data('bootstrapValidator').resetForm("true");
				publicTipMessage("error","账号或密码错误！");
				
			}
		}, 'json');

    });
});

//判断注册码是否存在
function autoRegEdit(){
	
	$.get("../ConfigParamController/autoRegEdit",function(result){
		/*var config = result.configParam;
		if(config != undefined){
			if(config.regEditCopy !="" && config.regEditCopy !=null ){	
				testPlc();//测试连接（连接正常，到任务页，不正常，到配置页）
				publicTipMessage("ok","登录成功！");
			}else{
				window.location = "../login/impower.html";
			}
		}*/
		if(result.stus==200){
			publicTipMessage("ok","登录成功！");
			testPlc();//测试连接（连接正常，到任务页，不正常，到配置页）
		}else if(result.stus==300){
			window.location = "../login/impower.html";
			publicTipMessage("error","注册码不正确！");
		}else if(result.stus==301){
			publicTipMessage("error","注册码不正确！");
		}
		
	})
}

//测试连接（连接正常，到任务页，不正常，到配置页）
function testPlc(){
	$.get("../PlcConnConfigController/testConnect",function(result){
		if(result.stus == true){
			window.location = "../currentTask/currentTask.html";
		}else{
			//plc连接失败
			window.location = "../Configuration/configuration.html";
		}
	})	
}
