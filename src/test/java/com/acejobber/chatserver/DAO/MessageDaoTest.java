package com.acejobber.chatserver.DAO;

import com.acejobber.chatserver.Entity.Message;
import com.acejobber.chatserver.Repository.MessageRepository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class MessageDaoTest {

    @Mock
    private SessionFactory sessionFactory; // Mock the Hibernate SessionFactory

    @Mock
    private Session session; // Mock the Hibernate Session

    @Mock
    private Transaction transaction; // Mock the Hibernate Transaction

    @InjectMocks
    private MessageRepository messageDao; // The DAO to be tested

    @SuppressWarnings("deprecation")
    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);

        // Mock sessionFactory behavior
        when(sessionFactory.openSession()).thenReturn(session);
        when(session.beginTransaction()).thenReturn(transaction);
    }

    @Test
    public void testSaveMessage() {
        // Prepare test data
        Message message = new Message();
        message.setMid(1L);
        message.setContent("Hello");

        // Call the DAO method
        messageDao.saveMessage(message);

        // Verify that session.save and transaction.commit were called
        verify(session, times(1)).persist(message);
        verify(transaction, times(1)).commit();
    }

    @Test
    public void testFindAll() {
        // Prepare test data
        List<Message> messages = new ArrayList<>();
        Message message1 = new Message();
        message1.setMid(1L);
        message1.setContent("Hello");
        messages.add(message1);

        // Mock session behavior
        @SuppressWarnings("unchecked")
        Query<Message> query = mock(Query.class);
        when(session.createQuery(anyString(), eq(Message.class))).thenReturn(query);
        when(query.list()).thenReturn(messages);

        // Call the DAO method
        List<Message> result = messageDao.findAllMessage();

        // Verify the result
        assertEquals(messages.size(), result.size());
        assertEquals(message1.getContent(), result.get(0).getContent());
    }

    @Test
    public void testGetImage() {
        // Prepare test data
        Long messageId = 1L;
        byte[] imageData = { 0x01, 0x02, 0x03 }; // Sample image data

        Message message = new Message();
        message.setMid(messageId);
        message.setImage(imageData);

        // Mock session.get() to return the test message
        when(session.get(Message.class, messageId)).thenReturn(message);

        // Call the DAO method
        Optional<byte[]> result = messageDao.getImagebyMessageId(messageId);

        // Verify the result
        assertTrue(result.isPresent());
        assertArrayEquals(imageData, result.get());
    }

    @Test
    public void testSaveImage() {
        // Prepare test data
        Long messageId = 1L;
        byte[] imageData = { 0x01, 0x02, 0x03 }; // Sample image data

        Message message = new Message();
        message.setMid(messageId);

        // Mock session.get() to return the test message
        when(session.get(Message.class, messageId)).thenReturn(message);

        // Call the DAO method
        messageDao.saveImagebyMessageId(messageId, imageData);

        // Verify 
        verify(session).update(message); 
        verify(transaction).commit(); 
    }

    @Test
    public void testFindById() {
        // Prepare test data
        Long messageId = 1L;
        String content = "Hello";

        Message expectedMessage = new Message();
        expectedMessage.setMid(messageId);
        expectedMessage.setContent(content);

        // Mock session.get() to return the test message
        when(session.get(Message.class, messageId)).thenReturn(expectedMessage);

        // Call the DAO method
        Message result = messageDao.findMessageById(messageId);

        // assert and Verify the result
        assertNotNull(result);
        assertEquals(expectedMessage.getMid(), result.getMid());
        assertEquals(expectedMessage.getContent(), result.getContent());
        verify(session).get(Message.class, messageId);
        verifyNoInteractions(transaction);
    }
}