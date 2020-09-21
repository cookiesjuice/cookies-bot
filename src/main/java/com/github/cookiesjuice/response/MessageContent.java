package com.github.cookiesjuice.response;

import lombok.Getter;
import lombok.Setter;

import java.io.File;

public class MessageContent {

    @Setter @Getter
    private MessageContent next;

    @Getter
    private final Object content;

    @Getter
    private final MessageType messageType;

    public MessageContent(){
        this.content = null;
        this.messageType = MessageType.END;
    }

    public MessageContent(String content){
        this.content = content;
        this.messageType = MessageType.STRING;
    }

    public MessageContent(File file){
        this.content = file;
        this.messageType = MessageType.FILE;
    }

    public enum MessageType{
        STRING, FILE, END
    }
}
