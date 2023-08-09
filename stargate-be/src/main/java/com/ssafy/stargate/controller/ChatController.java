package com.ssafy.stargate.controller;




import com.ssafy.stargate.exception.NotFoundException;
import com.ssafy.stargate.model.dto.common.ChatMessageDto;
import com.ssafy.stargate.model.dto.request.ChattingRoomRequestDto;
import com.ssafy.stargate.model.dto.response.ChattingRoomResponseDto;
import com.ssafy.stargate.model.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping("/new-room")
    public ResponseEntity<ChattingRoomResponseDto> createNewRoom(@RequestBody ChattingRoomRequestDto dto) {
        return ResponseEntity.ok(chatService.createChattingRoom(dto));
    }

    @PutMapping("/update-room")
    public ResponseEntity<ChattingRoomResponseDto> updateChatRoom(@RequestBody ChattingRoomRequestDto dto) throws NotFoundException {
        return ResponseEntity.ok(chatService.updateChattingRoom(dto));
    }


    @GetMapping("/rooms")
    public ResponseEntity<List<ChattingRoomResponseDto>> getAllRooms () {
        return ResponseEntity.ok(chatService.getAllChattingRoom());
    }

    @GetMapping("/messages")
    public ResponseEntity<List<ChatMessageDto>> getMessages (@RequestBody ChattingRoomRequestDto dto){
        return ResponseEntity.ok(chatService.getChattingMessages(dto));
    }

    // requestmapping 과 비슷
    @MessageMapping("/{roomNo}")  //app/chat/{roomNo}
    @SendTo("/topic/chat/{roomNo}")    // handler 에서 처리 마친 것을 topic/chat 경로로 전송
    public void message (@Payload ChatMessageDto message, @DestinationVariable(value = "roomNo") Long roomNo){
        chatService.sendMessage(message, roomNo);
    }

    @DeleteMapping("/message")
    public ResponseEntity<?> deleteMessage (@RequestBody ChatMessageDto dto){
        chatService.deleteMessage(dto);
        return ResponseEntity.ok(null);
    }

    @PutMapping("/message")
    public ResponseEntity<?> updateMessage (@RequestBody ChatMessageDto dto){
        return ResponseEntity.ok(chatService.updateMessage(dto));
    }
}
