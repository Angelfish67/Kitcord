package ch.samira.tesan.kitcord.chat;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chats")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping
    public List<ChatResponse> getChats() {
        return chatService.getChats()
                .stream()
                .map(ChatResponse::new)
                .toList();
    }

    @GetMapping("/{id}")
    public ChatResponse getChatById(@PathVariable Long id) {
        return new ChatResponse(chatService.getChatById(id));
    }

    @PostMapping
    public ChatResponse createChat(@RequestBody CreateChatRequest request) {
        return new ChatResponse(chatService.createChat(request));
    }

    @PutMapping("/{id}")
    public ChatResponse updateChat(@PathVariable Long id, @RequestBody Chat chat) {
        return new ChatResponse(chatService.updateChat(id, chat));
    }

    @DeleteMapping("/{id}")
    public void deleteChat(@PathVariable Long id) {
        chatService.deleteChat(id);
    }

    @PostMapping("/{id}/users/{userId}")
    public ChatResponse addUserToChat(@PathVariable Long id, @PathVariable Long userId) {
        return new ChatResponse(chatService.addUserToChat(id, userId));
    }

    @DeleteMapping("/{id}/users/{userId}")
    public ChatResponse removeUserFromChat(@PathVariable Long id, @PathVariable Long userId) {
        return new ChatResponse(chatService.removeUserFromChat(id, userId));
    }
}