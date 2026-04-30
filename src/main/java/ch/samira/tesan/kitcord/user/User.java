package ch.samira.tesan.kitcord.user;

import ch.samira.tesan.kitcord.chat.Chat;
import ch.samira.tesan.kitcord.message.Message;
import ch.samira.tesan.kitcord.user.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false, unique = true)
    @Size(max = 100)
    @NotEmpty
    private String username;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @JsonIgnore
    @ManyToMany(mappedBy = "users")
    private Set<Chat> chats = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    private Set<Message> messages = new HashSet<>();

    public User() {
    }

    public User(String username, Role role) {
        this.username = username;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Role getRole() {
        return role;
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

    public void setRole(Role role) {
        this.role = role;
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