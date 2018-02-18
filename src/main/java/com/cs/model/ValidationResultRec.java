package com.cs.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by pasha on 18.02.18.
 */
public class ValidationResultRec implements Serializable {
    @Getter
    @Setter
    private TradeRec tradeRec;
    @Getter
    @Setter
    private String validateResult;

    public ValidationResultRec(TradeRec rec, String msg) {
        this.tradeRec = rec;
        this.validateResult = msg;
    }
}
