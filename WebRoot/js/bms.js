/**
 * gtms通用和界面相关函数
 */

var ROOT=determineRoot();

function showCompanyDetailDlg(companyNo) {
	console.log("showCompanyDetailDlg ...2");
	art.dialog.open(ROOT + "opermgmt/company/companyDetail.action?companyNo=" + companyNo, {
		title: "运营商详情", 
		width: 450,
		height: 400,
		lock: true,
		close:function() {
			document.body.focus();
		}
	});
}

function showCardRegionDetailDlg(id) {
	art.dialog.open(ROOT + "opermgmt/region/cardRegionDetail.action?regionId=" + id, {
		title: "智能卡区域详情", 
		width: 450,
		height: 450,
		lock: true
	});
}

function showFeatureCodeDetailDlg(featureCodeNo) {
	art.dialog.open(ROOT + "opermgmt/featureCode/featureCodeDetail.action?featureCodeNo="+featureCodeNo, {
		title: "特征码详情", 
		width: 450,
		height: 300,
		lock: true
	});
}

function showClientDetailDlg(id){
	art.dialog.open(ROOT + "opermgmt/client/clientDetail.action?clientNo=" + id, {
		title: "CA码详情", 
		width: 450,
		height: 400,
		lock: true
	});
}

function showSpaceDetailDlg(id) {
	art.dialog.open(ROOT + "opermgmt/space/spaceDetail.action?spaceNo=" + id, {
		title: "空分组详情", 
		width: 450,
		height: 400,
		lock: true
	});
}
