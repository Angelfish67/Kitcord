package ch.samira.tesan.kitcord.chat;

import ch.samira.tesan.kitcord.user.User;
import ch.samira.tesan.kitcord.user.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatService {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    public ChatService(ChatRepository chatRepository, UserRepository userRepository) {
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
    }

    public List<Chat> getChats() {
        return chatRepository.findAll();
    }

    public Chat getChatById(Long id) {
        return chatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chat not found"));
    }

    public Chat createChat(Chat chat) {
        chat.setId(null);
        chat.setCreatedAt(LocalDateTime.now());
        return chatRepository.save(chat);
    }

    public Chat updateChat(Long id, Chat updatedChat) {
        Chat chat = getChatById(id);

        chat.setName(updatedChat.getName());
        chat.setChatType(updatedChat.getChatType());

        return chatRepository.save(chat);
    }

    public void deleteChat(Long id) {
        Chat chat = getChatById(id);
        chatRepository.delete(chat);
    }

    public Chat addUserToChat(Long chatId, Long userId) {
        Chat chat = getChatById(chatId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        chat.getUsers().add(user);

        return chatRepository.save(chat);
    }

    public Chat removeUserFromChat(Long chatId, Long userId) {
        Chat chat = getChatById(chatId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        chat.getUsers().remove(user);

        return chatRepository.save(chat);
    }
}