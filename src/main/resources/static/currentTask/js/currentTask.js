/**
 * Created by DELL on 2018/1/12.
 */
var  isStartWorkFlag=false;//是否点击总的启动按钮

$(function(){

	 getIsRun();

	//alert("123213");
	startWorkClick();//启动按钮 事件
	//全选
$("#curr_allChe").click(function () {
		if($("#curr_allChe").text()=="全选"){
			$(this).text('取消');

			$("#curr_ulAppend").find("input[type='checkbox']").prop("checked",true);
			$("#curr_ulAppend").find(".jinxing").prop("checked",false);

		}else if($("#curr_allChe").text()=="取消"){
			$(this).text('全选');
			$("#curr_ulAppend").find("input[type='checkbox']").prop("checked",false);

		}
	});

//扫描条码框 回车事件
/*$('#scan_good_no_input').keydown(function(e){ 
	if(e.keyCode==13){ 
		 getScanGoodNo();
	} 
	}); */

//扫描条码框 oninput事件
$('#scan_good_no_input').on("input propertychange",function(){
	var goodno=$("#scan_good_no_input").val();
	if(goodno.length==13){
		//alert(goodno.length);
		getScanGoodNo();
	}
});

//扫描条码 的点击事件
$("#scan_good_no_btn").click( function () {
		 getScanGoodNo();
	});

//判断是启动还是暂停
//isStartWorkFlag  true  已经启动了  是暂停  

//clearInterval(id);
	delUnfinish();//删除弹出框
	checkDelUnfinish();//确认删除
	getCurrentStatus();
	
	//复位按钮的状态
	 if($("#startWork").html()=="启动"){
		$("#reset").attr("disabled",false);
		$("#reset").removeClass("add_nav_ul_right2");
		$("#reset").addClass("add_nav_ul_right");
	}else{
		$("#reset").attr("disabled",true);
		$("#reset").removeClass("add_nav_ul_right");
		$("#reset").addClass("add_nav_ul_right2");
	}
});

//扫描条码
function  getScanGoodNo(){
	var fringeCode= $("#scan_good_no_input").val();
    if(fringeCode.trim()!=""){
    	var params={
    			"fringeCode":$("#scan_good_no_input").val(),
    			"workStatue":3//待执行
    	}
    	$.post("../workStep/changeWorkStatus",params, function(resultJson) {
          if(resultJson.code==404){
    			publicTipMessage("error","找不到此条码的入库作业！");
          }else if(resultJson.code==202){
    			publicTipMessage("ok","有此入库作业！");
    			$("#scan_good_no_input").val('');
    			getUnFinishWorkStep();
          }else if(resultJson.code==200){
  			publicTipMessage("ok","已加入到待执行作业中！");
			$("#scan_good_no_input").val('');
			getUnFinishWorkStep();
      }else{
    			publicTipMessage("error","出错了！");
          }
    	}, "json");	
    }else{
		publicTipMessage("error","请在文本框中输入值！");
    }
}

var  lastStatus=-1;//缓存plc上一次的状态
var  getCurrentStatusInterId="";
//获取要执行的列表
function  getIsRun(){
	$.post("../ConfigParamController/selectConfigParamOne", function(resultJson) {
		isStartWorkFlag=(resultJson.isRun==0?false:true);
		if(isStartWorkFlag){
			$("#startWork").html("暂停");
			$("#reset").attr("disabled",true);
			$("#reset").removeClass("add_nav_ul_right");
			$("#reset").addClass("add_nav_ul_right2");
		}else{
			$("#startWork").html("启动");
			$("#reset").attr("disabled",false);
			$("#reset").removeClass("add_nav_ul_right2");
			$("#reset").addClass("add_nav_ul_right");
		}
		getUnFinishWorkStep();
	}, "json");	
}


