package com.acejobber.chatserver.Entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

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
public class Message implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty("id")
    @Column(name = "mid")
    private Long mid;

    @Column(name = "status")
    private boolean status;

    @Lob
    @Column(name = "file_data")
    private byte[] fileData;

    @Column(name = "sender_id")
    private Long senderId;

    @Column(name = "receiver_id")
    private Long receiverId;

    @Column(name = "content")
    private String content;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inbox_id")
    @JsonBackReference
    private MessageInbox inbox;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message)) return false;
        Message message = (Message) o;
        return isStatus() == message.isStatus() &&
                Objects.equals(getFileData(), message.getFileData()) &&
                Objects.equals(getSenderId(), message.getSenderId()) &&
                Objects.equals(getReceiverId(), message.getReceiverId()) &&
                Objects.equals(getContent(), message.getContent()) &&
                Objects.equals(getImage(), message.getImage());
    }

    @Override
    public int hashCode() {
        return Objects.hash(isStatus(), getFileData(), getSenderId(), getReceiverId(), getContent(), getImage());
    }
}