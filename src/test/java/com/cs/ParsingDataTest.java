package com.cs;

import com.cs.model.TradeRec;
import com.cs.model.ValidationResultRec;
import com.cs.rule.AllRuleImpl;
import com.cs.rule.IRule;
import com.cs.service.ValidateService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by pasha on 17.02.18.
 */
public class ParsingDataTest {

    String data = "[\n" +
            "    {\"customer\":\"PLUTO1\",\"ccyPair\":\"EURUSD\",\"type\":\"Spot\",\"direction\":\"BUY\",\"tradeDate\":\"2016-08-11\",\"amount1\":1000000.00,\"amount2\":1120000.00,\"rate\":1.12,\"valueDate\":\"2016-08-15\",\"legalEntity\":\"CS Zurich\",\"trader\":\"Johann Baumfiddler\"},\n" +
            "    {\"customer\":\"PLUTO1\",\"ccyPair\":\"EURUSD\",\"type\":\"Spot\",\"direction\":\"SELL\",\"tradeDate\":\"2016-08-11\",\"amount1\":1000000.00,\"amount2\":1120000.00,\"rate\":1.12,\"valueDate\":\"2016-08-22\",\"legalEntity\":\"CS Zurich\",\"trader\":\"Johann Baumfiddler\"},\n" +
            "    {\"customer\":\"PLUTO2\",\"ccyPair\":\"EURUSD\",\"type\":\"Forward\",\"direction\":\"SELL\",\"tradeDate\":\"2016-08-11\",\"amount1\":1000000.00,\"amount2\":1120000.00,\"rate\":1.12,\"valueDate\":\"2016-08-22\",\"legalEntity\":\"CS Zurich\",\"trader\":\"Johann Baumfiddler\"},\n" +
            "    {\"customer\":\"PLUTO2\",\"ccyPair\":\"EURUSD\",\"type\":\"Forward\",\"direction\":\"BUY\",\"tradeDate\":\"2016-08-11\",\"amount1\":1000000.00,\"amount2\":1120000.00,\"rate\":1.12,\"valueDate\":\"2016-08-21\",\"legalEntity\":\"CS Zurich\",\"trader\":\"Johann Baumfiddler\"},\n" +
            "    {\"customer\":\"PLUTO2\",\"ccyPair\":\"EURUSD\",\"type\":\"Forward\",\"direction\":\"BUY\",\"tradeDate\":\"2016-08-11\",\"amount1\":1000000.00,\"amount2\":1120000.00,\"rate\":1.12,\"valueDate\":\"2016-08-08\",\"legalEntity\":\"CS Zurich\",\"trader\":\"Johann Baumfiddler\"},\n" +
            "    {\"customer\":\"PLUT02\",\"ccyPair\":\"EURUSD\",\"type\":\"Forward\",\"direction\":\"BUY\",\"tradeDate\":\"2016-08-11\",\"amount1\":1000000.00,\"amount2\":1120000.00,\"rate\":1.12,\"valueDate\":\"2016-08-08\",\"legalEntity\":\"CS Zurich\",\"trader\":\"Johann Baumfiddler\"},\n" +
            "    {\"customer\":\"PLUTO3\",\"ccyPair\":\"EURUSD\",\"type\":\"Forward\",\"direction\":\"BUY\",\"tradeDate\":\"2016-08-11\",\"amount1\":1000000.00,\"amount2\":1120000.00,\"rate\":1.12,\"valueDate\":\"2016-08-22\",\"legalEntity\":\"CS Zurich\",\"trader\":\"Johann Baumfiddler\"},\n" +
            "    {\"customer\":\"PLUTO1\",\"ccyPair\":\"EURUSD\",\"type\":\"VanillaOption\",\"style\":\"EUROPEAN\",\"direction\":\"BUY\",\"strategy\":\"CALL\",\"tradeDate\":\"2016-08-11\",\"amount1\":1000000.00,\"amount2\":1120000.00,\"rate\":1.12,\"deliveryDate\":\"2016-08-22\",\"expiryDate\":\"2016-08-19\",\"payCcy\":\"USD\",\"premium\":0.20,\"premiumCcy\":\"USD\",\"premiumType\":\"%USD\",\"premiumDate\":\"2016-08-12\",\"legalEntity\":\"CS Zurich\",\"trader\":\"Johann Baumfiddler\"},\n" +
            "    {\"customer\":\"PLUTO2\",\"ccyPair\":\"EURUSD\",\"type\":\"VanillaOption\",\"style\":\"EUROPEAN\",\"direction\":\"SELL\",\"strategy\":\"CALL\",\"tradeDate\":\"2016-08-11\",\"amount1\":1000000.00,\"amount2\":1120000.00,\"rate\":1.12,\"deliveryDate\":\"2016-08-22\",\"expiryDate\":\"2016-08-21\",\"payCcy\":\"USD\",\"premium\":0.20,\"premiumCcy\":\"USD\",\"premiumType\":\"%USD\",\"premiumDate\":\"2016-08-12\",\"legalEntity\":\"CS Zurich\",\"trader\":\"Johann Baumfiddler\"},\n" +
            "    {\"customer\":\"PLUTO1\",\"ccyPair\":\"EURUSD\",\"type\":\"VanillaOption\",\"style\":\"EUROPEAN\",\"direction\":\"BUY\",\"strategy\":\"CALL\",\"tradeDate\":\"2016-08-11\",\"amount1\":1000000.00,\"amount2\":1120000.00,\"rate\":1.12,\"deliveryDate\":\"2016-08-22\",\"expiryDate\":\"2016-08-25\",\"payCcy\":\"USD\",\"premium\":0.20,\"premiumCcy\":\"USD\",\"premiumType\":\"%USD\",\"premiumDate\":\"2016-08-12\",\"legalEntity\":\"CS Zurich\",\"trader\":\"Johann Baumfiddler\"},\n" +
            "    {\"customer\":\"PLUTO1\",\"ccyPair\":\"EURUSD\",\"type\":\"VanillaOption\",\"style\":\"AMERICAN\",\"direction\":\"BUY\",\"strategy\":\"CALL\",\"tradeDate\":\"2016-08-11\",\"amount1\":1000000.00,\"amount2\":1120000.00,\"rate\":1.12,\"deliveryDate\":\"2016-08-22\",\"expiryDate\":\"2016-08-19\",\"excerciseStartDate\":\"2016-08-12\",\"payCcy\":\"USD\",\"premium\":0.20,\"premiumCcy\":\"USD\",\"premiumType\":\"%USD\",\"premiumDate\":\"2016-08-12\",\"legalEntity\":\"CS Zurich\",\"trader\":\"Johann Baumfiddler\"},\n" +
            "    {\"customer\":\"PLUTO2\",\"ccyPair\":\"EURUSD\",\"type\":\"VanillaOption\",\"style\":\"AMERICAN\",\"direction\":\"SELL\",\"strategy\":\"CALL\",\"tradeDate\":\"2016-08-11\",\"amount1\":1000000.00,\"amount2\":1120000.00,\"rate\":1.12,\"deliveryDate\":\"2016-08-22\",\"expiryDate\":\"2016-08-21\",\"excerciseStartDate\":\"2016-08-12\",\"payCcy\":\"USD\",\"premium\":0.20,\"premiumCcy\":\"USD\",\"premiumType\":\"%USD\",\"premiumDate\":\"2016-08-12\",\"legalEntity\":\"CS Zurich\",\"trader\":\"Johann Baumfiddler\"},\n" +
            "    {\"customer\":\"PLUTO1\",\"ccyPair\":\"EURUSD\",\"type\":\"VanillaOption\",\"style\":\"AMERICAN\",\"direction\":\"BUY\",\"strategy\":\"CALL\",\"tradeDate\":\"2016-08-11\",\"amount1\":1000000.00,\"amount2\":1120000.00,\"rate\":1.12,\"deliveryDate\":\"2016-08-22\",\"expiryDate\":\"2016-08-25\",\"excerciseStartDate\":\"2016-08-12\",\"payCcy\":\"USD\",\"premium\":0.20,\"premiumCcy\":\"USD\",\"premiumType\":\"%USD\",\"premiumDate\":\"2016-08-12\",\"legalEntity\":\"CS Zurich\",\"trader\":\"Johann Baumfiddler\"},\n" +
            "    {\"customer\":\"PLUTO1\",\"ccyPair\":\"EURUSD\",\"type\":\"VanillaOption\",\"style\":\"AMERICAN\",\"direction\":\"BUY\",\"strategy\":\"CALL\",\"tradeDate\":\"2016-08-11\",\"amount1\":1000000.00,\"amount2\":1120000.00,\"rate\":1.12,\"deliveryDate\":\"2016-08-22\",\"expiryDate\":\"2016-08-19\",\"excerciseStartDate\":\"2016-08-10\",\"payCcy\":\"USD\",\"premium\":0.20,\"premiumCcy\":\"USD\",\"premiumType\":\"%USD\",\"premiumDate\":\"2016-08-12\",\"legalEntity\":\"CS Zurich\",\"trader\":\"Johann Baumfiddler\"},\n" +
            "    {\"customer\":\"PLUTO3\",\"ccyPair\":\"EURUSD\",\"type\":\"VanillaOption\",\"style\":\"AMERICAN\",\"direction\":\"SELL\",\"strategy\":\"CALL\",\"tradeDate\":\"2016-08-11\",\"amount1\":1000000.00,\"amount2\":1120000.00,\"rate\":1.12,\"deliveryDate\":\"2016-08-22\",\"expiryDate\":\"2016-08-19\",\"excerciseStartDate\":\"2016-08-10\",\"payCcy\":\"USD\",\"premium\":0.20,\"premiumCcy\":\"USD\",\"premiumType\":\"%USD\",\"premiumDate\":\"2016-08-12\",\"legalEntity\":\"CS Zurich\",\"trader\":\"Johann Baumfiddler\"}\n" +
            "]";

