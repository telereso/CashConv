/*
 * MIT License
 *
 * Copyright (c) 2023 Telereso
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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