package com.cs.rule;

import com.cs.model.TradeRec;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by pasha on 17.02.18.
 */
public class AllRuleImpl implements IRule {
    private static final Set<String> supportedParties = new HashSet<>(Arrays.asList("PLUTO1","PLUTO2"));
    private static final Set<String> supportedCurrencies = new HashSet<>();
    {
        supportedCurrencies.addAll(
                Currency.getAvailableCurrencies()
                        .stream()
                        .map(Currency::getCurrencyCode)
                        .collect(Collectors.toList()));
    }
    private static final Set<String> supportedCurrenciesCombinations = new HashSet<>();
    {
        for (Currency c1 : Currency.getAvailableCurrencies()) {
            supportedCurrenciesCombinations.addAll(
                    Currency.getAvailableCurrencies()
                            .stream()
                            .map(c2 -> c1.getCurrencyCode() + c2.getCurrencyCode())
                            .collect(Collectors.toList()));
        }
    }


    @Override
    public String validate(TradeRec rec) {
        StringBuilder sb = new StringBuilder();
        //- value date cannot be before trade date
        try {
            String validDate = rec.getMap().get("valueDate");
            String tradeDate = rec.getMap().get("tradeDate");
            if (validDate != null && tradeDate != null) {
                Date validDateD = sdf.parse(validDate);
                Date tradeDateD = sdf.parse(tradeDate);
                if (validDateD.before(tradeDateD)) {
                    sb.append("Value date cannot be before trade date\n");
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


        //- value date cannot fall on weekend or non-working day for currency
        try {
            String validDate = rec.getMap().get("valueDate");
            if (validDate != null) {
                Date validDateD = sdf.parse(validDate);

                Calendar c = Calendar.getInstance();
                c.setTime(validDateD);

                int dow = c.get(Calendar.DAY_OF_WEEK);
                if (dow == 1 || dow == 7) {
                    sb.append("Value date cannot fall on weekend or non-working day for currency\n");
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //- if the counterparty is one of the supported ones
        String party = rec.getMap().get("customer");
        if (!supportedParties.contains(party)) {
            sb.append("The counterparty is not of the supported ones\n");
        }


        //- validate currencies if they are valid ISO codes (ISO 4217)
        String ccyPair = rec.getMap().get("ccyPair");
        String payCcy = rec.getMap().get("payCcy");
        String premiumCcy = rec.getMap().get("premiumCcy");
        if (ccyPair != null && !supportedCurrenciesCombinations.contains(ccyPair)) {
            sb.append("The ccyPair value "+ccyPair+" is not a valid ISO code (ISO 4217)\n");
        }
        if (payCcy != null && !supportedCurrencies.contains(payCcy)) {
            sb.append("The payCcy value "+payCcy+" is not a valid ISO code (ISO 4217)\n");
        }
        if (premiumCcy != null && !supportedCurrencies.contains(premiumCcy)) {
            sb.append("The premiumCcy value "+premiumCcy+" is not a valid ISO code (ISO 4217)\n");
        }



        return sb.toString();
    }
}
