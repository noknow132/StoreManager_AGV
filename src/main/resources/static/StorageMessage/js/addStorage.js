$(function() {
	//alert("addSorage");
//	新增仓库弹框
	$(".add_no_button").click(function(){
		//if(isCreateStore()){
			$(".zhezhao").addClass("block").removeClass("none");
			$('#create_store_form').bootstrapValidator('resetForm', true);
		//};

	});
	$(".add_img_button").click(function(){
		//if(isCreateStore()){
			$(".zhezhao").addClass("block").removeClass("none");
			$('#create_store_form').bootstrapValidator('resetForm', true);	
		//};
	});
//仓库列表新增仓位弹框
$(".warehouse_add").click(function(){
	$.get("../createStoreHouse/searchCreateStoreHouseAllCount",function(count){
		if(count >= 5){
			publicTipMessage("error","仓库最多只能存在5个，不能在新建更多仓库！");
			return;
		}
		var doc = document.documentElement;  
		var relHeight = (doc.clientHeight > doc.scrollHeight) ? doc.clientHeight : doc.scrollHeight;//获取屏幕高度和当前页面高度中的较大值  
		document.getElementById('about_zhezhao2').style.height = relHeight+'px';//id为about_zhezhao的div就是我页面中的遮罩层  
		$(".zhezhao").addClass("block").removeClass("none");
		//$('#create_store_form').bootstrapValidator('resetForm', true);
	})
			
	
});
//仓库列表的选中切换样式
$(".warehouse_list li").click(function(){
	$(this).addClass("active").siblings().removeClass("active");
	$(this).find("div").addClass("active_f").parent().siblings().find("div").addClass("fc_8").removeClass("active_f");
	//$('#create_store_form').bootstrapValidator('resetForm', true);	
	searchCreateStoreHouseByStoreName();//根据建库名称查找建库(区位)
	storeBaseInfo();//仓库基本信息
	getStoreHouseData(true);//坐标显示
});
	/**
	 * 验证添加区位的信息
	 */
	$('#create_store_form').bootstrapValidator({
		container: 'tooltip',
		feedbackIcons: {
			valid: 'glyphicon glyphicon-ok',
			invalid: 'glyphicon glyphicon-remove',
			validating: 'glyphicon glyphicon-refresh'
		},
		fields : {
			//仓库名称
			storeName : {
				validators : {
					notEmpty : {
						message : '仓库名称不能为空!'
					},

					stringLength : {
						min : 1,
						max : 30,
						message : '仓库名称长度不能超过30！'
					}
				}
			},
			//仓库地址
			storeAddress : {
				validators : {
					notEmpty : {
						message : '仓库地址不能为空!'
					},

					stringLength : {
						min : 1,
						max : 200,
						message : '仓库地址长度不能超过200！'
					}
				}
			},
			//仓库类型
			storeType : {
				validators : {
					notEmpty : {
						message : '仓库类型不能为空!'
					},

					stringLength : {
						min : 1,
						max : 20,
						message : '仓库类型长度不能超过20！'
					}
				}
			},
			//负责人
			storeMaster : {
				validators : {
					notEmpty : {
						message : '负责人不能为空!'
					},

					stringLength : {
						min : 1,
						max : 10,
						message : '负责人长度不能超过10！'
					}
				}
			},
			//联系电话
			masterTel : {
				validators : {
					notEmpty : {
						message : '联系电话不能为空!'
					},
					regexp : {
						regexp : /^(13[0-9]|15[012356789]|17[678]|18[0-9])[0-9]{8}$/,
						message : '请输入正确格式的联系电话！'
					},
				}
			},

			//区位名称
			areaName: {
				threshold : 2 ,
				validators : {
					notEmpty : {
						message : '区位名称不能为空！'
					},
					regexp: {
						regexp: /^[a-zA-Z]$/,
						message: '请输入一个字母！'
					},
					remote: {//ajax验证。server result:{"valid",true or false} 向服务发送当前input name值，获得一个json数据。例表示正确：{"valid",true}  
						url: '../createStoreHouse/checkAreaNameExist',//验证地址      
						dataType: "json",
						data:{areaName:function(){return $("#areaNameEditId").val()}
						},
						message: '区位已存在！',//提示消息
						delay :  2000,//每输入一个字符，就发ajax请求，服务器压力还是太大，设置2秒发送一次ajax（默认输入一个字符，提交一次，服务器压力太大）
						type: 'POST'//请求方式
					}
				}
			},
			//行数
			rowsCount: {
				validators : {
					notEmpty : {
						message : '行数不能为空!'
					},

					regexp: {
						regexp: /^([1-9]|[1-4][0-9])$/,
						message: '请输入1到49之间的数字！'
					}
				}
			},
			//行起始值
		/*	rowsStart: {
				validators : {
					notEmpty : {
						message : '行起始值不能为空!'
					},

					regexp: {

						regexp: /^([0-9]|[1-9][0-9])$/,
						message: '请输入1到99之间的数字'
					}
				}
			},*/
			//列数
			columnsCount: {
				validators : {
					notEmpty : {
						message : '列数不能为空!'
					},

					regexp: {

						regexp: /^([1-9]|[1-4][0-9])$/,
						message: '请输入1到49之间的数字！'
					}
				}
			},
			//列起始值
			columnsStart : {
				validators : {
					notEmpty : {
						message : '列起始值不能为空!'
					}
				/*	regexp: {

						regexp: /^([0-9]|[1-4][0-9])$/,
						message: '请输入1到49之间的数字'
					}*/
				}
			},
			//行列数字
			sequence : {
				validators : {
					notEmpty : {
						message : '请选择行列顺序!'
					}
				}
			},

			//机器码
			robotNo : {
				validators : {
					stringLength: {
						min: 0,
						max: 50,
						message: '机器码长度不能超过20！'
					}
				}
			}	


		}
	}).on('success.form.bv', function(e) {
		e.preventDefault();
		var params= $('#create_store_form').serialize()
		$.post("../createStoreHouse/insertCreateStoreHouse",params,function(result){
			if(result.stus == 200){
				publicTipMessage("ok","新增成功！");
				$(".zhezhao").addClass("none").removeClass("block");
				window.location.reload();//刷新当前页
			}else{
				publicTipMessage("error","新增失败，请联系管理员！");
			}
		})
	});




//	点击新增区位1
//	$(".add_cang_bit").click(function(){
//	$(this).text("增加区位");
//	$(this).css({
//	border:"none",
//	background:"#078FFF",
//	color:"#fff"
//	});
//	$(".add_content_bitbox").addClass("block").removeClass("none");
//	});
//	确认新增
	$(".add_button_ok").unbind().click(function(){
//		if($(".add_cang_bits").hasClass("none")){ 
//			flag=false
//			if(isCreateStore()){
//				var bootstrapv=  $('#create_store_form').data('bootstrapValidator');
//				var vflag=  $('#create_store_form').data('bootstrapValidator').isValid();
//				bootstrapv.validate();
//				if(vflag){
//					saveCreateHourseInfo(false);
//					
//					//bindClick("successAdd.html");
//				}	
//				}			
//		}else{
//			$(".zhezhao").addClass("none").removeClass("block");//已经保存过了
//			getCreateStore();		
//
//		}
		var bootstrapv=  $('#create_store_form').data('bootstrapValidator');
		bootstrapv.validate();

	});
//	取消新增
	$(".add_button_no").click(function(){
		$(".zhezhao").addClass("none").removeClass("block");
	});
//	关闭新建弹框q
	$(".add_close").click(function(){
		$(".zhezhao").addClass("none").removeClass("block");
	});

	$(".add_cang_bits").click(function(){
		clearAreaContent();
	});
});
var flag=true;
//清空区位设置处理
function clearAreaContent(){
	$(".clearArea").val("");//清空input红class为 .clearArea的内容
	$(".input_disabled").attr("disabled",true);
	$(".add_cang_save").addClass("block").removeClass("none");
	$(".add_cang_bits").addClass("none").removeClass("block");
	
	$('#create_store_form').bootstrapValidator('resetForm', false); //清空验证痕迹
}

