package com.example.blog.controllers;

import com.example.blog.config.MyUserDetails;
import com.example.blog.models.User;
import com.example.blog.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class MainController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String home(Model model) {
        return "home";
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        return "registration";
    }

    @PostMapping("/registration")
    public String registrationPost(@RequestParam String username, @RequestParam String password, Model model) {
        User newUser = userRepository.findByName(username);
        if (newUser != null) {
            model.addAttribute("error", "This username is already in use");
            return "registration";
        }
        newUser = new User();
        newUser.setName(username);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setRoles("ROLE_USER");
        userRepository.save(newUser);

        return "redirect:/login";
    }

    @GetMapping("/profile")
    public String progile(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("user", auth.getName());
        return "profile";
    }
}