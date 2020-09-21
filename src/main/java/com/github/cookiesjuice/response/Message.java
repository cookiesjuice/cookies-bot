package com.github.cookiesjuice.response;

import org.jetbrains.annotations.Nullable;

import java.io.File;

public class Message {
    private final MessageContent head;
    private MessageContent tail;
    public Message(){
        this.head = new MessageContent();
        this.tail = this.head;
    }

    @Nullable
    public MessageContent getHead() {
        return head.getNext();
    }

    public Message put(String content){
        MessageContent newContent = new MessageContent(content);
        this.tail.setNext(newContent);
        this.tail = newContent;
        return this;
    }

    public Message put(File content){
        MessageContent newContent = new MessageContent(content);
        this.tail.setNext(newContent);
        this.tail = newContent;
        return this;
    }
}
