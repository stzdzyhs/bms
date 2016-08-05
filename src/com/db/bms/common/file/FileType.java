package com.db.bms.common.file;


public enum FileType {
	
	Excel(".xls"), ExcelX(".xlsx"), Text(".txt");
	
	private final String type;

	private FileType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public static FileType getType(String type) {
		 type = type.toLowerCase();
		 if(type.endsWith(".txt"))
			 return Text;
		 if(type.endsWith(".xls"))
			 return Excel;
		 if(type.endsWith(".xlsx"))
			 return ExcelX;
		 return Text;
	}
}
