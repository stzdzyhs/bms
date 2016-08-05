//素材删除js
$(document).ready(function(){
	resetOperBtn();
});

//勾选记录时，设置可操作按钮是否可用
function resetOperBtn(checkboxName){
	var originBtnDis=false,cancelBtnDis=false;
	if(checkboxName==null  || typeof(checkboxName)=='undefined' || checkboxName=="")
		checkboxName="ids";
	var sets = $("input[name='"+checkboxName+"']:checked");
	if(sets.length<=0){
		originBtnDis = true;
		cancelBtnDis = true;
	}else{
		var contentType=null,origin=null,contentType1=null;
		contentType1 = $("#contentType_"+sets[0].value).val();
	   var i=0;
	   sets.each(function(){
	   		contentType = $("#contentType_"+sets[i].value).val();
			origin = $("#origin_"+sets[i].value).val();
			if(i>0){
				if(contentType!=contentType1){
					originBtnDis = true;
				}
			}
			if(origin=='1' || origin==1 ){
				originBtnDis = true; 
			}
			if(origin=='0' || origin==0 ){
				cancelBtnDis = true;
			}
			i++;
	   	}
	   );
	}
	$("input[name='originBtn']").attr("disabled",originBtnDis);
	$("input[name='cancelBtn']").attr("disabled",cancelBtnDis);
}