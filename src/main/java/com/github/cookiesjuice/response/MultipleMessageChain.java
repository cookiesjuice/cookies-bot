package com.github.cookiesjuice.response;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class MultipleMessageChain {
    private MultipleMessageChain next;
    private final CompletableFuture<Message> value;

    public MultipleMessageChain(){
        this.value = new CompletableFuture<>();
    }

    public MultipleMessageChain put(Message message){
        this.value.complete(message);
        this.next = new MultipleMessageChain();
        return this.next;
    }

    public void end(Message message) {
        this.value.complete(message);
    }

    public MultipleMessageChain then(MessageHandler handler) throws ExecutionException, InterruptedException {
        Message message = value.get();
        handler.handle(message);
        return this.next;
    }

    public boolean hasEnded() throws ExecutionException, InterruptedException {
        value.get();
        return this.next == null;
    }
}
