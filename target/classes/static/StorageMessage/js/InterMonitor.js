$(function(){


})


function  zuoBiaoShow(){

	$("#areaNameSYsId").html($("#areaNameId").val());
	var sequence=($("#sequenceId").html()=='行/层/列'?0:1);
	var rowsStart=parseInt($("#rowsStartId").html());
	var rowsCount=parseInt($("#rowsCountId").html());
	var columnsCount=parseInt($("#columnsCountId").html());
	 appendSystemHtml(rowsCount,columnsCount,rowsStart);
	 getXYStoreHouseData(true,rowsCount,columnsCount,rowsStart,sequence);
}

function  zuoBiaoShow1(createStoreHouse){
	$("#areaNameSYsId").html($("#areaNameId").val());
	var sequence = createStoreHouse.sequence; 
	var rowsStart= createStoreHouse.rowsStart; 
	var rowsCount= createStoreHouse.rowsCount; 
	var columnsCount= createStoreHouse.columnsCount
	
	 appendSystemHtml(rowsCount,columnsCount,rowsStart);
	 getXYStoreHouseData(true,rowsCount,columnsCount,rowsStart,sequence);
}

//拼接坐标系横纵坐标
function   appendSystemHtml(rowsCount,columnsCount,rowsStart){
	$("#lie_li_id").empty();
	$("#ceng_ul_id").empty();

	//拼接横坐标
	for(var i=0;i<columnsCount;i++){
		var lie= rowsStart+i;
		var lieStr=""
		if(lie<10){
			lieStr="0"+ String(lie);
		}else{
			lieStr= String(lie);
		}
		$("#lie_li_id").append('<div>'
				 + '<span  class="fs12 lh32 fc_9">'+lieStr+'</span>'
					+'</div>');		
	}
/*	for(var i=columnsCount;i>0;i--){
		var lie= i-(1-rowsStart);
		var lieStr=""
		if(lie<10){
			lieStr="0"+ String(lie);
		}else{
			lieStr= String(lie);
		}
		$("#lie_li_id").append('<div>'
				 + '<span  class="fs12 lh32 fc_9">'+lieStr+'</span>'
					+'</div>');		
	}*/
	 
		for(var j=rowsCount;j>0;j--){
			var ceng= j-(1-rowsStart);
			var cengStr=""
			if(ceng<10){
				cengStr="0"+ String(ceng);
			}else{
				cengStr= String(ceng);
			}
			$("#ceng_ul_id").append('<li class="fs12 lh12 fc_9">'+cengStr+'</li>');
		}
	 
	 
	 
	//拼接纵坐标
/*	for(var j=0;j<rowsCount;j++){
		var ceng= rowsStart+j;
		var cengStr=""
		if(ceng<10){
			cengStr="0"+ String(ceng);
		}else{
			cengStr= String(ceng);
		}
		$("#ceng_ul_id").append('<li class="fs12 lh12 fc_9">'+cengStr+'</li>');
	}*/
}
//拼接坐标展示的仓位情况
function appendZuoBiaoHtml(list,rowsCount,columnsCount,rowsStart,sequence){
/*	for(var i=0;i<rowsCount;i++){
		$("#cang_ul_id").append('<li> </li>');
		for(var j=0;j<columnsCount;j++){
			var k=i*columnsCount+j;
			$("#cang_ul_id").find('li').eq(i).append('<div storeId="'+list[k].store_id+'" storeNoData="'+list[k].store_no+'" stateNoData="'+list[k].store_no+'" goodNoData="'+(list[k].good_no?list[k].good_no:"")+'"'
					+' orderIdData="'+(list[k].order_id?list[k].order_id:"")+'" countData="'+(list[k].count?list[i].count:"")+'" >'
			        	+'<span class="'+(list[k].store_statue==0?"":(list[k].store_statue==1?" inter_yu_zhan":" inter_has_zhan"))+'" ></span>'
			        	+'</div>');
		}
	}*/
	if(sequence==0){
		for(var i=0;i<rowsCount;i++){

			$("#cang_ul_id").prepend('<li> </li>');
			for(var j=0;j<columnsCount;j++){
				var k=i*columnsCount+j;
				$("#cang_ul_id").find('li').eq(0).append('<div storeId="'+list[k].store_id+'" storeNoData="'+list[k].store_no+'" stateNoData="'+list[k].store_no+'" goodNoData="'+(list[k].good_no?list[k].good_no:"")+'"'
						+' orderIdData="'+(list[k].order_id?list[k].order_id:"")+'" countData="'+(list[k].count?list[k].count:"")+'" >'
				        	+'<span class="'+(list[k].store_statue==0?"":(list[k].store_statue==1?" inter_yu_zhan":" inter_has_zhan"))+'" ></span>'
				        	+'</div>');
			}
		}
	}else{
		for(var i=0;i<columnsCount;i++){
			for(var j=rowsCount;j>0;j--){
				if(i==0){
					$("#cang_ul_id").append('<li> </li>');
				}
				var k=i*rowsCount+j-1;
				$("#cang_ul_id").find('li').eq(rowsCount-j).append('<div storeId="'+list[k].store_id+'" storeNoData="'+list[k].store_no+'" stateNoData="'+list[k].store_no+'" goodNoData="'+(list[k].good_no?list[k].good_no:"")+'"'
						+' orderIdData="'+(list[k].order_id?list[k].order_id:"")+'" countData="'+(list[k].count?list[k].count:"")+'" >'
				        	+'<span class="'+(list[k].store_statue==0?"":(list[k].store_statue==1?" inter_yu_zhan":" inter_has_zhan"))+'" ></span>'
				        	+'</div>');
			}
		}
		
	}	
	
}
//获取仓位信息
function getXYStoreHouseData(zuoBiaoFlag,rowsCount,columnsCount,rowsStart,sequence){
	var createstorehouseId=$("#storeNameId").attr("dataId");
	var storeName=$("#storeNameId").html();
	var search=$(".success_seach").val();
	var params={
			"createstorehouseId":createstorehouseId,
			"search":search,
			"storeName":storeName
	};
	$.post("../storeHouse/searchStoreHouse", params, function(resultJson) {
		$("#cang_ul_id").empty();
		appendZuoBiaoHtml(resultJson.list,rowsCount,columnsCount,rowsStart,sequence);
		 storehoseHover();//添加浮动事件
		 addClickFun();//添加点击事件
	}, "json");	
}

