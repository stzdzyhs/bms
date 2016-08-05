<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>投放策略列表</title>
<link rel="stylesheet" href="${path}/css/themes/cupertino/jquery-ui.css" type="text/css" />
<link rel="stylesheet" href="${path}/css/common.css" type="text/css" />
<script src="${path}/js/jquery/jquery-1.6.4.min.js"></script>
<script src="${path}/js/jquery/jquery.form.js" type="text/javascript"></script>
<!-- 弹出窗控件 -->
<link rel="stylesheet" href="${path}/js/artDialog/skins/blue.css" type="text/css" />
<script src="${path}/js/artDialog/artDialog.js"></script>
<script src="${path}/js/artDialog/plugins/iframeTools.js"></script>

<script src="${path}/js/checkbox.js"></script>
<script src="${path}/js/opermgmt/strategy/strategy.js"></script>
<script type="text/javascript">

function selectDisp(){	
	var ids = "";
	var names = "";
	var i = 0 ; 
	var html = "";
	$("input[name='strategyIds']").each(function(){
		if(this.checked){
			ids += this.value+",";
			names += $(this).parent().next().html()+",";
			html += "<input type='checkbox' checked value='"+this.value+"' class='checkbox' name='strategyId' id='strategyId' />"+$(this).parent().next().html();			
	    i++;
	     }         
			
		});
	
	art.dialog.data('html', html);//
	art.dialog.data('num', i);//总数	
	artDialog.close();
}


//详情
/* function toView(id){
	art.dialog.open("${path}/opermgmt/dispatch/dispatchView.action?id="+id, 
			{
				title: "策略详情", //'请选择频道'
				width: 800,
				height: 650,
				lock: true
			});
}

//确定
function selectDisp(){
	var ids = "";
	var names = "";
	var i = 0 ; 
	var html = "";
	var strategyIds=art.dialog.data('selectedStrategyId');
	for(var i=0;i<strategyIds.length;i++){		
			ids += strategyIds[i]+",";
			names += art.dialog.data(strategyIds[i])+",";
			html += "<input type='checkbox' checked value='"+strategyIds[i]+"' class='checkbox' name='strategyId' id='strategyId' />"+art.dialog.data(strategyIds[i]);			
	}
	art.dialog.data('html', html);//
	art.dialog.data('num', i);//总数	
	artDialog.close();
}

var selectedStrategyId= art.dialog.data('selectedStrategyId');
var strategyTypes=art.dialog.data('strategyTypes');
function selectOne(b){
	if(strategyTypes==''||strategyTypes==undefined){
		strategyTypes=new Array();
	}
	var type=$("#"+b.value+"_type").val();		
	var typeLen=strategyTypes.indexOf(type);		
		if(selectedStrategyId==''){
			selectedStrategyId=new Array();
		}			
		if(b.checked){	
			if(typeLen>=0){
				art.dialog.alert("不能选择相同类型的策略！");
				b.checked=false;
			}else{
				selectedStrategyId.push(b.value);	
				strategyTypes.push(type);
				art.dialog.data(b.value,$("#"+b.value+"_name").val());	
			}				
		}else{		
			var length=selectedStrategyId.indexOf(b.value);				
			if(length>=0){
				selectedStrategyId.splice(length, length+1);				
			}
			if(typeLen>=0){
				strategyTypes.splice(typeLen, typeLen+1);
			}
			
		} 
		art.dialog.data('selectedStrategyId', selectedStrategyId);//
		art.dialog.data('strategyTypes', strategyTypes);//	
}

function selectAll(b){
	if(selectedStrategyId==''){
		selectedStrategyId=new Array();
	}
	if(strategyTypes==''||strategyTypes==undefined){
		strategyTypes=new Array();
	}
	if(b.checked){					
		$("input[name='strategyIds']").each(function(){
			    var type=$("#"+this.value+"_type").val();
			    var typeLen=strategyTypes.indexOf(type);
			    if(typeLen>=0){					
					this.checked=false;
				}else{
					var length=selectedStrategyId.indexOf(this.value);
				    if(length<0){
					selectedStrategyId.push(this.value);
					strategyTypes.push(type);
					art.dialog.data(this.value,$("#"+this.value+"_name").val());
				    }
				}			    
		});
	}else{				
		$("input[name='strategyIds']").each(function(){
			var length=selectedStrategyId.indexOf(this.value);
			if(length>=0){
				selectedStrategyId.splice(length, length+1);	
				 var type=$("#"+this.value+"_type").val();
				 var typeLen=strategyTypes.indexOf(type);
				 if(typeLen>=0){
						strategyTypes.splice(typeLen, typeLen+1);
					}
			}
			
	});
	}
	art.dialog.data('selectedStrategyId', selectedStrategyId);
	art.dialog.data('strategyTypes', strategyTypes);//
}


$(document).ready(function(){
	
	if(selectedStrategyId==''){
		selectedStrategyId=new Array();
	}
	var a=0;var b=0;
	$("input[name='strategyIds']").each(function(){	
		a=a+1;
		var length=selectedStrategyId.indexOf(this.value);
		if(length>=0){			
			this.checked=true;	
			var type=$("#"+this.value+"_type").val();
		    strategyTypes.push(type)
			b=b+1;
		}
	});	
	if(a==b){		
		$("#pDel").each(function(){	
			this.checked=true;
		});
	}
});
*/
</script>
</head>
<body>
<div class="title">
  <h2>投放策略列表</h2>
