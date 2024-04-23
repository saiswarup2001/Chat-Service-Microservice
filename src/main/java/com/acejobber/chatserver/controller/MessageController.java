package com.acejobber.chatserver.controller;

import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.acejobber.chatserver.Entity.Conversation;
import com.acejobber.chatserver.Entity.Message;
import com.acejobber.chatserver.Entity.MessageInbox;
import com.acejobber.chatserver.Repository.ConversationRepository;
import com.acejobber.chatserver.Repository.InboxRepository;
import com.acejobber.chatserver.Repository.MessageRepository;

@RestController
@RequestMapping("/api")
public class MessageController {

    @Autowired
    private MessageRepository messageRepo;

    @Autowired
    private InboxRepository inboxRepo;

    @Autowired
    private ConversationRepository conversationRepo;

    // Message entity

    @PostMapping("/message/create")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        messageRepo.saveMessage(message);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    // @GetMapping("/message/{id}")
    // public ResponseEntity<Message> getMessageById(@PathVariable Long id) {
    // Message message = messageDao.findById(id);
    // return new ResponseEntity<>(message, HttpStatus.OK);
    // }
    @GetMapping("/message/{id}")
    public ResponseEntity<Message> getMessageById(@PathVariable Long id) {
        Message message = messageRepo.findMessageById(id);
        if (message != null) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Message not found with id: " + id);
        }
    }

    @GetMapping("/message/getAll")
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = messageRepo.findAllMessage();
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @PostMapping("/message/saveImage/{id}")
    public ResponseEntity<Message> saveImage(@PathVariable Long id, @RequestBody byte[] image) {
        messageRepo.saveImagebyMessageId(id, image);
        Message message = messageRepo.findMessageById(id);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @SuppressWarnings("unused")
    private String convertImageToBase64(byte[] image) {
        return Base64.getEncoder().encodeToString(image);
    }

    @GetMapping("/message/getImage/{id}")
    public ResponseEntity<ByteArrayResource> getImage(@PathVariable Long id) {
        Optional<byte[]> image = messageRepo.getImagebyMessageId(id);
        if (image.isPresent()) {
            ByteArrayResource resource = new ByteArrayResource(image.get());
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=image.jpg"); 
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.IMAGE_JPEG) 
                    .body(resource);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found for message with id: " + id);
        }
    }

    // INBOX
    @PostMapping("/inbox/create")
    public ResponseEntity<MessageInbox> createInbox(@RequestBody MessageInbox messageInbox) {
        inboxRepo.saveInbox(messageInbox);
        return new ResponseEntity<>(messageInbox, HttpStatus.CREATED);
    }

    @GetMapping("/inbox/{id}")
    public ResponseEntity<MessageInbox> getInboxById(@PathVariable Long id) {
        MessageInbox messageInbox = inboxRepo.findInboxById(id);
        return new ResponseEntity<>(messageInbox, HttpStatus.OK);
    }

    @GetMapping("/inbox/all")
    public ResponseEntity<List<MessageInbox>> getAllInboxes() {
        List<MessageInbox> inboxes = inboxRepo.findAllInboxes();
        return new ResponseEntity<>(inboxes, HttpStatus.OK);
    }

    // Conversation
    @PostMapping("/convo/create")
    public ResponseEntity<Conversation> createConversation(@RequestBody Conversation conversation) {
        conversationRepo.saveConversation(conversation);
        return new ResponseEntity<>(conversation, HttpStatus.CREATED);
    }

    @GetMapping("/convo/{id}")
    public ResponseEntity<Conversation> getConversationById(@PathVariable Long id) {
        Conversation conversation = conversationRepo.findMessageByConversationId(id);
        return new ResponseEntity<>(conversation, HttpStatus.OK);
    }

    @GetMapping("/convo")
    public ResponseEntity<List<Conversation>> getAllConversations() {
        List<Conversation> conversations = conversationRepo.findAllconversation();
        return new ResponseEntity<>(conversations, HttpStatus.OK);
    }

}
