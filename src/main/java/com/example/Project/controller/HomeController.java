package com.example.Project.controller;

import com.example.Project.model.Car;
import com.example.Project.model.StashSet;
import com.example.Project.model.User;
import com.example.Project.repositories.*;
import com.example.Project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {
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

    @GetMapping("/")
    public String index(Model model){
        int x = 1;
        int id = 0;
        List<Car> carsList = carRepository.findAll();
        model.addAttribute("cars", carsList);
        model.addAttribute("stashSet", new StashSet());
        model.addAttribute("count", x);
        model.addAttribute("id", id);
        model.addAttribute("isAuth", this.getAuthUser());


        return "main";
    }
    @GetMapping("/main")
    public String main(Model model){
        List<Car> carsList = carRepository.findAll();
        model.addAttribute("cars", carsList);
        model.addAttribute("isAuth", this.getAuthUser());

        return "main";
    }



}
