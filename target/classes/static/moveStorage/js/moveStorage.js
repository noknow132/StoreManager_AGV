/**
 * Created by DELL on 2018/1/10.
 */
var storeFrom = "";//移库起始仓库id
var storeTo = "";//移库目标仓库id
$(function () {
	searchMoveStore();
	loadStore();//加载仓库信息

    //选择移库仓库
    $("#moveMoYes").click(function () {    	
    	 if($(".move_haveStrLiAct").attr("dataNo") == undefined){
             $(this).attr("disabled",true);
             publicTipMessage("error","请选择仓位!");
             return
         }else{
             var val = $(".move_haveStrLiAct").text();
             $(".move_after").text(val);
             $(this).attr("disabled",false);
             $('#move_cho').modal('hide');//隐藏仓库选择模态框
             $(".move_ZtBox").css("display","block");//显示准备好的预移库列表
             
             storeTo = $(".move_haveStrLiAct").attr("dataNo");
             
             insertMoveStore();//添加移库
             searchMoveStore();//刷新预移库列表
 			 loadStore();//刷新仓库表
         }
    });
    
    //搜索事件
    $(".success_search_img").unbind().click(function(){
    	loadStore();//刷新仓库表
    })
});



//加载移库仓库信息
function loadStore(){
	$.post("../storeHouse/searchStoreHouseByNotInMoveStore",{
		condition:$("#condition").val(),
		},function(list){
			$("#moveS_ulAppend").empty();
			if(typeof list != "string"){
				for(var i = 0;i < list.length;i++){
					$("#moveS_ulAppend").append(' <li class="success_message_table_content">' +
					        '<div><span class="ml30">'+(list[i].order_id==undefined?"":list[i].order_id)+'</span></div>' +
					        '<div>'+(list[i].store_no==undefined?"":list[i].store_no)+'</div>' +
					        '<div>'+(list[i].good_no==undefined?"":list[i].good_no)+'</div>' +
					        '<div><span class="success_message_table_zhan '+(list[i].store_statue == 0 ?"":"current_zhan")+'"></span></div>' +
					        '<div>'+(list[i].count==undefined?"":list[i].count)+'</div>' +
					        '<div>'+(list[i].input_time==undefined?"":format(list[i].input_time))+'</div>' +
					        '<div style="color: #078fff;" class="cursor">' +
					        '<a  style="color: #078fff;" class="move_btn" dataNo="'+list[i].store_id+'">移库</a>' +
					        '</div>' +
					        '</li>')
				}
			}
	// 	移库按钮 加载空仓库
	    $('.move_btn').click(function() {
	        $("#move_cho").modal({
	            backdrop : false,
	        });
	        storeFrom = $(this).attr("dataNo");
	        searchEmptyStoreHouse();
	    });
	})
}

//空仓位列表
function searchEmptyStoreHouse(){
	$.post("../storeHouse/searchEmptyStoreHouseByCreateStoreHouseId",{storeId:storeFrom,storeNo:$("#storeNo").val()},function(list){
		$(".move_haveStrUl").empty();//先清空
		for(var i = 0; i < list.length; i++){
			$(".move_haveStrUl").append('<li dataNo="'+list[i].storeId+'">'+list[i].storeNo+'</li>');
		}
		
		//移库仓库选中样式
	    $(".move_haveStrUl li").click(function () {
	        $(".move_haveStrUl li").removeClass("move_haveStrLiAct");
	        $(this).addClass("move_haveStrLiAct");
	        $("#moveMoYes").attr("disabled",false);//启用确认按钮
	    });
	})	
}


//查询预移库列表
function searchMoveStore(){
	$.get("../MoveStoreController/searchtMoveStore",{status:0},function(result){
		$(".move_ZtBox").empty();
		if(typeof result != "string"){
		for (var i = 0; i < result.length; i++) {
			//待执行的移库列表
		    $(".move_ZtBox").append('<div class="fl move_zt clearfix" style="position:relative;"><div class="move_zt_del"><img dataNo="'+result[i].move_id+'" src="../image/move/move_03.png" class="moveDel" style="cursor:pointer;width: 25px;height: 25px;"></div>' +
		        '<span class="fl move_before">'+result[i].storeFrom+'</span>' +
		        '<span class="fl" style="position: absolute; top: 50%; margin-top: -26px; left: 50%; margin-left: -10px;"><img src="../image/move/move_06.png" ></span>' +
		        ' <span class="fr move_after">'+result[i].storeTo+'</span>' +
		        '</div>');	
		}
		} 
	    //预移库列表删除按钮样式
	    $(".move_zt").hover(function () {
	       $(this).find(".move_zt_del").css("display","block");
	    },function () {
	    	$(".move_zt_del").css("display","none");
	    });
	    
	    //打开删除模态框
	    $(".moveDel").click(function(){
	    	$("#move_del").modal({
	            backdrop : false,
	        });
	    	//模态框隐藏域赋值
	    	$("#move_del_moveId").val($(this).attr("dataNo"));
	    })
	    //删除操作按钮
	    $("#moveDel").unbind().click(function(){
	    	$.post("../MoveStoreController/deleteMoveStore",{moveId:$("#move_del_moveId").val()},function(result){
	    		if(result.stus == "200"){	    			
	    			searchMoveStore();//刷新预移库列表
	    			loadStore();//刷新仓库表
	    			$("#move_del").modal("hide");//隐藏增加模态框
	    			publicTipMessage("ok","删除成功!");
	    		}else{
	    			publicTipMessage("error","删除失败!");
	    		}
	    	})
	    });
	})
}

//移库添加
function insertMoveStore(){
	$.post("../MoveStoreController/insertMoveStore",{
		storeFromId:storeFrom,
		storeToId:storeTo
		},function(result){
		if(result.stus == "200"){
			publicTipMessage("ok","操作成功!");
			searchMoveStore();//刷新预移库列表
			loadStore();//刷新
		}else{
			publicTipMessage("error","操作失败!");
		}
	})
}

//执行移库作业
function startMoveStore(){
	var moveStoreArray = new Array();//入库id数组
	var moveStoreList = $(".moveDel");//获得预移库的id
	if(moveStoreList.length == 0){
		publicTipMessage("error","当前没有可执行的移库作业!");
		return;
	}
	for (var i = 0; i < moveStoreList.length; i++) {
		moveStoreArray[i] = $(moveStoreList[i]).attr("dataNo")
	}
	$.ajax({
		url:"../MoveStoreController/startMoveStore",
		type:"POST",
		data:{moveIds:moveStoreArray},
		traditional: true,
		success:function(result){
			if(result.stus == "200"){
    			publicTipMessage("ok","操作成功!");
    			$(".move_ZtBox").hide();
    			searchMoveStore();//刷新预移库列表
    			loadStore();//刷新
    		}else{
    			publicTipMessage("error","操作失败!");
    		}
		}
	})
}