package ch.samira.tesan.kitcord.user;

import ch.samira.tesan.kitcord.user.dto.CreateUserRequest;
import ch.samira.tesan.kitcord.user.dto.PasswordChangeRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private KeycloakAdminService keycloakAdminService;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("samira");
        user.setKeycloakId("keycloak-user-id-123");
    }

    @Test
    void createUserShouldCreateUserInKeycloakAndSaveLocalUser() {
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("samira");
        request.setPassword("Test123!");

        when(userRepository.existsByUsername("samira")).thenReturn(false);
        when(keycloakAdminService.createUser("samira", "Test123!"))
                .thenReturn("keycloak-user-id-123");

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId(1L);
            return savedUser;
        });

        User result = userService.createUser(request);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("samira", result.getUsername());
        assertEquals("keycloak-user-id-123", result.getKeycloakId());

        verify(userRepository).existsByUsername("samira");
        verify(keycloakAdminService).createUser("samira", "Test123!");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUserShouldThrowExceptionWhenUsernameAlreadyExists() {
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("samira");
        request.setPassword("Test123!");

        when(userRepository.existsByUsername("samira")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.createUser(request)
        );

        assertEquals("Username already exists", exception.getMessage());

        verify(userRepository).existsByUsername("samira");
        verify(keycloakAdminService, never()).createUser(anyString(), anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void createUserShouldThrowExceptionWhenPasswordIsTooShort() {
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("samira");
        request.setPassword("T1!");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.createUser(request)
        );

        assertEquals("Password must be at least 8 characters long", exception.getMessage());

        verify(userRepository, never()).existsByUsername(anyString());
        verify(keycloakAdminService, never()).createUser(anyString(), anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void getUserByIdShouldReturnUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("samira", result.getUsername());
        assertEquals("keycloak-user-id-123", result.getKeycloakId());

        verify(userRepository).findById(1L);
    }

    @Test
    void getUserByIdShouldThrowExceptionWhenUserDoesNotExist() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userService.getUserById(99L)
        );

        assertEquals("User not found", exception.getMessage());

        verify(userRepository).findById(99L);
    }

    @Test
    void deleteUserShouldDeleteUserInKeycloakAndDatabase() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.deleteUser(1L);

        verify(userRepository).findById(1L);
        verify(keycloakAdminService).deleteUser("keycloak-user-id-123");
        verify(userRepository).delete(user);
    }

    @Test
    void changePasswordShouldUpdatePasswordInKeycloak() {
        PasswordChangeRequest request = new PasswordChangeRequest();
        request.setId(1L);
        request.setNewPassword("NewTest123!");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.changePassword(request);

        verify(userRepository).findById(1L);
        verify(keycloakAdminService).resetPassword("keycloak-user-id-123", "NewTest123!");
    }
}