package demo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;

import static demo.SecurityRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class SecurityTests {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private Filter springSecurityFilterChain;

    private MockMvc mvc;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .addFilters(springSecurityFilterChain)
                .build();

        // This is a workaround for an issue related to thymeleaf security dialect throwing error when accessing application context.
        // Refer to: http://stackoverflow.com/questions/24999469/how-to-unit-test-a-secured-controller-which-uses-thymeleaf-without-getting-temp
        context.getServletContext().setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, context);
        // End of workaround
    }

    @Test
    public void homeRequiresLogin() throws Exception {
        mvc.perform(get("/"))
                .andExpect(redirectedUrl("http://localhost/signin"));
    }

    @Test
    public void loginWithInvalidUsernamePassword() throws Exception {
        RequestBuilder request = post("/signin")
                .param("username", "jeff")
                .param("password", "invalid")
                .with(csrf());

        mvc.perform(request)
                .andExpect(redirectedUrl("/signin?error"));
    }

    @Test
    public void loginNotAllowedForDisabledUser() throws Exception {
        RequestBuilder request = post("/signin")
                .param("username", "test")
                .param("password", "test1234")
                .with(csrf());

        mvc.perform(request)
                .andExpect(redirectedUrl("/signin?error"));
    }

    @Test
    public void loginRequiresCsrf() throws Exception {
        RequestBuilder request = post("/signin")
                .param("username", "jeff")
                .param("password", "test1234");

        mvc.perform(request)
                .andExpect(status().isForbidden());
    }

    private ResultActions login() throws Exception {
        RequestBuilder request = post("/signin")
                .param("username", "jeff")
                .param("password", "test1234")
                .with(csrf());

        return mvc.perform(request);
    }

    @Test
    public void loginWithValidUsernamePassword() throws Exception {
        login().andExpect(redirectedUrl("/"));
    }

    @Test
    public void logoutRequiresCsrf() throws Exception {
        login();

        mvc.perform(post("/logout"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void logoutWithCsrf() throws Exception {
        login();

        mvc.perform(post("/logout").with(csrf()))
                .andExpect(redirectedUrl("/signin?logout"));
    }

    @Test
    public void signupPage() throws Exception {
        mvc.perform(get("/signup"))
                .andExpect(status().isOk())
                .andExpect(view().name("signup"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    public void signupWithInvalidData() throws Exception {

    }

    @Test
    public void signupWithValidData() throws Exception {

    }
}
