package com.acejobber.chatserver.Services;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.acejobber.chatserver.Entity.Conversation;
import com.acejobber.chatserver.Repository.ConversationRepo;

@Service
public class ConversationService implements ConversationRepo {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional
    public void saveConversation(Conversation conversation) {
        Transaction transaction = null;
        Session session = sessionFactory.openSession();
        try {
            transaction = session.beginTransaction();
            session.save(conversation);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public Conversation findMessageByConversationId(Long id) {
        Session session = sessionFactory.openSession();
        try {
            
            return session.createQuery("select distinct c from Conversation c where c.cid = :id", Conversation.class)
                .setParameter("id", id)
                    .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    @Transactional
    public List<Conversation> findAllconversation() {
        Session session = sessionFactory.openSession();
        try {
            return session.createQuery("SELECT DISTINCT c FROM Conversation c", Conversation.class).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}