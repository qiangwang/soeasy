package com.qiangwang.soeasy.message;

import com.qiangwang.soeasy.account.Account;

public class NewsMessage {
    
    private Account owner;
    private long id;
    private Account author;
    private String content;
    private String createdTime;
    private int commentsCount;
    
    public NewsMessage(Account owner, long id, Account author, String content, String createdTime, int commentsCount) {
        this.owner = owner;
        this.id = id;
        this.author = author;
        this.content = content;
        this.createdTime = createdTime;
        this.commentsCount = commentsCount;
    }
    
    public Account getOwner() {
        return owner;
    }
    public void setOwner(Account owner) {
        this.owner = owner;
    }
    
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    
    public Account getAuthor() {
        return author;
    }
    public void setAuthor(Account author) {
        this.author = author;
    }
    
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    
    public String getCreatedTime() {
        return createdTime;
    }
    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }
    
    public int getCommentsCount() {
        return commentsCount;
    }
    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }
}
