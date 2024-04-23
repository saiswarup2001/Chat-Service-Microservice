package com.acejobber.chatserver.Entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "message", schema = "sdtest")
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty("id")
    @Column(name = "mid")
    private Long mid;

    @Column
    private boolean readMarker;

    @Column
    private boolean deliveryMarker;

    @Column
    private Long senderId;

    @Column
    private Long receiverId;

    @Column
    private String content;

    @Column(name = "image")
    private byte[] image;

    @JsonBackReference 
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inbox_id")
    private MessageInbox inbox;

    public byte[] getImage() {
        return image;
    }
}
