package cloud.cinder.web.tools.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping("/tools")
public class ConverterController {

    @RequestMapping(value = {"/converter", "/wei-converter", "/gwei-converter"}, method = GET)
    public String index() {
        return "tools/converter";
    }
}
