package cloud.cinder.web.wallet.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@Controller
public class LogoutController {

    @GetMapping("/logout")
    public String logout(final HttpServletRequest httpServletRequest) {
        SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
        HttpSession session = httpServletRequest.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/wallet/login";
    }
}
