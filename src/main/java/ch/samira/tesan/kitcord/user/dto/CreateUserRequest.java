package ch.samira.tesan.kitcord.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateUserRequest {

    @Schema(example = "samira")
    @NotBlank
    @Size(min = 3, max = 100)
    private String username;

    @NotNull
    private String password;

    public CreateUserRequest() {
    }

    public CreateUserRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}