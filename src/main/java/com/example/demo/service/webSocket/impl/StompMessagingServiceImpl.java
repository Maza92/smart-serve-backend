package com.example.demo.service.webSocket.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.example.demo.service.webSocket.IStompMessagingService;

@Service
@RequiredArgsConstructor
@Slf4j
public class StompMessagingServiceImpl implements IStompMessagingService {

    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void sendMessage(String destination, Object payload) {
        log.info("Sending message to destination {}: {}", destination, payload);
        messagingTemplate.convertAndSend(destination, payload);
    }

    @Override
    public void sendToUser(String username, String destination, Object payload) {
        log.info("Sending message to user '{}' at destination {}: {}", username, destination, payload);
        messagingTemplate.convertAndSendToUser(username, destination, payload);
    }
}
