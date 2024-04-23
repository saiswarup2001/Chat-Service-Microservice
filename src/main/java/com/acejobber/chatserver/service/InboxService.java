package com.acejobber.chatserver.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.acejobber.chatserver.Entity.MessageInbox;

@Service
public interface InboxService {

    void saveInbox(MessageInbox messageInbox); 
    MessageInbox findInboxById(Long id);
    List<MessageInbox> findAllInboxes();
    // void deleteAll();
    // void update(MessageInbox messageInbox);
    // void delete(MessageInbox messageInbox);
}
