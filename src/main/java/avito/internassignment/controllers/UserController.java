package avito.internassignment.controllers;

import avito.internassignment.DTO.ChangeRequest;
import avito.internassignment.DTO.TransferRequest;
import avito.internassignment.model.User;
import avito.internassignment.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}/check")
    public BigDecimal getCurrentAmount(@PathVariable("id") Long id, @RequestParam(required = false) String currency) {
        return userService.getCurrentAmount(id, currency);
    }

    @PostMapping("/deposit")
    public User deposit(@Valid @RequestBody ChangeRequest changeRequest) {
        return userService.deposit(changeRequest.getUserId(), changeRequest.getAmount(), changeRequest.getComment());
    }

    @PostMapping("/withdraw")
    public User withdraw(@Valid @RequestBody ChangeRequest changeRequest) {
        return userService.withdraw(changeRequest.getUserId(), changeRequest.getAmount(), changeRequest.getComment());
    }

    @PostMapping("/transfer")
    public List<User> transfer(@Valid @RequestBody TransferRequest transferRequest) {
        return userService.transfer(transferRequest.getIdToWithdraw(), transferRequest.getIdToDeposit(), transferRequest.getAmount(), transferRequest.getComment());
    }
}