</div>
<form action="${path}/opermgmt/strategy/selectStrategyList.action" method="post" name="strategyForm" id="strategyForm">
<input type="hidden" name="strategyId" id="strategyId" value="" />
  <div class="searchWrap">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tbody>
        <tr>
          <td class="searchLeft"></td>
          <td class="searchBg"><table class="search" id="searchb" width="100%" border="0" cellspacing="0" cellpadding="0">
              <tbody>
                <tr>
                  <td width="90px" height="30">策略名称：</td>
                  <td width="160">
                  	<input name="strategyName" class="form120px" value="${search.strategyName}" maxlength="50"/>
                  </td>
                  <td width="90px" height="30">策略类型：</td>
                  <td width="160">
                  <select name="strategyType" class="form130px"  >
                  		<option value="">--请选择--</option>
					  	<c:forEach items="${strategyTypeMap}" var="t" > 
							<option value="${t.key }" <c:if test="${search.strategyType==t.key }"> selected="selected" </c:if>>${t.value }</option>
				 		</c:forEach> 
				  </select>
                  </td>
                   <c:if test="${activeOperator.type == 0 }">
                  <td width="90px" height="30">所属运营商：</td>
                  <td width="160">
                  <select name="searchCompanyNo" class="form130px"  >
                  		<option value="">--请选择--</option>
					  	<c:forEach items="${companyList}" var="c" > 
							<option value="${c.companyNo }" <c:if test="${search.searchCompanyNo==c.companyNo }"> selected="selected" </c:if>>${c.companyName }</option>
				 		</c:forEach> 
				  </select>
                  </td>
                  <td width="90px" height="30">创建用户：</td>
                  <td width="160">
                  <select name="searchOperatorNo" class="form130px"  >
                  		<option value="">--请选择--</option>
					  	<c:forEach items="${operatorList}" var="o" > 
							<option value="${o.operatorNo }" <c:if test="${search.searchOperatorNo==o.operatorNo }"> selected="selected" </c:if>>${o.operatorName }</option>
				 		</c:forEach> 
				  </select>
                  </td>
                  </c:if>
                  <td><input class="btnQuery" name="" type="submit" value="查询"></td>
                </tr>
              </tbody>
            </table></td>
          <td class="searchRight"></td>
        </tr>
        <tr>
          <td class="searchButtomLeft"></td>
          <td class="searchButtom"></td>
          <td class="searchButtoRight"></td>
        </tr>
      </tbody>
    </table>
  </div>
  <!--标题 end-->
  <div class="toolBar">
    <jsp:include page="/views/common/pageTemplate.jsp" />
    <!--翻页 end-->
    <!--对表格数据的操作 end-->
  </div>
  <!--工具栏 end-->
  <div class="tableWrap">
    <table class="tableCommon tableInterlace" width="100%" border="0" cellspacing="0" cellpadding="0">
      <thead>
        <tr>
          <th width="5px"></th>
          <th width="*%">策略名称</th>
        <!--   <th width="*%">策略类型</th>
          <th width="*%">策略分类</th> -->
          <th width="*%">所属运营商</th>
          <th width="*%">创建用户</th>
          <th width="*%">创建时间</th>
          <th width="*%">更新时间</th>
          
        </tr>
      </thead>
      <tbody>
      <c:if test="${list == null || fn:length(list) == 0}">
		<tr>
			<td colspan="8">
				抱歉，没有相关的结果。
			</td>
		</tr>
	  </c:if>
      <c:forEach items="${list}" var="s">
      <input type="hidden" name="isShare${s.id}" id="isShare${s.id}" value="${s.isShare}" />
      <input type="hidden" name="permissions${s.id}" id="permissions${s.id}" value="${s.permissions}" />
        <tr>
          <td align="center">
	         <input type="radio" name="strategyIds" value="${s.id }">
          </td>
          <td align="center">
            <c:choose>
             <c:when test="${fn:length(s.strategyName) > 10}">
                 ${fn:substring(s.strategyName, 0, 10)}...
             </c:when>
             <c:otherwise>
                 ${s.strategyName }               
             </c:otherwise>
            </c:choose> 
             <input type="hidden" id="${s.id}_name" value="${s.strategyName}">
          </td>
      <%--     <td align="center">
            <c:forEach items="${strategyTypeMap}" var="t" > 
					<c:if test="${s.strategyType==t.key }"> ${t.value }</c:if>
		    </c:forEach> 
		    <input type="hidden" id="${s.id}_type" value="${s.strategyType}">
          </td>
          <td align="center">
             <c:if test="${s.strategyType == 3}">
                <c:forEach items="${timeStrategyChildTypeMap}" var="t" > 
					<c:if test="${s.childType==t.key }"> ${t.value }</c:if>
		        </c:forEach>                    
             </c:if>
             <c:if test="${s.strategyType == 4}">
               <c:forEach items="${frequencyStrategyChildTypeMap}" var="t" > 
					<c:if test="${s.childType==t.key }"> ${t.value }</c:if>
		        </c:forEach>  
             </c:if>
          </td> --%>
          <td align="center">${s.company.companyName }</td>
          <td align="center">${s.operator.operatorName }</td>
          <td align="center">${s.createTime }</td>
          <td align="center">${s.updateTime }</td>
    
        </tr>
      </c:forEach>
      </tbody>
    </table>
  </div>
  </form>
 <div style="width:100%; text-align:center; margin-top:10px;">
<input value="确定" type="button" class="btnQuery" onClick="selectDisp()" />
<input value="关闭" type="button" class="btnQuery" onClick="javascript:artDialog.close()" />
</div>
  </div>
</body>
</html>