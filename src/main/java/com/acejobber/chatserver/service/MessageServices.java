package com.acejobber.chatserver.service;

import com.acejobber.chatserver.Entity.Message;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public interface MessageServices {

    void saveMessage(Message message);
    Message findMessageById(Long id);
    List<Message> findAllMessage();
    void saveImagebyMessageId(Long messageId, byte[] imageData);
    Optional<byte[]> getImagebyMessageId(Long messageId);
    // void deleteAll();
    // void delete(Message message);
    // void update(Message message);
    
}