function  storehoseHover(){
	$('.inter_mointor_ceng li').hover(function() {
		
		//让当前元素透明度到一其他元素透明度都0.5 
		$(this).stop().fadeTo(500,1).siblings().stop().fadeTo(500,0.2);
	}, function() {
		//让所有的元素都还原
		$('.inter_mointor_ceng li').stop().fadeTo(500,1)
	});
	// 动画效果
		$('.inter_mointor_ceng li>div').hover(function() {
			//让当前元素透明度到一其他元素透明度都0.5 
			$(this).stop().fadeTo(500,1).siblings().stop().fadeTo(500,0.2);
			//$('.inter_mointor_ceng li').stop().fadeTo(500,0.1);
			
			$(this).parents(".clearfix_top").find(".stateNoSys").html($(this).attr("stateNoData"));
			$(this).parents(".clearfix_top").find(".goodNoSys").val($(this).attr("goodNoData"));
			$(this).parents(".clearfix_top").find(".countSys").val($(this).attr("countData"));
			$(this).parents(".clearfix_top").find(".orderIdSys").val($(this).attr("orderIdData"));
		}, function() {
			//让所有的元素都还原
			$('.inter_mointor_ceng li>div').stop().fadeTo(500,1)
			 $(this).parents(".clearfix_top").find(".stateNoSys").html("");
			$(this).parents(".clearfix_top").find(".goodNoSys").val("");
			$(this).parents(".clearfix_top").find(".countSys").val("");
			$(this).parents(".clearfix_top").find(".orderIdSys").val("");

		});
}
//拼接区位列表
//function addQu(){
//	$("#inter_ul_qu").append('<li class="clearfix">'
//			+'<div class="clearfix state_div" style="padding-top:32px;background:#fff;padding-right:57px;">'
//			+'<div class="inter_qu_name fl fc_6 fs30" id="areaNameSYsId">A区</div>'
//			+'<div style="height:28px;width:1px;background:#dddddd;" class="fl ml15"></div>'
//			+'<div class="fl fs24 lh30 ml15" id="stateNoSys" style="width:80px;height:55px;color:#3379d1;"></div>'
//			+'<div class="fl ml20">'
//				+'<span class="fl fs12 fc_9 lh30">订单号</span>'
//				+'<input type="text" class="inter_input ml5" id="orderIdSys"/>'
//			+'</div>'
//			+'<div class="fl ml20">'
//				+'<span class="fl fs12 fc_9 lh30">货物条码</span>'
//				+'<input type="text" class="inter_input ml5" id="goodNoSys"/>'
//			+'</div>'
//			+'<div class="fl ml20">'
//				+'<span class="fl fs12 fc_9 lh30">数量</span>'
//				+'<input type="text" class="inter_input ml5" id="countSys" style="width:40px;"/>'
//			+'</div>'
//			+'<div class="inter_no_button fs15 fc_f fr cursor ml15" style="height:27px;margin-top:14px;">'
//				+'<img src="../image/bianji4.png" class="fl" style="margin-top: 0px;width:14px;height:15px;" />'
//				+'<span class="fl fs14 fc_4d" style="height:27px;">编辑区位</span>'
//			+'</div>'
//			+'<ul class="clearfix fr">'
//				+'<li class="fl clearfix">'
//					+'<div class="clearfix">'
//						+'<span class="fl fs10 fc_9e font_r" style="font-size:10px;">层&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;数：</span>' 
//						+'<span class="fl fs10 fc_9e" id="rowsCountId" style="font-size:10px;">10</span>'
//					+'</div>'
//					+'<div class="clearfix mt10">'
//						+'<span class="fl fs10 fc_9e font_r" style="font-size:10px;">行起始值：</span>'
//						+'<span class="fl fs10 fc_9e" id="rowsStartId" style="font-size:10px;">1</span>'
//					+'</div>'
//				+'</li>'
//				+'<li class="fl clearfix ml15">'
//					+'<div class="clearfix">'
//						+'<span class="fl fs10 fc_9e font_r" style="font-size:10px;">列&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;数：</span>' 
//						+'<span class="fl fs10 fc_9e" id="columnsCountId" style="font-size:10px;">10</span>'
//					+'</div>'
//					+'<div class="clearfix mt10">'
//						+'<span class="fl fs10 fc_9e font_r" style="font-size:10px;">列起始值：</span>' 
//						+'<span class="fl fs10 fc_9e" id="columnsStartId" style="font-size:10px;">10</span>'
//					+'</div>'
//				+'</li>'
//				+'<li class="fl clearfix ml20">'
//					+'<div class="clearfix">'
//						+'<span class="fl fs10 fc_9e font_r" style="font-size:10px;">区位名称：</span>' 
//						+'<select class="fl fs10 fc_9e" id="areaNameId" style="width: 55px;border: none;border-bottom: 1px solid #9e9e9e;font-size:10px;">'
//							+'<option value="A1">A1</option>'
//						+'</select>'
//					+'</div>'
//					+'<div class="clearfix" style="margin-top:11px;">'
//						+'<span class="fl fs10 fc_9e" style="font-size:10px;">行列顺序：</span>' 
//						+'<span class="fl fs10 fc_9e" id="sequenceId" style="font-size:10px;">行、列</span>'
//					+'</div>'
//				+'</li>'
//			+'</ul>'
//		
//		+'</div>'
//	  +'</li>')
//}

function  addClickFun(){
	//点击进入历史记录页面
	$(".inter_mointor_ceng li div ").click(function(){
		var storeId=$(this).attr("storeId");
		var storeNo=$(this).attr("storeNoData");
		window.location = "historyRecord.html?storeId="+storeId+"&storeNo="+storeNo;
	});
}


