package com.weixin.entity;

public class TextMessage extends BaseMessage {

	private String Content;// �ı���Ϣ����
	private long MsgId;// ��Ϣid��64λ����

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	public long getMsgId() {
		return MsgId;
	}

	public void setMsgId(long msgId) {
		MsgId = msgId;
	}

}
