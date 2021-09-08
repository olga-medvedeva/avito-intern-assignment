package avito.internassignment.service;

import avito.internassignment.exceptions.CurrencyNotFoundException;
import avito.internassignment.external.CurrencyHolder;
import avito.internassignment.model.Currency;
import avito.internassignment.repo.CurrencyRepository;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

@Service
@EnableScheduling
public class CurrencyService {
    private final CurrencyRepository currencyRepo;

    public CurrencyService(CurrencyRepository currencyRepo) {
        this.currencyRepo = currencyRepo;
    }

    public Currency findCurrencyById(String id) {
        return currencyRepo.findById(id).orElseThrow(() -> new CurrencyNotFoundException("Currency by id " + id + " was not found"));
    }

    @Scheduled(fixedDelay = 3600000)
    public void currencyUpdate() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://api.exchangeratesapi.io/v1/latest?access_key=fa445f238965aa1255ab3cfb02bb84ba&format=1";
        CurrencyHolder currencyHolder = restTemplate.getForEntity(url, CurrencyHolder.class).getBody();

        if (currencyHolder != null) {
            for (Map.Entry<String, BigDecimal> entry : currencyHolder.getRates().entrySet()) {
                Currency currency;
                try {
                    currency = findCurrencyById(entry.getKey());
                } catch (CurrencyNotFoundException e) {
                    currency = new Currency(entry.getKey(), entry.getValue());
                }
                currencyRepo.save(currency);
            }
        }
    }
}
