$(function() {
//	点击调库
	getLibraryOutData();//获取要调库的仓库信息列表
	getChangeStoreData();//获取调库单的列表
	delChangeStore();//删除调库单操作
	getChangeIds();//调库操作
	/**
	 * 搜索按钮
	 */
	$("#changeSearchBtnId").click(function(){
		getLibraryOutData();
	})
});
var storeIdFrom=""; //调库的仓位id

//获取要调库列表
function getLibraryOutData(){
	var params={
			"search":$(".library_seach").val()
	};
	$.post("../changeStore/searchChangeStoreBySearch", params, function(resultJson) {
		//$("#libraryOut_ul_id").empty();
		appendLibraryOutHtml(resultJson.list);
		addLibrary_diao_click();//调库点击事件
	}, "json");	
}

//拼接调库列表html
function appendLibraryOutHtml(list){
	debugger
	var  isSearch=false;
	if($(".library_seach").val()==""){
		$("#libraryOut_ul_id").empty();
		isSearch=false;
	}else{
		isSearch=true;
	}

	for(var i in list){
		var  content='<li class="library_table_content '+(isSearch?"lilibrary_table_content_current":"")+'" idData="'+(list[i].store_id?list[i].store_id:"")+'">'
			+'<div>'
			+'<a  class="library_overflow ml40 fc_8" title="'+(list[i].order_no?list[i].order_no:"")+'">'+(list[i].order_no?list[i].order_no:"")+'</a>'
			+'</div>'
			+'<div>'+(list[i].store_no?list[i].store_no:"")+'</div>'
			+'<div>'+(list[i].good_no?list[i].good_no:"")+'</div>'
			+'<div><span class="library_message_table_zhan current_zhan"></span></div>'
			+'<div>'+(list[i].count?list[i].count:0)+/*(list[i].unit?list[i].unit:"")+*/'</div>'
			+'<div>'+(list[i].input_time?format(list[i].input_time):"")+'</div>'
			+'<div class="library_last">'
			+'<span class="fc_4d fl cursor library_diao" createstorehouseData="'+list[i].createstorehouse_id+'"   storeIdData="'+list[i].store_id+'" storeNoData="'+(list[i].order_no?list[i].order_no:"")+'">调库</span>'
			+'<img src="../image/lr.png" class="library_lr_img fl ml10 library_bl none">'
			+'<span class="fl fc_9 ml10 library_bl none">请选择</span>'
			+'<img src="../image/guan.png" style="width:18px;" class="fl none library_bl ml20 mt10 library_guan"/>'
			+'</div>'
			+'</li>';
		if($(".library_seach").val()==""){
			$("#libraryOut_ul_id").append(content);
		}else{
			$("#libraryOut_ul_id").find("li").each(function(){
				debugger
				var idData=$(this).attr("idData");
				if(list[i].store_id==idData){
					$(this).remove();
					return false;
				}
			});
		}
	}	
	for(var i in list){
		var  content='<li class="library_table_content '+(isSearch?"lilibrary_table_content_current":"")+'" idData="'+(list[i].store_id?list[i].store_id:"")+'">'
			+'<div>'
			+'<a  class="library_overflow ml40 fc_8" title="'+(list[i].order_no?list[i].order_no:"")+'">'+(list[i].order_no?list[i].order_no:"")+'</a>'
			+'</div>'
			+'<div>'+(list[i].store_no?list[i].store_no:"")+'</div>'
			+'<div>'+(list[i].good_no?list[i].good_no:"")+'</div>'
			+'<div><span class="library_message_table_zhan current_zhan"></span></div>'
			+'<div>'+(list[i].count?list[i].count:0)+/*(list[i].unit?list[i].unit:"")+*/'</div>'
			+'<div>'+(list[i].input_time?format(list[i].input_time):"")+'</div>'
			+'<div class="library_last">'
			+'<span class="fc_4d fl cursor library_diao" createstorehouseData="'+list[i].createstorehouse_id+'"   storeIdData="'+list[i].store_id+'" storeNoData="'+(list[i].order_no?list[i].order_no:"")+'">调库</span>'
			+'<img src="../image/lr.png" class="library_lr_img fl ml10 library_bl none">'
			+'<span class="fl fc_9 ml10 library_bl none">请选择</span>'
			+'</div>'
			+'</li>';
		if(isSearch){
			$("#libraryOut_ul_id").prepend(content);
		}
	}		
}

