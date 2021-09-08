package avito.internassignment.service;

import avito.internassignment.model.Currency;
import avito.internassignment.model.Transaction;
import avito.internassignment.model.User;
import avito.internassignment.repo.TransactionRepository;
import avito.internassignment.repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    private UserRepository userRepo;

    @Mock
    private CurrencyService currencyService;

    @Mock
    private TransactionRepository transactionRepo;

    Long USER_ID = 1L;

    User user;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        user = new User(USER_ID);
        user.setAmount(BigDecimal.valueOf(500));
    }

    @Test
    final void testFindUserById() {
        when(userRepo.findById(USER_ID)).thenReturn(java.util.Optional.of(user));

        User result = userService.findUserById(USER_ID);

        assertNotNull(result);
        assertEquals(user.getAmount(), result.getAmount());
    }

    @Test
    final void testGetCurrentAmount() {
        BigDecimal rubValue = BigDecimal.valueOf(10L);
        BigDecimal eurValue = BigDecimal.valueOf(5L);

        when(userRepo.findById(USER_ID)).thenReturn(java.util.Optional.of(user));
        when(currencyService.findCurrencyById("RUB")).thenReturn(new Currency("RUB", rubValue));
        when(currencyService.findCurrencyById("EUR")).thenReturn(new Currency("EUR", eurValue));

        BigDecimal testAmount = user.getAmount().divide(rubValue, 2, RoundingMode.FLOOR).multiply(eurValue).setScale(2, RoundingMode.FLOOR);

        BigDecimal result = userService.getCurrentAmount(USER_ID, "EUR");

        assertNotNull(result);
        assertEquals(testAmount, result);
    }

    @Test
    final void testDeposit() {
        BigDecimal depositSum = BigDecimal.valueOf(250L);

        when(userRepo.findById(USER_ID)).thenReturn(java.util.Optional.of(user));

        User testUser = new User(1L);
        testUser.setAmount(user.getAmount().add(depositSum));

        User result = userService.deposit(USER_ID, depositSum, null);

        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepo).save(transactionCaptor.capture());

        assertNotNull(result);
        assertEquals(testUser.getId(), result.getId());
        assertEquals(testUser.getAmount(), result.getAmount());
        assertEquals(transactionCaptor.getValue().getUserId(), result.getId());
        assertEquals(transactionCaptor.getValue().getAmount(), depositSum);
    }

    @Test
    final void testWithdraw() {
        BigDecimal withdrawSum = BigDecimal.valueOf(250L);

        when(userRepo.findById(USER_ID)).thenReturn(java.util.Optional.of(user));

        User testUser = new User(1L);
        testUser.setAmount(user.getAmount().subtract(withdrawSum));

        User result = userService.withdraw(USER_ID, withdrawSum, null);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepo).save(userCaptor.capture());

        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepo).save(transactionCaptor.capture());

        assertNotNull(result);
        assertEquals(testUser.getId(), result.getId());
        assertEquals(testUser.getAmount(), result.getAmount());
        assertEquals(userCaptor.getValue().getId(), result.getId());
        assertEquals(userCaptor.getValue().getAmount(), result.getAmount());
        assertEquals(transactionCaptor.getValue().getUserId(), result.getId());
        assertEquals(transactionCaptor.getValue().getAmount(), withdrawSum.negate());
    }

    @Test
    final void transferTest() {
        BigDecimal transferAmount = BigDecimal.valueOf(200L);
        User transferUser = new User(2L);

        User testUserToWithdraw = new User(1L);
        testUserToWithdraw.setAmount(BigDecimal.valueOf(500));
        User testUserToDeposit = new User(2L);

        when(userRepo.findById(USER_ID)).thenReturn(java.util.Optional.of(user));
        when(userRepo.findById(2L)).thenReturn(java.util.Optional.of(transferUser));

        testUserToWithdraw.setAmount(testUserToWithdraw.getAmount().subtract(transferAmount));
        testUserToDeposit.setAmount(testUserToDeposit.getAmount().add(transferAmount));

        List<User> result = userService.transfer(USER_ID, transferUser.getId(), transferAmount, null);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepo, times(2)).save(userCaptor.capture());

        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepo, times(2)).save(transactionCaptor.capture());

        assertNotNull(result);
        assertEquals(testUserToWithdraw.getId(), result.get(0).getId());
        assertEquals(testUserToWithdraw.getAmount(), result.get(0).getAmount());
        assertEquals(testUserToDeposit.getId(), result.get(1).getId());
        assertEquals(testUserToDeposit.getAmount(), result.get(1).getAmount());
        assertEquals(userCaptor.getAllValues().get(0).getId(), result.get(0).getId());
        assertEquals(userCaptor.getAllValues().get(0).getAmount(), result.get(0).getAmount());
        assertEquals(userCaptor.getAllValues().get(1).getId(), result.get(1).getId());
        assertEquals(userCaptor.getAllValues().get(1).getAmount(), result.get(1).getAmount());
        assertEquals(transactionCaptor.getAllValues().get(0).getUserId(), result.get(0).getId());
        assertEquals(transactionCaptor.getAllValues().get(0).getAmount(), transferAmount.negate());
        assertEquals(transactionCaptor.getAllValues().get(1).getUserId(), result.get(1).getId());
        assertEquals(transactionCaptor.getAllValues().get(1).getAmount(), transferAmount);
    }

}