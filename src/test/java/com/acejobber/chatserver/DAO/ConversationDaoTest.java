package com.acejobber.chatserver.DAO;

import com.acejobber.chatserver.Entity.Conversation;
import com.acejobber.chatserver.Services.ConversationService;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.hibernate.query.Query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

public class ConversationDaoTest {

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @Mock
    private Transaction transaction;

    @Mock
    private Query<Conversation> query;


    @InjectMocks
    private ConversationService conversationDao;

    @SuppressWarnings("deprecation")
    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);

        // Stub sessionFactory and session behavior
        when(sessionFactory.openSession()).thenReturn(session);
        when(session.beginTransaction()).thenReturn(transaction);
    }

    @Test
    public void testSave() {
        // Prepare test data
        Conversation conversation = new Conversation();

        // Call the DAO method
        conversationDao.saveConversation(conversation);

        // Verify that session.save() was called with the conversation object
        verify(session).save(conversation);
        verify(transaction).commit();
    }

    @Test
    public void testFindById() {
        // Prepare test data
        Long conversationId = 1L;
        Conversation expectedConversation = new Conversation();
        expectedConversation.setCid(conversationId);

        // Stub session.createQuery() to return the query
        when(session.createQuery("select distinct c from Conversation c where c.cid = :id", Conversation.class))
                .thenReturn(query);

        // Stub query.setParameter() and query.uniqueResult() to return the expected Conversation
        when(query.setParameter("id", conversationId)).thenReturn(query);
        when(query.uniqueResult()).thenReturn(expectedConversation);

        // Call the DAO method
        Conversation result = conversationDao.findMessageByConversationId(conversationId);

        // Verify the result
        assertNotNull(result);
        assertEquals(expectedConversation.getCid(), result.getCid());
        verify(session).createQuery("select distinct c from Conversation c where c.cid = :id", Conversation.class);
        verify(query).setParameter("id", conversationId);
        verify(query).uniqueResult();
    }

    @Test
    public void testFindAll() {
        // Prepare test data
        List<Conversation> expectedConversations = new ArrayList<>();
        Conversation conversation1 = new Conversation();
        conversation1.setCid(1L);
        Conversation conversation2 = new Conversation();
        conversation2.setCid(2L);
        expectedConversations.add(conversation1);
        expectedConversations.add(conversation2);

        // Mock session.createQuery() and query.list() to return the expected list of conversations
        when(session.createQuery("SELECT DISTINCT c FROM Conversation c", Conversation.class)).thenReturn(query);
        when(query.list()).thenReturn(expectedConversations);

        // Call the DAO method
        List<Conversation> result = conversationDao.findAllconversation();

        // Verify the result
        assertNotNull(result);
        assertEquals(expectedConversations.size(), result.size());

        // Verify interaction with session and query
        verify(session).createQuery("SELECT DISTINCT c FROM Conversation c", Conversation.class);
        verify(query).list();
    }

    @Test
    public void testFindById_ErrorHandling() {
        // Prepare test data
        Long conversationId = 1L;

        // Stub session.createQuery() to throw an exception
        when(session.createQuery("select distinct c from Conversation c where c.cid = :id", Conversation.class))
                .thenThrow(new RuntimeException("Failed to create query"));

        // Call the DAO method and expect it to handle the exception gracefully
        Conversation result = conversationDao.findMessageByConversationId(conversationId);

        // Verify that the result is null due to exception handling
        assertNull(result);
        verify(session).createQuery("select distinct c from Conversation c where c.cid = :id", Conversation.class);
    }

    @Test
    public void testFindById_UnexpectedResult() {
        // Prepare test data
        Long conversationId = 1L;

        // Stub session.createQuery() to return the query
        when(session.createQuery("select distinct c from Conversation c where c.cid = :id", Conversation.class))
                .thenReturn(query);

        // Stub query.setParameter() to return the query
        when(query.setParameter("id", conversationId)).thenReturn(query);

        // Stub query.uniqueResult() to return null (unexpected result)
        when(query.uniqueResult()).thenReturn(null);

        // Call the DAO method
        Conversation result = conversationDao.findMessageByConversationId(conversationId);

        // Verify that the result is null due to unexpected query result
        assertNull(result);

        // Verify interaction with session and query
        verify(session).createQuery("select distinct c from Conversation c where c.cid = :id", Conversation.class);
        verify(query).setParameter("id", conversationId);
        verify(query).uniqueResult();
    }
}