
package com.db.bms.sync.portal.protocal;

import java.util.List;

import com.db.bms.utils.Regexs;
import com.db.bms.utils.RegexsUtils;
import com.db.bms.utils.StringUtils;


public class InjectVideoImageREQT {

	private String serialNo;

	private String assetId;

	private String assetTitle;

	private List<ImageInfo> imageList;

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getAssetId() {
		return assetId;
	}

	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}

	public String getAssetTitle() {
		return assetTitle;
	}

	public void setAssetTitle(String assetTitle) {
		this.assetTitle = assetTitle;
	}

	public List<ImageInfo> getImageList() {
		return imageList;
	}

	public void setImageList(List<ImageInfo> imageList) {
		this.imageList = imageList;
	}

	@Override
	public String toString() {
		return "InjectVideoImageREQT [serialNo=" + this.serialNo + ", assetId="
		+ this.assetId + ", assetTitle=" + this.assetTitle  + ", imageList=" + this.imageList.size()  + "]";
	}
	
	public InjectVideoImageRESP checkData() {
		InjectVideoImageRESP response = new InjectVideoImageRESP();
		response.setSerialNo(this.getSerialNo());
		response.setAssetId(this.getAssetId());
		response.setResultCode(CommonResultCode.SUCCESS.getResultCode());
		response.setResultDesc("Check data successfully.");
		
		if (StringUtils.isEmpty(this.serialNo)){
			response.setResultCode(CommonResultCode.INVALID_PARAM.getResultCode());
			response.setResultDesc("The [serialNo] parameter is empty.");
			return response;
		}
		
		if (StringUtils.isEmpty(this.assetId)){
			response.setResultCode(CommonResultCode.INVALID_PARAM.getResultCode());
			response.setResultDesc("The [assetId] parameter is empty.");
			return response;
		}
		
		if (this.assetId.length() > 20){
			response.setResultCode(CommonResultCode.INVALID_PARAM.getResultCode());
			response.setResultDesc("The [assetId] parameter length is too long.");
			return response;
		}
		
		if (StringUtils.isEmpty(this.assetTitle)){
			response.setResultCode(CommonResultCode.INVALID_PARAM.getResultCode());
			response.setResultDesc("The [assetTitle] parameter is empty.");
			return response;
		}
		
		if (this.assetTitle.length() > 30){
			response.setResultCode(CommonResultCode.INVALID_PARAM.getResultCode());
			response.setResultDesc("The [assetTitle] parameter length is too long.");
			return response;
		}
		
		if (!RegexsUtils.checkData(this.assetTitle, Regexs.ASSET_TITLE)){
			response.setResultCode(CommonResultCode.INVALID_PARAM.getResultCode());
			response.setResultDesc("The [assetTitle] parameter format error.");
			return response;
		}
		
		return response;
		
	}
}
