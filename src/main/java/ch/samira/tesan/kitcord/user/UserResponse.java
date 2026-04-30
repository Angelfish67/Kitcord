package ch.samira.tesan.kitcord.user;

import ch.samira.tesan.kitcord.user.enums.Role;

public class UserResponse {

    private Long id;
    private String username;
    private Role role;

    public UserResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.role = user.getRole();
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
}