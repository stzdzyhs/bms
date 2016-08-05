<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>权限分配</title>
<link rel="stylesheet" href="${path}/css/common.css" type="text/css" />
<script src="${path}/js/jquery/jquery-1.6.4.min.js"></script>
<script src="${path}/js/jquery/jquery.form.js" type="text/javascript"></script>
<!-- 弹出窗控件 -->
<link rel="stylesheet" href="${path}/js/artDialog/skins/blue.css"
	type="text/css" />
<script src="${path}/js/artDialog/artDialog.js"></script>
<script src="${path}/js/artDialog/plugins/iframeTools.js"></script>

<script src="${path}/js/permission.js" type="text/javascript"></script>

<script type="text/javascript">
$(document).ready(function() {
	$("#selectAll").click(function() {//全选
        if (document.getElementById("selectAll").checked) {
			$("#options :checkbox").each(function() {
					document.getElementById($(this).attr("id")).checked = true;});
		} else {
			$("#options :checkbox").each(function() {
						document.getElementById($(this).attr("id")).checked = false;});
		}
   });
});

	function submitForm() {
		$("#form1").ajaxSubmit({
			type : "POST",
			dataType : "html",
			url : "${path}/sysmgmt/role/savePermission.action",
			beforeSend : function() {
				art.dialog.through({
					id : 'broadcastLoading',
					title : false,
					content : '<img src="${path}/images/08.gif" />',
					lock : true
				});
			},
			error : function(a, b) {
				art.dialog.list['broadcastLoading'].close();
				art.dialog.alert("保存失败！");
				return false;
			},
			success : function(data) {
				art.dialog.list['broadcastLoading'].close();
				var rsobj = eval("(" + data + ")");
				if (rsobj.result == "true") {
					art.dialog.alert("保存成功！", goBack);
				} else {
					art.dialog.alert("保存失败！");
				}
			}
		});
	}

	function selectCheckBox(panent, name) {
		if (document.getElementById(name).checked) {
			if ($("#" + name + "List :checkbox[checked]").length == 0) {
				$("#" + name + "List :checkbox").each(function() {
					document.getElementById($(this).attr("id")).checked = true;
				});
			}
			if (panent != null) {
				if (document.getElementById(panent).checked == false) {
					document.getElementById(panent).click();
				}
			}
		} else {
			$("#" + name + "List :checkbox").each(function() {
				document.getElementById($(this).attr("id")).checked = false;
			});
		}
	}

	function goBack() {
		document.getElementById("form2").action = "${path}/sysmgmt/role/roleList.action";
		document.getElementById("form2").submit();
	}
