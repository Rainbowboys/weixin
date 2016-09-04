package com.weixin.entity;

public class Weather {
	private int errNum;
	private String errMsg;
	private Data retData;

	public int getErrNum() {
		return errNum;
	}

	public void setErrNum(int errNum) {
		this.errNum = errNum;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public Data getRetData() {
		return retData;
	}

	public void setRetData(Data retData) {
		this.retData = retData;
	}

}
