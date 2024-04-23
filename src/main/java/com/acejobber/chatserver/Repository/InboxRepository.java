package com.acejobber.chatserver.Repository;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.acejobber.chatserver.Entity.MessageInbox;
import com.acejobber.chatserver.service.InboxService;

@Repository
public class InboxRepository implements InboxService {

    private @Autowired SessionFactory sessionFactory;

    @Override
    public void saveInbox(MessageInbox messageInbox) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.save(messageInbox);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            if (session != null) {
                session.close();
            }
            e.printStackTrace();
        }finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public MessageInbox findInboxById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(MessageInbox.class, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<MessageInbox> findAllInboxes() {
        try (Session session = sessionFactory.openSession()) {
            List<MessageInbox> inboxDetails = session
                    .createQuery("SELECT DISTINCT mi FROM MessageInbox mi", MessageInbox.class).list();
            return inboxDetails;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Update Messages

    // @Override
    // public void update(MessageInbox messageInbox) {
    // Transaction transaction = null;
    // try (Session session = sessionFactory.openSession()) {
    // transaction = session.beginTransaction();
    // session.update(messageInbox);
    // transaction.commit();
    // } catch (Exception e) {
    // if (transaction != null) {
    // transaction.rollback();
    // }
    // e.printStackTrace();
    // }
    // }

    // delete Messages
    // @Override
    // public void delete(MessageInbox messageInbox) {
    // Transaction transaction = null;
    // try (Session session = sessionFactory.openSession()) {
    // transaction = session.beginTransaction();
    // session.delete(messageInbox);
    // transaction.commit();
    // } catch (Exception e) {
    // if (transaction != null) {
    // transaction.rollback();
    // }
    // e.printStackTrace();
    // }
    // }

    // delete all Messages inbox
    // @Override
    // public void deleteAll() {
    // Transaction transaction = null;
    // try (Session session = sessionFactory.openSession()) {
    // transaction = session.beginTransaction();
    // session.createQuery("delete from MessageInbox").executeUpdate();
    // transaction.commit();
    // } catch (Exception e) {
    // if (transaction != null) {
    // transaction.rollback();
    // }
    // e.printStackTrace();
    // }
    // }
}
