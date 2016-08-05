/**
 * 多选框(checkbox) 操作
 *
 * @author ld
 */

/*
全选/全不选
<input type=checkbox name=mm value=e onclick="checkItem(this, 'mmAll')">
<input type=checkbox name=mmAll onclick="checkAll(this, 'mm')">
*/
function checkAll(e, itemName){
	var aa = document.getElementsByName(itemName);
	for (var i=0; i<aa.length; i++)
		aa[i].checked = e.checked;
}
function checkItem(e, allId){
	var aa = document.getElementsByName(e.name);
	var allItem = document.getElementById(allId);
	if(allItem==null || typeof(allItem)=="undefined"){
		var allItemArray = document.getElementsByName(allId);
		if(allItemArray==null || typeof(allItemArray)=="undefined" || allItemArray.length<=0){
			return;
		}
		allItem = allItemArray[0];
	}
	for (var i=0; i<aa.length; i++){
		if(!aa[i].checked){
			allItem.checked=false;
			return;
		}
	}
	allItem.checked=true;
}
//取得已选中框的值	返回值为Array
function getCheckBoxValue(itemName){
	var checkValue = new Array(0);
	var aa = document.getElementsByName(itemName);
	for (var i=0; i<aa.length; i++){
		if(aa[i].checked){
			checkValue.push(aa[i].value);
		}
	}
	return checkValue;
}

// 获取checkbox信息,选中,未选中,当前选中 
function getCheckBoxInfo(formObj, itemName) { 
    var checkboxes = document.getElementsByName(itemName);
    var checkedStr = ","; 
    var uncheckedStr = ","; 
    var url = ""; 
    for(var i = 0; i < checkboxes.length; i++) { 
        var checkbox = checkboxes[i]; 
        if(checkbox.checked) { 
           checkedStr = checkedStr + checkbox.value + ","; 
       }else { 
           uncheckedStr = uncheckedStr + checkbox.value + ","; 
       } 
   } 
   formObj.now_selected.value = checkedStr; 
   formObj.no_selected.value = uncheckedStr; 
}