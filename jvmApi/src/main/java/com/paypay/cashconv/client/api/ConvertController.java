package io.telereso.cashconv.client.api;


import io.telereso.cashconv.client.CashconvClientManager;
import io.telereso.cashconv.models.Amount;
import io.telereso.cashconv.models.Currency;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

import javax.websocket.server.PathParam;

@Controller
// Main class
public class ConvertController {

    @RequestMapping("")
    @ResponseBody
    public String getString() {
        return "Hello world!";
    }

    @RequestMapping("/convert/{value}/{from}/{to}")
    @ResponseBody
    public Map<String, String> convert(@PathVariable("value") String value,
                                       @PathVariable("from") String from,
                                       @PathVariable("to") String to) {


        String converted = CashconvClientManager
                .getInstance()
                .convert(new Amount(
                        Currency.from(from.toUpperCase()),
                        value,
                        Currency.from(to.toUpperCase()),
                        "-"
                )).getConvertedValue();
        HashMap<String, String> map = new HashMap();
        map.put("converted", converted);
        return map;
    }

}