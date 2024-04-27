package com.acejobber.chatserver.Entity;

import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "conversation", schema = "sdtest",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"receiverId", "senderId"})})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long cid;

    @Column
    private Long receiverId; 

    @Column
    private Long senderId; 

    @Column
    private Long jobId;

    @JsonManagedReference
    @OneToMany(mappedBy = "conversation", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MessageInbox> inboxes;
    
}
