
//确认修改密码
$(".userpro_ok").unbind().click(function(){

})

//验证修改密码
$('#user_form').bootstrapValidator({
		container: 'tooltip',
		//trigger: 'blur',
		feedbackIcons: {
			valid: 'glyphicon glyphicon-ok',
			invalid: 'glyphicon glyphicon-remove',
			validating: 'glyphicon glyphicon-refresh'
		},
		fields: {
			//原密码
			oldPwd: {
				validators : {
					notEmpty : {
						message : '原密码不能为空!'
					}
				}
			},
			//新密码
			newPwd: {
				validators : {
					notEmpty : {
						message : '新密码不能为空!'
					},
					stringLength: {
                        min: 6,
                        max: 30,
                        message: '新密码长度必须在6到30之间'
                    }
				}
			},
	
			//确认密码
			comPwd: {
				validators : {
					notEmpty : {
						message : '确认密码不能为空!'
					},
					stringLength: {
                        min: 6,
                        max: 30,
                        message: '确认密码长度必须在6到30之间'
                    },
                    identical: {//相同
                        field: 'newPwd', //需要进行比较的input name值
                        message: '两次密码不一致'
                    },
				}
			},
		}
	}).on('success.form.bv', function(e) {
		e.preventDefault();
		var $form = $(e.target);
		//确认
		$.post("../UserInfoController/updatePassWord",
				{
			userId:loginUser.userId,
			oldPwd:$("#oldPwd").val(),
			newPwd:$("#newPwd").val()
				},function(result){
			if(result.stus == 200){
				publicTipMessage("ok", "修改成功！");
				
				setTimeout(function(){
					publicTipMessage("error", "请重新登陆！");
					setTimeout(function(){
						$.get("../UserInfoController/initSession",function(){
							window.location = "../login/login.html";
						})
					},1000);
				},1000);
//				$("#oldPwd").val("");//清空
//				$("#newPwd").val("");//清空	
				
			}else if(result.stus == 300){
				publicTipMessage("error", "原密码不正确！");
			}else{
				publicTipMessage("error", "修改出错，请联系管理员！");
			}
		})
});
