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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;

import static demo.SecurityRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class UserTests {

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
    }

    @Test
    public void homeRequiresLogin() throws Exception {
        mvc.perform(get("/"))
                .andExpect(result -> {
                    status().isMovedTemporarily().match(result);
                    redirectedUrl("http://localhost/signin").match(result);
                });
    }

    @Test
    public void loginWithInvalidUsernamePassword() throws Exception {
        RequestBuilder request = post("/signin")
                .param("username", "jeff")
                .param("password", "invalid")
                .with(csrf());

        mvc.perform(request)
                .andExpect(result -> {
                    status().isMovedTemporarily().match(result);
                    redirectedUrl("/signin?error").match(result);
                });
    }

    @Test
    public void loginNotAllowedForDisabledUser() throws Exception {
        RequestBuilder request = post("/signin")
                .param("username", "test")
                .param("password", "test1234")
                .with(csrf());

        mvc.perform(request)
                .andExpect(result -> {
                    status().isMovedTemporarily().match(result);
                    redirectedUrl("/signin?error").match(result);
                });
    }

    @Test
    public void loginWithValidUsernamePassword() throws Exception {
        RequestBuilder request = post("/signin")
                .param("username", "jeff")
                .param("password", "test1234")
                .with(csrf());

        mvc.perform(request)
                .andExpect(redirectedUrl("/"));
    }
}
