package com.cs.service;

import com.cs.model.TradeRec;
import com.cs.model.ValidationResultRec;
import com.cs.rule.AllRuleImpl;
import com.cs.rule.IRule;
import com.cs.rule.OptionRuleImpl;
import com.cs.rule.SpotForwardRuleImpl;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by pasha on 17.02.18.
 */
@Service
public class ValidateService {
    private static Map<String, List<IRule>> rulesbyType = new HashMap<>();
    {
        rulesbyType.put("Spot", Arrays.asList(new AllRuleImpl(), new SpotForwardRuleImpl()));
        rulesbyType.put("Forward", Arrays.asList(new AllRuleImpl(), new SpotForwardRuleImpl() ));
        rulesbyType.put("VanillaOption", Arrays.asList(new AllRuleImpl(), new OptionRuleImpl() ));
    }

    public List<ValidationResultRec> doValidateCheck(List<TradeRec> data) {
        List<ValidationResultRec> resultRecs = new ArrayList<>();
        for (TradeRec rec : data) {
            String type = rec.getMap().get("type");
            List<IRule> rules = rulesbyType.get(type);
            if (rules == null) {
                resultRecs.add(new ValidationResultRec(rec, "No validation rule for type " + type));
                continue;
            }

            StringBuilder sb = new StringBuilder();
            for (IRule rule : rules) {
                String res = rule.validate(rec);
                if (res != null && !"".equals(res)) {
                    sb.append(res);
                }
            }
            if (sb.length() > 0) {
                resultRecs.add(new ValidationResultRec(rec, sb.toString()));
            }
        }
        return resultRecs;
    }

}
