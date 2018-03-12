
// Created by DELL on 2018/1/9.

var delUserInfoId = "";//单个删除账号的id，用于单选删除模态框
$(function () {
	loadUserInfo();//加载账号信息
	deleteManyUserInfoById();//多选删除
	//取消模态框 也要清空内容
	$("#user_add .putIn_btn_T").click(function(){
		$('#user_addForm').bootstrapValidator('resetForm', true); //清空验证痕迹和内容
	})

    $("#putIn_allChe").click(function () {
        if($("#putIn_allChe").text()=="全选"){
            $(this).text('取消');
            $("#userInfoUlApp").find("input[type='checkbox']").prop("checked",true);
        }else if($("#putIn_allChe").text()=="取消"){
            $(this).text('全选');
            $("#userInfoUlApp").find("input[type='checkbox']").prop("checked",false);
        }
    });

    // 弹出 新增模态框
    $('.addUser').click(function() {
        $("#user_add").modal({
            backdrop : false,
        });
        $("input[name='userId']").val($(this).attr("dataNo"));
        $('input[name="userName"]').val('');
        $('select[name="roleLevel"]').val('');
        $('input[name="loginName"]').val('');
        $('input[name="password"]').val('');
    });


    

//新增或修改 账号
    $('#user_addForm').bootstrapValidator({
        container: 'tooltip',
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            userName: {
                validators: {
                    notEmpty: {
                        message: '姓名不能为空！'
                    },
               

                }
            },
            password: {
                validators: {
                    notEmpty: {
                        message: '密码不能为空！'
                    },
                

                }
            },
            loginName: {
                validators: {
                    notEmpty: {
                        message: '账号不能为空！'
                    },  
                    remote: {//ajax验证。server result:{"valid",true or false} 向服务发送当前input name值，获得一个json数据。例表示正确：{"valid",true}  
                        url: '../UserInfoController/checkLoginNameExist',//验证地址      
                        dataType: "json",
                        data:{loginName:function(){return $("input[name='loginName']").val()},
                    		userInfoId:function(){return $("#userId").val()}
                        },
                        message: '账号已存在！',//提示消息
                        delay :  2000,//每输入一个字符，就发ajax请求，服务器压力还是太大，设置2秒发送一次ajax（默认输入一个字符，提交一次，服务器压力太大）
                        type: 'POST'//请求方式
                    }
                 

                }
            },
            roleLevel: {
                validators: {
                    callback: {
                        message: '职能不能为空！',
                        callback:function(value,validator){
                            if(value==""||$(this).length==0){
                                return false;
                            }
                            return true;
                        }
                    }
                }
            },


        }
    }).on('success.form.bv', function(e) {
        // Prevent form submission
        e.preventDefault();
        var $form = $(e.target);
        //添加或修改账号
        $.post("../UserInfoController/saveUserInfo",$form.serialize(),function(result){
    		if(result.staus == "200"){
    			$('#user_add').modal('hide')//隐藏增加模态框
    			$('#user_addForm').bootstrapValidator('resetForm', true); //清空验证痕迹和内容
    			loadUserInfo();//重新加载账号列表
    			publicTipMessage("ok","操作成功！");
    		}else{
    			publicTipMessage("error","操作失败！");
    		}
    	})
        
    });
    
  //多选删除模态框
    $('.manyDelete').click(function() {
        $("#user_Alldel").modal({
            backdrop : false,
        });
    });
    
});

