package avito.internassignment.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "transaction")
public class Transaction {

    @Id
    @Column(nullable = false, updatable = false)
    @SequenceGenerator(name = "transaction_sequence",
            sequenceName = "transaction_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "transaction_sequence")
    private Long id;

    @Column(nullable = false, updatable = false)
    private Date date;

    @Column(nullable = false, updatable = false)
    private BigDecimal amount;

    @Column(nullable = false, updatable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private TransactionType transactionType;

    private String comment;

    public Transaction() {
    }

    public Transaction(BigDecimal amount, Long userId, TransactionType transactionType, String comment) {
        date = new Date();
        this.amount = amount;
        this.userId = userId;
        this.transactionType = transactionType;
        this.comment = comment;
    }

    public enum TransactionType {
        DEPOSIT,
        WITHDRAW,
        TRANSFER
    }

    public Long getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Long getUserId() {
        return userId;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public String getComment() {
        return comment;
    }
}
