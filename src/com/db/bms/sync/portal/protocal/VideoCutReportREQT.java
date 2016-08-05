
package com.db.bms.sync.portal.protocal;

import com.db.bms.utils.StringUtils;


public class VideoCutReportREQT {

	private String serialNo;

	private String assetId;

	private String resultCode;

	private String resultDesc;

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

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultDesc() {
		return resultDesc;
	}

	public void setResultDesc(String resultDesc) {
		this.resultDesc = resultDesc;
	}

	@Override
	public String toString() {
		return "VideoCutReportREQT [serialNo=" + this.serialNo + ", assetId="
				+ this.assetId + ", resultCode=" + this.resultCode
				+ ", resultDesc=" + this.resultDesc + "]";
	}

	public VideoCutReportRESP checkData() {
		VideoCutReportRESP response = new VideoCutReportRESP();
		response.setSerialNo(this.getSerialNo());
		response.setAssetId(this.getAssetId());
		response.setResultCode(CommonResultCode.SUCCESS.getResultCode());
		response.setResultDesc("Check data successfully.");

		if (StringUtils.isEmpty(this.serialNo)) {
			response.setResultCode(CommonResultCode.INVALID_PARAM
					.getResultCode());
			response.setResultDesc("The [serialNo] parameter is invalid.");
			return response;
		}

		if (StringUtils.isEmpty(this.assetId)) {
			response.setResultCode(CommonResultCode.INVALID_PARAM
					.getResultCode());
			response.setResultDesc("The [assetId] parameter is invalid.");
			return response;
		}

		if (StringUtils.isEmpty(this.resultCode)) {
			response.setResultCode(CommonResultCode.INVALID_PARAM
					.getResultCode());
			response.setResultDesc("The [resultCode] parameter is invalid.");
			return response;
		}
		return response;

	}
	
	public enum ReportStatusCode {
		ASSET_ID_ERROR("100001"), SOURCE_URL_ERROR("100002"), WIDTH_ERROR("100003"), HEIGHT_ERROR("100004"), INTERVAL_ERROR("1000005"),
		REPORT_URL_ERROR("100006"),INJECT_IMAGE_URL_ERROR("100007"),DOWNLOAD_FILE_FAILED("100008"),VIDEO_FILE_FORMAT_ERROR("100009")
		,CAPTURE_IMAGE_ERROR("100010"),CAPTURE_IMAGE_FAILED("100011"),DOWNLOADING_VIDEO("800001"),DOWNLOADING_VIDEO_SUCCESS("800002")
		,CAPTURE_IAMGE_SUCCESS("800003"),CAPTURE_IMAGE_FINISHED("800000"),OTHER("000009");
		
		private final String resultCode;

		private ReportStatusCode(String resultCode) {
			this.resultCode = resultCode;
		}

		public String getResultCode() {
			return resultCode;
		}

		public static ReportStatusCode getResultCode(String resultCode) {
			if ("100001".equals(resultCode)){
				return ASSET_ID_ERROR;
			}else if ("100002".equals(resultCode)){
				return SOURCE_URL_ERROR;
			}else if ("100003".equals(resultCode)){
				return WIDTH_ERROR;
			}else if ("100004".equals(resultCode)){
				return HEIGHT_ERROR;
			}else if ("1000005".equals(resultCode)){
				return INTERVAL_ERROR;
			}else if ("100006".equals(resultCode)){
				return REPORT_URL_ERROR;
			}else if ("100007".equals(resultCode)){
				return INJECT_IMAGE_URL_ERROR;
			}else if ("100008".equals(resultCode)){
				return DOWNLOAD_FILE_FAILED;
			}else if ("100009".equals(resultCode)){
				return VIDEO_FILE_FORMAT_ERROR;
			}else if ("100010".equals(resultCode)){
				return CAPTURE_IMAGE_ERROR;
			}else if ("100011".equals(resultCode)){
				return CAPTURE_IMAGE_FAILED;
			}else if ("800001".equals(resultCode)){
				return DOWNLOADING_VIDEO;
			}else if ("800002".equals(resultCode)){
				return DOWNLOADING_VIDEO_SUCCESS;
			}else if ("800003".equals(resultCode)){
				return CAPTURE_IAMGE_SUCCESS;
			}else if ("800000".equals(resultCode)){
				return CAPTURE_IMAGE_FINISHED;
			}
			
			return OTHER;
		}
	}
}
