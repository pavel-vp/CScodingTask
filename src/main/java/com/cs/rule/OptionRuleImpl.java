package com.cs.rule;

import com.cs.model.TradeRec;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by pasha on 17.02.18.
 */
public class OptionRuleImpl extends AllRuleImpl {

    @Override
    public String validate(TradeRec rec) {
        StringBuilder sb = new StringBuilder();
        // the style can be either American or European
        String style = rec.getMap().get("style");
        if (!"AMERICAN".equals(style) && !"EUROPEAN".equals(style)) {
            sb.append("The style can be either American or European\n");
        }
        // American option style will have in addition the excerciseStartDate, which has to be after the trade date but before the expiry date
        if ("AMERICAN".equals(style)) {
            try {
                Date excerciseStartDate = sdf.parse(rec.getMap().get("excerciseStartDate"));
                Date tradeDate = sdf.parse(rec.getMap().get("tradeDate"));
                Date expiryDate = sdf.parse(rec.getMap().get("expiryDate"));
                if (excerciseStartDate.after(tradeDate) && excerciseStartDate.before(expiryDate)) {
                    sb.append("American option style will have in addition the excerciseStartDate, which has to be after the trade date but before the expiry date\n");
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        // expiry date and premium date shall be before delivery date
        try {
            Date premiumDate = sdf.parse(rec.getMap().get("premiumDate"));
            Date expiryDate = sdf.parse(rec.getMap().get("expiryDate"));
            Date deliveryDate = sdf.parse(rec.getMap().get("deliveryDate"));
            if (premiumDate.after(deliveryDate) && expiryDate.before(deliveryDate)) {
                sb.append("Expiry date and premium date shall be before delivery date\n");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

}
