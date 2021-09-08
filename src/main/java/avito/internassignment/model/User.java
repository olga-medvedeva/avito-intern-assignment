package avito.internassignment.model;


import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "user")
public class User {

    @Id
    @Column(nullable = false, updatable = false)
    private Long id;

    private BigDecimal amount;

    public User(Long id) {
        amount = new BigDecimal(0);
        this.id = id;
    }

    public User() {
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
