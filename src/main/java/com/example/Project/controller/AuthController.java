package com.example.Project.controller;

import com.example.Project.model.Car;
import com.example.Project.model.Role;
import com.example.Project.model.StashSet;
import com.example.Project.model.User;
import com.example.Project.repositories.*;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Controller
public class AuthController {
    @Autowired
    private CarRepository carRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StashSetRepository stashSetRepository;
    @Autowired
    private OrderSetRepository orderSetRepository;
    @Autowired
    private OrderRepository orderRepository;
    public User getAuthUser(){
        return userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @GetMapping("/registration")
    public String registration(Model model){
        model.addAttribute("user",new User());
        model.addAttribute("isAuth", this.getAuthUser());
        return "registration";
    }

    @GetMapping("/login")
    public String login(Model model){

        model.addAttribute("isAuth", this.getAuthUser());

        return "login";
    }

    @PostMapping("/logout")
    public String lgout()
    {
        return  "index";
    }







    @PostMapping("/registration")
    public  String registration(@ModelAttribute("user") User user){


        User isRealUsername = userRepository.findByUsername(user.getUsername());
        User isRealEmail = userRepository.findByEmail(user.getEmail());
        if (isRealUsername == null && isRealEmail== null) {
            user.setRoles(Collections.singleton(Role.ROLE_USER));
            user.setActive(true);
            userRepository.save(user);
        }
        else{
            return "registration";
        }

        return "login";
    }


}
