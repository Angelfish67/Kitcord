package ch.samira.tesan.kitcord.user;

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

@Tag(
        name = "Users",
        description = "REST controller for managing users"
)
@RestController
@SecurityRequirement(name = "bearerAuth")
@Validated
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Get current user",
            description = "Returns the currently authenticated user."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Current user loaded successfully",
                    content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "401", description = "Not authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "No permission", content = @Content)
    })
    @PreAuthorize("hasAnyAuthority('ROLE_read', 'ROLE_admin')")
    @GetMapping("/me")
    public User getCurrentUser() {
        return userService.getCurrentUser();
    }

    @Operation(
            summary = "Get user by ID",
            description = "Returns a user by their ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "401", description = "Not authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "No permission", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @PreAuthorize("hasAnyAuthority('ROLE_read', 'ROLE_admin')")
    @GetMapping("/{id}")
    public User getUserById(
            @Parameter(description = "User ID", example = "1", required = true)
            @PathVariable Long id
    ) {
        return userService.getUserById(id);
    }

    @Operation(
            summary = "Create user",
            description = "Creates a new user."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User created successfully",
                    content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "401", description = "Not authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "No permission", content = @Content)
    })
    @PreAuthorize("hasAnyAuthority('ROLE_update', 'ROLE_admin')")
    @PostMapping
    public User createUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Data for the new user",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CreateUserRequest.class))
            )
            @Valid @RequestBody CreateUserRequest request
    ) {
        return userService.createUser(request);
    }
}