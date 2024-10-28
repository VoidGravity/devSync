package com.devsync.user;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.devsync.dao.UserDAO;
import com.devsync.model.User;
import com.devsync.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserDAO userDAO;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userDAO);

        // Use reflection to set the mocked DAO
        try {
            java.lang.reflect.Field field = UserService.class.getDeclaredField("userDAO");
            field.setAccessible(true);
            field.set(userService, userDAO);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testFindExistingUser() {
        // Arrange
        String username = "abdellah";
        User expectedUser = new User();
        expectedUser.setUsername(username);
        expectedUser.setLastName("abde");
        expectedUser.setFirstName("brc");
        expectedUser.setEmail("abdellahbrc@gmail.com");

        when(userDAO.findUserByUsername(username)).thenReturn(expectedUser);

        // Act
        User actualUser = userService.getUserByUsername(username);

        // Assert
        assertNotNull(actualUser);
        assertEquals(expectedUser.getUsername(), actualUser.getUsername());
        assertEquals(expectedUser.getLastName(), actualUser.getLastName());
        assertEquals(expectedUser.getFirstName(), actualUser.getFirstName());
        assertEquals(expectedUser.getEmail(), actualUser.getEmail());

        verify(userDAO).findUserByUsername(username);
    }

    @Test
    void testCreateUser() {
        // Arrange
        User newUser = new User();
        newUser.setUsername("newuser");
        newUser.setLastName("Doe");
        newUser.setFirstName("John");
        newUser.setPassword("password123");
        newUser.setEmail("john.doe@example.com");

        when(userDAO.create(newUser)).thenReturn(true);  // Changed to return boolean
        when(userDAO.findUserByUsername(newUser.getUsername())).thenReturn(null); // User doesn't exist yet

        // Act
        User createdUser = userService.createUser(newUser);

        // Assert
        assertNotNull(createdUser);
        assertEquals(newUser.getUsername(), createdUser.getUsername());
        assertEquals(newUser.getLastName(), createdUser.getLastName());
        assertEquals(newUser.getFirstName(), createdUser.getFirstName());
        assertEquals(newUser.getEmail(), createdUser.getEmail());

        verify(userDAO).create(newUser);
    }

    @Test
    void testCreateUser_NullUser() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userService.createUser(null));
        verify(userDAO, never()).create(any());
    }

    @Test
    void testGetUserByUsername_NullUsername() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userService.getUserByUsername(null));
        verify(userDAO, never()).findUserByUsername(any());
    }

    @Test
    void testGetUserByUsername_EmptyUsername() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userService.getUserByUsername(""));
        verify(userDAO, never()).findUserByUsername(any());
    }

    @Test
    void testCreateUser_ExistingUsername() {
        // Arrange
        User newUser = new User();
        newUser.setUsername("existinguser");
        newUser.setPassword("password123");

        when(userDAO.findUserByUsername(newUser.getUsername())).thenReturn(new User()); // User already exists

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userService.createUser(newUser));
        verify(userDAO, never()).create(any());
    }
}