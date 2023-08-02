package com.ssafy.stargate.controller;


import com.ssafy.stargate.model.dto.common.ChatDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j
@RequiredArgsConstructor
public class ChatController {

    @Autowired
    private final SimpMessageSendingOperations sendingOperations;


    @MessageMapping("/chat/message")
    @SendTo("/sub/chat/message")
    public void message(ChatDto message){
        sendingOperations.convertAndSend("sub/chat/message/" + message.getRoomNo(), message);
    }


}