package ch.samira.tesan.kitcord.chat;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "ChatController",
        description = "REST controller for managing direct and group chats"
)
@SecurityRequirement(name = "bearerAuth")
@Validated
@RestController
@RequestMapping("/chats")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @Operation(
            summary = "Get all chats",
            description = "Returns all saved chats."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Chats loaded successfully",
                    content = @Content(schema = @Schema(implementation = ChatResponse.class))),
            @ApiResponse(responseCode = "401", description = "Not authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "No permission", content = @Content)
    })
    @PreAuthorize("hasAnyAuthority('ROLE_read', 'ROLE_admin')")
    @GetMapping
    public List<ChatResponse> getChats() {
        return chatService.getChats()
                .stream()
                .map(ChatResponse::new)
                .toList();
    }

    @Operation(
            summary = "Get chat by ID",
            description = "Returns one chat by its ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Chat found",
                    content = @Content(schema = @Schema(implementation = ChatResponse.class))),
            @ApiResponse(responseCode = "401", description = "Not authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "No permission", content = @Content),
            @ApiResponse(responseCode = "404", description = "Chat not found", content = @Content)
    })
    @PreAuthorize("hasAnyAuthority('ROLE_read', 'ROLE_admin')")
    @GetMapping("/{id}")
    public ChatResponse getChatById(
            @Parameter(description = "Chat ID", example = "1", required = true)
            @PathVariable Long id
    ) {
        return new ChatResponse(chatService.getChatById(id));
    }

    @Operation(
            summary = "Create chat",
            description = "Creates a new direct chat or group chat."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Chat created successfully",
                    content = @Content(schema = @Schema(implementation = ChatResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "401", description = "Not authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "No permission", content = @Content)
    })
    @PreAuthorize("hasAnyAuthority('ROLE_update', 'ROLE_admin')")
    @PostMapping
    public ChatResponse createChat(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Data for the new chat",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CreateChatRequest.class))
            )
            @Valid @RequestBody CreateChatRequest request
    ) {
        return new ChatResponse(chatService.createChat(request));
    }

    @Operation(
            summary = "Update chat",
            description = "Updates the name or type of an existing chat."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Chat updated successfully",
                    content = @Content(schema = @Schema(implementation = ChatResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "401", description = "Not authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "No permission", content = @Content),
            @ApiResponse(responseCode = "404", description = "Chat not found", content = @Content)
    })
    @PreAuthorize("hasAnyAuthority('ROLE_update', 'ROLE_admin')")
    @PutMapping("/{id}")
    public ChatResponse updateChat(
            @Parameter(description = "Chat ID", example = "1", required = true)
            @PathVariable Long id,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated chat data",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UpdateChatRequest.class))
            )
            @Valid @RequestBody UpdateChatRequest request
    ) {
        return new ChatResponse(chatService.updateChat(id, request));
    }

    @Operation(
            summary = "Delete chat",
            description = "Deletes an existing chat by its ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Chat deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Not authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "No permission", content = @Content),
            @ApiResponse(responseCode = "404", description = "Chat not found", content = @Content)
    })
    @PreAuthorize("hasAuthority('ROLE_admin')")
    @DeleteMapping("/{id}")
    public void deleteChat(
            @Parameter(description = "Chat ID", example = "1", required = true)
            @PathVariable Long id
    ) {
        chatService.deleteChat(id);
    }

    @Operation(
            summary = "Add user to chat",
            description = "Adds an existing user to an existing chat."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User added to chat successfully",
                    content = @Content(schema = @Schema(implementation = ChatResponse.class))),
            @ApiResponse(responseCode = "401", description = "Not authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "No permission", content = @Content),
            @ApiResponse(responseCode = "404", description = "Chat or user not found", content = @Content)
    })
    @PreAuthorize("hasAnyAuthority('ROLE_update', 'ROLE_admin')")
    @PostMapping("/{id}/users/{userId}")
    public ChatResponse addUserToChat(
            @Parameter(description = "Chat ID", example = "1", required = true)
            @PathVariable Long id,

            @Parameter(description = "User ID", example = "2", required = true)
            @PathVariable Long userId
    ) {
        return new ChatResponse(chatService.addUserToChat(id, userId));
    }

    @Operation(
            summary = "Remove user from chat",
            description = "Removes an existing user from an existing chat."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User removed from chat successfully",
                    content = @Content(schema = @Schema(implementation = ChatResponse.class))),
            @ApiResponse(responseCode = "401", description = "Not authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "No permission", content = @Content),
            @ApiResponse(responseCode = "404", description = "Chat or user not found", content = @Content)
    })
    @PreAuthorize("hasAnyAuthority('ROLE_update', 'ROLE_admin')")
    @DeleteMapping("/{id}/users/{userId}")
    public ChatResponse removeUserFromChat(
            @Parameter(description = "Chat ID", example = "1", required = true)
            @PathVariable Long id,

            @Parameter(description = "User ID", example = "2", required = true)
            @PathVariable Long userId
    ) {
        return new ChatResponse(chatService.removeUserFromChat(id, userId));
    }
}