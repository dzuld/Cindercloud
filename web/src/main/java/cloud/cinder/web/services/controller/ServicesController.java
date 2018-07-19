package cloud.cinder.web.services.controller;

import cloud.cinder.common.mail.MailService;
import cloud.cinder.web.services.controller.dto.RequestDevelopmentCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/services")
public class ServicesController {

    @Autowired
    private MailService mailService;

    @GetMapping
    public String index() {
        return "services/index";
    }

    @GetMapping("/looking-for-advice")
    public String requestAdvice(final ModelMap modelMap) {
        return "services/advisor";
    }

    @GetMapping("/smart-contract-development-and-security")
    public String requestDevelopment(final ModelMap modelMap) {
        modelMap.putIfAbsent("requestDevelopmentCommand", new RequestDevelopmentCommand());
        return "services/security-and-development";
    }

    @PostMapping("/smart-contract-development-and-security")
    public String doRequestDevelopment(@Valid  @ModelAttribute("requestDevelopmentCommand") final RequestDevelopmentCommand requestDevelopmentCommand,
                                       final BindingResult bindingResult,
                                       final ModelMap modelMap,
                                       final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return requestDevelopment(modelMap);
        } else {
            mailService.send(requestDevelopmentCommand.getName() + " just messaged you on cindercloud", requestDevelopmentCommand.toContent());
            redirectAttributes.addFlashAttribute("success", "Your message has been sent to Cindercloud");
            return "redirect:/services";
        }
    }
}
