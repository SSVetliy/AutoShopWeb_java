package com.example.AutoShop.Controllers;
import com.example.AutoShop.DB;
import com.example.AutoShop.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.request.RequestContextHolder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@SessionAttributes("err")
@Controller
public class LoginController {
    @GetMapping("/login")
    public String login(Model model, SessionStatus sessionStatus) {
        sessionStatus.setComplete();
        return "login";
    }

    @GetMapping("/regist")
    public String regist(Model model, SessionStatus sessionStatus) {
        sessionStatus.setComplete();
        return "regist";
    }

    @PostMapping("/checklog")
    public String checklog(Model model, String log, String pass, SessionStatus sessionStatus) {
        User user = DB.getUser(log, hash(pass), RequestContextHolder.currentRequestAttributes().getSessionId());

        if(user == null) {
            model.addAttribute("err", "Упс, что-то пошло не так(");
            return "redirect:/login";
        }
        else {
            sessionStatus.setComplete();
            return "redirect:/";
        }
    }

    @GetMapping("/exit")
    public String exit(Model model, SessionStatus sessionStatus) {
        DB.exitUser(RequestContextHolder.currentRequestAttributes().getSessionId());
        return "redirect:/";
    }

    @PostMapping("/checkreg")
    public String checkreg(Model model, String name, String log, String pass, SessionStatus sessionStatus) {
        User user = DB.regUser(name, log, hash(pass), RequestContextHolder.currentRequestAttributes().getSessionId());
        if(user == null) {
            model.addAttribute("err", "Упс, что-то пошло не так(");
            return "redirect:/regist";
        }
        else {
            DB.getUser(log, hash(pass), RequestContextHolder.currentRequestAttributes().getSessionId());
            sessionStatus.setComplete();
            return "redirect:/";
        }
    }

    private String hash(String password) {
        if(password.length() == 0)
            return "";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException(e);
            System.out.println("err hash");
            return "";
        }
    }
}

