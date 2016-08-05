function repainChannels(json){
	var html = "", numHtml = "已选择频道", delItems=null;		
	if(json!=null && typeof(json)!='undefined' && json!=""){
		var dgData = eval(json);
		var channelType = art.dialog.data('channelType');		
		$("#showChannels").empty();
		$("#channelType").val(channelType);
	
		if(art.dialog.data('channelType')==1){
			numHtml = "已选择频道分组";
		}
		var items = dgData.items;
		if(items!=null && items.length>0){							
			for(var i in items){
				if($("input[name='channelId'][value='"+items[i].channelId+"']").length<=0){
					html += '<li><label><input name="channelId" id="channelId" type="checkbox" checked value="'+items[i].channelId+'" /><label>'+items[i].channelName;						
					html += '</label></label></li>';					
			 }
		   }
		}
		delItems = dgData.delItems;
		if(delItems!=null && delItems.length>0){
			for(var i in delItems){					
					$("input[name='channelId'][value='"+delItems[i].channelId+"']").parent().parent().remove();
			}
		}
		$(html).appendTo("#showChannels");			
		var number = $("input[name='channelId']").length;
		$("#num_showchannels").html(numHtml+number+"个");
	}
	art.dialog.data('channelGroupData', '');
	art.dialog.data('channelData', '');
	Win.setDataKey('channelData');//把存储key的名字默认恢复为'ChannelData'
}


//获取已选择频道，用于传递到弹出频道窗口内容
 function collectChannels () {
	
		var dataName = 'channelData';
		var channelType = 0;	
		if($("#channelType").val()=='1'){
			dataName = 'channelGroupData';
			channelType = 1;
		}
		Win.cleanSelectChannels();
		var json = "";
		$("input[name='channelId']:checked").each(function(){
			if(json!=""){
				json += ",";
			}
			json += "{channelId: '"+$(this).val()+"',channelName:'"+$(this).next().html()+"'}";
		});
		if(json!=""){
			json = "({channelType:'"+channelType+"',items:["+json+"]})";
		}

		art.dialog.data(dataName, json);
		art.dialog.data('channelType',channelType);	
}
 
 

 function repainPrograms(json){
		var html = "", numHtml = "已选择节目", delItems=null;		
		if(json!=null && typeof(json)!='undefined' && json!=""){
			var dgData = eval(json);
			var programType = art.dialog.data('programType');		
			$("#showPrograms").empty();
			$("#programType").val(programType);
		
			if(art.dialog.data('programType')==1){
				numHtml = "已选择节目分组";
			}
			var items = dgData.items;
			if(items!=null && items.length>0){							
				for(var i in items){
					if($("input[name='programId'][value='"+items[i].programId+"']").length<=0){
						html += '<li><label><input name="programId" id="programId" type="checkbox" checked value="'+items[i].programId+'" /><label>'+items[i].programName;						
						html += '</label></label></li>';					
				 }
			   }
			}
			delItems = dgData.delItems;
			if(delItems!=null && delItems.length>0){
				for(var i in delItems){					
						$("input[name='programId'][value='"+delItems[i].programId+"']").parent().parent().remove();
				}
			}
			$(html).appendTo("#showPrograms");			
			var number = $("input[name='programId']").length;
			$("#num_showprograms").html(numHtml+number+"个");
		}
		art.dialog.data('programGroupData', '');
		art.dialog.data('programData', '');
		Win.setDataKey('programData');//把存储key的名字默认恢复为'ChannelData'
	}


	//获取已选择节目，用于传递到弹出节目窗口内容
	function collectPrograms () {		
		var dataName = 'programData';
		var programType = 0;	
		if($("#programType").val()=='1'){
			dataName = 'programGroupData';
			programType = 1;
		}
		Win.cleanSelectChannels();
		var json = "";
		$("input[name='programId']:checked").each(function(){
			if(json!=""){
				json += ",";
			}
			json += "{programId: '"+$(this).val()+"',programName:'"+$(this).next().html()+"'}";
		});
		if(json!=""){
			json = "({programType:'"+programType+"',items:["+json+"]})";
		}

		art.dialog.data(dataName, json);
		art.dialog.data('programType',programType);		
	}
	
	
	 function repainColumns(json){
			var html = "", numHtml = "已选择栏目", delItems=null;		
			if(json!=null && typeof(json)!='undefined' && json!=""){
				var dgData = eval(json);
				var columnType = art.dialog.data('columnType');		
				$("#showColumns").empty();
				$("#columnType").val(columnType);
			
				if(art.dialog.data('columnType')==1){
					numHtml = "已选择栏目分组";
				}
				var items = dgData.items;
				if(items!=null && items.length>0){							
					for(var i in items){
						if($("input[name='columnId'][value='"+items[i].columnId+"']").length<=0){
							html += '<li><label><input name="columnId" id="columnId" type="checkbox" checked value="'+items[i].columnId+'" /><label>'+items[i].columnName;						
							html += '</label></label></li>';					
					 }
				   }
				}
				delItems = dgData.delItems;
				if(delItems!=null && delItems.length>0){
					for(var i in delItems){					
							$("input[name='columnId'][value='"+delItems[i].columnId+"']").parent().parent().remove();
					}
				}
				$(html).appendTo("#showColumns");			
				var number = $("input[name='columnId']").length;
				$("#num_showcolumns").html(numHtml+number+"个");
			}
			art.dialog.data('columnGroupData', '');
			art.dialog.data('columnData', '');
			Win.setDataKey('columnData');//把存储key的名字默认恢复为'ChannelData'
		}


		//获取已选择节目，用于传递到弹出节目窗口内容
		function collectColumns () {		
			var dataName = 'columnData';
			var columnType = 0;	
			if($("#columnType").val()=='1'){
				dataName = 'columnGroupData';
				columnType = 1;
			}
			Win.cleanSelectChannels();
			var json = "";
			$("input[name='columnId']:checked").each(function(){
				if(json!=""){
					json += ",";
				}
				json += "{columnId: '"+$(this).val()+"',columnName:'"+$(this).next().html()+"'}";
			});
			if(json!=""){
				json = "({columnType:'"+columnType+"',items:["+json+"]})";
			}
			art.dialog.data(dataName, json);
			art.dialog.data('columnType',columnType);		
		}
 
