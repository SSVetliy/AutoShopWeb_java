package com.example.AutoShop.Controllers;

import com.example.AutoShop.DB;
import com.example.AutoShop.Feedback;
import com.example.AutoShop.Tovar;
import com.example.AutoShop.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

@SessionAttributes("err")
@Controller
public class TovarController {
    @GetMapping("/category")
    public String category(Model model, @RequestParam(name = "cat", required = false, defaultValue = "") String cat) {
        User user = DB.getUser(RequestContextHolder.currentRequestAttributes().getSessionId());
        if(!cat.equals("") && StringUtils.isNumeric(cat)) {
            ArrayList<Tovar> list = DB.getTovars(Integer.parseInt(cat));
            if(list.size() == 0)
                return "redirect:/";
            model.addAttribute("name_cat", DB.getCategory(Integer.parseInt(cat)).getName());
            model.addAttribute("list", list);
            model.addAttribute("user", user);
            return "tovars";
        }
        return "redirect:/";
    }

    @PostMapping("/category")
    public String category(Model model, String but_delete, @RequestParam(name = "cat", required = false, defaultValue = "") String cat) {
        if (but_delete != null)
            DB.deleteTovar(but_delete);

        User user = DB.getUser(RequestContextHolder.currentRequestAttributes().getSessionId());
        if (!cat.equals("") && StringUtils.isNumeric(cat)) {
            ArrayList<Tovar> list = DB.getTovars(Integer.parseInt(cat));
            model.addAttribute("name_cat", DB.getCategory(Integer.parseInt(cat)).getName());
            model.addAttribute("list", list);
            model.addAttribute("user", user);
            return "redirect:/category?cat=" + cat;
        }
        return "redirect:/";
    }

    @GetMapping("/add_tovar")
    public String add_tovar(Model model, SessionStatus sessionStatus) {
        User user = DB.getUser(RequestContextHolder.currentRequestAttributes().getSessionId());
        if(user != null && user.checkRule(3)) {
            sessionStatus.setComplete();
            model.addAttribute("user", user);
            model.addAttribute("list", DB.getCategory());
            return "add_tovar";
        }
        return "redirect:/";
    }

    @PostMapping("/checkaddtovar")
    public String checkaddtovar(Model model, String name,
                                String brand, String description, String price,
                                String category, SessionStatus sessionStatus, MultipartFile file) {
        User user = DB.getUser(RequestContextHolder.currentRequestAttributes().getSessionId());
        String filename = "";
        if(user != null && user.checkRule(3)) {
            try {
                if (!file.isEmpty()) {
                    File res = new File("src/main/resources/static/img");
                    if (!res.exists())
                        res.mkdir();

                    filename = UUID.randomUUID() + getFileExtension(file.getOriginalFilename());
                    file.transferTo(new File(res.getAbsoluteFile() + "/" + filename));
                }
            } catch (IOException e) {
                model.addAttribute("err", "Упс, что-то пошло не так(");
                return "redirect:/add_tovar";
            }
            if(DB.addTovar(name, brand, description, price, category, filename)) {
                sessionStatus.setComplete();
                return "redirect:/";
            }
            else {
                model.addAttribute("err", "Упс, что-то пошло не так(");
                return "redirect:/add_tovar";
            }
        }
        return "redirect:/";
    }

    private static String getFileExtension(String str) {
        int index = -1;
        while (true){
            int i = str.indexOf(".", index + 1);
            if(i != -1)
                index = i;
            else
                break;
        }
        return str.substring(index);
    }

    @GetMapping("/tovar")
    public String tovar(Model model, @RequestParam(name = "id", required = false, defaultValue = "") String id) {
        if(!id.equals("") && StringUtils.isNumeric(id)) {
            Tovar tovar = DB.getTovar(Integer.parseInt(id));
            if(tovar == null)
                return "redirect:/";
            User user = DB.getUser(RequestContextHolder.currentRequestAttributes().getSessionId());
            model.addAttribute("user", user);
            ArrayList<Feedback> list = DB.getFeedback(Integer.parseInt(id));
            if(list.size() != 0)
                model.addAttribute("list", list);
            model.addAttribute("tovar", tovar);
            return "tov";
        }
        return "redirect:/";
    }

    @PostMapping("/baytovar")
    public String baytovar(Model model, String but_in_korzina_tov) {
        User user = DB.getUser(RequestContextHolder.currentRequestAttributes().getSessionId());
        if(user != null) {
            Tovar tovar = DB.getTovar(Integer.parseInt(but_in_korzina_tov));
            DB.buyTovar(user, tovar);
            DB.setUserVipRule(user);
        }
        return "redirect:/";
    }

}