//新增调库单
function addLibrary_diao_click(){
	$(".library_last").click(function(){
		if($(this).find(".library_diao").text()=="调库"){
			var  changeStore=$(this);
			storeIdFrom=changeStore.find(".library_diao").attr("storeIdData");
			var createstorehouseData=changeStore.find(".library_diao").attr("createstorehouseData");//对调
			console.log("createstorehouseData");

			changeStore.find(".library_bl").addClass("block").removeClass("none");
			//$(this).parent().siblings().children(".library_last").find(".library_diao").text("对调");
			changeStore.parent().siblings().children(".library_last").find(".library_diao").each(function(){
				//alert($(this).text())
				if($(this).attr("createstorehouseData")==createstorehouseData){
					$(this).text("对调");
				}else{
					$(this).hide();
					$(this).parent().unbind();
				}
			});
			changeStore.parent().siblings().children(".library_last").find(".library_diao").addClass("fc_29").removeClass("fc_4d");


		}else if($(this).find(".library_diao").text()=="对调"){
			$(this).find(".library_bl").addClass("none").removeClass("block");
			$(".library_list").addClass("block").removeClass("none");
			$(".library_diao").text("调库").addClass("fc_4d").removeClass("fc_29");
			$(".library_bl").addClass("none").removeClass("block");
			var storeIdTo=$(this).find(".library_diao").attr("storeIdData");
			var params={
					"storeIdFrom":storeIdFrom,
					"storeIdTo":storeIdTo
			}
			$.post("../changeStore/insertChangeStore", params, function(resultJson) {
				if(resultJson.code==500){
					publicTipMessage("error","操作失败！");
					return;
				}
				if(resultJson.code==200){
					publicTipMessage("ok","操作成功！");
					getChangeStoreData();
					getLibraryOutData();
					$('#changeStore_del').modal('hide');
				}
			}, "json");	

		}
	});	
	//关闭调库选择
	$(".library_guan").click(function(){
		$(this).hide();
		$(".library_bl").addClass("none").removeClass("block");
		$(".library_list").addClass("block").removeClass("none");
		$(".library_diao").text("调库").addClass("fc_4d").removeClass("fc_29");
		$(".library_bl").addClass("none").removeClass("block");
		getChangeStoreData();
		getLibraryOutData();
	})
}
//获取调库列表
function getChangeStoreData(){
	var params={
			"status":0
	};
	$.post("../changeStore/searchChangeStoreOrderList", params, function(resultJson) {
		$("#library_list_ul_id").empty();
		appendChangeStoreHtml(resultJson.list);
		//添加点击事件
		$(".library_delect_out").click(function(){
			$("#changeStore_del").modal({
				backdrop : false,
			});

			$("#delChangeStoreId").attr("changeIdData",$(this).attr("changeIdData"));	
		});
		/*addLibrary_diao_click();*/
	}, "json");	
}

//拼接出库列表html
function appendChangeStoreHtml(list){
	for(var i in list){
		$("#library_list_ul_id").append(		
				'<li class="fl clearfix pos_r" style="margin-top:15px;margin-bottom:10px;"  changeIdData="'+list[i].change_id+'">'
				+'<span class="fc_8 fs12 fl fromStore" storeIdData="'+list[i].store_id_from+'" >'+list[i].fromno+'</span>'
				+'<img src="../image/rl.png" class="fl library_list_img pos_a"/>'
				+'<span class="fc_8 fs12 fr toStore" storeIdData="'+list[i].store_id_to+'" >'+list[i].tono+'</span>'
				+'<div class="pos_a library_delect_out" changeIdData="'+list[i].change_id+'">'
				+'<img src="../image/shan.png" class="pos_a">'
				+'</div>'
				+'</li>'
		);
	}

}	

function delChangeStore(){
//	删除模态框
	$("#delChangeStoreId").click(function(){
		var changeId= $(this).attr("changeIdData");
		var params={
				"changeId":changeId
		};
		$.post("../changeStore/delChangeStore", params, function(resultJson) {
			if(resultJson.code==500){
				publicTipMessage("error","删除失败！");

				return;
			}
			if(resultJson.code==200){
				publicTipMessage("ok","删除成功！");

				getChangeStoreData();
				getLibraryOutData();
				$('#changeStore_del').modal('hide');
			}
		}, "json");	
	});
}

//执行调库操作
function getChangeIds(){
	$("#zhi_change_store").click(function(){
		var	jsonArr=[];
		$("#library_list_ul_id").find("li").each(function(){
			var changeId= $(this).attr("changeIdData");
			jsonArr.push(changeId);
		});

		if(jsonArr.length>0){
			var params=JSON.stringify(jsonArr);
			addChangeStore(params);
		}else{
			publicTipMessage("error","当前没有作业可操作！");
		}
	})
}

function addChangeStore(params){
	$.ajax({
		type:'POST',
		url:"../changeStore/addChangeStores",
		data:params,
		dataType:"json",
		contentType:"application/json",
		success:function(resultJson){
			if(resultJson.code==200){
				flag=false;//重置为false
				getChangeStoreData();
				publicTipMessage("ok","操作成功，请前往当前作业进行查看！");
			}
			if(resultJson.code==500){
				publicTipMessage("error","操作失败！");
			}
		}
	})
}

