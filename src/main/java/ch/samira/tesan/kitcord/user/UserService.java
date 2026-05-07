package ch.samira.tesan.kitcord.user;

import ch.samira.tesan.kitcord.user.dto.CreateUserRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User createUser(CreateUserRequest request) {
        User user = new User();
        checkPassword(request.getPassword());

        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return userRepository.save(user);
    }

    public void checkPassword(String password){
        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }

        if (!password.matches(".*[!@#$%^&*()_+=\\-{}\\[\\]:;\"'<>,.?/].*")) {
            throw new IllegalArgumentException("Password must include a special character");
        }

        if (!password.matches(".*[A-Z].*")) {
            throw new IllegalArgumentException("Password must uppercase letters");
        }

        if (!password.matches(".*[a-z].*")) {
            throw new IllegalArgumentException("Password must lowercase characters");
        }

        if (!password.matches(".*\\d.*")) {
            throw new IllegalArgumentException("Password must include a number");
        }

    }
}