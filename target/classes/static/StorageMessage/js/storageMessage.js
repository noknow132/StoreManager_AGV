
//  初始化开始

$(function() {
	getCreateStore();// 获取建库信息
	searchCreateStoreHouseGroupByStoreName();//查询已建仓库
	searchCreateStoreHouseByStoreName();//根据建库名称查找建库(区位)
	storeBaseInfo()//仓库基本信息
	deleteCreateStoreArea();//删除区位 点击事件
	deleteCreateStoreHouse();//删除仓库事件	
//界面监控按钮
$(".storage_monitor_table").click(function(){
	$(this).addClass("none").removeClass("block");
	$("#storage_list_success").addClass("block").removeClass("none");
	$(".table_box_div").addClass("none").removeClass("block");
	$(".inter_box_father").addClass("block").removeClass("none");
//	zuoBiaoShow();
	getStoreHouseData(true);//坐标显示
});
//返回列表页
$(".storage_monitor_list").click(function(){
	$(this).addClass("none").removeClass("block");
	$("#storage_monitor").addClass("block").removeClass("none");
	$(".table_box_div").addClass("block").removeClass("none");
	$(".inter_box_father").addClass("none").removeClass("block");
	getStoreHouseData();

});
//吸附导航
//var offTop = $('.state_div').offset().top;

//$(window).scroll(function(){
//
//	// 动态获取被浏览器吃掉高度
//	var myTop = $(window).scrollTop();
//	// 当浏览器吃掉的高度大于168的时候，就给nav变成固定定位
//	if(myTop > 480){
//		$('.state_div').addClass('current_state');
//		//为什么添加一个margin值，因为当nav变成固定定位了就脱标不占位置，所以需要margin去挤出nav原来的高度
//		$('.state_min').css('margin-top',87);
//	}else{
//		$('.state_div').removeClass('current_state');
//		$('.state_min').css('margin-top',0);
//	}
//
//})
	
});
//--初始化结束

// 获取建库信息
 
function getCreateStore(){
	var params={};
	$.post("../createStoreHouse/searchCreateStoreHouse2", params, function(resultJson) {
		
		if (resultJson.code==500) {
			$(".stora_no_num").addClass("block").removeClass("none");
			$(".stora_has_num").addClass("none").removeClass("block");
			publicTipMessage("error","操作失败！");
			//alert("error");
			return;
		}
		if(resultJson.total==0){
			$(".stora_no_num").addClass("block").removeClass("none");
			$(".stora_has_num").addClass("none").removeClass("block");
			//return;
		}else{
			$(".stora_no_num").addClass("none").removeClass("block");
			$(".stora_has_num").addClass("block").removeClass("none");
//			getStoreNameData();		
		}
	}, "json");	
}

//查询已建仓库
function searchCreateStoreHouseGroupByStoreName(){
	$.ajax({
		url:"../createStoreHouse/searchCreateStoreHouseGroupByStoreName",
		type:"GET",
		async:false,
		success:function(result){
			if(result != null){
				$("#warehouseUl").empty();
				for (var i = 0; i < result.length; i++) {
					if(i == 0){
						$("#warehouseUl").append('<li class="active">'
								+ '<div class="warehouse_list_name fs14 center active_f" dataNo="'+result[i].createstorehouse_id+'">'+result[i].store_name+'</div>'
								+ '</li>')
					}else{
						$("#warehouseUl").append('<li>'
								+ '<div class="warehouse_list_name fs14 center fc_8" dataNo="'+result[i].createstorehouse_id+'">'+result[i].store_name+'</div>'
								+ '</li>')
					}
					
				}
				}
		}
	})

}
//仓库基本信息
function storeBaseInfo(){
	$.post("../createStoreHouse/searchStoreBaseInfo",{createStoreHouseId:$(".warehouse_list_name.active_f").attr("dataNo")},function(result){
		//仓库基本信息
		$("#storeTypeId").text(result.store_type);
		$("#storeMasterId").text(result.store_master);
		$("#masterTelId").text(result.master_tel);
		$("#storeAddressId").text(result.store_address);
		$("#creatorId").text(result.user_name);
		
		$("#createTimeId").text(format(result.create_time));
	})
}