//加载账号信息
function loadUserInfo(){
	$.post("../UserInfoController/userManager",{
		userName:$("input[name='userName']").val(),
		nowPage:1,
		pageSize:9999
		},function(result){
		$("#userInfoUlApp").empty();
		var list = result.list;
		if(list!=undefined){
			for(var i = 0;i < list.length;i++){
				$("#userInfoUlApp").append('<li class="out_table_content">'+
				        '<div> <input type="checkbox" value="'+list[i].user_id+'" class="out_input_checkbox mt8 ml40"/></div>'+
				        '<div>'+(list[i].user_name==undefined?"":list[i].user_name)+'</div>'+
				        '<div>'+(list[i].login_name==undefined?"":list[i].login_name)+'</div>'+
				       // '<div>'+(list[i].password==undefined?"":list[i].password)+'</div>'+
				        '<div>'+(list[i].role_level=="0"?"管理员":"操作员")+'</div>'+
				        '<div><img src="'+(list[i].is_used=="0"?"../image/user/user1_03.png":"../image/user/user1_07.png")+'" alt=""></div>'+
				       //'<div>2018.01.02  20：20：16</div>'+
				    '<div>'+
				        '<span class="putIn_btn2 deleteUser" dataNo="'+list[i].user_id+'">删除</span>'+
				        '<span class="putIn_btn2 editUser" dataNo="'+list[i].user_id+'">编辑</span>'+
				        '<span class="putIn_btn2 switchUser" dataNo="'+list[i].user_id+'">'+(list[i].is_used=="0"?"启用":"禁用")+'</span>'+
				        '</div>'+
				        '</li>');
			}	
		}
		
		// 弹出修改模态框
	    $('.editUser').unbind().click(function() {
	        $("#user_add").modal({
	            backdrop : false,
	        });
	        $("#user_addForm input[name='userId']").val($(this).attr("dataNo"));//隐藏域 账号id
	        //查询编辑信息
	        $.post("../UserInfoController/searchUserInfoById",{id:$(this).attr("dataNo")},function(result){
	        	$("#user_addForm input[name='userName']").val(result.userName);
		        $("#user_addForm select[name='roleLevel']").val(result.roleLevel);
		        $("#user_addForm input[name='loginName']").val(result.loginName);
		        $("#user_addForm input[name='password']").val(result.password);
	        })
	        
	    });
	    
	    // 删除模态框
	    $('.deleteUser').click(function() {
	        $("#user_del").modal({
	            backdrop : false,
	        });
	        delUserInfoId = $(this).attr("dataNo");
	    });
	    
	    //单选删除
	    $('#delUserYes').unbind().click(function(){
	    	$.post("../UserInfoController/deleteUserInfoById",{id:delUserInfoId},function(result){
	    		if(result.staus == "200"){
	    			loadUserInfo();//重新加载账号列表
	    			publicTipMessage("ok","删除成功！");
	    			$('#user_del').modal('hide')//隐藏增加模态框
	    		}else{
	    			publicTipMessage("error","删除失败！");
	    		}
	    	})
	    })
	    
	    //启用或禁用
	   $('.switchUser').unbind().click(function(){
			var isUsed = "1";//默认启用
			if($(this).text() == "禁用"){
				isUsed = "0";//禁用
			}
			$.post("../UserInfoController/updateUserInfoIsUsedById", {
				id : $(this).attr("dataNo"),
				isUsed : isUsed
			}, function(result) {
				if (result.staus == "200") {
					loadUserInfo();// 重新加载账号列表
					publicTipMessage("ok","操作成功！");

				} else {
					publicTipMessage("error","操作失败！");

				}
			}) 
	   }) 
	  
	})
}


//多选删除
function deleteManyUserInfoById(){
	$("#AlldelUserYes").click(function(){
		var userIdArray = new Array();
		var userIdList = $("input[type='checkbox']:checked");
		if(userIdList.length == 0){
			 publicTipMessage("error","请选择删除的账号！");
			return;
		}
		for (var i = 0; i < userIdList.length; i++) {
			userIdArray[i] = userIdList[i].value;
		}
		$.ajax({
			url:"../UserInfoController/deleteManyUserInfoById",
			data:{ids:userIdArray},
			type:"POST",
			traditional:true,
			success:function(result){
				if(result.staus == "200"){
	    			loadUserInfo();//重新加载账号列表
	    			publicTipMessage("ok","删除成功！");
	    			$('#user_Alldel').modal('hide')//隐藏增加模态框
	    		}else{
	    			 publicTipMessage("error","删除失败！");
	    		}
			}
		})
	})
	
}




