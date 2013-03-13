package com.qiangwang.soeasy.api;


public interface APIListener<R, E> {

    public void onSuccess(R result);
    
    public void onError(E e);
    
}
