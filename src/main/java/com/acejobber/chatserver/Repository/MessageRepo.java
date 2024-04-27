package com.acejobber.chatserver.Repository;

import com.acejobber.chatserver.Entity.Message;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;


@Repository
public interface MessageRepo {

    void saveMessage(Message message);
    Message findMessageById(Long id);
    List<Message> findAllMessage();
    void saveImagebyMessageId(Long messageId, byte[] imageData);
    Optional<byte[]> getImagebyMessageId(Long messageId);
    void saveFileDataByMessageId(Long messageId, byte[] fileData); 
    Optional<byte[]> getFileDataByMessageId(Long messageId); 
    // void deleteAll();
    // void delete(Message message);
    // void update(Message message);
    
}

