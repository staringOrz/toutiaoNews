package com.xijian.toutiao.aysnc;

public enum EventType {
    LIKE(0),
    DILIKE(1),
    MAIL(2),
    Message(3);
    int value;
     EventType(int value){
        this.value=value;
    }

    public int getValue() {
        return value;
    }
}
