
//menuName = "电子秤信息";
$(function(){
	//menuConfig()//加载标题菜单栏
	
//一键初始化弹出框
	//添加点击事件
	$(".one_key").click(function(){
		$("#one_key_del").modal({
			backdrop : false,
		});

		$("#delChangeStoreId").attr("changeIdData",$(this).attr("changeIdData"));	
	});
//配置导航切换样式
	$(".config_con_ul li").click(function(){
		$(this).addClass("current").siblings().removeClass("current");
	});

	 getChainWeights();
	
});



//获取要执行的列表
function  getChainWeights(){
	$.post("../chainWeights/searchChainWeights", function(resultJson) {
	//	isStartWorkFlag=(resultJson.isRun==0?false:true);
		$("#chainWeight1").val(resultJson.cws.chainWeight1);
		$("#chainWeight2").val(resultJson.cws.chainWeight2);
		$("#chainWeight2").val(resultJson.cws.chainWeight2);
		$("#chainWeight3").val(resultJson.cws.chainWeight3);
		$("#chainWeight4").val(resultJson.cws.chainWeight4);
		$("#chainWeight5").val(resultJson.cws.chainWeight5);
		$("#chainWeight6").val(resultJson.cws.chainWeight6);
		$("#chainWeight7").val(resultJson.cws.chainWeight7);
		$("#chainWeight8").val(resultJson.cws.chainWeight8);
		$("#chainWeight9").val(resultJson.cws.chainWeight9);
		$("#chainWeight10").val(resultJson.cws.chainWeight10);
		$("#chainWeight11").val(resultJson.cws.chainWeight11);

	}, "json");	
}