//根据建库名称查找建库(区位)
function searchCreateStoreHouseByStoreName(){
	$(".inter_ul_qu").empty();
	$.post("../CreateStoreAreaController/searchCreateStoreAreaByCreateStoreHouseId",{CreateStoreHouseId:$(".warehouse_list_name.active_f").attr("dataNo")},function(result){
		console.info(result)
		if(result != null){
		for (var i = 0; i < result.length; i++) {
			var sequence = result[i].sequence; 
			var rowsStart= result[i].rows_start; 
			var rowsCount= result[i].rows_count; 
			var columnsCount= result[i].columns_count
			var areaName = result[i].area_name;
			var lieDiv = "";//行 坐标
			var cengLi = "";//层 坐标
			var cangLi = "";//格子
			//拼接横坐标
			for(var l=0;l<columnsCount;l++){
				var lie= rowsStart+l;
				var lieStr=""
				if(lie<10){
					lieStr="0"+ String(lie);
				}else{
					lieStr= String(lie);
				}
//				$("#lie_li_id").append('<div>'
//						 + '<span  class="fs12 lh32 fc_9">'+lieStr+'</span>'
//							+'</div>');	
				lieDiv += '<div><span  class="fs12 lh32 fc_9">'+lieStr+'</span></div>';
			}
			//纵坐标
			for(var c=rowsCount;c>0;c--){
				var ceng= c-(1-rowsStart);
				var cengStr=""
				if(ceng<10){
					cengStr="0"+ String(ceng);
				}else{
					cengStr= String(ceng);
				}
//				$("#ceng_ul_id").append('<li class="fs12 lh12 fc_9">'+cengStr+'</li>');
				cengLi +='<li class="fs12 lh12 fc_9">'+cengStr+'</li>';
			}
			
			//获取仓位信息	
//			var createstorehouseId = result[i].createstorehouse_id;
			var createstoreareaId = result[i].createstorearea_id;
			var storeName = result[i].store_name;
			//var search = $(".success_seach").val();
			var search="";
			var params={
					"createstoreareaId":createstoreareaId,
//					"search":search,
					"storeName":storeName
			};
			
			$.ajax({
				url:"../storeHouse/searchStoreHouse",
				type:"POST",
				data:params,
				async:false,
				success:function(resultJson){
					var list = resultJson.list;
					console.info(list)
					//拼接坐标展示的仓位情况
					if(sequence==0){//层列
						for(var m=0;m<rowsCount;m++){
							cangLi += '<li>';
							for(var j=0;j<columnsCount;j++){
								var k =  (columnsCount * (rowsCount-m) ) - columnsCount + j;
								cangLi += '<div storeId="'+list[k].store_id+'" storeNoData="'+list[k].store_no+'" stateNoData="'+list[k].store_no+'" goodNoData="'+(list[k].good_no?list[k].good_no:"")+'"'
								+' orderIdData="'+(list[k].order_id?list[k].order_id:"")+'" countData="'+(list[k].count?list[k].count:"")+'" >'
					        	+'<span class="'+(list[k].store_statue==0?"":(list[k].store_statue==1?" inter_yu_zhan":" inter_has_zhan"))+'" ></span>'
					        	+'</div>';
							}
							cangLi += '</li>';
						}
					}else{//列层
						for(var m=0;m<rowsCount;m++){
							cangLi += '<li>';
							for(var j=0;j<columnsCount;j++){							
//							var k =(columnsCount - 1) * j + (rowsCount -1) -m;
//							var k =m+(j*rowsCount);
							var k =(rowsCount-m-1)+(j*rowsCount);
							cangLi += '<div storeId="'+list[k].store_id+'" storeNoData="'+list[k].store_no+'" stateNoData="'+list[k].store_no+'" goodNoData="'+(list[k].good_no?list[k].good_no:"")+'"'
								+' orderIdData="'+(list[k].order_id?list[k].order_id:"")+'" countData="'+(list[k].count?list[k].count:"")+'" >'
					        	+'<span class="'+(list[k].store_statue==0?"":(list[k].store_statue==1?" inter_yu_zhan":" inter_has_zhan"))+'" ></span>'
					        	+'</div>';
							}
							cangLi += '</li>';
						}
						
					}
				}
			})

			//小格子
				$(".inter_ul_qu").append('<div class="clearfix_top"><li class="clearfix ">'
			+'<div class="clearfix state_div" style="padding-top:40px;background:#fff;padding-right:57px;">'
			+'<div class="inter_qu_name fl fc_6 fs30" id="areaNameSYsId">'+(result[i].area_name?result[i].area_name:"")+'</div>'
			+'<div style="height:28px;width:1px;background:#dddddd;" class="fl ml15"></div>'
			+'<div class="fl fs24 lh30 ml15 stateNoSys" id="stateNoSys" style="width:80px;height:38px;color:#3379d1;"></div>'
			+'<div class="fl ml20">'
				+'<span class="fl fs12 fc_9 lh30">订单号</span>'
				+'<input type="text" class="inter_input ml5 orderIdSys" id="orderIdSys"/>'
			+'</div>'
			+'<div class="fl ml20">'
				+'<span class="fl fs12 fc_9 lh30">货物条码</span>'
				+'<input type="text" class="inter_input ml5 goodNoSys" id="goodNoSys"/>'
			+'</div>'
			+'<div class="fl ml20">'
				+'<span class="fl fs12 fc_9 lh30">数量</span>'
				+'<input type="text" class="inter_input ml5 countSys" id="countSys" style="width:40px;"/>'
			+'</div>'
			+'<div class="inter_no_button fs15 fc_f fr cursor ml15" dataNo="'+result[i].createstorearea_id+'" style="height:27px;margin-top:14px;">'
				+'<img src="../image/bianji4.png" class="fl" style="margin-top: 0px;width:14px;height:15px;" />'
				+'<span class="fl fs14 fc_4d" style="height:27px;">编辑区位</span>'
			+'</div>'
			+'<ul class="clearfix fr">'
				+'<li class="fl clearfix">'
					+'<div class="clearfix">'
						+'<span class="fl fs10 fc_9e font_r" style="font-size:10px;">层&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;数：</span>' 
						+'<span class="fl fs10 fc_9e" id="rowsCountId" style="font-size:10px;">'+(result[i].rows_count?result[i].rows_count:"")+'</span>'
					+'</div>'
					+'<div class="clearfix mt5" style="margin-top:5px;">'
						+'<span class="fl fs10 fc_9e font_r" style="font-size:10px;">层列起始：</span>'
						+'<span class="fl fs10 fc_9e" id="rowsStartId" style="font-size:10px;">'+(result[i].rows_start !=undefined?result[i].rows_start:"")+'</span>'
					+'</div>'
				+'</li>'
				+'<li class="fl clearfix ml15">'
					+'<div class="clearfix">'
						+'<span class="fl fs10 fc_9e font_r" style="font-size:10px;">列&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;数：</span>' 
						+'<span class="fl fs10 fc_9e" id="columnsCountId" style="font-size:10px;">'+(result[i].columns_count?result[i].columns_count:"")+'</span>'
					+'</div>'
					+'<div class="clearfix" style="margin-top:5px;">'
					+'<span class="fl fs10 fc_9e" style="font-size:10px;">层列顺序：</span>' 
					+'<span class="fl fs10 fc_9e" id="sequenceId" style="font-size:10px;">'+(result[i].sequence == 0?"行/层/列":"行/列/层")+'</span>'
				+'</div>'
//					+'<div class="clearfix mt10">'
//						+'<span class="fl fs10 fc_9e font_r" style="font-size:10px;">列起始值：</span>' 
//						+'<span class="fl fs10 fc_9e" id="columnsStartId" style="font-size:10px;">'+result[i].columnsStart+'</span>'
//					+'</div>'
				+'</li>'
				+'<li class="fl clearfix ml20">'
					//+'<div class="clearfix">'
						//+'<span class="fl fs10 fc_9e font_r" style="font-size:10px;">区位名称：</span>' 
						//+'<select class="fl fs10 fc_9e" id="areaNameId" style="width: 55px;border: none;border-bottom: 1px solid #9e9e9e;font-size:10px;">'
							//+'<option value="A1">'+result[i].areaName+'</option>'
						//+'</select>'
					//+'</div>'
				
				+'</li>'
			+'</ul>'
		
		+'</div>'
	  +'</li>'+
	  
	  '<div class="mt10 clearfix inter_min_width state_min">'+
			'<div class="inter_place_top_wai fl pos_r">'+
				'<span class="pos_a inter_ceng fs12 fc_9">列</span>'+
				'<div class="inter_xuanzhuan"></div>'+
				'<span class="pos_a inter_lie fs12 fc_9">层</span>'+
			'</div>'+
			'<ul class="fl inter_lie_num" id="lie_ul_id">'+
				'<li id="lie_li_id">'+
				lieDiv+
				'</li>'+
			'</ul>	'+
		'</div>'+
		'<div class="inter_ceng_box clearfix inter_min_width" >'+
				'<ul class="inter_ceng_ul fl" style="padding-top:10px;" id="ceng_ul_id">'+
				cengLi+
				'</ul>'+
				'<ul class="inter_mointor_ceng fl pos_r" id="cang_ul_id">'+
				cangLi+
				'</ul>'+
		'</div>'+
		'</div>');
					
		}
		storehoseHover();//添加浮动事件
		 addClickFun();//添加点击事件
		
		//编辑区位弹框
		$(".inter_no_button").click(function(){
			var doc = document.documentElement;  
			var relHeight = (doc.clientHeight > doc.scrollHeight) ? doc.clientHeight : doc.scrollHeight;//获取屏幕高度和当前页面高度中的较大值  
			document.getElementById('about_zhezhao1').style.height = relHeight+'px';//id为about_zhezhao的div就是我页面中的遮罩层 
			$(".zhezhao_qu").addClass("block").removeClass("none");
			$(".edit_qu").text("编辑区位");
			$(".success_button_delect").addClass("block").removeClass("none");
			 $('html,body').animate({scrollTop:0},1000);//回到顶端
			$('#successAdd_areaId').bootstrapValidator('resetForm', true);
			var createstoreareaId = $(this).attr("datano");
			
			$("#createstoreareaId").val(createstoreareaId)//隐藏域区位id
			$("#insertOrEdit").val(1)//设置为1是修改
			//查询编辑仓库的信息
			$.post("../CreateStoreAreaController/selectCreateStoreAreaById",{createStoreAreaId:createstoreareaId},function(result){
				console.info(result)
				$("#areaNameEditId").val((result.areaName).charAt(0));//区位名称
				$("#rowsCountEditId").val(result.rowsCount);//行
				$("#columnsCountEditId").val(result.columnsCount);//列
				$("#robotNoEditId").val(result.robotNo);//机器码
				$("input[name='columnsStart'][value='"+result.rowsStart+"']").prop("checked",true);//行列起始值
				$("input[name='sequence'][value='"+result.sequence+"']").prop("checked",true);//行列顺序
				$("#successAdd_areaId .success_button_delect").attr("createStoreId",createStoreId);//区位删除 按钮添加 建库（区位）id属性
				
			})
		});
		}
		
		
	})
}
//删除区位 点击事件
function deleteCreateStoreArea(){
	$(".success_button_delect").unbind().click(function(){
		publicTipMessage("busy","请等待！")
		$.post("../createStoreHouse/deleteCreateStoreAreaById",{createstoreareaId:$("#createstoreareaId").val()},function(result){
			if(result.stus == 200){
				searchCreateStoreHouseByStoreName();//根据建库名称查找建库(区位)
				storeBaseInfo();//仓库基本信息
				/**隐藏编辑区位模态框 start**/
				$('#successAdd_hourseId').bootstrapValidator('resetForm', false);
				$('#successAdd_areaId').bootstrapValidator('resetForm', true);
				$(".zhezhaos").addClass("none").removeClass("block");
				$(".zhezhao_qu").addClass("none").removeClass("block");
				/**隐藏编辑区位模态框 end**/
				publicTipMessage("ok","删除成功！")
			}else if(result.stus == 300){
				publicTipMessage("error","当前区位中存在有货仓位，不可删除！")
			}else {
				publicTipMessage("error","删除失败，请联系管理员！")
			}
		})
	})
}

//删除仓库
function deleteCreateStoreHouse(){
	$(".add_button_delect").unbind().click(function(){
		publicTipMessage("busy","请等待！")
		$.post("../createStoreHouse/deleteCreateStoreHouseById",{createstorehouseId:$(".warehouse_list_name.active_f").attr("dataNo")},function(result){
			if(result.stus == 200){
				publicTipMessage("ok","删除成功！")
				location.reload();
			}else if(result.stus == 300){
				publicTipMessage("ok","仓位有货，不可删除！")
			}else{
				publicTipMessage("ok","删除失败，请联系管理员！")
			}
		})
	})
}
