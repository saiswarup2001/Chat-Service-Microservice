package com.acejobber.chatserver.controller;

import java.io.IOException;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.acejobber.chatserver.Entity.Conversation;
import com.acejobber.chatserver.Entity.Message;
import com.acejobber.chatserver.Entity.MessageInbox;
import com.acejobber.chatserver.Services.ConversationService;
import com.acejobber.chatserver.Services.InboxService;
import com.acejobber.chatserver.Services.MessageService;

@RestController
@RequestMapping("/api")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private InboxService inboxRepo;

    @Autowired
    private ConversationService conversationService;

    // Message entity

    @PostMapping("/message/create")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        messageService.saveMessage(message);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    // @GetMapping("/message/{id}")
    // public ResponseEntity<Message> getMessageById(@PathVariable Long id) {
    // Message message = messageDao.findById(id);
    // return new ResponseEntity<>(message, HttpStatus.OK);
    // }
    @GetMapping("/message/{id}")
    public ResponseEntity<Message> getMessageById(@PathVariable Long id) {
        Message message = messageService.findMessageById(id);
        if (message != null) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Message not found with id: " + id);
        }
    }

    @GetMapping("/message/getAll")
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = messageService.findAllMessage();
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @PostMapping("/message/saveImage/{id}")
    public ResponseEntity<Message> saveImage(@PathVariable Long id, @RequestBody byte[] image) {
        messageService.saveImagebyMessageId(id, image);
        Message message = messageService.findMessageById(id);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @SuppressWarnings("unused")
    private String convertImageToBase64(byte[] image) {
        return Base64.getEncoder().encodeToString(image);
    }

    @GetMapping("/message/getImage/{id}")
    public ResponseEntity<ByteArrayResource> getImage(@PathVariable Long id) {
        Optional<byte[]> image = messageService.getImagebyMessageId(id);
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

    @PostMapping("/message/saveFileData/{id}")
    public ResponseEntity<Message> saveFileData(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            byte[] fileData = file.getBytes();
            messageService.saveFileDataByMessageId(id, fileData);
            Message message = messageService.findMessageById(id);
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to save file data for message with id: " + id);
        }
    }

    @GetMapping("/message/getFileData/{id}")
    public ResponseEntity<ByteArrayResource> getFileData(@PathVariable Long id) {
        Optional<byte[]> fileData = messageService.getFileDataByMessageId(id);
        if (fileData.isPresent()) {
            ByteArrayResource resource = new ByteArrayResource(fileData.get());
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=fileData");
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File data not found for message with id: " + id);
        }
    }

    // INBOX
    @PostMapping("/inbox/create")
    public ResponseEntity<List<MessageInbox>> createInbox(@RequestBody List<MessageInbox> messageInboxes) {
        List<MessageInbox> savedInboxes = new ArrayList<>();
        for (MessageInbox messageInbox : messageInboxes) {
            try {
                inboxRepo.saveInbox(messageInbox);
                savedInboxes.add(messageInbox);
            } catch (Exception e) {
                e.printStackTrace(); 
            }
        }
        return new ResponseEntity<>(savedInboxes, HttpStatus.CREATED);
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
        conversationService.saveConversation(conversation);
        return new ResponseEntity<>(conversation, HttpStatus.CREATED);
    }

    @GetMapping("/convo/{jobId}")
    public ResponseEntity<Conversation> getConversationById(@PathVariable Long jobId) {
        Conversation conversation = conversationService.findMessageByConversationId(jobId);
        return new ResponseEntity<>(conversation, HttpStatus.OK);
    }

    @GetMapping("/convo")
    public ResponseEntity<List<Conversation>> getAllConversations() {
        List<Conversation> conversations = conversationService.findAllconversation();
        return new ResponseEntity<>(conversations, HttpStatus.OK);
    }

}
