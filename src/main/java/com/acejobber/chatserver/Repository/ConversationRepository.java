package com.acejobber.chatserver.Repository;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.acejobber.chatserver.Entity.Conversation;
import com.acejobber.chatserver.service.ConversationService;

@Repository
public class ConversationRepository implements ConversationService{
    
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void saveConversation(Conversation conversation) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
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
    public Conversation findMessageByConversationId(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("select distinct c from Conversation c where c.cid = :id", Conversation.class)
                .setParameter("id", id)
                .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Conversation> findAllconversation() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("SELECT DISTINCT c FROM Conversation c", Conversation.class).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // @Override
    // public void update(Conversation conversation) {
    //     Transaction transaction = null;
    //     try (Session session = sessionFactory.openSession()) {
    //         transaction = session.beginTransaction();
    //         session.update(conversation);
    //         transaction.commit();
    //     } catch (Exception e) {
    //         if (transaction != null) {
    //             transaction.rollback();
    //         }
    //         e.printStackTrace();
    //     }
    // }

    // @Override
    // public void delete(Conversation conversation) {
    //     Transaction transaction = null;
    //     try (Session session = sessionFactory.openSession()) {
    //         transaction = session.beginTransaction();
    //         session.delete(conversation);
    //         transaction.commit();
    //     } catch (Exception e) {
    //         if (transaction != null) {
    //             transaction.rollback();
    //         }
    //         e.printStackTrace();
    //     }
    // }

    

    // @Override
    // public void deleteAll() {
    //     Transaction transaction = null;
    //     try (Session session = sessionFactory.openSession()) {
    //         transaction = session.beginTransaction();
    //         session.createQuery("delete from Conversation").executeUpdate();
    //         transaction.commit();
    //     } catch (Exception e) {
    //         if (transaction != null) {
    //             transaction.rollback();
    //         }
    //         e.printStackTrace();
    //     }
    // }

}