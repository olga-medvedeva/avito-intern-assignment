package avito.internassignment.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "currency")
public class Currency {

    @Id
    private String currencyName;

    private BigDecimal currencyValue;

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public BigDecimal getCurrencyValue() {
        return currencyValue;
    }

    public Currency() {
    }

    public Currency(String currencyName, BigDecimal currencyValue) {
        this.currencyName = currencyName;
        this.currencyValue = currencyValue;
    }

    public void setCurrencyValue(BigDecimal currencyValue) {
        this.currencyValue = currencyValue;
    }


}
