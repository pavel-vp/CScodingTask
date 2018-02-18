package com.cs.rule;

import com.cs.model.TradeRec;

import java.text.SimpleDateFormat;
import java.util.Currency;

/**
 * Created by pasha on 17.02.18.
 */
public interface IRule {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    String validate(TradeRec rec);




}
