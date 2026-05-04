package ch.samira.tesan.kitcord.user;

import ch.samira.tesan.kitcord.user.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateUserRequest {

    @Schema(example = "samira")
    @NotBlank
    @Size(min = 3, max = 100)
    private String username;

    @Schema(example = "USER")
    @NotNull
    private Role role;

    public CreateUserRequest() {
    }

    public CreateUserRequest(String username, Role role) {
        this.username = username;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public Role getRole() {
        return role;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}