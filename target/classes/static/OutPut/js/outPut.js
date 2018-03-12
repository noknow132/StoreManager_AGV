$(function() {
	getOutPutData();
//	全选q
	$(".out_all").click(function(){
		if($(this).text()=="全选"){
			$(".out_table_content div").find("input[type='checkbox']").prop("checked",true);
			$(this).text("取消");
		}else if($(this).text()=="取消"){
			$(".out_table_content div").find("input[type='checkbox']").prop("checked",false);
			$(this).text("全选");
		}
	});

	
	
	$(".out_carry").click(function(){
		var idsJsonArr=[];
		var ids=getOutputIds(idsJsonArr);
		if(flag){
			searchLetOut(idsJsonArr);			
		}/*else{
			publicTipMessage("error","请至少选择一个值！");

		}*/
		//执行调库选择库位
	});	
	//统一出口赋值
	$("#select_letout_same_id").change(function(){
		$(".out_chu_select").val($(this).val());
	});
	//选择出口确认按钮
	$("#outLetModalYes").click(function(){
		var jsonArr=[];
		var selects=$("#select_letout_id li").find("select");
		var flagSele=true;
		selects.each(function(){
			var outletId= $(this).val();
			var storeNo= $(this).attr("storeNoData");
			var storeId=$(this).attr("idData");
			if(outletId==""){
			  publicTipMessage("error","请为"+storeNo+"选择出口！");
			  flagSele=false;
			  return;
			}else{
				var jsonObj={"storeId":storeId,"storeNo":storeNo,"outletId":outletId};
				jsonArr.push(jsonObj);
				
			}
		});
		if(flagSele){
		    $('#outLetModalId').modal('hide');
			//console.info(JSON.stringify(jsonArr))
			 addOutPut(JSON.stringify(jsonArr));	
		}
	
	})
	
//	删除模态框
	$(".out_delect").click(function(){
		 $("#user_del").modal({
	            backdrop : false,
	        });
	});
	
	//搜索按钮点击事件
	$("#outPut_search").click(function(){
		getOutPutData();
	});
});

var flag=false;//true表示checkbox有选中值，false 表示一个都没有选中
//获取出库列表
function getOutPutData(){
	var params={
			"search":$(".out_seach").val()
	};
	$.post("../outputStore/searchOutputStoreBySearch", params, function(resultJson) {
		$("#outPut_ul_id").empty();
		appendOutPutHtml(resultJson.list);
	}, "json");	
}

//拼接出库列表html
function appendOutPutHtml(list){
	for(var i in list){
		$("#outPut_ul_id").append('	<li class="out_table_content">'
				+'<div>'
				+'<input type="checkbox"  class="out_input_checkbox mt8 ml40" style="margin-left:40px;" storeNoData="'+list[i].store_no+'" value="'+list[i].store_id+'"/>'
				+'</div>'
				+'<div>'+(list[i].order_no?list[i].order_no:"")+'</div>'
				+'<div>'+(list[i].store_no?list[i].store_no:"")+'</div>'
				+'<div>'+(list[i].good_no?list[i].good_no:"")+'</div>'
				+'<div><span class="out_message_table_zhan current_zhan"></span></div>'
				+'<div>'+(list[i].count?list[i].count:0)+/*(list[i].unit?list[i].unit:"")+*/'</div>'
				+'<div>'
				+'<a  class="out_overflow fc_8" title="'+(list[i].input_time?format(list[i].input_time):"")+'">'+(list[i].input_time?format(list[i].input_time):"")+'</a>'
				+'</div>'
				+'</li>');
	}
	//点击一行复选框选中
	$("#outPut_ul_id li").click(function(){
		if($(this).find("input[type='checkbox']").is(":checked")){
			$(this).find("input[type='checkbox']").attr("checked",false);
		}else{
			$(this).find("input[type='checkbox']").attr("checked",true);
		}
	});
}

//执行出库作业
function addOutPut(params){
$.ajax({
	type:'POST',
	url:"../outputStore/addOutputStore",
	data:params,
	dataType:"json",
	contentType:"application/json",
	success:function(resultJson){
		if(resultJson.code==200){
			flag=false;//重置为false
			publicTipMessage("ok","操作成功，请前往当前作业进行查看！");
			$('#outLetModalId').modal('hide');
			getOutPutData();
		}
		if(resultJson.code==500){
			publicTipMessage("error","操作失败！");
		}
	}
})
		

}
//拼接要出库的id
function  getOutputIds(idsJsonArr){
	var  addIds ="";
//	var idsJsonArr=[];
	var checkboxs=$("#outPut_ul_id").find('li input[type="checkbox"]');
	checkboxs.each(function(){
		if($(this).is(":checked")){
			flag=true;
			var id=$(this).val();
			var storeNo=$(this).attr("storeNoData");
			var outletId="";
			var idsJsonObj={"id":id,"storeNo":storeNo,"outletId":outletId};
			idsJsonArr.push(idsJsonObj);
			addIds=addIds+","+$(this).val();
		}
	});
	if(!flag){
		  publicTipMessage("error","请选择至少一个值！");
		 return ;
	}else{
		return addIds;
	}
}

//选择的
function searchSelected(ids){
	var params={
			"ids":ids
	};
	$.post("../outputStore/addOutputStore", params, function(resultJson) {
		if(resultJson.code==200){
			  publicTipMessage("ok","操作成功！");
			$('#outLetModalId').modal('hide');
		}
		if(resultJson.code==500){
			publicTipMessage("error","操作失败！");

		}	
	}, "json");	
}


// 选择出口弹出框 
// jsonArr 选择要出库的仓位
// optionStr 出口

function searchOutLetModal(jsonArr,optionStr){
	 for(var i=0,len=jsonArr.length;i<len;i++){
		$("#select_letout_id").append('<li class="clearfix">'
                	+'<div class="fl"><img src="../image/putIn/putIn_07.png" alt=""> &nbsp;'+jsonArr[i].storeNo+'</div>'
                	+'<div class="fr">'
                	+'<select class="out_chu_select" idData="'+jsonArr[i].id+'" storeNoData="'+jsonArr[i].storeNo+'" >'
                	+optionStr
                	+'</select>'
                	+'</div>'
                	+'</li>');
	 }
	$("#select_letout_same_id").append(optionStr);
}

//查找出口
function  searchLetOut(idsJsonArr){
	var params={
			"type":1
	};
	$.post("../outLet/searchOutLetByType", params, function(resultJson) {
		if(resultJson.code==500){
			publicTipMessage("error","出错了！");
			return;
		}
		if(resultJson.total!=0){
		var	optionStr=appendLetOutOption(resultJson.list);
		   $("#select_letout_id").empty();
		   $("#select_letout_same_id").empty(optionStr);
			searchOutLetModal(idsJsonArr,optionStr);
			$("#outLetModalId").modal({
				backdrop : false,
			});
		}
	}, "json");	
}

//拼接出口的option
function  appendLetOutOption(list){
	var optionStr="<option value=''>请选择出口</option>";
   for(var i in list){
	   optionStr=optionStr+'<option value="'+list[i].outletId+'">'+list[i].outletName+'</option>';
   }
   return optionStr;
}


