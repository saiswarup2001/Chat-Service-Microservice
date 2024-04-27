package com.acejobber.chatserver.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.acejobber.chatserver.Entity.Conversation;
import com.acejobber.chatserver.Entity.Message;
import com.acejobber.chatserver.Entity.MessageInbox;
import com.acejobber.chatserver.Services.ConversationService;
import com.acejobber.chatserver.Services.InboxService;
import com.acejobber.chatserver.Services.MessageService;
import com.fasterxml.jackson.databind.ObjectMapper;

// @RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MessageControllerTest {

    @Mock
    private MessageService messageService;

    @Mock
    private InboxService inboxService;

    @Mock
    private ConversationService conversationService;

    @InjectMocks
    private MessageController messageController;

    private MockMvc mockMvc;

    MvcResult result;
    MockHttpServletResponse response;

    private ObjectMapper objectMapper = new ObjectMapper();

    @SuppressWarnings("deprecation")
    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(messageController).build();
    }

    // =========== creating Message test ===============

    @Test
    public void testCreateMessage() throws Exception {
        // Arrange
        Message message = new Message(1L, false, null, 10L, 20L, "Hello World", null, null);

        // ResponseEntity responseEntity = new ResponseEntity(message, HttpStatus.CREATED);
        // when(messageController.createMessage(message)).thenReturn(responseEntity);

        doNothing().when(messageService).saveMessage(message);

        // Act
        mockMvc.perform(post("/api/message/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(message)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(message.getMid()))
                .andExpect(jsonPath("$.senderId").value(message.getSenderId()))
                .andExpect(jsonPath("$.receiverId").value(message.getReceiverId()))
                .andExpect(jsonPath("$.content").value(message.getContent()));

        // Verification
        verify(messageService, times(1)).saveMessage(message);
    }

    // =========== Get All Message Testing ============
    @Test
    public void testGetAllMessages() throws Exception {
        // Arrange
        List<Message> messages = new ArrayList<>();
        messages.add(new Message(1L, false, null, 10L, 20L, "Hello World", null, null));
        messages.add(new Message(2L, false, null, 11L, 12L, "This is a test message", null, null));

        when(messageService.findAllMessage()).thenReturn(messages);

        // Act
        mockMvc.perform(get("/api/message/getAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].content").value("Hello World"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].content").value("This is a test message"));

        // Verification
        verify(messageService, times(1)).findAllMessage();
    }

    // ============= Getting Message by Id Test =================
    @Test
    public void testgetMessageById() throws Exception {
        // Arrange
        Long id = 1L;
        Message message = new Message(id, false, null, 10L, 11L, "Test Message byID", null, null);
        when(messageService.findMessageById(id)).thenReturn((Message) message);

        // Act
        mockMvc.perform(get("/api/message/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.content").value("Test Message byID"))
                .andExpect(jsonPath("$.senderId").value(10L))
                .andExpect(jsonPath("$.receiverId").value(11L));

        // Verification
        verify(messageService, times(1)).findMessageById(id);
    }

    // ============ If message not found by the id ========================
    @Test
    public void testgetMessageById_NotFound() {
        // Arrange
        Long id = 1000L;
        when(messageService.findMessageById(id)).thenReturn(null);

        // Act and Assert
        try {
            messageController.getMessageById(id);
            fail("Expected ResponseStatusException with status 404, but no exception was thrown.");
        } catch (ResponseStatusException ex) {
            assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode(),
                    "Expected ResponseStatusException with status 404 but got a different status.");
        }

        // Verification
        verify(messageService, times(1)).findMessageById(id);
    }

    // ============= Save Image Testing using message Id =================
    @Test
    public void testSaveImage() throws Exception {
        // Setting up the data
        Long id = 1L;
        byte[] image = "test image data".getBytes();

        // when(messageDao.saveImage(id, image)).thenReturn(true);
        doNothing().when(messageService).saveImagebyMessageId(id, image);
        when(messageService.findMessageById(id)).thenReturn(new Message(id, false, null, 10L, 20L, "Hello World", null, null));

        // Act using api
        mockMvc.perform(post("/api/message/saveImage/{id}", id)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .content(image))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.content").value("Hello World"));

        // Verification
        verify(messageService, times(1)).saveImagebyMessageId(id, image);
        verify(messageService, times(1)).findMessageById(id);
    }

    // ========= Getting Image using message Id =================
    @Test
    public void testGetImage() throws Exception {
        // Arrange
        Long id = 1L;
        byte[] imageData = "some image data".getBytes();
        when(messageService.getImagebyMessageId(id)).thenReturn(Optional.of(imageData));

        // Act
        mockMvc.perform(get("/api/message/getImage/{id}", id))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=image.jpg"))
                .andExpect(content().contentType(MediaType.IMAGE_JPEG))
                .andExpect(content().bytes(imageData));

        // Verification
        verify(messageService, times(1)).getImagebyMessageId(id);
    }

    // ==== creating the inbox ===
    @Test
    void testCreateInbox() throws Exception {
        // Initialize MockMvc with standalone setup for the controller
        mockMvc = MockMvcBuilders.standaloneSetup(messageController).build();

        // Prepare test data
        MessageInbox messageInbox = new MessageInbox();
        messageInbox.setReceiverId(1L);
        messageInbox.setSenderId(2L);

        // Mock the save behavior of InboxService
        doNothing().when(inboxService).saveInbox(messageInbox);

        // Perform POST request to controller endpoint
        mockMvc.perform(post("/api/inbox/create")
                .contentType("application/json")
                .content("{\"receiverId\": 1, \"senderId\": 2}")) // JSON representation of messageInbox
                .andExpect(status().isCreated()); // Expect HTTP 201 Created

        // Verify that the save method of InboxService was called
        verify(inboxService).saveInbox(messageInbox);
    }

    // ==== Get the inbox through inbox id===
    @Test
    void testGetInboxById() throws Exception {
        // Initialize MockMvc with standalone setup for the controller
        mockMvc = MockMvcBuilders.standaloneSetup(messageController).build();

        List<Message> messages = new ArrayList<>();
        messages.add(new Message(1L, false, null, 10L, 20L, "Hello World", null, null));
        messages.add(new Message(2L, false, null, 11L, 12L, "This is a test message", null, null));

        // Prepare test data
        Long id = 1L;
        MessageInbox messageInbox = new MessageInbox();
        messageInbox.setId(id);
        messageInbox.setReceiverId(1L);
        messageInbox.setSenderId(2L);
        messageInbox.setMessages(messages);

        // Mock the findById behavior of InboxService
        when(inboxService.findInboxById(id)).thenReturn(messageInbox);

        // Perform GET request to controller endpoint
        mockMvc.perform(get("/api/inbox/{id}", id))
                .andExpect(status().isOk()) // Expect HTTP 200 OK
                .andExpect(jsonPath("$.id").value(id)) // Validate response JSON fields
                .andExpect(jsonPath("$.receiverId").value(messageInbox.getReceiverId()))
                .andExpect(jsonPath("$.senderId").value(messageInbox.getSenderId()));

        // Verify that the findById method of InboxService was called with the specified
        // id
        verify(inboxService).findInboxById(id);
    }

    // ==== Getting all the inbox ===
    @Test
    void testGetAllInboxes() throws Exception {
        // Initialize MockMvc with standalone setup for the controller
        mockMvc = MockMvcBuilders.standaloneSetup(messageController).build();

        List<Message> messages1 = new ArrayList<>();
        messages1.add(new Message(1L, false, null, 10L, 20L, "Hello World", null, null));
        messages1.add(new Message(2L, false, null, 11L, 12L, "This is a test message", null, null));

        List<Message> messages2 = new ArrayList<>();
        messages2.add(new Message(3L, false, null, 100L, 200L, "Hello World", null, null));
        messages2.add(new Message(4L, false, null, 101L, 102L, "This is a test message", null, null));
        // Prepare test data
        List<MessageInbox> inboxList = new ArrayList<>();
        MessageInbox inbox1 = new MessageInbox();
        inbox1.setId(1L);
        inbox1.setReceiverId(1L);
        inbox1.setSenderId(2L);
        inbox1.setMessages(messages1);
        MessageInbox inbox2 = new MessageInbox();
        inbox2.setId(2L);
        inbox2.setReceiverId(3L);
        inbox2.setSenderId(4L);
        inboxList.add(inbox1);
        inboxList.add(inbox2);
        inbox2.setMessages(messages2);

        // Mock the findAll behavior of InboxService
        when(inboxService.findAllInboxes()).thenReturn(inboxList);

        // Perform GET request to controller endpoint
        mockMvc.perform(get("/api/inbox/all"))
                .andExpect(status().isOk()) // Expect HTTP 200 OK
                .andExpect(jsonPath("$.size()").value(inboxList.size())) // Validate response JSON size
                .andExpect(jsonPath("$[0].id").value(inbox1.getId())) // Validate response JSON fields
                .andExpect(jsonPath("$[0].receiverId").value(inbox1.getReceiverId()))
                .andExpect(jsonPath("$[0].senderId").value(inbox1.getSenderId()))
                .andExpect(jsonPath("$[1].id").value(inbox2.getId()))
                .andExpect(jsonPath("$[1].receiverId").value(inbox2.getReceiverId()))
                .andExpect(jsonPath("$[1].senderId").value(inbox2.getSenderId()));

        // Verify that the findAll method of InboxService was called
        verify(inboxService).findAllInboxes();
    }

    // ========= Creating Conversation Test ========
    @Test
    public void testCreateConversation() throws Exception {
        // Prepare test data
        Long id = 1L; // conversation id

        // Message inbox
        List<MessageInbox> inboxList = new ArrayList<>();
        MessageInbox inbox1 = new MessageInbox();
        inbox1.setId(1L);
        inbox1.setReceiverId(1L);
        inbox1.setSenderId(2L);
        MessageInbox inbox2 = new MessageInbox();
        inbox2.setId(2L);
        inbox2.setReceiverId(3L);
        inbox2.setSenderId(4L);
        inboxList.add(inbox1);
        inboxList.add(inbox2);

        // Create a sample Conversation object
        Conversation conversation = new Conversation();
        conversation.setCid(id);
        conversation.setJobId(1L);
        conversation.setSenderId(10L);
        conversation.setReceiverId(11L);
        conversation.setInboxes(inboxList);

        // Mock the save behavior of ConversationDao
        doNothing().when(conversationService).saveConversation(ArgumentMatchers.any(Conversation.class));

        // Perform POST request to controller endpoint
        mockMvc.perform(MockMvcRequestBuilders.post("/api/convo/create")
                .contentType("application/json")
                .content(
                        "{ \"cid\": 1, \"senderId\": 10, \"receiverId\": 11, \"JobId\": 1, \"inboxes\": [{ \"id\": 1, \"senderId\": 2, \"receiverId\": 1 }, { \"id\": 2, \"senderId\": 4, \"receiverId\": 3 }] }"))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.cid").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("$.senderId").value(10L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.receiverId").value(11L));

        // Verify that the save method of ConversationDao was called
        verify(conversationService).saveConversation(ArgumentMatchers.any(Conversation.class));
    }

    // ===== get Message conversation by conversation Id ============
    @Test
    public void testGetConversationById_Found() throws Exception {
        // Test data
        Long conversationId = 1L;
        Conversation conversation = new Conversation(conversationId, 10L, 11L, 1L, null);

        // Mock ConversationService findById behavior
        Mockito.when(conversationService.findMessageByConversationId(conversationId)).thenReturn(conversation);

        // Perform endpoint
        mockMvc.perform(get("/api/convo/{id}", conversationId))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.cid").value(conversationId))
                // .andExpect(jsonPath("$.JobId").value(1L))
                .andExpect(jsonPath("$.senderId").value(11L))
                .andExpect(jsonPath("$.receiverId").value(10L));

        // Verify that ConversationService findById was called with the correct argument
        Mockito.verify(conversationService).findMessageByConversationId(conversationId);
    }

    // ======= Get all coversation ==============
    // @Test
    public void testGetAllConversations() throws Exception {
        // Prepare test data
        List<Message> messages1 = new ArrayList<>();
        messages1.add(new Message(1L, false, null, 10L, 20L, "Hello World", null, null));
        messages1.add(new Message(2L, false, null, 11L, 12L, "This is a test message", null, null));

        List<Message> messages2 = new ArrayList<>();
        messages2.add(new Message(3L, false, null, 100L, 200L, "Hello World", null, null));
        messages2.add(new Message(4L, true, null, 101L, 102L, "This is a test message", null, null));

        List<MessageInbox> inboxList1 = new ArrayList<>();
        MessageInbox inbox1 = new MessageInbox();
        inbox1.setId(1L);
        inbox1.setReceiverId(1L);
        inbox1.setSenderId(2L);
        inbox1.setMessages(messages1);

        MessageInbox inbox2 = new MessageInbox();
        inbox2.setId(2L);
        inbox2.setReceiverId(3L);
        inbox2.setSenderId(4L);
        inbox2.setMessages(messages2);

        inboxList1.add(inbox1);
        inboxList1.add(inbox2);

        List<MessageInbox> inboxList2 = new ArrayList<>();
        MessageInbox inbox3 = new MessageInbox();
        inbox3.setId(3L);
        inbox3.setReceiverId(11L);
        inbox3.setSenderId(12L);
        inbox3.setMessages(messages2);

        MessageInbox inbox4 = new MessageInbox();
        inbox4.setId(4L);
        inbox4.setReceiverId(3L);
        inbox4.setSenderId(4L);
        inbox4.setMessages(messages1);

        inboxList2.add(inbox3);
        inboxList2.add(inbox4);

        List<Conversation> conversations = new ArrayList<>();
        conversations.add(new Conversation(1L, 10L, 11L, 1L, inboxList1));
        conversations.add(new Conversation(2L, 12L, 13L, 2L, inboxList2));

        // Mock ConversationService findAll behavior
        Mockito.when(conversationService.findAllconversation()).thenReturn(conversations);

        // Perform GET request to controller endpoint
        mockMvc.perform(get("/api/convo"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(conversations.size()))
                .andExpect(jsonPath("$[0].cid").value(1L))
                .andExpect(jsonPath("$[0].senderId").value(11L))
                .andExpect(jsonPath("$[0].receiverId").value(10L))
                .andExpect(jsonPath("$[0].inboxes[0].id").value(1L))
                .andExpect(jsonPath("$[0].inboxes[0].receiverId").value(1L))
                .andExpect(jsonPath("$[0].inboxes[0].senderId").value(2L))
                .andExpect(jsonPath("$[0].inboxes[0].messages[0].senderId").value(10L))
                .andExpect(jsonPath("$[0].inboxes[0].messages[0].receiverId").value(20L))
                .andExpect(jsonPath("$[0].inboxes[0].messages[0].content").value("Hello World"));

        // Verify that ConversationService findAll was called
        Mockito.verify(conversationService).findAllconversation();
    }
}
