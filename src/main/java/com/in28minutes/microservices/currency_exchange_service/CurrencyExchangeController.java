package com.in28minutes.microservices.currency_exchange_service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CurrencyExchangeController {

    private Logger logger = LoggerFactory.getLogger(CurrencyExchangeController.class);

    @Autowired
    private Environment environment;
    
    @Autowired
    private CurrencyExchangeRepository currencyExchangeRepository;

    @GetMapping("currency-exchange/from/{from}/to/{to}")
    public CurrencyExchange retrieveExchangeValue(
        @PathVariable String from, 
        @PathVariable String to) {
        
        // 2024-06-19T18:00:22.845+02:00  INFO [currency-exchange-service,8fcb4f21160e77fa76fb204f1ae13cb8,9395b572f8df97f0] 10756 --- [currency-exchange-service] [nio-8000-exec-1] [8fcb4f21160e77fa76fb204f1ae13cb8-9395b572f8df97f0] c.i.m.c.CurrencyExchangeController       : retrieveExchangeValue called with GBP to INR
        // Hibernate: select ce1_0.id,ce1_0.conversion_multiple,ce1_0.environment,ce1_0.currency_from,ce1_0.currency_to from currency_exchange ce1_0 where ce1_0.currency_from=? and ce1_0.currency_to=?
        logger.info("retrieveExchangeValue called with {} to {}", from, to);
        
        CurrencyExchange currencyExchange = currencyExchangeRepository.findByFromAndTo(from, to);

        if(currencyExchange == null) {
            throw new RuntimeException("unable to Find data for "+ from +" to "+to);
        }

        String port = environment.getProperty("local.server.port");

        //Kuberntes change
        String host = environment.getProperty("HOSTNAME");
        String version = "v12";
        
        currencyExchange.setEnvironment(port+" "+version+" "+host);

        return currencyExchange;
    }
}