//获取机械臂的当前状态
function getCurrentStatus(){
	var params={
	};
	$.post("../workStep/getNowPlace", params, function(resultJson) {
		getCurrentStatusInterId=setInterval('getCurrentStatus()',1000); //开启定时任务  去读状态
		if(resultJson.code==500){
			publicTipMessage("error","请检查机器状态！");
			appendNowPlaceHtml(resultJson);
		}else if(resultJson.code==200){
			appendNowPlaceHtml(resultJson);
		}else if(resultJson.code==404){
			publicTipMessage("error","PLC连接失败，请检查PLC连接是否正常！");
			appendNowPlaceHtml(resultJson);
			
		}
	}, "json");	
	if(getCurrentStatusInterId!=""){
		clearInterval(getCurrentStatusInterId);//当请求发送过去后避免还没有响应继续发送  清楚定时任务
	}
}



//拼接机械臂的当前状态html
function appendNowPlaceHtml(obj){
	debugger
	//$("#currentHId").val(obj.currentH);
	$("#currentLId").val(obj.currentL?obj.currentL:"");
	$("#currentCId").val(obj.currentC?obj.currentC:"");
	$("#getHId").val(obj.getH?obj.getH:"");
	$("#getLId").val(obj.getL?obj.getL:"");
	$("#getCId").val(obj.getC?obj.getC:"");
	$("#putHId").val(obj.putH?obj.putH:"");
	$("#putLId").val(obj.putL?obj.putL:"");
	$("#putCId").val(obj.putC?obj.putC:"");
	//$("#currentStartId").val(obj.currentStart==1?"已连接PLC":"未连接PLC");
	
	//$("#currentStatusId").val(obj.currentStatus==0?"等待中":"作业中");
	if((obj.workStatus2==0&&obj.workStatus1!=0)||(obj.workStatus2==9&&obj.workStatus1==0)){
		$("#currentStatusId").val("等待中");
	}else if(obj.workStatus2!=9&&obj.workStatus1!=0){
		$("#currentStatusId").val("进行中");

	}else if(obj.workStatus2==9&&obj.workStatus1!=0){
		$("#currentStatusId").val("已完成");
	}
	
	//如果值为空，则表示没有链接PLC，启动按钮和复位按钮禁用
	if($("#getHId").val() ==""){
		$("#currentStartId").val("未连接PLC");
		/*if($("#startWork").html()=="暂停"){
			alert(1);
			$("#reset").attr("disabled",true);
			$("#reset").removeClass("add_nav_ul_right");
			$("#reset").addClass("add_nav_ul_right2");
		}else if($("#startWork").html()=="启动"){
			alert(0);
			$("#reset").attr("disabled",false);
			$("#reset").removeClass("add_nav_ul_right2");
			$("#reset").addClass("add_nav_ul_right");
		}*/

		$("#startWork").attr("disabled",true);
		$("#startWork").removeClass("add_nav_ul_right");
		$("#startWork").addClass("add_nav_ul_right2");
		//$(".cuDoHide").hide();//上面的启动按钮成功后下面的 操作列隐藏
		$("#currentStatusId").val("");
		
	}else{
		//$(".cuDoHide").show();//上面的启动按钮失败后下面的 操作列显示
		$("#currentStartId").val("已连接PLC");
		$("#startWork").attr("disabled",false);
		$("#startWork").removeClass("add_nav_ul_right2");
		$("#startWork").addClass("add_nav_ul_right");
	}

	//文字颜色更改
	if($("#currentStartId").val()=="未连接PLC"){
		$("#currentStartId").css("color","orange");
	}else{
		$("#currentStartId").css("color","#5de01c");
	}
	
	//文字颜色更改
	if($("#currentStatusId").val()=="等待中"){
		$("#currentStatusId").css("color","orange");
	}else{
		$("#currentStatusId").css("color","#5de01c");
	}
	if(lastStatus!=obj.workStatus2 ){
		console.log("getUnFinishWorkStep");
		lastStatus=obj.workStatus2;
		getUnFinishWorkStep();
	}else{
		console.log("nogetUnFinishWorkStep");
		lastStatus=obj.workStatus2;
	}
	
}

//获取要执行的列表
function  getUnFinishWorkStep(){
	var params={
			"workStatue":0
	};
	$.post("../workStep/searchWorkStepsByWorkStatue", params, function(resultJson) {
		$("#curr_ulAppend").empty();
		appendUnFinishWSHtml(resultJson.list);
		if(!isStartWorkFlag){
			//$(".cuDoHide").addClass("block").removeClass("none");//上面的启动按钮  操作列表显示 
			startWorkOneClick();
		}else{
			//$(".cuDoHide").addClass("none").removeClass("block");;//上面的启动按钮成功后下面的 操作列隐藏
		}
/*		$(".finishWork").click(function(){
			$.post("../workStep/finishCurrentWorkStep", {}, function(resultJson) {
			}, "json");	
		})*/
	}, "json");	
}

