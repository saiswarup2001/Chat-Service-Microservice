package com.acejobber.chatserver.Entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@Table(name = "inbox", schema = "sdtest")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageInbox {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "inbox_id")
    private Long id;

    @Column
    private Long receiverId; // chatter ID

    @Column
    private Long senderId; // User ID

    @JsonManagedReference 
    @OneToMany(mappedBy = "inbox", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Message> messages;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;


    public void addMessage(Message message) {
    if (messages == null) {
        messages = new ArrayList<>();
    }
    message.setInbox(this);
    messages.add(message);
}
}
