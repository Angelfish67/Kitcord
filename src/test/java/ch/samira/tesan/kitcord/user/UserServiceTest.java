package ch.samira.tesan.kitcord.user;

import ch.samira.tesan.kitcord.user.dto.CreateUserRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserController userController;

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
