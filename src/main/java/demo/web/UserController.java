package demo.web;

import demo.domain.User;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(@AuthenticationPrincipal @ModelAttribute User user) {
        return "home";
    }

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public ModelAndView signup() {
        return new ModelAndView("signup", "user", new User());
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String signup(User user) {
        // TODO:
        return "redirect:/";
    }
}
