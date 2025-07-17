package com.example.demo.service.webSocket;

public interface IStompMessagingService {
    void sendMessage(String destination, Object payload);

    void sendToUser(String username, String destination, Object payload);
}
