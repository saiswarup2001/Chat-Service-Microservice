package com.acejobber.chatserver.Services;

import com.acejobber.chatserver.Entity.Message;
import com.acejobber.chatserver.Repository.MessageRepo;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

@Service
public class MessageService implements MessageRepo {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional
    public void saveMessage(Message message) {
        Session session = sessionFactory.openSession();
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
    }

    @Override
    @Transactional
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
    @Transactional
    public List<Message> findAllMessage() {
        Session session = sessionFactory.openSession();
        try {
            return session.createQuery("SELECT DISTINCT m from Message m", Message.class).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    @Transactional
    public Optional<byte[]> getImagebyMessageId(Long messageId) {
        Session session = sessionFactory.openSession();
        try {
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
    @Transactional
    public void saveImagebyMessageId(Long messageId, byte[] imageData) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
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

    @Override
    @Transactional
    public void saveFileDataByMessageId(Long messageId, byte[] fileData) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Message message = session.get(Message.class, messageId);
            if (message != null) {
                message.setFileData(fileData);
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

    @Override
    @Transactional
    public Optional<byte[]> getFileDataByMessageId(Long messageId) {
        Session session = sessionFactory.openSession();
        try {
            Message message = session.get(Message.class, messageId);
            if (message != null && message.getFileData() != null) {
                return Optional.of(message.getFileData());
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
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
