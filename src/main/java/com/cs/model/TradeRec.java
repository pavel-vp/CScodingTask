package com.cs.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * Created by pasha on 17.02.18.
 */
public class TradeRec {
    @Getter
    @Setter
    private Map<String,String> map;

    public TradeRec(Map<String, String> m) {
        this.map = m;
    }
}
