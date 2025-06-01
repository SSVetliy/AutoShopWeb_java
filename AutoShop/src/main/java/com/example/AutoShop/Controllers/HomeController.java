package com.example.AutoShop.Controllers;

import com.example.AutoShop.DB;
import com.example.AutoShop.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.RequestContextHolder;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home(Model model) {
        User user = DB.getUser(RequestContextHolder.currentRequestAttributes().getSessionId());
        model.addAttribute("user", user);
        model.addAttribute("list", DB.getCategory());
        return "home";
    }

    @PostMapping("/")
    public String home(Model model, String txt_search) {
        if(txt_search.equals(""))
            return "redirect:/";
        model.addAttribute("name_cat", "Поиск");
        model.addAttribute("list", DB.searchTovar(txt_search));
        model.addAttribute("val_search", txt_search);
        User user = DB.getUser(RequestContextHolder.currentRequestAttributes().getSessionId());
        model.addAttribute("user", user);
        if(user != null) {
            model.addAttribute("name", user.getName());
            model.addAttribute("rule", user.getRule());
        }
        return "tovars";
    }
}
