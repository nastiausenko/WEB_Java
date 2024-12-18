package dev.usenkonastia.api.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class GreetingControllerIT extends AbstractIt{
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGreetingWithMockLogin() throws Exception {
        mockMvc.perform(get("/api/v1/greetings")
                        .with(oauth2Login().attributes(attrs -> attrs.put("name", "MockGitHubUser"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.greeting").value("Welcome, MockGitHubUser!"))
                .andExpect(jsonPath("$.message").value("We are glad to see you here! Enjoy exploring our API."));
    }
}
