$(function() {
	//查询所有区位信息
//    $("#areaNameId").change(function(){
//    	getCreateStoreByAreaName();
//    	
//    });

//	编辑仓库弹框
	$(".success_no_button").click(function(){
		var doc = document.documentElement;  
		var relHeight = (doc.clientHeight > doc.scrollHeight) ? doc.clientHeight : doc.scrollHeight;//获取屏幕高度和当前页面高度中的较大值  
		document.getElementById('about_zhezhao').style.height = relHeight+'px';//id为about_zhezhao的div就是我页面中的遮罩层  
		$(".zhezhaos").addClass("block").removeClass("none");
		$('#successAdd_hourseId').bootstrapValidator('resetForm', false);
		
		$.post("../createStoreHouse/selectCreateStoreHouseById",{createStoreId:$(".warehouse_list_name.active_f").attr("dataNo")},function(result){
			$("#storeNameEditId").val(result.storeName);
			$("#storeTypeEditId").val(result.storeType);
			$("#storeMasterEditId").val(result.storeMaster);
			$("#masterTelEditId").val(result.masterTel);
			$("#storeAddressEditId").val(result.storeAddress);
		})
	});


	
//	新增区位弹框
	$(".success_no_addqu").click(function(){
		//遮罩的高度
		var doc = document.documentElement;  
		var relHeight = (doc.clientHeight > doc.scrollHeight) ? doc.clientHeight : doc.scrollHeight;//获取屏幕高度和当前页面高度中的较大值    
		document.getElementById('about_zhezhao1').style.height = relHeight+'px';//id为about_zhezhao的div就是我页面中的遮罩层  
//		$(".clearArea").val("");
		$(".edit_qu").text("新增区位");
		$(".success_button_delect").addClass("none").removeClass("block");
		$(".zhezhao_qu").addClass("block").removeClass("none");
		$('#successAdd_areaId').bootstrapValidator('resetForm', true);
		$("#createstoreareaId").val("")//清空隐藏域区位id
		$("#insertOrEdit").val(0)//设置0为新增
	});

//	点击新增区位1
	$(".success_cang_bit").click(function(){
		$(this).addClass("none").removeClass("block");
		$(".success_cang_induce").addClass("block").removeClass("none");
		$(".success_content_bitbox").addClass("block").removeClass("none");
	});


	$(".success_cang_bit").click(function(){
		clearAreaContentSuccess();
	});
	
	//搜索按钮
	$("#successAddSearch").click(function(){
		if($("#storage_monitor").hasClass("none")){
			getStoreHouseData(true);//坐标显示
		}else{
			getStoreHouseData();//列表显示
		}
	});


//	取消新增
	$(".success_button_no").click(function(){
		$('#successAdd_hourseId').bootstrapValidator('resetForm', false);
		$('#successAdd_areaId').bootstrapValidator('resetForm', true);

		$(".zhezhaos").addClass("none").removeClass("block");
		$(".zhezhao_qu").addClass("none").removeClass("block");
		
	});
//	关闭新建弹框q
	$(".success_close").click(function(){
		$('#successAdd_hourseId').bootstrapValidator('resetForm', false);
		$('#successAdd_areaId').bootstrapValidator('resetForm', true);
		$(".zhezhaos").addClass("none").removeClass("block");
		$(".zhezhao_qu").addClass("none").removeClass("block");
		
	});

//	关闭修改库存信息弹出框
	$(".success_close1").click(function(){
		debugger
		$('#successAdd_areaId_store').bootstrapValidator('resetForm', false);
		//$('#successAdd_areaId').bootstrapValidator('resetForm', true);
		$(".zhezhao_store").addClass("none").removeClass("block");
		$(".zhezhao_qu").addClass("none").removeClass("block");
		
	});
	

	
	//新增区位验证
	debugger
	$('#successAdd_areaId').bootstrapValidator({
		container: 'tooltip',
		//trigger: 'blur',
		feedbackIcons: {
			valid: 'glyphicon glyphicon-ok',
			invalid: 'glyphicon glyphicon-remove',
			validating: 'glyphicon glyphicon-refresh'
		},
		fields: {
			//区位名称
			areaName: {
				threshold : 2 ,
				validators : {
					notEmpty : {
						message : '区位名称不能为空！'
					},
					regexp: {
						regexp:  /^[A-Z]/,
						message: '请输入一个大写字母!'
					},
					 remote: {//ajax验证。server result:{"valid",true or false} 向服务发送当前input name值，获得一个json数据。例表示正确：{"valid",true}  
	                        url: '../CreateStoreAreaController/checkAreaNameExist',//验证地址      
	                        dataType: "json",
	                        data:{areaName:function(){return $("#areaNameEditId").val()},
	                        	  createstorehouseId:function(){return $(".warehouse_list_name.active_f").attr("dataNo")}
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
	/*		rowsStart: {
				validators : {
					notEmpty : {
						message : '行起始值不能为空!'
					},

					regexp: {

						regexp: /^([0-9]|[1-4][0-9])$/,
						message: '请输入1到49之间的数字'
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
/*					regexp: {
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
		var $form = $(e.target);
		var insertOrEdit = $("#insertOrEdit").val();
		if(insertOrEdit == 0){
			//新增区位
			saveCreateHourseInfoSuccess();
		}else{
			//编辑区位
			publicTipMessage("busy","请等待！")
			var params={
					"areaName":$("#areaNameEditId").val(),
					"rowsStart":$("#columnsStartEditId0").is(':checked')?0:1,
					"columnsCount":$("#columnsCountEditId").val(),
					"columnsStart":	$("#columnsStartEditId0").is(':checked')?0:1,
					"sequence":$("#sequenceEditId0").is(':checked')?0:1,
					"rowsCount":$("#rowsCountEditId").val(),
					"areaName":$("#areaNameEditId").val(),
					"createstoreareaId":$("#createstoreareaId").val(),
					"robotNo":$("#robotNoEditId").val(),
					"createstorehouseId":$(".warehouse_list_name.active_f").attr("dataNo")
				}
			$.post("../createStoreHouse/editCreateStoreHouse2",params,function(result){
				if(result.stus == 200){
					publicTipMessage("ok","编辑成功！")
					$('#successAdd_hourseId').bootstrapValidator('resetForm', false);
					$('#successAdd_areaId').bootstrapValidator('resetForm', true);

					$(".zhezhaos").addClass("none").removeClass("block");
					$(".zhezhao_qu").addClass("none").removeClass("block");
//					setTimeout(function(){
						searchCreateStoreHouseByStoreName()//重新加载区位
//					}, 2000)
					
				}else{
					publicTipMessage("error","编辑失败，请联系管理员！")
				}
				
			})
		}
		
	});
	
	//修改库存信息的验证
	$('#successAdd_areaId_store').bootstrapValidator({
		container: 'tooltip',
		//trigger: 'blur',
		feedbackIcons: {
			valid: 'glyphicon glyphicon-ok',
			invalid: 'glyphicon glyphicon-remove',
			validating: 'glyphicon glyphicon-refresh'
		},
		fields: {
			//行列数字
			storeStatue : {
				validators : {
					notEmpty : {
						message : '状态不能为空！'
					}
				}
			},
			//货物码
			goodNo: {
				validators : {
					
					notEmpty : {
						message : '货物码不能为空！'
					},
					////if($("input[name='order']").val().length < 13 || $("input[name='order']").val().length > 13){
        			//publicTipMessage("error","条形码为13位！");
    				//return;
					callback : {
						message : '货物码必须为13位数字',
						callback:function(value,validator){
							debugger
							var reg = /^\d{13}$/;
							console.info(value.length)
							if(reg.test(value)){
								return true;
							}
							return false;
                    }
					
				}
			}
			},
			//订单号
			orderId: {
				validators : {
					notEmpty : {
						message : '订单号不能为空!'
					},
					callback : {
						message : '订单号必须为13位数字',
						callback:function(value,validator){
							debugger
							var rEg = /^\d{13}$/;
							if(rEg.test(value)){
								return true;
							}
							return false;
                    }
					
				}
			}
			},
			//数量
			
			count: {
				validators : {
					notEmpty : {
						message : '数量不能为空!'
					},

					callback : {
						message : '数量必须为数字！',
						callback:function(value,validator){
							debugger
							var rEgs = /^([1-9]|[1-9][0-9]|[0-1][0-9][0-9])$/;
							if(rEgs.test(value)){
								return true;
							}
							return false;
                    }
					
				}
			}
			},
			
			//备注
//			remark : {
//				validators : {
//					stringLength: {
//						min: 0,
//						max: 50,
//						message: '备注不能为空！'
//					}
//				}
//			}
		}
	}).on('success.form.bv', function(e) {
		e.preventDefault();
		var $form = $(e.target);
		$.post("../storeHouse/updateStoreHouse", $form.serialize(), function(result) {
			if(result.stus == 200){
				publicTipMessage("ok","修改成功！");
				$(".zhezhao_store").addClass("none").removeClass("block");
				getStoreHouseData();//刷新列表
			}else{
				publicTipMessage("error","操作失败！");
			}
		});	
	});
	
	//清空修改库存中的数据
	$(".success_button_no_stroe").unbind().click(function(){
		$("#successAdd_areaId_store input[name='storeStatue']").prop("checked",false);//状态
		$("#successAdd_areaId_store input[name='goodNo']").val("");//货物码
		$("#successAdd_areaId_store input[name='orderId']").val("");//订单号
		$("#successAdd_areaId_store input[name='count']").val("");//数量
		$("#successAdd_areaId_store textarea[name='remark']").val("");//备注
	})
});





//获取建库信息请求
function getCreateStoreData(){
	var params={};
	$.post("../createStoreHouse/searchCreateStoreHouse", params, function(resultJson) {
		appendCreateStoreHtml(resultJson.cs);
		if($("#storage_monitor").hasClass("none")){
			
			//zuoBiaoShow();
		}else{
			getStoreHouseData();
		}		
	}, "json");	
}

//拼接建库信息html
function appendCreateStoreHtml(cs){
	$("#storeNameId").html(cs.storeName);
	$("#storeTypeId").html(cs.storeType);
	$("#storeAddressId").html(cs.storeAddress);
	$("#storeMasterId").html(cs.storeMaster);
	$("#masterTelId").html(cs.masterTel);
	$("#areaNameId").val(cs.areaName);
	$("#rowsCountId").html(cs.rowsCount);
	$("#rowsStartId").html(cs.rowsStart);
	$("#columnsCountId").html(cs.columnsCount);
	$("#columnsStartId").html(cs.columnsStart);
	$("#sequenceId").html(cs.sequence==0?"行/层/列":"行/列/层");
	$("#creatorId").html(cs.creator);
	$("#createTimeId").html(cs.createTime?format(cs.createTime):"");
	$("#storeNameId").attr("dataId",cs.createstorehouseId);	
   //	初始编辑弹出框内的内容 
	$("#storeNameEditId").val(cs.storeName);
	$("#storeAddressEditId").val(cs.storeAddress);
	$("#masterTelEditId").val(cs.masterTel);
	$("#storeTypeEditId").val(cs.storeType);
	$("#storeMasterEditId").val(cs.storeMaster);
}

//获取仓位信息
function getStoreHouseData(zuoBiaoFlag,rowsCount,columnsCount,rowsStart){
	debugger
	var createstorehouseId=$("#storeNameId").attr("dataId");
	var storeName=$("#storeNameId").html();
	var search=$(".success_seach").val();
	var params={
			"createstorehouseId":createstorehouseId,
			"search":search,
			"storeName":storeName
	};
	$.post("../storeHouse/searchStoreHouse", params, function(resultJson) {
		if(zuoBiaoFlag){
			$(".inter_tu").removeClass("inter_tu");
			debugger
			//appendZuoBiaoHtml(list,rowsCount,columnsCount,rowsStart);
			if(search!=""){			
				$(".inter_ul_qu").find(".clearfix_top").each(function(){
					debugger
					var parentUl=$(this);
					parentUl.find(".inter_mointor_ceng li div").each(function(){
						debugger
						var sondiv=$(this);
					 var storeid=sondiv.attr("storeid");
						if(isList(resultJson.list,storeid)){
							sondiv.find("span").addClass("inter_tu");
						}
					});
				  });
			}

			
		}else{
			$("#storeHourse_ul_id").empty();
			appendStoreHouseHtml(resultJson.list);
		}
		
	}, "json");	
}

//搜索当是坐标展示时   list是否包含该仓位
function isList(list,storeid){
	for(var i in list){
		if(list[i].store_id==storeid){
			return true;
		}
		
	}
	return false;
}

//拼接仓位html
function appendStoreHouseHtml(list){
	for(var i in list){
		$("#storeHourse_ul_id").append('<li class="success_message_table_content">'
				+'<div><span class="ml30">'+list[i].store_no+'</span></div>'
				+'<div>'+(list[i].good_no?list[i].good_no:"")+'</div>'
				+'<div class="fc_f3"><span class="success_message_table_zhan '+(list[i].store_statue==0?"":(list[i].store_statue==1?" current_zhan2":" current_zhan"))+'"></span></div>'
				+'<div>'+(list[i].count?list[i].count:"")+'</div>'
				+'<div>'+(list[i].input_time?format(list[i].input_time):"")+'</div>'
				+'<div style="color: #078fff;" class="cursor">'
				+'<a href="historyRecord.html?storeId='+list[i].store_id+'&storeNo='+list[i].store_no+'" style="color: #078fff;">历史记录</a>'
				+'<span class="success_edit_message ml30" style="color: #078fff;" dataNo="'+list[i].store_id+'">修改库存</span>'
				+'</div>'
				+'</li>');
	}
	
	//修改库存信息弹出框
	$(".success_edit_message").click(function(){
		//遮罩的高度
		var doc = document.documentElement;  
		var relHeight = (doc.clientHeight > doc.scrollHeight) ? doc.clientHeight : doc.scrollHeight;//获取屏幕高度和当前页面高度中的较大值    
		document.getElementById('zhezhao_store').style.height = relHeight+'px';//id为about_zhezhao的div就是我页面中的遮罩层  
		$(".zhezhao_store").addClass("block").removeClass("none");
		 $('html,body').animate({scrollTop:0},500);//回到顶端
		//$(".success_content_bitbox").addClass("block").removeClass("none");
		//查询仓位信息
		$.post("../storeHouse/searchStoreHouseByIdOnStorageMessage",{storeId:$(this).attr("dataNo")},function(result){
			console.info(result)
			$("#successAdd_areaId_store div[name='storeNo']").text(result.store_no);//仓位号
			$("#successAdd_areaId_store span[name='createhourseName']").text(result.store_name);//仓库名
			$("#successAdd_areaId_store span[name='areaName']").text(result.area_name);//区位名
			
			$("#successAdd_areaId_store input[name='storeStatue'][value='"+result.store_statue+"']").prop("checked",true);//状态
			$("#successAdd_areaId_store input[name='goodNo']").val(result.good_no==undefined?"":result.good_no);//货物码
			$("#successAdd_areaId_store input[name='orderId']").val(result.order_id==undefined?"":result.order_id);//订单号
			$("#successAdd_areaId_store input[name='count']").val(result.count==undefined?"":result.count);//数量
			$("#successAdd_areaId_store textarea[name='remark']").val(result.remark==undefined?"":result.remark);//备注
			
			$("#successAdd_areaId_store input[name='storeId']").val(result.store_id);//仓位id隐藏域
			
		})
	});
}		


//清空区位设置处理
function clearAreaContentSuccess(){
	$(".clearArea").val("");
	$(".success_cang_save").addClass("block").removeClass("none");
	$(".success_cang_bits").addClass("none").removeClass("block");
}


//新增区位保存
function saveCreateHourseInfoSuccess(){
	publicTipMessage("busy","请等待！")
	var params={
		"areaName":$("#areaNameEditId").val(),
		"rowsStart":$("#columnsStartEditId0").is(':checked')?0:1,
		"columnsCount":$("#columnsCountEditId").val(),
		"columnsStart":	$("#columnsStartEditId0").is(':checked')?0:1,
		"sequence":$("#sequenceEditId0").is(':checked')?0:1,
		"rowsCount":$("#rowsCountEditId").val(),
		"robotNo":$("#robotNoEditId").val(),
		"createstorehouseId":$(".warehouse_list_name.active_f").attr("dataNo")
	}
	$.post("../CreateStoreAreaController/addCreateStoreArea", params, function(resultJson) {
		if (resultJson.code==500) {
			publicTipMessage("error","操作失败！");
			return;
		}
		if(resultJson.code==200){
			$(".zhezhao_qu").addClass("none").removeClass("block");
			publicTipMessage("ok","操作成功！");
			searchCreateStoreHouseByStoreName()//重新加载区位
//			getStoreNameData();
//			$(".success_cang_save").addClass("none").removeClass("block");
//			$(".success_cang_bits").addClass("block").removeClass("none");
			return;
		}
	}, "json");	
}

//获取所有区位option信息
function getStoreNameData(){
	
	var params={
	};
	$.post("../createStoreHouse/searchAreaNames", params, function(resultJson) {
		$("#areaNameId").empty();
		appendAreaNameOptionHtml(resultJson.list);
		//获取最新的区位信息
		getCreateStoreData();
	}, "json");	
}

//拼接区位信息option
function appendAreaNameOptionHtml(list){
	for(var i in list){
		$("#areaNameId").append('<option value="'+list[i].area_name+'">'+list[i].area_name+'</option>');
	}
}	

//根据区位查找建库信息
function getCreateStoreByAreaName(){
	var params={
			"areaName":$("#areaNameId").val()
	};
	$.post("../createStoreHouse/searchCreateStoreHouseByAreaName", params, function(resultJson) {
		appendCreateStoreHtml(resultJson.cs);
		if($("#storage_monitor").hasClass("none")){
			zuoBiaoShow();
		}else{
			getStoreHouseData();
		}
	}, "json");	
}

