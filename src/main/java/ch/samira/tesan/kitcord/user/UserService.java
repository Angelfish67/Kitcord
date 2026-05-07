package ch.samira.tesan.kitcord.user;

import ch.samira.tesan.kitcord.user.dto.CreateUserRequest;
import ch.samira.tesan.kitcord.user.dto.PasswordChangeRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final KeycloakAdminService keycloakAdminService;

    public UserService(UserRepository userRepository, KeycloakAdminService keycloakAdminService) {
        this.userRepository = userRepository;
        this.keycloakAdminService = keycloakAdminService;
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional
    public User createUser(CreateUserRequest request) {
        checkPassword(request.getPassword());

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        String keycloakId = keycloakAdminService.createUser(
                request.getUsername(),
                request.getPassword()
        );

        User user = new User();
        user.setUsername(request.getUsername());
        user.setKeycloakId(keycloakId);

        return userRepository.save(user);
    }

    public void checkPassword(String password) {
        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }

        if (!password.matches(".*[!@#$%^&*()_+=\\-{}\\[\\]:;\"'<>,.?/].*")) {
            throw new IllegalArgumentException("Password must include a special character");
        }

        if (!password.matches(".*[A-Z].*")) {
            throw new IllegalArgumentException("Password must include uppercase letters");
        }

        if (!password.matches(".*[a-z].*")) {
            throw new IllegalArgumentException("Password must include lowercase letters");
        }

        if (!password.matches(".*\\d.*")) {
            throw new IllegalArgumentException("Password must include a number");
        }
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = getUserById(id);

        keycloakAdminService.deleteUser(user.getKeycloakId());
        userRepository.delete(user);
    }

    public void changePassword(@Valid PasswordChangeRequest passwordChangeRequest) {
        User user = getUserById(passwordChangeRequest.getId());

        checkPassword(passwordChangeRequest.getNewPassword());

        keycloakAdminService.resetPassword(
                user.getKeycloakId(),
                passwordChangeRequest.getNewPassword()
        );
    }
}