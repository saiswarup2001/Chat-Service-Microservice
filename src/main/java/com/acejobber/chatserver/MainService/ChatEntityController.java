package com.acejobber.chatserver.MainService;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;



@RestController
@RequestMapping("/chat")
public class ChatEntityController {

    private @Autowired ChatEntityDao chatEntityDao;

    //creating the Message
    @PostMapping("/create")
    public ChatEntity createChatEntity(@RequestBody ChatEntity chatEntity) {
        return chatEntityDao.save(chatEntity);
    }

    //Getting all the data [Messages, inboxes, conversation]
    @GetMapping("/get/message")
    public List<ChatEntity> getAllChatEntities() {
        return chatEntityDao.findAll();
    }


// @GetMapping("/get/message")
// public List<ChatEntity> getAllChatEntities() {
//     List<ChatEntity> chatEntities = chatEntityDao.findAll();
//     // Correctly stream the chatEntities list
//     return chatEntities.stream()
//                         .map(chatEntity -> modelMapper.map(chatEntity, ChatEntity.class))
//                         .collect(Collectors.toList());
// }


    //Get a perticular chat inbox using conversation id
    @GetMapping("/get/{id}")
    public ResponseEntity<ChatEntity> getChatEntityById(@PathVariable Long id) {
        ChatEntity chatEntity = chatEntityDao.findById(id);
        return ResponseEntity.ok(chatEntity);
    }
    
}
