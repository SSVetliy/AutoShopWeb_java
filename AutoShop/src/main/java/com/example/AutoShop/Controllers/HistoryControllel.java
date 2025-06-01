package com.example.AutoShop.Controllers;

import com.example.AutoShop.DB;
import com.example.AutoShop.Tovar;
import com.example.AutoShop.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.ArrayList;

@Controller
public class HistoryControllel {
    @GetMapping("/history")
    public String history(Model model) {
        User user = DB.getUser(RequestContextHolder.currentRequestAttributes().getSessionId());
        if(user != null) {
            ArrayList<Tovar> list = DB.getHistory(user);
            model.addAttribute("user", user);
            model.addAttribute("list", list);
            model.addAttribute("rule", user.getRule());
            return "history";
        }
        return "redirect:/";
    }
}
