package ch.samira.tesan.kitcord.user;

import ch.samira.tesan.kitcord.user.dto.CreateUserRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserController userController;

    @Test
    void createUser() {

        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("Samira");
        createUserRequest.setPassword("Ab1!aaaaa");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("Samira");

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> {
                    User user = invocation.getArgument(0);
                    user.setId(1L);
                    return user;
                });

        User result = userService.createUser(createUserRequest);

        assertEquals(1L, result.getId());
        assertEquals("Samira", result.getUsername());
        assertTrue(
                new BCryptPasswordEncoder()
                        .matches("Ab1!aaaaa", result.getPassword())
        );

    }

    @Test
    void createUserWithTooShortPassword() {

        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("Samira");
        createUserRequest.setPassword("Ab1!");

        assertThrows(
                IllegalArgumentException.class,
                // () -> userService.checkPassword("Ab1!")
                () -> userService.createUser(createUserRequest)
        );

        verifyNoInteractions(userRepository);
    }

    @Test
    void createUserWithNoSpecialCharacters() {

        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("Samira");
        createUserRequest.setPassword("Abbbb333");

        assertThrows(
                IllegalArgumentException.class,
                () -> userService.createUser(createUserRequest)
        );

        verifyNoInteractions(userRepository);
    }

    @Test
    void createUserWithNoUppercaseLetters() {

        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("Samira");
        createUserRequest.setPassword("abbbbbbb1!");

        assertThrows(
                IllegalArgumentException.class,
                () -> userService.createUser(createUserRequest)
        );

        verifyNoInteractions(userRepository);
    }

    @Test
    void createUserWithNoLowercaseLetters() {

        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("Samira");
        createUserRequest.setPassword("ABBBBBB1!");

        assertThrows(
                IllegalArgumentException.class,
                () -> userService.createUser(createUserRequest)
        );

        verifyNoInteractions(userRepository);
    }

    @Test
    void createUserWithNoNumbers() {

        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("Samira");
        createUserRequest.setPassword("ABBBBBBb!");

        assertThrows(
                IllegalArgumentException.class,
                () -> userService.createUser(createUserRequest)
        );

        verifyNoInteractions(userRepository);
    }

}
