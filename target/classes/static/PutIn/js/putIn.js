
// Created by DELL on 2018/1/10.

        $(function () {
        	loadInputStore();//加载未确认的入库单
            $("#putIn_allChe").click(function () {
                if($("#putIn_allChe").text()=="全选"){
                    $(this).text('取消');
                    $("#moveS_ulAppend").find("input[type='checkbox']").prop("checked",true);
                }else if($("#putIn_allChe").text()=="取消"){
                    $(this).text('全选');
                    $("#moveS_ulAppend").find("input[type='checkbox']").prop("checked",false);
                }
            });
            
			
			 //删除模态框
			$('#putINAlldel').click(function() {
				
				var flag=false;
				$("#moveS_ulAppend input").each(function(){
					if($(this).is(':checked')){
						flag=true;			
					}
				});

				if(flag){
					$("#putIn_Alldel").modal({
						backdrop : false,
					});
				}else{
					publicTipMessage("error","请至少选中一个!");

				}
				
			});
			
			
        });
        
        
        //加载未确认的入库单
        function loadInputStore(){
        	$.post("../InputStoreController/searchInputStore",{status:0},function(result){
        		$("#moveS_ulAppend").empty();
        		console.info(result.list);
        		var list = result.list;
        		for(var i = 0;i < list.length;i++){
        			$("#moveS_ulAppend").append('<li class="success_message_table_content">'+
                            '<div><span class="ml40"><input type="checkbox" value="'+list[i].input_store_id+'"></span></div>'+
                            '<div>'+list[i].order_id+'</div>'+
                            '<div>'+list[i].store_no+'</div>'+
                            '<div>'+list[i].bar_code+'</div>'+
                            '<div class="putInNu">'+(list[i].count==undefined?"0":list[i].count)+'</div>'+
                           '<div class="take">2</div>'+
                            ' </li>');
        		}
        		//点击一行复选框选中
            	$("#moveS_ulAppend li").click(function(){
            		if($(this).find("input[type='checkbox']").is(":checked")){
            			$(this).find("input[type='checkbox']").attr("checked",false);
            		}else{
            			$(this).find("input[type='checkbox']").attr("checked",true);
            		}
            	});
        	})
        	
        }
        
        
        //入库添加
        function insertInputStore(){
        	if($(".move_haveStrLiAct").attr("dataNo") == undefined){
        		publicTipMessage("error","请选择仓位!");
        		return;
        	}
        	$('#move_cho').modal('hide')//隐藏仓位选择模态框
        	$.post("../InputStoreController/insertGoods",{
        		barCode:$("input[name='barCode']").val(),
        		count:$("input[name='count']").val(),
        		storeId:$(".move_haveStrLiAct").attr("dataNo")
        		},function(result){
        		if(result.stus == "200"){
        			publicTipMessage("ok","操作成功!");
                    $(".success_seach").val("");
//        			searchEmptyStoreHouse();//刷新仓库下拉框
        			loadInputStore();//刷新
        		}else{
        			publicTipMessage("error","操作失败!");
        		}
        	})
        }
        
        //入库删除
        function deleteInputStore(){
        	var inputStoreArray = new Array();//入库id数组
        	console.info($("input[type='checkbox']:checked"))
        	var checkboxList = $("input[type='checkbox']:checked");//选中的入库作业
        	for (var i = 0; i < checkboxList.length; i++) {
        		inputStoreArray[i] = checkboxList[i].value;
    		}
         	$.ajax({
        		url:"../InputStoreController/deleteManyInputStoreById",
        		type:"POST",
        		data:{ids:inputStoreArray},
        		traditional: true,
        		success:function(result){
        			if(result.stus == "200"){
            			publicTipMessage("ok","删除成功!");
            			$('#putIn_Alldel').modal('hide')//隐藏增加模态框
//            			searchEmptyStoreHouse();//刷新仓库下拉框
            			loadInputStore();//刷新入库作业
            		}else{
            			publicTipMessage("error","删除失败!");
            		}
        		}
        	})
        }
        
        //执行入库作业
        function startInputStore(){
        	var inputStoreArray = new Array();//入库id数组
        	console.info($("input[type='checkbox']:checked"))
        	var checkboxList = $("input[type='checkbox']:checked");//选中的入库作业
        	if(checkboxList.length == 0){
        		publicTipMessage("error","请选择入库作业!");
        		return;
        	}
        	for (var i = 0; i < checkboxList.length; i++) {
        		inputStoreArray[i] = checkboxList[i].value;
    		}
        	
        	$.ajax({
        		url:"../InputStoreController/startInputStore",
        		type:"POST",
        		data:{ids:inputStoreArray},
        		traditional: true,
        		success:function(result){
        			if(result.stus == "200"){
            			publicTipMessage("ok","操作成功!");
            			loadInputStore();//刷新
            		}else{
            			publicTipMessage("error","操作失败!");
            		}
        		}
        	})
        }
        
        //空仓位列表 选择仓位绑定事件
        function searchEmptyStoreHouse(){
        	$.get("../storeHouse/searchEmptyStoreHouse",{storeNo:$("#storeNo").val()},function(list){
        		console.info(list)
        		$(".move_haveStrUl").empty();//先清空
        		if($("input[name='count']").val()<0||$("input[name='count']").val()==0|| $("input[name='count']").val()>200 ||$("input[name='barCode']").val()==''){
				publicTipMessage("error","请填写正确的条形码和数量！");
				return;				
        		}      		       		
        		
        		//条形码为13位
        		if($("input[name='barCode']").val().length < 13 || $("input[name='barCode']").val().length > 13){
        			publicTipMessage("error","条形码为13位！");
    				return;	
        		}
        		
        		
        		$.post("../InputStoreController/checkBarCode",{barCode:$("input[name='barCode']").val()},function(count){
        			if(count != 0){
        				publicTipMessage("error","已存在相同的条形码！");
        				return;
        			}
        			//打开模态框
            		$("#move_cho").modal({
    					backdrop : false,
    				});
            		for (var j = 0; j < list.length; j++) {
            			var storeList =  list[j].storeList;//建库下的仓位集合
						
						if(storeList.length != 0){
							$(".move_haveStrUl").append('<li style="width:100%">'+list[j].createStoreHouseStoreName+'：</li>')//建库名
						}
						for(var i = 0; i < storeList.length; i++){
	            			$(".move_haveStrUl").append('<li dataNo="'+storeList[i].storeId+'"><img src="../image/putIn/putIn_07.png" alt=""> &nbsp;'+storeList[i].storeNo+'</li>');
	            			
	            		}
					}
            		//点击仓库高亮显示
        		    $(".move_haveStrUl li").click(function () {
        		        $(".move_haveStrUl li").removeClass("move_haveStrLiAct");
        		        $(this).addClass("move_haveStrLiAct");
        		        $("#moveMoYes").attr("disabled",false);
        		    });
        		})
        		
        	})
        	
        }