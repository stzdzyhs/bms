//素材删除js
$(document).ready(function(){
	resetOperBtn();
});

//勾选记录时，设置可操作按钮是否可用
function resetOperBtn(checkboxName){
	var delBtnDis=false,editBtnDis=false;
	if(checkboxName==null  || typeof(checkboxName)=='undefined' || checkboxName=="")
		checkboxName="ids";
	var sets = $("input[name='"+checkboxName+"']:checked");
	if(sets.length<=0){
		delBtnDis = true;
		editBtnDis = true;
	}else{
		var operatorNo=null, status=null, operatorGrade=null;
		sets.each(function(){
				operatorNo = $("#operatorNo_"+this.value).val();
				
				if(operatorNo!=curOperatorNo){
					delBtnDis = true;
					editBtnDis = true;
				}
			}
		);
	}
	$("input[name='delBtn']").attr("disabled",delBtnDis);
	$("input[name='editBtn']").attr("disabled",editBtnDis);
}