$(function() {
	var obj= GetRequest("storeId");
	 storeId=obj.storeId;
	
	getStoreHouseRecordData(obj.storeId);
	$("#storeNoHisId").html(obj.storeNo);
	
	
	//select下拉框
	$("#HisSearchSelect").change(function(){
		
		getStoreHouseRecordData(obj.storeId);
		
	})
	
	//条件搜索查询
	$("#HisSearchBtn").click(function(){
		getStoreHouseRecordData(obj.storeId);
	})

//	全选q
	$(".history_all").click(function(){
		if($(this).text()=="全选"){
			$(".history_table_content div").find("input[type='checkbox']").prop("checked",true);
			$(this).text("取消");
		}else if($(this).text()=="取消"){
			$(".history_table_content div").find("input[type='checkbox']").prop("checked",false);
			$(this).text("全选");
		}

	});

//	$(".history_delect").click(function(){
//		var InputT = $(".history_table_content div").find("input[type='checkbox']");
//		if(InputT.is(":checked")){
//			flag=true;
//			//delIds=delIds+","+$(this).val();
//		}
//		if(!flag){
//			publicTipMessage("error","至少选择一个!");
//			//getDelIds();
//			return;
//		}else{
//			$("#history_del").modal({
//	            backdrop : false,
//	        });
//			getDelIds();
//			
//		}
//	});
	
	
	$(".history_delect").click(function(){
		 getDelIds();
		if(!flag){
			publicTipMessage("error","至少选择一个！");
			return;
		}else{
			$("#history_del").modal({
	            backdrop : false,
	        });
			
		}
	});
	
//	确认删除
	$("#delUserYes").click(function(){
		var ids=getDelIds();
		ids=ids.substring(1,ids.length);
		delStoreHouseRecordByIds(ids);
		
		$("#history_del").hide();
	});
});
var flag=false;
var storeId="";
//获取历史记录信息
function getStoreHouseRecordData(storeId){
	var params={
			"storeId":storeId,
			"search":$("#history_seach_input").val(),
			"operateType":$("#HisSearchSelect").val()
	};
	$.post("../storeHouseRecord/searchStoreHouseRecord", params, function(resultJson) {
		$("#storeHourseRecord_ul_id").empty();
		appendStoreHouseRecordHtml(resultJson.list);
	}, "json");	
}

//拼接历史记录html
function appendStoreHouseRecordHtml(list){
	for(var i in list){
		$("#storeHourseRecord_ul_id").append('<li class="history_table_content">'
				+'<div>'
				+'<input type="checkbox"   class="history_input_checkbox mt8 ml40" style="margin-left:40px;" value="'+list[i].record_id+'"/>'
				+'</div>'
				+'<div>'
				+'<a  class="history_overflow fc_8" title="'+(list[i].order_time?(format(list[i].order_time)):"")+'">'+(list[i].order_time?(format(list[i].order_time)):"")+'</a>'
				+'</div>'
				+'<div class="fc_92">'+(list[i].operate_type==0?"入库":list[i].operate_type==1?"出库":list[i].operate_type==2?"调库":"移库")+'</div>'
				+'<div>'
				+'<a  class="history_overflow fc_8" title="'+(list[i].order_id?list[i].order_id:"")+'">'+(list[i].order_id?list[i].order_id:"")+'</a>'
				+'</div>'
				+'<div>'+(list[i].bar_code?list[i].bar_code:"")+'</div>'
				+'<div><span class="history_zhi current_zhi"></span></div>'
				+'<div>'+(list[i].outstoreno?list[i].outstoreno:"")+'</div>'
				+'<div style="padding-left:0;">'+(list[i].instoreno?list[i].instoreno:"")+'</div>'
				+'<div style="padding-left:0;">'
				+'<a  class="history_overflow fc_8" title="'+format(list[i].operate_time_start)+'">'+format(list[i].operate_time_start)+'</a>'
				+'</div>'
				+'<div>'+(list[i].count?list[i].count:"")+/*(list[i].unit?list[i].unit:"")+*/'</div>'
				+'<div>'+(list[i].outlet_name?list[i].outlet_name:"")+'</div>'
				+'<div>'
				+'<a  class="history_overflow fc_8"  title="'+format(list[i].operate_time_end)+'">'+format(list[i].operate_time_end)+'</a>'
				+'</div>'
				+'</li>');
	}
}

//拼接要删除的id
function  getDelIds(){
	var  delIds ="";
	var checkboxs=$("#storeHourseRecord_ul_id").find('li input[type="checkbox"]');
	checkboxs.each(function(){
		if($(this).is(":checked")){
			flag=true;
			delIds=delIds+","+$(this).val();
		}
		});
		if(flag){
			return delIds;	
		}else{
			publicTipMessage("error","请选择至少一个值！");
			return ;
		}	
}

//获取历史记录信息
function delStoreHouseRecordByIds(ids){
	var params={
			"ids":ids
	};
	$.post("../storeHouseRecord/delStoreHouseRecord", params, function(resultJson) {
		appendStoreHouseRecordHtml(resultJson.list);
		if(resultJson.code==500){
			publicTipMessage("error","删除失败！");
			return;
		}
		if(resultJson.code==200){
			publicTipMessage("ok","删除成功！");
			flag=false;
			getStoreHouseRecordData(storeId);
		}
		
	}, "json");	
}