//拼接要未完成的列表
function appendUnFinishWSHtml(list){
	/*	'+(list[i].work_statue==0?"false":"true")+'*/
	for(var i in list){
		var appendHtml='<li class="success_message_table_content">'+
		'<div><span class="ml30" style="margin-left:40px;"><input  class="unfinshCheck  '+(list[i].work_statue==0?"":"jinxing")+'" '+(list[i].work_statue==0||list[i].work_statue==3?"":"disabled")+'  type="checkbox" workIdData="'+list[i].work_id+'" ></span></div>'+
		'<div style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;" title="'+list[i].order_no+'">'+list[i].order_no+'</div>'+
		'<div style="text-align:center;">'+(list[i].work_type==0?"入库":(list[i].work_type==1?"出库":
			(list[i].work_type==2?"移库":"调库")))
			+'</div>'+
			'<div style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;" title="'+(list[i].fringe_code==undefined?"":list[i].fringe_code)+'">'+(list[i].fringe_code==undefined?"":list[i].fringe_code)+'</div>'+
			'<div class="curr_z" style="color:'+(list[i].work_statue==0?"#ffa422":"#5de01c")+'">'+(list[i].work_statue==0?"等待中":list[i].work_statue==1?"进行中":"待执行")+'</div>'+  //a==1是等待中的  a！=1是执行中的
			'<div>'+(list[i].get_place==undefined?"":list[i].get_placeNo)+'</div>'+
			'<div>'+(list[i].put_place==undefined?"":list[i].put_placeNo)+'</div>'+
			'<div style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;" title="'+(list[i].insert_time?format(list[i].insert_time):"")+'">'+ (list[i].insert_time?format(list[i].insert_time):"")+'</div>'+

			// 扫描时间 暂时注释！误删  '<div style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;" title="'+(list[i].insert_time?format(list[i].insert_time):"")+'">'+ (list[i].insert_time?format(list[i].insert_time):"")+'</div>'+
			
			'<div style="text-align:center;">'+(list[i].count==undefined?0:list[i].count)+'</div>'+
			'<div class="finishWork">'+(list[i].outlet_name==undefined?"":(list[i].outlet_name==-1?"":list[i].outlet_name))+'</div>'+
			/*'<div><span workIdData="'+list[i].work_id+'" class="cuDoHide  startWorkOneBtn none" style="color:#5183c1;cursor:pointer;">启动</span></div>'+*/
			'</li>';
		
//		if(list[i].work_statue==0){
			$("#curr_ulAppend").append(appendHtml
//					'<li class="success_message_table_content">'+
//					'<div><span class="ml30" style="margin-left:40px;"><input  class="unfinshCheck  '+(list[i].work_statue==0?"":"jinxing")+'" '+(list[i].work_statue==0||list[i].work_statue==3?"":"disabled")+'  type="checkbox" workIdData="'+list[i].work_id+'" ></span></div>'+
//					'<div style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;" title="'+list[i].order_no+'">'+list[i].order_no+'</div>'+
//					'<div>'+(list[i].work_type==0?"入库":(list[i].work_type==1?"出库":
//						(list[i].work_type==2?"移库":"调库")))
//						+'</div>'+
//						'<div style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;" title="'+(list[i].fringe_code==undefined?"":list[i].fringe_code)+'">'+(list[i].fringe_code==undefined?"":list[i].fringe_code)+'</div>'+
//						'<div class="curr_z" style="color:'+(list[i].work_statue==0?"#ffa422":"#5de01c")+'">'+(list[i].work_statue==0?"等待中":list[i].work_statue==1?"进行中":"待执行")+'</div>'+  //a==1是等待中的  a！=1是执行中的
//						'<div>'+(list[i].get_place==undefined?"":list[i].get_place)+'</div>'+
//						'<div>'+(list[i].put_place==undefined?"":list[i].put_place)+'</div>'+
//						'<div style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;" title="'+(list[i].insert_time?format(list[i].insert_time):"")+'">'+ (list[i].insert_time?format(list[i].insert_time):"")+'</div>'+
//						/*'<div>2018/1/11 11:53</div>'+*/
//						'<div>'+(list[i].count==undefined?0:list[i].count)+'</div>'+
//						'<div class="finishWork">'+(list[i].outlet_name==undefined?"":(list[i].outlet_name==-1?"":list[i].outlet_name))+'</div>'+
//						/*'<div><span workIdData="'+list[i].work_id+'" class="cuDoHide  startWorkOneBtn none" style="color:#5183c1;cursor:pointer;">启动</span></div>'+*/
//						'</li>'

			);
//		}else{
//			$("#curr_ulAppend").prepend(appendHtml
//					'<li class="success_message_table_content">'+
//					'<div><span class="ml30" style="margin-left:40px;"><input  class="unfinshCheck  '+(list[i].work_statue==0?"":"jinxing")+'" '+(list[i].work_statue==0||list[i].work_statue==3?"":"disabled")+'  type="checkbox" workIdData="'+list[i].work_id+'" ></span></div>'+
//					'<div style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;" title="'+list[i].order_no+'">'+list[i].order_no+'</div>'+
//					'<div>'+(list[i].work_type==0?"入库":(list[i].work_type==1?"出库":
//						(list[i].work_type==2?"移库":"调库")))
//						+'</div>'+
//						'<div style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;" title="'+(list[i].fringe_code==undefined?"":list[i].fringe_code)+'">'+(list[i].fringe_code==undefined?"":list[i].fringe_code)+'</div>'+
//						'<div class="curr_z" style="color:'+(list[i].work_statue==0?"#ffa422":"#5de01c")+'">'+(list[i].work_statue==0?"等待中":list[i].work_statue==1?"进行中":"待执行")+'</div>'+  //a==1是等待中的  a！=1是执行中的
//						'<div>'+(list[i].get_place==undefined?"":list[i].get_place)+'</div>'+
//						'<div>'+(list[i].put_place==undefined?"":list[i].put_place)+'</div>'+
//						'<div style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;" title="'+(list[i].insert_time?format(list[i].insert_time):"")+'">'+(list[i].insert_time?format(list[i].insert_time):"")+'</div>'+
//						/*'<div>2018/1/11 11:53</div>'+*/
//						'<div>'+(list[i].count==undefined?0:list[i].count)+'</div>'+
//						'<div class="finishWork">'+(list[i].outlet_name==undefined?"":(list[i].outlet_name==-1?"":list[i].outlet_name))+'</div>'+
//						/*'<div><span  workIdData="'+list[i].work_id+'" class="cuDoHide  startWorkOneBtn none"  style="color:#5183c1;cursor:pointer;">启动</span></div>'+*/
//						'</li>'
//			);	
//		}
		

	}
}

