package ch.samira.tesan.kitcord.chat;

import ch.samira.tesan.kitcord.chat.enums.ChatType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatControllerTest {

    @Mock
    private ChatService chatService;

    @InjectMocks
    private ChatController chatController;

    @Test
    void getChats_shouldReturnAllChats() {
        Chat chat1 = new Chat();
        chat1.setId(1L);
        chat1.setName("General");
        chat1.setChatType(ChatType.GROUP);
        chat1.setCreatedAt(LocalDateTime.now());
        chat1.setUsers(Set.of());

        Chat chat2 = new Chat();
        chat2.setId(2L);
        chat2.setName("Direct Chat");
        chat2.setChatType(ChatType.DIRECT);
        chat2.setCreatedAt(LocalDateTime.now());
        chat2.setUsers(Set.of());

        when(chatService.getChats()).thenReturn(List.of(chat1, chat2));

        List<ChatResponse> result = chatController.getChats();

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("General", result.get(0).getName());
        assertEquals(ChatType.GROUP, result.get(0).getChatType());
        assertEquals(2L, result.get(1).getId());
        assertEquals("Direct Chat", result.get(1).getName());
        assertEquals(ChatType.DIRECT, result.get(1).getChatType());

        verify(chatService).getChats();
    }

    @Test
    void getChatById_shouldReturnChat() {
        Chat chat = new Chat();
        chat.setId(1L);
        chat.setName("General");
        chat.setChatType(ChatType.GROUP);
        chat.setCreatedAt(LocalDateTime.now());
        chat.setUsers(Set.of());

        when(chatService.getChatById(1L)).thenReturn(chat);

        ChatResponse result = chatController.getChatById(1L);

        assertEquals(1L, result.getId());
        assertEquals("General", result.getName());
        assertEquals(ChatType.GROUP, result.getChatType());

        verify(chatService).getChatById(1L);
    }

    @Test
    void createChat_shouldCreateChat() {
        CreateChatRequest request = new CreateChatRequest();
        request.setName("New Group");
        request.setChatType(ChatType.GROUP);
        request.setUserIds(List.of(1L, 2L));

        Chat createdChat = new Chat();
        createdChat.setId(1L);
        createdChat.setName("New Group");
        createdChat.setChatType(ChatType.GROUP);
        createdChat.setCreatedAt(LocalDateTime.now());
        createdChat.setUsers(Set.of());

        when(chatService.createChat(ArgumentMatchers.any(CreateChatRequest.class))).thenReturn(createdChat);

        ChatResponse result = chatController.createChat(request);

        assertEquals(1L, result.getId());
        assertEquals("New Group", result.getName());
        assertEquals(ChatType.GROUP, result.getChatType());

        verify(chatService).createChat(ArgumentMatchers.any(CreateChatRequest.class));
    }

    @Test
    void updateChat_shouldUpdateChat() {
        UpdateChatRequest request = new UpdateChatRequest();
        request.setName("Updated Chat");
        request.setChatType(ChatType.GROUP);

        Chat updatedChat = new Chat();
        updatedChat.setId(1L);
        updatedChat.setName("Updated Chat");
        updatedChat.setChatType(ChatType.GROUP);
        updatedChat.setCreatedAt(LocalDateTime.now());
        updatedChat.setUsers(Set.of());

        when(chatService.updateChat(
                ArgumentMatchers.eq(1L),
                ArgumentMatchers.any(UpdateChatRequest.class)
        )).thenReturn(updatedChat);

        ChatResponse result = chatController.updateChat(1L, request);

        assertEquals(1L, result.getId());
        assertEquals("Updated Chat", result.getName());
        assertEquals(ChatType.GROUP, result.getChatType());

        verify(chatService).updateChat(
                ArgumentMatchers.eq(1L),
                ArgumentMatchers.any(UpdateChatRequest.class)
        );
    }

    @Test
    void deleteChat_shouldDeleteChat() {
        doNothing().when(chatService).deleteChat(1L);

        assertDoesNotThrow(() -> chatController.deleteChat(1L));

        verify(chatService).deleteChat(1L);
    }
}