
<input type="hidden" name="sortKey"  id="sortKey" value="${search.sortKey}" }>
<input type="hidden" name="sortType" id="sortType" value="${search.sortType}">
<script type="text/javascript">
$(document).ready(function(){
	SortRow.init();
});
function SortRow() {}
SortRow.init = function(){
	$(".sortRow").click(function(){		
		SortRow.sortList(this.id);
	});
	var sortKey = $("#sortKey").val();
	var sortType = $("#sortType").val();
  
	if(sortKey!='' && sortType!=''){		
		var sortRow = document.getElementById(sortKey);
		if(sortRow!=null && typeof(sortRow)!='undefined'){
			$(sortRow).css("background-image", "url(${path}/images/sort_"+sortType+".png)");
			$(sortRow).css("background-repeat", "no-repeat");
			$(sortRow).css("background-position", "95% 55%");
		}
	}
}
SortRow.sortList=function (sortKey){
	var sortKeyObj = $("#sortKey");
	var sortTypeObj = $("#sortType");
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
	$("#form1").submit();
/* 	$("form").get(0).submit(); */
}
</script>
