/**
 * Created by DELL on 2018/1/12.
 */
var warnTypeList;//报警类型
$(function(){
	loadWarnRecord();//加载报警记录
	searchWarnTypeAll();//查询报警类型
	searchRunStepRecord();//查询运行步骤记录
	//全选
	$("#Yundo_all").click(function () {
		if($("#Yundo_all").text()=="全选"){
			$(this).text('取消');
			$("#YXhistoryUlApp").find("input[type='checkbox']").prop("checked",true);
		}else if($("#Yundo_all").text()=="取消"){
			$(this).text('全选');
			$("#YXhistoryUlApp").find("input[type='checkbox']").prop("checked",false);
		}
	});


	
	$("#YXhisSeachSelect").change(function () {
		getFinishWorkStep();//获取记录列表
	});

	//报警记录下拉框 改变事件
	$("#warnSelect").change(function () {
		loadWarnRecord();
	});
	
	//运行步骤记录下拉框 改变事件
	$("#runSelect").change(function () {
		searchRunStepRecord();
	});
	
/*	//删除确认按钮
	$("#AlldelYXHisYes").click(function(){
		publicTipMessage("error","删除失败");
		$('#YXHis_Alldel').modal('hide');//隐藏仓库选择模态框

		//publicTipMessage("ok","删除成功");
	})*/

	getFinishWorkStep();//获取记录列表
	delfinish();//删除功能
	checkDelfinish();//确认删除

	
	//tab btn
	$(".Yxtabbtn").eq(0).css("color","#fff");
	$(".Yxtabbtn").eq(0).click(function(){
		$(".BJtabBox").addClass("none");
		$(".DDtabBox").removeClass("none");
		$(".YXBZtabBox").addClass("none");
		$(".Yxtabbtn").css("color","#a4cbff");
		$(this).css("color","#fff");
		var val = $(this).text();
		$("#changeTexe").text(val);
		$(".Yxtabbtn").find("span").addClass("none");
		$(this).find("span").removeClass("none");
	});
	$(".Yxtabbtn").eq(1).click(function(){
		$(".BJtabBox").removeClass("none");
		$(".DDtabBox").addClass("none");
		$(".YXBZtabBox").addClass("none");
		$(".Yxtabbtn").css("color","#a4cbff");
		$(this).css("color","#fff");
		var val = $(this).text();
		$("#changeTexe").text(val);
		$(".Yxtabbtn").find("span").addClass("none");
		$(this).find("span").removeClass("none");
	});
	$(".Yxtabbtn").eq(2).click(function(){
		$(".YXBZtabBox").removeClass("none");
		$(".BJtabBox").addClass("none");
		$(".DDtabBox").addClass("none");
		$(".Yxtabbtn").css("color","#a4cbff");
		$(this).css("color","#fff");
		var val = $(this).text();
		$("#changeTexe").text(val);
		$(".Yxtabbtn").find("span").addClass("none");
		$(this).find("span").removeClass("none");
	});
	
//	//此处拼接的是报警记录
//	$("#BJhistoryUlApp").append(
//			'<li class="history_table_content" style="padding:0 15px;">'
//            +'<div>'
//            +'    <a  class="history_overflow" title="2017/12/28 12:43:22">2017/12/28 12:43:22</a>'
//            +'</div>'
//            +'<div>取货位无货</div>'
//            +'<div>出库</div>'
//            +'<div>'
//			+'	<a  class="history_overflow" title="2018010210">2018010208</a>'
//			+'</div>'
//            +'<div>'
//			+'	<a  class="history_overflow" title="2018010210">2018010208</a>'
//			+'</div>'
//            +'<div>A1013</div>'
//            +'<div>'
//            +'   <a  class="history_overflow" title="2018010210">2018010208</a>'
//            +'</div>'
//            +'<div>'
//            +'    <a  class="history_overflow" title="2018010210">2018010210</a>'
//            +'</div>'
//            +'<div>A10101</div>'
//            +'<div>'
//			+'		<a  class="history_overflow" title="2017/12/28 12:43:22">2017/12/28 12:43:22</a>'
//			+'</div>'
//        +'</li>');
	
	
//	//此处拼接的是运行步骤记录
//	$("#YXBZhistoryUlApp").append(
//			'<li class="history_table_content1" style="padding:0 15px;height: 244px;line-height: 30px;">'
//            +'<div style=" line-height: 244px; ">'
//            +'   <a  class="history_overflow" title="2018010210">2018010208</a>'
//            +'</div>'
//            +'<div style=" line-height: 244px; ">出库</div>'
//            +'<div style=" line-height: 244px; ">'
//			+'	<a  class="history_overflow" title="2018010210">2018010208</a>'
//			+'</div>'
//            +'<div>'
//			+'	<ol>'
//			+'			<li>1.启动初始化、行走，去取货位</li>'
//			+'			<li>2.取货伸叉伸</li>'
//			+'			<li>3.取货叉伸抬伸</li>'
//			+'			<li>4.取货叉收回</li>'
//			+'			<li>5.行走，去放货位</li>'
//			+'			<li>6.放货伸叉伸</li>'
//			+'			<li>7.放货叉伸下降</li>'
//			+'			<li>8.放货叉伸收回</li>'
//			+'	</ol>'
//			+'</div>'
//            +'<div>'
//            +'		<ol>'
//			+'			<li><a  class="history_overflow" title="2017/12/28 12:43:22">2017/12/28 12:43:22</a></li>'
//			+'			<li><a  class="history_overflow" title="2017/12/28 12:43:22">2017/12/28 12:43:22</a></li>'
//			+'			<li><a  class="history_overflow" title="2017/12/28 12:43:22">2017/12/28 12:43:22</a></li>'
//			+'			<li><a  class="history_overflow" title="2017/12/28 12:43:22">2017/12/28 12:43:22</a></li>'
//			+'			<li><a  class="history_overflow" title="2017/12/28 12:43:22">2017/12/28 12:43:22</a></li>'
//			+'			<li><a  class="history_overflow" title="2017/12/28 12:43:22">2017/12/28 12:43:22</a></li>'
//			+'			<li><a  class="history_overflow" title="2017/12/28 12:43:22">2017/12/28 12:43:22</a></li>'
//			+'			<li><a  class="history_overflow" title="2017/12/28 12:43:22">2017/12/28 12:43:22</a></li>'
//			+'	</ol>'
//           +'</div>'
//			+'</li>');
	
	
	
});

