package avito.internassignment.controllers;

import avito.internassignment.DTO.ChangeRequest;
import avito.internassignment.DTO.TransferRequest;
import avito.internassignment.model.User;
import avito.internassignment.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;


import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    @InjectMocks
    UserController userController;

    @Mock
    UserService userService;

    User user;

    final Long USER_ID = 1L;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        user = new User(USER_ID);
        user.setAmount(BigDecimal.valueOf(500));
    }

    @Test
    final void testGetCurrentAmount() {
        Mockito.when(userService.getCurrentAmount(USER_ID, null)).thenReturn(user.getAmount());

        BigDecimal result = userController.getCurrentAmount(USER_ID, null);

        assertNotNull(result);
        assertEquals(user.getAmount(), result);
    }

    @Test
    final void testDeposit() {
        Mockito.when(userService.deposit(USER_ID, user.getAmount(), null)).thenReturn(user);

        ChangeRequest changeRequest = new ChangeRequest();
        changeRequest.setUserId(USER_ID);
        changeRequest.setAmount(user.getAmount());
        User result = userController.deposit(changeRequest);

        assertNotNull(result);
        assertEquals(user, result);
    }

    @Test
    final void testWithdraw() {
        Mockito.when(userService.withdraw(USER_ID, user.getAmount(), null)).thenReturn(user);

        ChangeRequest changeRequest = new ChangeRequest();
        changeRequest.setUserId(USER_ID);
        changeRequest.setAmount(user.getAmount());
        User result = userController.withdraw(changeRequest);

        assertNotNull(result);
        assertEquals(user, result);
    }

    @Test
    final void testTransfer() {
        User userToTransfer = new User(2L);
        Mockito.when(userService.transfer(USER_ID, userToTransfer.getId(), user.getAmount(), null)).thenReturn(Arrays.asList(user, userToTransfer));

        TransferRequest transferRequest = new TransferRequest();
        transferRequest.setIdToWithdraw(USER_ID);
        transferRequest.setIdToDeposit(userToTransfer.getId());
        transferRequest.setAmount(user.getAmount());

        List<User> result = userController.transfer(transferRequest);

        assertNotNull(result);
        assertEquals(Arrays.asList(user, userToTransfer), result);
    }
}