package com.example.Project.controller;

import com.example.Project.model.*;
import com.example.Project.repositories.*;
import org.aspectj.weaver.ast.Or;
import org.dom4j.rule.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.PresentationDirection;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

@Controller

public class CatalogController {
    Date date = new Date();
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

    public User getAuthUser() {
        return userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
    }


    @GetMapping("/cart/add/{id}")
    public String cartAdd(@PathVariable("id") Long id, @RequestParam("count") int count) {
        boolean isFind = false;


        if (stashSetRepository.findFirstByUserIdAndCarId(getAuthUser().getId(), id) != null) {
            StashSet stashSet = stashSetRepository.findFirstByUserIdAndCarId(getAuthUser().getId(), id);
            stashSet.setAmount(stashSet.getAmount() + count);
            stashSetRepository.save(stashSet);

        } else {
            stashSetRepository.save(new StashSet(getAuthUser(), carRepository.getOne(id), count));

        }

        return "redirect:/main";

    }


    @GetMapping("/cart/edit/{id}")
    public String cartEdit(@PathVariable("id") Long id, @RequestParam("count") int count) {
        StashSet stashSet = stashSetRepository.getOne(id);
        stashSet.setAmount(count);
        stashSetRepository.save(stashSet);
        return "redirect:/cabinet/cart";
    }


    @GetMapping("/cart/delete/{id}")
    public String cartDelete(@PathVariable("id") Long id) {
        stashSetRepository.deleteById(id);
        return "redirect:/cabinet/cart";
    }

    @GetMapping("/main/{id}")
    public String carView(@PathVariable("id") Long id, Model model) {
        model.addAttribute("car", carRepository.getOne(id));
        model.addAttribute("isAuth", this.getAuthUser());
        return "carview";

    }

    @GetMapping("/buyIt/{id}")
    public String buyIt(@PathVariable("id") Long id, Model model) {

        model.addAttribute("isAuth", this.getAuthUser());
        try {
            if (carRepository.getOne(id).getBrand() != null) {
                Car car = carRepository.getOne(id);
                model.addAttribute("order", new Order());
                model.addAttribute("car", car);
                model.addAttribute("orderSet", new OrderSet(null, null, 1));
                return "buy";
            }
        } catch (Exception exception) {


            return "redirect:/main";

        }
        return "redirect:/main";

    }

    @GetMapping("/buyIt/edit/{id}")
    public String buyItEdit(@RequestParam("count") int count, @PathVariable("id") Long id, Model model) {
        model.addAttribute("isAuth", getAuthUser());
        try {
            if (carRepository.getOne(id).getBrand() != null) {
                Car car = carRepository.getOne(id);


                model.addAttribute("order", new Order());
                model.addAttribute("car", car);
                model.addAttribute("orderSet", new OrderSet(null, null, count));
                return "buy";
            }
        } catch (Exception exception) {
            return "redirect:/main";
        }
        return "redirect:/main";


    }
    @Transactional
    @PostMapping("/buyIt/")
    public String buying(@ModelAttribute("orderSet") OrderSet orderSet, @ModelAttribute("order") Order order, @RequestParam("count") int count, @RequestParam("carId") Long id) {
        try {

            Car car = carRepository.getOne(id);
            if (car.getAmount()>=count) {
                Order order1 = new Order(getAuthUser(), new Date(), order.getFirstName(), order.getLastName(), order.getAdress(), order.getPhone(), count * (car.getCost()));
                orderRepository.save(order1);
                OrderSet orderSet1 = new OrderSet(order1, car, count);
                carRepository.getOne(id).setAmount(car.getAmount()-count);
                orderSetRepository.save(orderSet1);

            }
            else return "redirect:/main";
        } catch (Exception exception) {

        }
        return "redirect:/main";

    }


    @GetMapping("/cabinet")
    public String cabinet(Model model) {
        model.addAttribute("log", this.getAuthUser());

        return "cabinet";

    }

    @GetMapping("/cabinet/cart")
    public String cart(Model model) {
        model.addAttribute("isAuth", this.getAuthUser());
        List<StashSet> stashSets = stashSetRepository.findAllByUserId(getAuthUser().getId());
        int totalCost = 0;
        for (int i = 0; i < stashSets.size(); i++) {
            if (stashSets.get(i).getAmount() > stashSets.get(i).getCar().getAmount()) {
                stashSets.get(i).setAmount(stashSets.get(i).getCar().getAmount());
                stashSetRepository.save(stashSets.get(i));
            }
            totalCost += (stashSets.get(i).getAmount() * stashSets.get(i).getCar().getCost());
        }


        stashSets.stream().map(x->x.getCar().getId()).sorted(Comparator.naturalOrder());

        model.addAttribute("totalCost", totalCost);
        model.addAttribute("stashSets", stashSets);

        return "cart";

    }

    @Transactional
    @GetMapping("/cabinet/cart/clear")
    public String clearCart(Model model) {
        stashSetRepository.deleteAllByUserId(getAuthUser().getId());
        List<StashSet> stashSets = stashSetRepository.findAllByUserId(getAuthUser().getId());
        model.addAttribute("stashSets", stashSets);
        return "redirect:/cabinet/cart";
    }

    @GetMapping("/cabinet/cart/buying")
    public String cartBuy(Model model) {

        model.addAttribute("isAuth", getAuthUser());
        model.addAttribute("order", new Order());
        List<StashSet> stashSets = stashSetRepository.findAllByUserId(getAuthUser().getId());
        model.addAttribute("stashSets", stashSets);
        int cost = stashSets
                .stream()
                .mapToInt(x -> x.getCar().getCost() * x.getAmount())
                .reduce((x, y) -> x + y).getAsInt();
        model.addAttribute("totalCost", cost);
        return "cartBuy";


    }

    @Transactional
    @PostMapping("/cabinet/cart/buying")
    public String cartBuy(@ModelAttribute("order") Order order) {
        order.setDate(new Date());
        order.setUser(getAuthUser());
        List<StashSet> stashSets = stashSetRepository.findAllByUserId(getAuthUser().getId());

        int cost = stashSets
                .stream()
                .mapToInt(x -> x.getCar().getCost() * x.getAmount())
                .reduce((x, y) -> x + y).getAsInt();


        order.setTotalCost(cost);
        orderRepository.save(order);
        for (int i = 0; i < stashSets.size(); i++) {
            if (stashSets.get(i).getCar().getAmount() < stashSets.get(i).getAmount()) {
                return "cart";
            }
        }

        for (int i = 0; i < stashSets.size(); i++) {

            orderSetRepository.save(new OrderSet(order, stashSets.get(i).getCar(), stashSets.get(i).getAmount()));
            stashSetRepository.deleteById(stashSets.get(i).getId());
            carRepository.getOne(stashSets.get(i).getCar().getId())
                    .setAmount(stashSets.get(i).getCar().getAmount()-stashSets.get(i).getAmount());
        }
        return "redirect:/main";
    }
}