package avito.internassignment.external;

import java.math.BigDecimal;
import java.util.Map;

public class CurrencyHolder {
    private Map<String, BigDecimal> rates;

    public CurrencyHolder() {
    }

    public Map<String, BigDecimal> getRates() {
        return rates;
    }

    public void setRates(Map<String, BigDecimal> rates) {
        this.rates = rates;
    }
}
