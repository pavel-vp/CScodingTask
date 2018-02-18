package com.cs.web;

import com.cs.model.TradeRec;
import com.cs.model.ValidationResultRec;
import com.cs.service.ValidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by pasha on 17.02.18.
 */
@RestController
@RequestMapping(value = "validator")
public class ValidateController {

    ValidateService validateService;

    @Autowired
    public ValidateController(ValidateService validateService) {
        this.validateService = validateService;
    }

    @PostMapping("")
    public @ResponseBody List<ValidationResultRec> parseAndValidate(@RequestBody List<Map<String,String>> data) throws IOException {

        List<TradeRec> recs = new ArrayList<>();
        for (Map<String, String> m : data) {
            recs.add(new TradeRec(m));
        }

        List<ValidationResultRec> result = validateService.doValidateCheck(recs);

        return result;
    }


}
