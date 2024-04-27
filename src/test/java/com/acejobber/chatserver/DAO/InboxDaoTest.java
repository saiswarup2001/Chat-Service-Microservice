package com.acejobber.chatserver.DAO;

import com.acejobber.chatserver.Entity.MessageInbox;
import com.acejobber.chatserver.Services.InboxService;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.hibernate.query.Query;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

public class InboxDaoTest {

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @Mock
    private Transaction transaction;

    @Mock
    private Query<MessageInbox> query;

    @InjectMocks
    private InboxService inboxDao;

    @SuppressWarnings("deprecation")
    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        when(sessionFactory.openSession()).thenReturn(session);// Stub sessionFactory and session behavior
        when(session.beginTransaction()).thenReturn(transaction);
    }

    @Test
    public void testSave() {
        // Prepare test data
        MessageInbox messageInbox = new MessageInbox();
        messageInbox.setReceiverId(10L);
        messageInbox.setSenderId(11L);
        // Call the DAO method
        inboxDao.saveInbox(messageInbox);

        // Verify 
        verify(session).save(messageInbox);
        verify(transaction).commit();
    }

    @Test
    public void testFindById() {
        // Prepare test data
        Long inboxId = 1L;
        MessageInbox expectedMessageInbox = new MessageInbox();
        expectedMessageInbox.setId(inboxId);

        // Mock session.get() to return the expected MessageInbox
        when(session.get(MessageInbox.class, inboxId)).thenReturn(expectedMessageInbox);

        // Call the DAO method
        MessageInbox result = inboxDao.findInboxById(inboxId);

        // Verify the result
        assertNotNull(result);
        assertEquals(expectedMessageInbox.getId(), result.getId());
        verify(session).get(MessageInbox.class, inboxId);
    }

    @Test
    public void testFindAll() {
        // Prepare test data
        List<MessageInbox> expectedInboxes = new ArrayList<>();
        MessageInbox inbox1 = new MessageInbox();
        inbox1.setId(1L);
        MessageInbox inbox2 = new MessageInbox();
        inbox2.setId(2L);
        expectedInboxes.add(inbox1);
        expectedInboxes.add(inbox2);

        // Mock session.createQuery() and query.list() to return the expected list of inboxes
        when(session.createQuery("SELECT DISTINCT mi FROM MessageInbox mi", MessageInbox.class)).thenReturn(query);
        when(query.list()).thenReturn(expectedInboxes);

        // Call the DAO method
        List<MessageInbox> result = inboxDao.findAllInboxes();

        // Verify the result
        assertNotNull(result);
        assertEquals(expectedInboxes.size(), result.size());

        // Verify interaction with session and query
        verify(session).createQuery("SELECT DISTINCT mi FROM MessageInbox mi", MessageInbox.class);
        verify(query).list();
    }
}