//获取要执行的列表
function  getFinishWorkStep(){
	var params={
			"workStatue":2,
			"workType":$("#YXhisSeachSelect").val(),
			"no":$("#success_seach1").val()
	};
	$.post("../workStep/searchWorkStepsByWorkStatue", params, function(resultJson) {
		$("#YXhistoryUlApp").empty();
		appendFinishWSHtml(resultJson.list);
	}, "json");	
}

//拼接要完成的列表
function appendFinishWSHtml(list){
	for(var i in list){
		$("#YXhistoryUlApp").append(
				'<li class="history_table_content1" style="height: 38px; line-height: 38px;">'+
				'<div>'+
				'<input type="checkbox"   class="history_input_checkbox mt8 ml40 finshCheck" style="margin-left:40px;" workIdData="'+list[i].work_id+'"/>'+
				'</div>'+
				'<div>'+
				'<span  class="history_overflow fc_8" title="'+(list[i].output_store_time?format(list[i].output_store_time):"")+'">'+(list[i].output_store_time?format(list[i].output_store_time):"")+'</span>'+
				'</div>'+
				//'<div>'+ //订单号
				//'<a  class="history_overflow fc_8" title="'+list[i].order_no+'">'+(list[i].order_no?list[i].order_no:"")+'</a>'+
				//'</div>'+
				'<div>'+ //子订单号
				'<span  class="history_overflow fc_8" title="'+list[i].order_no+'">'+(list[i].order_no?list[i].order_no:"")+'</span>'+
				'</div>'+
				'<div class="fc_92"  style="text-align:center;">'+(list[i].work_type==0?"入库":(list[i].work_type==1?"出库":
					(list[i].work_type==2?"移库":"调库")))
					+'</div>'+
				'<div>'+(list[i].fringe_code?list[i].fringe_code:"")+'</div>'+
				'<div  style="text-align:center;"><img src="../image/start.png"></div>'+
				'<div>'+(list[i].get_place?list[i].get_placeNo:"")+'</div>'+
				'<div>'+(list[i].put_place?list[i].put_placeNo:"")+'</div>'+
				'<div>'+
				'<span  class="history_overflow fc_8" title="'+(list[i].input_store_time?format(list[i].input_store_time):"")+'">'+(list[i].input_store_time?format(list[i].input_store_time):"")+'</span>'+
				'</div>'+
				//'<div>'+ //扫描时间
				//'<a  class="history_overflow fc_8" title="'+(list[i].input_store_time?format(list[i].input_store_time):"")+'">'+(list[i].input_store_time?format(list[i].input_store_time):"")+'</a>'+
				//'</div>'+
				'<div  style="text-align:center;">'+list[i].count+'</div>'+
				'<div>'+(list[i].outlet_name==-1?"":list[i].outlet_name)+'</div>'+
				'</li>'
		)
	}
}


//删除弹出框
function delfinish(){
			//删除模态框
		$('#AlldelYXHis').click(function() {
			var flag=false;
			$(".finshCheck").each(function(){
				if($(this).is(':checked')){
					flag=true;
				}
			});
			if(flag){
			$("#YXHis_Alldel").modal({
				backdrop : false,
			});
			}else{
				publicTipMessage("error","请至少选中一个！");
			}
		});
}

