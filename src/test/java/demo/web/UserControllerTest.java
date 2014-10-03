package demo.web;

import demo.domain.User;
import demo.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.ModelAndViewAssert.assertModelAttributeValue;
import static org.springframework.test.web.ModelAndViewAssert.assertViewName;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    private User user;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Before
    public void initTestUser() {
        user = new User("jeff");
    }

    @Test
    public void home() {
        String username = user.getUsername();

        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn(username);
        when(userService.findByUsername(username)).thenReturn(user);

        ModelAndView mav = userController.home(principal);

        verify(userService).findByUsername(username);
        assertViewName(mav, "home");
        assertModelAttributeValue(mav, "user", user);
    }
}
