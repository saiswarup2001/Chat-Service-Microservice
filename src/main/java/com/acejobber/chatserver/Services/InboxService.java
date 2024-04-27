package com.acejobber.chatserver.Services;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.acejobber.chatserver.Entity.MessageInbox;
import com.acejobber.chatserver.Repository.InboxRepo;

@Service
public class InboxService implements InboxRepo {

    private @Autowired SessionFactory sessionFactory;

    @Override
    @Transactional
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
    @Transactional
    public MessageInbox findInboxById(Long id) {
        Session session = sessionFactory.openSession();
        try {
            return session.get(MessageInbox.class, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    @Transactional
    public List<MessageInbox> findAllInboxes() {
        Session session = sessionFactory.openSession();
        try {
            List<MessageInbox> inboxDetails = session
                    .createQuery("SELECT DISTINCT mi FROM MessageInbox mi", MessageInbox.class).list();
            return inboxDetails;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
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