</script>
</head>
<body>
	<form id="form1" name="form1" method="post" action="">
		<input type="hidden" name="roleNo" id="roleNo" value="${role.roleNo }" />
		<div class="title">
			<h2>权限分配</h2>
		</div>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td>
					<div id="widget-docs"
						class="ui-widget ui-widget-content ui-corner-all">
						<div id="options" class="searchWrap">
							<div class="toggle-docs-links">
								<label for="selectAll">
								   <input type="checkbox" class="checkbox" id="selectAll" />全选
								</label>
                                <a href="#" class="toggle-docs-detail">显示</a>
							</div>
							<ul class="options-list">
								<c:if test="${commandList == null || fn:length(commandList) == 0}">
			                                                          抱歉，没有相关的结果。
	  	                        </c:if>
								<c:forEach items="${commandList}" var="u" varStatus="st">
									<c:forEach items="${activeList}" var="a">
										<c:if test="${a.commandNo == u.commandNo}">
											<li id="option-value" class="option">
											    <!-- 广告管理 -->
												<div class="option-header">
													<h3 class="option-name">
														<a href="#option-value">${u.commandName }</a>
													</h3>
													<dl>
														<dt class="option-default-label">
															<input type="checkbox" id="${u.commandId }"
																name="commandNo" value="${u.commandNo }"
																onclick="selectCheckBox(null, '${u.commandId }')"
																<c:forEach items="${role.commands}" var="r"><c:if test="${ r.commandNo == u.commandNo}"> checked </c:if></c:forEach> />请选择下面的子菜单:
														</dt>
													</dl>
												</div>
												<div class="option-examples" id="${u.commandId }List">
													<c:forEach items="${u.commands}" var="u1" varStatus="st1">
														<c:forEach items="${activeList}" var="a">
															<c:if test="${a.commandNo == u1.commandNo}">
															     <!-- 广告位管理 -->
																<c:if test="${u1.commandLevel == 2}">
																	<h4>
																		<label for="${u1.commandId }">
																		    <input type="checkbox" class="checkbox"
																			id="${u1.commandId }" name="commandNo"
																			value="${u1.commandNo }"
																			onclick="selectCheckBox('${u.commandId }', '${u1.commandId }')"
																			<c:forEach items="${role.commands}" var="r"><c:if test="${r.commandNo == u1.commandNo }"> checked </c:if></c:forEach> />
																			${u1.commandName}(${fn:length(u1.commands)})
																		</label>
																	</h4>
																	<!-- 广告位和广告模板 -->
																	<dl class="option-examples-list" id="${u1.commandId }List">
																		<c:forEach items="${u1.commands}" var="u2" varStatus="st2">
																			<c:forEach items="${activeList}" var="a">
																				<c:if test="${a.commandNo == u2.commandNo}">
																					<dd>
																						<label for="${u2.commandId }">
																						    <input type="checkbox" class="checkbox"
																							id="${u2.commandId }" name="commandNo"
																							value="${u2.commandNo }"
																							onclick="selectCheckBox('${u1.commandId }', '${u2.commandId }')"
																							<c:forEach items="${role.commands}" var="r"><c:if test="${ r.commandNo == u2.commandNo}"> checked </c:if></c:forEach> />
																							${u2.commandName}
																						</label>
																						<!-- 添加、修改、删除 -->
																						<div id="${u2.commandId }List">
																							<c:forEach items="${u2.commands}" var="u3" varStatus="st3">
																								<c:forEach items="${activeList}" var="a">
																									<c:if test="${a.commandNo == u3.commandNo}">
																										<label for="${u3.commandId }">
																										    <input type="checkbox" class="checkbox"
																											id="${u3.commandId }" name="commandNo"
																											value="${u3.commandNo }"
																											onclick="selectCheckBox('${u2.commandId }', '${u3.commandId }')"
																											<c:forEach items="${role.commands}" var="r"><c:if test="${ r.commandNo == u3.commandNo}"> checked </c:if></c:forEach> />
																											${u3.commandName}
																										</label>
																									</c:if>
																								</c:forEach>
																							</c:forEach>
																						</div>
																					</dd>
																				</c:if>
																			</c:forEach>
																		</c:forEach>
																	</dl>
																</c:if>
																<c:if test="${u1.commandLevel == 3}">
																	<dl class="option-examples-list">
																		<dd>
																			<label for="${u1.commandId }">
																			    <input type="checkbox" class="checkbox"
																				id="${u1.commandId }" name="commandNo"
																				value="${u1.commandNo }"
																				onclick="selectCheckBox('${u.commandId }', '${u1.commandId }')"
																				<c:forEach items="${role.commands}" var="r"><c:if test="${ r.commandNo == u1.commandNo}"> checked </c:if></c:forEach> />
																				${u1.commandName}
																			</label>
																			<div id="${u1.commandId }List">
																				<c:forEach items="${u1.commands}" var="u2" varStatus="st2">
																					<c:forEach items="${activeList}" var="a">
																						<c:if test="${a.commandNo == u2.commandNo}">
																							<label for="${u2.commandId }">
																							    <input type="checkbox" class="checkbox"
																								id="${u2.commandId }" name="commandNo"
																								value="${u2.commandNo }"
																								onclick="selectCheckBox('${u1.commandId }', '${u2.commandId }')"
																								<c:forEach items="${role.commands}" var="r"><c:if test="${ r.commandNo == u2.commandNo}"> checked </c:if></c:forEach> />
																								${u2.commandName}
																							</label>
																						</c:if>
																					</c:forEach>
																				</c:forEach>
																			</div>
																		</dd>
																	</dl>
																</c:if>
															</c:if>
														</c:forEach>
													</c:forEach>
												</div></li>
										</c:if>
									</c:forEach>
								</c:forEach>
							</ul>
						</div>
					</div>
					<div
						style="width: 100%; text-align: center; margin-top: 10px; float: left;">
						<input value="保存" type="button" class="btnQuery"
							onclick="submitForm()" /> &nbsp;&nbsp; <input value="取消"
							type="button" class="btnQuery" onClick="goBack()" />
					</div></td>
			</tr>
		</table>
	</form>
	<form id="form2" name="form2" method="post">
<!-- 缓存查询条件 start -->
<input type="hidden" name="roleId"  value="${search.roleId}" />
<input type="hidden" name="roleName"  value="${search.roleName}" />
<input type="hidden" name="status"  value="${search.status}" />
<input type="hidden" name="pageUtil.pageSize"  value="${search.pageUtil.pageSize}" />
<input type="hidden" name="pageUtil.pageId"  value="${search.pageUtil.pageId}" />
<!-- 缓存查询条件 end -->
</form>
</body>
</html>
