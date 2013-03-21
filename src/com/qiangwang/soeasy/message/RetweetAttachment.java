package com.qiangwang.soeasy.message;

public class RetweetAttachment extends Attachment {

	private NewsMessage retweeted;

	public RetweetAttachment(NewsMessage retweeted) {
		this.retweeted = retweeted;
	}

	public NewsMessage getRetweeted() {
		return retweeted;
	}

	public void setRetweeted(NewsMessage retweeted) {
		this.retweeted = retweeted;
	}
	
	
}
