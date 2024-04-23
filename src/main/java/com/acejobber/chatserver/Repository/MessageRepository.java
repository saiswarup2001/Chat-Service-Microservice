package com.acejobber.chatserver.Repository;

import com.acejobber.chatserver.Entity.Message;
import com.acejobber.chatserver.service.MessageServices;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
// import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@ComponentScan("com.acejobber.chatserver.DAO")
public class MessageRepository implements MessageServices {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void saveMessage(Message message) {
        Session session =  sessionFactory.openSession();
        Transaction transaction = null;
        try {
            
            transaction = session.beginTransaction();
            session.persist(message);
            transaction.commit();
           
        } catch (Exception e) {

            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
           
        } 
        
        // return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public Message findMessageById(Long id) {
        try {
            Session session = sessionFactory.openSession();
            return session.get(Message.class, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Message> findAllMessage() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("SELECT DISTINCT m from Message m", Message.class).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Optional<byte[]> getImagebyMessageId(Long messageId) {
        try (Session session = sessionFactory.openSession()) {
            Message message = session.get(Message.class, messageId);
            if (message != null && message.getImage() != null) {
                return Optional.of(message.getImage());
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public void saveImagebyMessageId(Long messageId, byte[] imageData) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Message message = session.get(Message.class, messageId);
            if (message != null) {
                message.setImage(imageData);
                session.update(message);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    // @Override
    // public void deleteAll() {
    // Transaction transaction = null;
    // try (Session session = sessionFactory.openSession()) {
    // transaction = session.beginTransaction();
    // session.createQuery("delete from Message").executeUpdate();
    // transaction.commit();
    // } catch (Exception e) {
    // if (transaction != null) {
    // transaction.rollback();
    // }
    // e.printStackTrace();
    // }
    // }

    // @Override
    // public void delete(Message message) {
    // Transaction transaction = null;
    // try (Session session = sessionFactory.openSession()) {
    // transaction = session.beginTransaction();
    // session.delete(message);
    // transaction.commit();
    // } catch (Exception e) {
    // if (transaction != null) {
    // transaction.rollback();
    // }
    // e.printStackTrace();
    // }
    // }

    // @Override
    // public void update(Message message) {
    // Transaction transaction = null;
    // try (Session session = sessionFactory.openSession()) {
    // transaction = session.beginTransaction();
    // session.update(message);
    // transaction.commit();
    // } catch (Exception e) {
    // if (transaction != null) {
    // transaction.rollback();
    // }
    // e.printStackTrace();
    // }
    // }
}
