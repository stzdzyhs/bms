package com.db.bms.sync.portal.protocal;




public enum CommonResultCode {
	SUCCESS("000000"), INVALID_PARAM("000001"), JSON_STRUCTURE_ERROR("000002"), JSON_PARSE_ERROR("000003"), COMMUNICATION_EXCEPTION("000004"),
	NOT_FOUND_SYSTEM("000005"),NO_ACCESS_RIGHTS("000006"),NOT_FOUND_ASSET_ID("000007"),OTHER("000009");
	
	private final String resultCode;

	private CommonResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultCode() {
		return resultCode;
	}

	public static CommonResultCode getResultCode(String resultCode) {
		if ("000000".equals(resultCode)){
			return SUCCESS;
		}else if ("000001".equals(resultCode)){
			return INVALID_PARAM;
		}else if ("000002".equals(resultCode)){
			return JSON_STRUCTURE_ERROR;
		}else if ("000003".equals(resultCode)){
			return JSON_PARSE_ERROR;
		}else if ("000004".equals(resultCode)){
			return COMMUNICATION_EXCEPTION;
		}else if ("000005".equals(resultCode)){
			return NOT_FOUND_SYSTEM;
		}else if ("000006".equals(resultCode)){
			return NO_ACCESS_RIGHTS;
		}else if ("000007".equals(resultCode)){
			return NOT_FOUND_ASSET_ID;
		}
		
		return OTHER;
	}
}