/**
 * 建库保存
 * isSumbit 是否是点击的保存按钮
 */
function saveCreateHourseInfo(isSumbit){
	var params= $('#create_store_form').serialize()
	console.info(params)
	$.post("../createStoreHouse/addCreateStoreHouse", params, function(resultJson) {
		if (resultJson.code==500) {
			publicTipMessage("error","操作失败！");
			return;
		}
		if(resultJson.code==200){
			publicTipMessage("ok","操作成功！");
			flag=true;
			if(isSumbit){
				$(".add_cang_save").addClass("none").removeClass("block");
				$(".add_cang_bits").addClass("block").removeClass("none");
			}else{
				$(".zhezhao").addClass("none").removeClass("block");//没有保存过的
				getCreateStore();		
			}
			return;
		}
	}, "json");	
}


/**
 * 获取建库信息
 */
function isCreateStore(){
	var params={};
	var flag=false
	$.ajax({
		async:false,
		type:'POST',
		url:"../createStoreHouse/searchCreateStoreHouse",
		data:params,
		dataType:"json",
		contentType:"application/json",
		success:function(resultJson){
			if (resultJson.code==500) {
				publicTipMessage("error","出错了！");
				 flag=false;
			}
			if(resultJson.total==0){
				flag=true;
			}else{
				publicTipMessage("error","仓库已经存在，不能再新增了！");
				flag=false;
			}
		}
	})
	return flag;
}