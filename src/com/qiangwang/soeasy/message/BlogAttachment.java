package com.qiangwang.soeasy.message;

public class BlogAttachment extends Attachment {

	private String title;
	private String summary;
	private String href;

	public BlogAttachment(String title, String summary, String href) {
		this.title = title;
		this.summary = summary;
		this.href = href;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

}
