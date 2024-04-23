package com.acejobber.chatserver.MainService;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;


public class ChatEntityRepo implements ChatEntityServices {

    @Autowired
    private ChatEntityDao chatEntityDao;

    @Override
    @Transactional
    public ChatEntity save(ChatEntity chatEntity) {
        return chatEntityDao.save(chatEntity);
    }

    @Override
    @Transactional
    public List<ChatEntity> findAll() {
        return chatEntityDao.findAll();
    }

    @Override
    @Transactional
    public ChatEntity findById(Long id) {
        return chatEntityDao.findById(id);
    }
    
}
