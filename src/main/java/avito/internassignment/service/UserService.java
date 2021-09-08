package avito.internassignment.service;

import avito.internassignment.exceptions.IdenticalTransferIdException;
import avito.internassignment.exceptions.NotEnoughMoneyException;
import avito.internassignment.exceptions.UserNotFoundException;
import avito.internassignment.model.Transaction;
import avito.internassignment.model.Transaction.TransactionType;
import avito.internassignment.model.User;
import avito.internassignment.repo.CurrencyRepository;
import avito.internassignment.repo.TransactionRepository;
import avito.internassignment.repo.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepo;
    private final TransactionRepository transactionRepo;
    private final CurrencyService currencyService;

    public UserService(UserRepository userRepo, TransactionRepository transactionRepo, CurrencyRepository currencyRepo,
                       CurrencyService currencyService) {
        this.userRepo = userRepo;
        this.transactionRepo = transactionRepo;
        this.currencyService = currencyService;
    }

    public User findUserById(Long id) {
        return userRepo.findById(id).orElseThrow(() -> new UserNotFoundException("User by id " + id + " was not found"));
    }

    public BigDecimal getCurrentAmount(Long id, String currency) {
        BigDecimal amount = findUserById(id).getAmount();
        if (currency != null) {
            amount = amount.divide(currencyService.findCurrencyById("RUB").getCurrencyValue(), 2, RoundingMode.FLOOR);
            amount = amount.multiply(currencyService.findCurrencyById(currency).getCurrencyValue()).setScale(2, RoundingMode.FLOOR);
        }
        return amount;
    }

    public User deposit(Long id, BigDecimal amount, String comment) {
        User user;
        try {
            user = findUserById(id);
        } catch (UserNotFoundException e) {
            user = new User(id);
        }
        user.setAmount(user.getAmount().add(amount));
        userRepo.save(user);
        transactionRepo.save(new Transaction(amount, id, TransactionType.DEPOSIT, comment));
        return user;
    }

    public User withdraw(Long id, BigDecimal amount, String comment) {
        User user = findUserById(id);
        BigDecimal userAmount = user.getAmount();
        if (userAmount.compareTo(amount) <= 0) {
            throw new NotEnoughMoneyException("User by id " + id + " doesn't have enough money");
        }
        user.setAmount(userAmount.subtract(amount));
        userRepo.save(user);
        transactionRepo.save(new Transaction(amount.negate(), id, TransactionType.WITHDRAW, comment));
        return user;
    }

    public List<User> transfer(Long idToWithdraw, Long idToDeposit, BigDecimal amount, String comment) {
        if (idToWithdraw == idToDeposit) {
            throw new IdenticalTransferIdException("Users have the same ip");
        }
        User userToWithdraw = findUserById(idToWithdraw);
        BigDecimal userToWithdrawAmount = userToWithdraw.getAmount();
        User userToDeposit;
        try {
            userToDeposit = findUserById(idToDeposit);
        } catch (UserNotFoundException e) {
            userToDeposit = new User(idToDeposit);
        }
        BigDecimal userToDepositAmount = userToDeposit.getAmount();
        if (userToWithdrawAmount.compareTo(amount) <= 0) {
            throw new NotEnoughMoneyException("User by id " + idToWithdraw + " doesn't have enough money");
        }
        userToWithdraw.setAmount(userToWithdrawAmount.subtract(amount));
        userToDeposit.setAmount(userToDepositAmount.add(amount));
        userRepo.save(userToWithdraw);
        userRepo.save(userToDeposit);
        transactionRepo.save(new Transaction(amount.negate(), idToWithdraw, TransactionType.TRANSFER, comment));
        transactionRepo.save(new Transaction(amount, idToDeposit, TransactionType.TRANSFER, comment));
        return Arrays.asList(userToWithdraw, userToDeposit);
    }
}
