package demo.web;

import demo.domain.User;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class UserController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView home(@AuthenticationPrincipal User user) {
        return new ModelAndView("home", "user", user);
    }

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String signup(User user) {
        return "signup";
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String signup(@Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "signup";
        }

        // TODO:
        return "redirect:/";
    }
}
