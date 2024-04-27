package com.acejobber.chatserver.Repository;

import java.util.List;

import org.springframework.stereotype.Repository;
import com.acejobber.chatserver.Entity.MessageInbox;

@Repository
public interface InboxRepo {

    void saveInbox(MessageInbox messageInbox); 
    MessageInbox findInboxById(Long id);
    List<MessageInbox> findAllInboxes();
    // void deleteAll();
    // void update(MessageInbox messageInbox);
    // void delete(MessageInbox messageInbox);
}