//确认删除
function checkDelfinish(){
	//删除确认按钮
	$("#AlldelYXHisYes").click(function(){
		var jsonArr=[];
		$(".finshCheck").each(function(){
			if($(this).is(':checked')){
			var workIdData=	$(this).attr("workIdData");
			jsonArr.push(workIdData);
			}
		});
		var params=JSON.stringify(jsonArr);
		$.ajax({
			type:'POST',
			url:"../workStep/delWorkStepsFinished",
			data:params,
			dataType:"json",
			contentType:"application/json",
			success:function(resultJson){
				if(resultJson.code==200){
					$('#YXHis_Alldel').modal('hide');//隐藏仓库选择模态框
					publicTipMessage("ok","操作成功！");
					getFinishWorkStep();
				}
				if(resultJson.code==500){
					publicTipMessage("error","删除失败！");
				}
			}
		})
	})
}

//加载报警记录
function loadWarnRecord(){
	$.post("../WarnRecordController/searchWarnRecord",{type:$("#warnSelect").val(),no:$("#success_seach2").val()},function(result){
		$("#BJhistoryUlApp").empty();
		if(result != undefined){
		for (var i = 0; i < result.length; i++) {
			//报警类型
			var warnType = "";
			for (var j = 0; j < warnTypeList.length; j++) {
				if(warnTypeList[j].warnCode == result[i].warnType){
					warnType = warnTypeList[j].warnName;
					break;
				}
			}
			
			//作业类型
			var workType = "";
			if(result[i].workType == 0){
				workType = "入库";
			}else if(result[i].workType == 1){
				workType = "出库";
			}else if(result[i].workType == 2){
				workType = "移库";
			}else{
				workType = "调库";
			}
			
			$("#BJhistoryUlApp").append(
			'<li class="history_table_content" style="padding:0 15px; height: 38px; line-height: 38px;">'
            +'<div>'
            +'    <span  class="history_overflow" title="">'+(result[i].warnTime==undefined?"":format(result[i].warnTime))+'</span>'
            +'</div>'
            +'<div>'+(result[i].warnType==undefined?"":warnType)+'</div>'
            +'<div>'+(result[i].workType==undefined?"":workType)+'</div>'
            +'<div>'
			+'	<span  class="history_overflow" title="">'+(result[i].orderNo1==undefined?"":result[i].orderNo1)+'</span>'
			+'</div>'
            +'<div>'
			+'	<span  class="history_overflow" title="">'+(result[i].barConde1==undefined?"":result[i].barConde1)+'</span>'
			+'</div>'
            +'<div>'+(result[i].storeNo1==undefined?"":result[i].storeNo1)+'</div>'
            +'<div>'
            +'   <span  class="history_overflow" title="">'+(result[i].orderNo2==undefined?"":result[i].orderNo2)+'</span>'
            +'</div>'
            +'<div>'
            +'    <span  class="history_overflow" title="">'+(result[i].barConde2==undefined?"":result[i].barConde2)+'</span>'
            +'</div>'
            +'<div>'+(result[i].storeNo2==undefined?"":result[i].storeNo2)+'</div>'
            +'<div>'
			+'		<span  class="history_overflow" title="">'+(result[i].resolveTime==undefined?"":format(result[i].resolveTime))+'</span>'
			+'</div>'
        +'</li>');
		}
		}
		
	})
}

//查询报警类型
function searchWarnTypeAll(){
	$.get("../WarnTypeController/searchWarnTypeAll",function(result){
		warnTypeList = result;
	})
}

//查询运行步骤记录
function searchRunStepRecord(){
	$("#YXBZhistoryUlApp").empty();//清空
	$.post("../RunStepRecordController/searchRunStepRecord",{type:$("#runSelect").val(),no:$("#success_seach3").val()},function(result){
		for (var i = 0; i < result.length; i++) {
			var setpOl = '<ol>';//运行步骤
			var timeOl = '<ol>';//运行时间
			var list = result[i];
			var workTypeName = "";
				if(list[0].work_type == 0){
					workTypeName = "入库";
				}else if(list[0].work_type == 1){
					workTypeName = "出库";
				}else if(list[0].work_type == 2){
					workTypeName = "移库";
				}else if(list[0].work_type == 3){
					workTypeName = "调库";
				}
			for (var m = 0; m < list.length; m++) {
				setpOl += '<li>'+(list[m].step_name == undefined?"":list[m].run_step_id+"."+list[m].step_name)+'<li>';
				timeOl += '<li>'+(list[m].run_time == undefined?"":format(list[m].run_time))+'<li>';
			}
			setpOl +='</ol>';//结尾
			timeOl +='</ol>';//结尾
			//此处拼接的是运行步骤记录
			$("#YXBZhistoryUlApp").append(
					'<li class="history_table_content1" style="padding:0 15px;">'
		            +'<div style="line-height: 30px;">'
		            +'   <span  class="history_overflow" title="">'+list[0].order_id+'</span>'
		            +'</div>'
		            +'<div  style="line-height: 30px;">'+workTypeName+'</div>'
		            +'<div >'
					+'	<span  class="history_overflow" title="" style="line-height: 30px;">'+list[0].bar_code+'</span>'
					+'</div>'
		            +'<div>'
		            +setpOl
					+'</div>'
		            +'<div>'
		            +timeOl
		           +'</div>'
					+'</li>');
		}		
		
	})
}





