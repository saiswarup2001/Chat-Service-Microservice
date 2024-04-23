package com.acejobber.chatserver.MainService;

import java.util.List;

import javax.persistence.*;

import com.acejobber.chatserver.Entity.Conversation;
import com.acejobber.chatserver.Entity.Message;
import com.acejobber.chatserver.Entity.MessageInbox;

import lombok.Data;

@Data
@Entity
@Table(name = "chat", schema = "sdtest")
public class ChatEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<MessageInbox> inboxes;

    @OneToMany(mappedBy = "inbox", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Message> messages;
    
}
