package com.acejobber.chatserver.MainService;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface ChatEntityServices {

    ChatEntity save(ChatEntity chatEntity);
    List<ChatEntity> findAll();
    ChatEntity findById(Long id);
    
}