//删除弹出框
function delUnfinish(){
	//删除模态框
	$('#currAllDel').click(function() {
		var flag=false;
		$(".unfinshCheck").each(function(){
			if($(this).is(':checked')){
				flag=true;			
			}
		});
		if(flag){
			$("#curr_Alldel").modal({
				backdrop : false,
			});
		}else{
			publicTipMessage("error","请至少选中一个！");
		}
	});
}

//确认删除
function checkDelUnfinish(){
	//删除确认按钮
	$("#AlldelCurrYes").click(function(){
		var jsonArr=[];
		$(".unfinshCheck").each(function(){
			if($(this).is(':checked')){
				var workIdData=	$(this).attr("workIdData");
				jsonArr.push(workIdData);
			}
		});
		var params=JSON.stringify(jsonArr);
		$.ajax({
			type:'POST',
			url:"../workStep/delWorkStepsUnfinished",
			data:params,
			dataType:"json",
			contentType:"application/json",
			success:function(resultJson){
				if(resultJson.code==200){
					publicTipMessage("ok","删除成功！");
					$('#curr_Alldel').modal('hide');
					getUnFinishWorkStep();
					//隐藏仓库选择模态框
				}
				if(resultJson.code==500){
					publicTipMessage("error","删除失败！");
				}
			}
		})
	})
}