    ObjectMapper objectMapper = new ObjectMapper();
    List<Map<String, String>> inputRecs;
    List<Map<String, String>> inputRec1;
    SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD");
    ValidateService service;

    @Before
    public void parseAll() throws IOException {
        inputRecs = objectMapper.readValue(data,  new TypeReference<ArrayList<Map<String, String>>>() {});
        inputRec1 = new ArrayList<>(Arrays.asList(inputRecs.get(0)));
        service = new ValidateService();
    }

    @Test
    public void testAllRule1() {
        IRule rule = new AllRuleImpl();
        for (Map<String, String> m : inputRecs) {
            TradeRec tr = new TradeRec(m);
            System.out.println(m);
            String result = rule.validate(tr);
            if (result != null) {
                System.out.println(result);
            }
        }
    }

    @Test
    public void testAllRulesByService() {
        List<TradeRec> listRecs = new ArrayList<>();

        for (Map<String, String> m : inputRecs) {
            TradeRec tr = new TradeRec(m);
            listRecs.add(tr);
        }

        List<ValidationResultRec> result = service.doValidateCheck(listRecs);
        Assert.assertEquals(result.size(), 8);
    }

    @Test
    public void test1RuleByService() {
        List<TradeRec> listRecs = new ArrayList<>();

        for (Map<String, String> m : inputRec1) {
            TradeRec tr = new TradeRec(m);
            listRecs.add(tr);
        }

        List<ValidationResultRec> result = service.doValidateCheck(listRecs);
        Assert.assertEquals(result.size(), 0);
    }






}
