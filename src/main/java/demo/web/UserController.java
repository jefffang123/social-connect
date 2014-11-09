package demo.web;

import demo.domain.User;
import demo.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping("/")
    public ModelAndView home(Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username);

        return new ModelAndView("home", "user", user);
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
