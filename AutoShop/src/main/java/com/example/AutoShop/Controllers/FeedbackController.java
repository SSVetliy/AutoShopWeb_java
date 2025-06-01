package com.example.AutoShop.Controllers;

import com.example.AutoShop.DB;
import com.example.AutoShop.Tovar;
import com.example.AutoShop.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;

@Controller
public class FeedbackController {

    @GetMapping("/feedback")
    public String read_feedback(Model model, @RequestParam(name = "id", required = false, defaultValue = "") String id) {
        User user = DB.getUser(RequestContextHolder.currentRequestAttributes().getSessionId());
        if(user == null)
            return "redirect:/";

        model.addAttribute("tovar", DB.getTovar(Integer.parseInt(id)));
        model.addAttribute("user", user);
        return "write_feedback";
    }

    @PostMapping("/feedback")
    public String read_feedback(Model model, @RequestParam(name = "id", required = false, defaultValue = "") String id, String feedback, String rating) {
        User user = DB.getUser(RequestContextHolder.currentRequestAttributes().getSessionId());
        Tovar tovar = DB.getTovar(Integer.parseInt(id));

        if(user != null && tovar != null)
            DB.addFeedback(user, tovar, Integer.parseInt(rating), feedback);

        return "redirect:/";
    }
}
