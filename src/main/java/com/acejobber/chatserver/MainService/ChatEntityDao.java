package com.acejobber.chatserver.MainService;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;




@Component
public class ChatEntityDao implements ChatEntityServices {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public ChatEntity save(ChatEntity chatEntity) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(chatEntity);
            transaction.commit();
            session.close();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return chatEntity;
    }

    @Override
    public List<ChatEntity> findAll() {
        List<ChatEntity> chatEntities = null;
        try (Session session = sessionFactory.openSession()) {
            chatEntities = session.createQuery("FROM ChatEntity", ChatEntity.class).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chatEntities;
    }

    @Override
    public ChatEntity findById(Long id) {
        ChatEntity chatEntity = null;
        try (Session session = sessionFactory.openSession()) {
            chatEntity = session.get(ChatEntity.class, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chatEntity;
    }

    
}
