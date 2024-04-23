package com.acejobber.chatserver.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.acejobber.chatserver.Entity.Conversation;


@Service
public interface ConversationService {

    void saveConversation(Conversation conversation);
    Conversation findMessageByConversationId(Long id);
    List<Conversation> findAllconversation();
    // void deleteAll();
    // void delete(Conversation conversation);
    // void update(Conversation conversation);
    
}
