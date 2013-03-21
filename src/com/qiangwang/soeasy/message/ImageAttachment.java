package com.qiangwang.soeasy.message;

public class ImageAttachment extends Attachment {

	private String smallPicUrl;
	
	public ImageAttachment(String smallPicUrl){
		this.setSmallPicUrl(smallPicUrl);
	}

	public String getSmallPicUrl() {
		return smallPicUrl;
	}

	public void setSmallPicUrl(String smallPicUrl) {
		this.smallPicUrl = smallPicUrl;
	}
}
