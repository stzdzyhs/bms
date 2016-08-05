package com.db.bms.entity;

import com.alibaba.fastjson.JSON;

public class Wordstock extends BaseModel implements java.io.Serializable {

	protected Long wordNo;

	protected String word;

	protected Long operatorNo;
	protected Operator operator;
	
	protected String createTime;

	@Override
	public boolean equals(Object o) {
		if(o==this) {
			return true;
		}
		Wordstock c = (Wordstock)o;
		
		if(this.wordNo!=null) {
			if(!this.wordNo.equals(c.wordNo)) {
				return false;
			}
		}
		else {
			if(c.wordNo!=null) {
				return false;
			}
		}
		
		if(this.word!=null) {
			if(!this.word.equals(c.word)) {
				return false;
			}
		}
		else {
			if(c.word!=null) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
	
	public static Wordstock fromString(String s) {
		Wordstock c = JSON.parseObject(s, Wordstock.class);
		return c;
	}

	@Override
	public void setDefaultNull() {
		// TODO Auto-generated method stub		
	}

	//--------------------------------------------------
	// getter and setter
	//--------------------------------------------------
	
	public Long getWordNo() {
		return wordNo;
	}

	public void setWordNo(Long wordNo) {
		this.wordNo = wordNo;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public Long getOperatorNo() {
		return operatorNo;
	}

	public void setOperatorNo(Long operatorNo) {
		this.operatorNo = operatorNo;
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}
