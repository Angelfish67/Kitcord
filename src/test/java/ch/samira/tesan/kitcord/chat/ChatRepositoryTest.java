package ch.samira.tesan.kitcord.chat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class ChatRepositoryTest {

    @Autowired
    private ChatRepository chatRepository;

    @Test
    void save_shouldCreateChat() {
        Chat chat = new Chat();
        chat.setName("General");
        chat.setChatType(ChatType.GROUP);
        chat.setCreatedAt(LocalDateTime.now());

        Chat savedChat = chatRepository.save(chat);

        assertNotNull(savedChat.getId());
        assertEquals("General", savedChat.getName());
        assertEquals(ChatType.GROUP, savedChat.getChatType());
    }

    @Test
    void findById_shouldReturnChat() {
        Chat chat = new Chat();
        chat.setName("General");
        chat.setChatType(ChatType.GROUP);
        chat.setCreatedAt(LocalDateTime.now());

        Chat savedChat = chatRepository.save(chat);

        Optional<Chat> foundChat = chatRepository.findById(savedChat.getId());

        assertTrue(foundChat.isPresent());
        assertEquals(savedChat.getId(), foundChat.get().getId());
        assertEquals("General", foundChat.get().getName());
        assertEquals(ChatType.GROUP, foundChat.get().getChatType());
    }

    @Test
    void findAll_shouldReturnAllChats() {
        Chat chat1 = new Chat();
        chat1.setName("General");
        chat1.setChatType(ChatType.GROUP);
        chat1.setCreatedAt(LocalDateTime.now());

        Chat chat2 = new Chat();
        chat2.setName("Direct Chat");
        chat2.setChatType(ChatType.DIRECT);
        chat2.setCreatedAt(LocalDateTime.now());

        chatRepository.save(chat1);
        chatRepository.save(chat2);

        List<Chat> chats = chatRepository.findAll();

        assertEquals(2, chats.size());
    }

    @Test
    void update_shouldUpdateChat() {
        Chat chat = new Chat();
        chat.setName("Old Name");
        chat.setChatType(ChatType.GROUP);
        chat.setCreatedAt(LocalDateTime.now());

        Chat savedChat = chatRepository.save(chat);

        savedChat.setName("New Name");
        savedChat.setChatType(ChatType.DIRECT);

        Chat updatedChat = chatRepository.save(savedChat);

        assertEquals(savedChat.getId(), updatedChat.getId());
        assertEquals("New Name", updatedChat.getName());
        assertEquals(ChatType.DIRECT, updatedChat.getChatType());
    }

    @Test
    void delete_shouldDeleteChat() {
        Chat chat = new Chat();
        chat.setName("General");
        chat.setChatType(ChatType.GROUP);
        chat.setCreatedAt(LocalDateTime.now());

        Chat savedChat = chatRepository.save(chat);

        chatRepository.delete(savedChat);

        Optional<Chat> deletedChat = chatRepository.findById(savedChat.getId());

        assertTrue(deletedChat.isEmpty());
    }
}