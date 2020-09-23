package com.github.cookiesjuice.response;

import org.jetbrains.annotations.Nullable;

public class Message {
    private final MessageContent head;
    private MessageContent tail;

    public Message() {
        this.head = new MessageContent();
        this.tail = this.head;
    }

    @Nullable
    public MessageContent getHead() {
        return head.getNext();
    }

    public Message put(MessageContent newContent) {
        this.tail.setNext(newContent);
        this.tail = newContent;
        return this;
    }
}
