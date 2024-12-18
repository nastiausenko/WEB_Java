package dev.usenkonastia.api.web;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;

@RestController
@RequestMapping("/api/v1/greetings")
public class GreetingController {

    @GetMapping
    public ResponseEntity<Map<String, String>> getCustomerById(@AuthenticationPrincipal OAuth2User principal) {
        Map<String, String> messages = new HashMap<>();
        messages.put("greeting", format("Welcome, %s!", principal.getAttributes().get("name")));
        messages.put("loggedIn", String.valueOf(LocalDateTime.now()));
        messages.put("message", "We are glad to see you here! Enjoy exploring our API.");
        return ResponseEntity.ok(messages);
    }
}
