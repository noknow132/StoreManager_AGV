/**
 * Created by DELL on 2018/1/12.
 */
$(function(){
    //全选
    $(".history_all").click(function () {
        if($(".history_all").text()=="全选"){
            $(this).text('取消');
            $("#DDhistoryUlApp").find("input[type='checkbox']").prop("checked",true);
        }else if($(".history_all").text()=="取消"){
            $(this).text('全选');
            $("#DDhistoryUlApp").find("input[type='checkbox']").prop("checked",false);
        }
    });

    //拼接
    $("#DDhistoryUlApp").append(
        '<li class="history_table_content">'+
        '<div>'+
        '<input type="checkbox"  class="history_input_checkbox mt8 ml40" style="margin-left:40px;"/>'+
        '</div>'+
        '<div>'+
        '<a  class="history_overflow" title="2017/12/28 12:43">2017/12/28 12:43</a>'+
        '</div>'+
        '<div>'+
        '<a  class="history_overflow" title="2018010210">2018010210</a>'+
        '</div>'+
        '<div class="fc_92">出库</div>'+
        '<div>1</div>'+
        '<div><span class="history_zhi"></span></div>'+
        '<div>A1012</div>'+
        '<div>A1013</div>'+
        '<div>'+
        '<a  class="history_overflow" title="2017/12/28 12:43">2017/12/28 12:43</a>'+
        '</div>'+
        '<div>'+
        '<a  class="history_overflow" title="2017/12/28 12:43">2017/12/28 12:43</a>'+
        '</div>'+
        '<div>1000</div>'+
        '<div>1</div>'+
        '</li>'
    )
    
     //删除模态框
	$('#DDhisAllDel').click(function() {
		$("#DDHis_Alldel").modal({
			backdrop : false,
		});
	});
	//删除确认按钮
	$("#AlldelDDHisYes").click(function(){
		publicTipMessage("error","删除失败！");
		$('#DDHis_Alldel').modal('hide');//隐藏仓库选择模态框

		//publicTipMessage("ok","删除成功");
	})
	
});