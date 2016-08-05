
function sortList(sortKey){
	var sortKeyObj = $("#sortKey");		
	var sortTypeObj =$("#sortType");
	if(sortKey==sortKeyObj.val()){
		if(sortTypeObj.val()=="asc"){
			sortTypeObj.val("desc");
		}else{
			sortTypeObj.val("asc");
		}
	}else{
		sortKeyObj.val(sortKey);
		sortTypeObj.val("asc");
	}
	$("#form1").submit()
}

