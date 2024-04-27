package com.acejobber.chatserver.Repository;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.acejobber.chatserver.Entity.Conversation;


@Repository
public interface ConversationRepo {

    void saveConversation(Conversation conversation);
    Conversation findMessageByConversationId(Long id);
    List<Conversation> findAllconversation();
    // void deleteAll();
    // void delete(Conversation conversation);
    // void update(Conversation conversation);
    
}
