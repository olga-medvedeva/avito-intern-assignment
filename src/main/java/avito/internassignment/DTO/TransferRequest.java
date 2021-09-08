package avito.internassignment.DTO;

import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import java.math.BigDecimal;

public class TransferRequest {
    @Min(value = 1,
            message = "Not a valid id")
    private Long idToWithdraw;

    @Min(value = 1,
            message = "Not a valid id")
    private Long idToDeposit;

    @DecimalMin(value = "0.00", inclusive = false, message = "Transferred amount of money must be more than 0")
    @Digits(integer = Integer.MAX_VALUE,  fraction = 2, message = "Amount must have a fraction of no more than 2 digits")
    private BigDecimal amount;
    private String comment;

    public TransferRequest() {
    }

    public Long getIdToWithdraw() {
        return idToWithdraw;
    }

    public Long getIdToDeposit() {
        return idToDeposit;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getComment() {
        return comment;
    }

    public void setIdToWithdraw(Long idToWithdraw) {
        this.idToWithdraw = idToWithdraw;
    }

    public void setIdToDeposit(Long idToDeposit) {
        this.idToDeposit = idToDeposit;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
