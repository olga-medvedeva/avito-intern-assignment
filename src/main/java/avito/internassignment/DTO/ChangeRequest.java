package avito.internassignment.DTO;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import java.math.BigDecimal;

@Validated
public class ChangeRequest {
    @Min(value = 1,
    message = "Not a valid id")
    private Long userId;

    @DecimalMin(value = "0.00", inclusive = false, message = "Changing amount of money must be more than 0")
    @Digits(integer = Integer.MAX_VALUE,  fraction = 2, message = "Amount must have a fraction of no more than 2 digits")
    private BigDecimal amount;
    private String comment;

    public ChangeRequest() {
    }

    public Long getUserId() {
        return userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getComment() {
        return comment;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
