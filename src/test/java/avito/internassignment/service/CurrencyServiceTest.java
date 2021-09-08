package avito.internassignment.service;

import avito.internassignment.model.Currency;
import avito.internassignment.repo.CurrencyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class CurrencyServiceTest {

    @InjectMocks
    CurrencyService currencyService;

    @Mock
    CurrencyRepository currencyRepo;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    final void testFindCurrencyById() {
        Currency currency = new Currency();
        currency.setCurrencyName("EUR");
        currency.setCurrencyValue(BigDecimal.valueOf(50L));

        when(currencyRepo.findById(anyString())).thenReturn(java.util.Optional.of(currency));

        Currency result = currencyService.findCurrencyById("EUR");

        assertNotNull(result);
        assertEquals(currency, result);
    }
}