//输入条形码 启动任务
function startWorkOneClick(){
	$("#codeStart").click(function(){
//		var workId=$(this).attr("workIdData");
		publicTipMessage("busy","请等待...");
		setTimeout(function(){
			//检查网络连接
			if(isNetStateConnect()){
				//检查PLC连接
				var PLCstus = connectPlcConn();//PLC的返回值
				console.info(PLCstus.stus)
				
				if(PLCstus.stus == true){	
					//启动机器 开始任务
					var params={
							"code":$("#codeInput").val()
					}
					$.get("../workStep/startWorkStep",params,function(result){
						if(result.stus == 200){
							publicTipMessage("ok","启动完成！");
						}else if(result.stus == 300){
							publicTipMessage("error","当前没有可操作的任务！");
						}else if(result.stus == 301){
							publicTipMessage("error","当前存在执行中的任务！");
						}else if(result.stus == 302){
							publicTipMessage("error","条形码不存在！");
						}else{
							isLinkFlag=false;//是否连接成功
							publicTipMessage("error","启动失败，请重试或联系管理员！");
						}
						console.info(result)
					})
				}else if(PLCstus.stus == 300){
					publicTipMessage("error","请检查系统配置参数是否正确！");
				}else{
					publicTipMessage("error","PLC连接失败，请检查PLC连接是否正常！");
				}
			}else{
				publicTipMessage("error","网络连接出错，请检查网络是否正常！");
			}
		}, 100);	
	})
}

//绑定启动机器，开始任务点击事件
function  startWorkClick(){
	
	$("#startWork").click(function(){
		if($("#startWork").html()=="启动"){//启动
		publicTipMessage("busy","请等待...");
		setTimeout(function(){
			//检查网络连接
			if(isNetStateConnect()){
				//检查PLC连接
				var PLCstus = connectPlcConn();//PLC的返回值
				console.info(PLCstus.stus)
				if(PLCstus.stus == true){	
					//启动机器 开始任务
					$.get("../workStep/startWorkStep",function(result){
						if(result.stus == 200){
//							getCurrentStatus();
//							isLinkFlag=true;//是否连接成功
							 isStartWorkFlag=true;//是否点击总的启动按钮
							 $("#startWork").html("暂停");
							publicTipMessage("ok","启动完成！");
							getUnFinishWorkStep();
							//$(".cuDoHide").hide();//上面的启动按钮成功后下面的 操作列隐藏
						}else if(result.stus == 300){
//							getCurrentStatus();
//							isLinkFlag=true;//是否连接成功
							publicTipMessage("error","当前没有可操作的任务！");
							//$(".cuDoHide").show();//上面的启动按钮失败后下面的 操作列显示
						}else if(result.stus == 301){
//							isLinkFlag=true;
//							getCurrentStatus(); //是否连接成功
							publicTipMessage("error","当前存在执行中的任务！");
							//$(".cuDoHide").show();//上面的启动按钮失败后下面的 操作列显示
						}else{
							isLinkFlag=false;//是否连接成功
							publicTipMessage("error","启动失败，请重试或联系管理员！");
							//$(".cuDoHide").show();//上面的启动按钮失败后下面的 操作列显示
						}
					})
				}else if(PLCstus.stus == 300){
					publicTipMessage("error","请检查系统配置参数是否正确！");
				}else{
					publicTipMessage("error","PLC连接失败，请检查PLC连接是否正常！");
				}
			}else{
				publicTipMessage("error","网络连接出错，请检查网络是否正常！");
			}
		}, 100);
		}else{//暂停
			var params={
					"isRun":0
			};
			$.post("../ConfigParamController/updateConfigParamIsRun", params, function(resultJson) {
				if(resultJson.stus==200){
					 $("#startWork").html("启动");
					 publicTipMessage("ok","操作成功！");
					 isStartWorkFlag=false;//是否点击总的启动按钮
					 getUnFinishWorkStep();
					//隐藏仓库选择模态框
				}
				if(resultJson.stus==500){
					publicTipMessage("error","操作成功！");
				}
			}, "json");	
		}
	})
}



