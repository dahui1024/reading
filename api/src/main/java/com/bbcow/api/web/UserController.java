package com.bbcow.api.web;

import com.bbcow.service.impl.UserService;
import com.bbcow.service.mongo.entity.UserToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Created by adan on 2017/10/17.
 */
@Controller
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/register.html", method = RequestMethod.GET)
    public String toRegister(Model model) {

        model.addAttribute("bbcow_t", "烂白菜-网文世界风向标");
        model.addAttribute("bbcow_d", "烂白菜，网文世界风向标，告诉你关于网文的一切。");
        model.addAttribute("bbcow_k", "烂白菜,白菜,大白菜,网文,网文大全,玄幻排行榜,都市排行榜,总裁排行榜,小说排行榜");
        model.addAttribute("bbcow_mu", "/register.html");
        return "register";
    }
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public String register(@RequestParam String name, @RequestParam String phone, @RequestParam int code, @RequestParam String password, @RequestParam String confirmPassword, Model model, RedirectAttributes redirectAttributes) {

        if (!password.equals(confirmPassword)){

        }

        UserToken userToken = userService.register(name, phone, password);

        redirectAttributes.addAttribute("name", name);
        redirectAttributes.addAttribute("token", userToken.getToken());
        return "redirect:/";
    }
    @RequestMapping(value = "/user.html", method = RequestMethod.GET)
    public String user(@RequestParam String token, Model model, RedirectAttributes redirectAttributes) {

        return "redirect:/";
    }
    @RequestMapping(value = "/login.html", method = RequestMethod.GET)
    public String toLogin(Model model) {

        model.addAttribute("bbcow_t", "烂白菜-网文世界风向标");
        model.addAttribute("bbcow_d", "烂白菜，网文世界风向标，告诉你关于网文的一切。");
        model.addAttribute("bbcow_k", "烂白菜,白菜,大白菜,网文,网文大全,玄幻排行榜,都市排行榜,总裁排行榜,小说排行榜");
        model.addAttribute("bbcow_mu", "/login.html");
        return "login";
    }
    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    public String login(@RequestParam String name, @RequestParam String password, RedirectAttributes redirectAttributes) {

        UserToken userToken = userService.loginWithName(name, password);
        if (userToken != null){
            redirectAttributes.addAttribute("name", name);
            redirectAttributes.addAttribute("token", userToken.getToken());
        }

        return "redirect:/";
    }
    @RequestMapping(value = "/user/logout", method = RequestMethod.POST)
    public String logout(@RequestParam String token, Model model) {

        return "redirect:/";
    }
}
