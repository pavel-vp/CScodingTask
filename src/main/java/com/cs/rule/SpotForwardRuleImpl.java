package com.cs.rule;

import com.cs.model.TradeRec;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by pasha on 17.02.18.
 */
public class SpotForwardRuleImpl extends AllRuleImpl {

    private static Date currentDate;

    {
        try {
            currentDate = sdf.parse("2016-10-09");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String validate(TradeRec rec) {
        // validate the value date against the product type
        String type = rec.getMap().get("type");
        String valueDate = rec.getMap().get("valueDate");
        if ("Spot".equals(type) || "Forward".equals(type)) {
                try {
                    currentDate.before(sdf.parse(valueDate));
                } catch (Exception e) {
                    return "Validation the value date against the product type went wrong\n";
                }
        }
        return "";
    }
}
