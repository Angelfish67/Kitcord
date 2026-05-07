package ch.samira.tesan.kitcord.user;

import ch.samira.tesan.kitcord.chat.Chat;
import ch.samira.tesan.kitcord.message.Message;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 3, max = 100)
    @Column(length = 100, nullable = false, unique = true)
    private String username;

    @NotBlank
    @Size(min = 6, max = 255)
    @JsonIgnore
    @Column(length = 255, nullable = false)
    private String password;

    @JsonIgnore
    @ManyToMany(mappedBy = "users")
    private Set<Chat> chats = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    private Set<Message> messages = new HashSet<>();

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Set<Chat> getChats() {
        return chats;
    }

    public Set<Message> getMessages() {
        return messages;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setChats(Set<Chat> chats) {
        this.chats = chats;
    }

    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }

    public void addChat(Chat chat) {
        this.chats.add(chat);
    }

    public void removeChat(Chat chat) {
        this.chats.remove(chat);
    }

    public void addMessage(Message message) {
        this.messages.add(message);
        message.setSender(this);
    }

    public void removeMessage(Message message) {
        this.messages.remove(message);
        message.setSender(null);
    }
}