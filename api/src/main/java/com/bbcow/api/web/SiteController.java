package com.bbcow.api.web;

import com.bbcow.service.impl.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by adan on 2017/10/17.
 */
@Controller
public class SiteController {

    @Autowired
    SiteService siteService;

    @RequestMapping("/site")
    public String home(Model model) {

        model.addAttribute("bbcow_t", "【CP合作】_烂白菜-网文世界风向标");
        model.addAttribute("bbcow_d", "为CP合作方提供实时舆情报告。");
        model.addAttribute("bbcow_k", "烂白菜,CP合作");
        model.addAttribute("bbcow_mu", "/site");
        return "site";
    }
    @RequestMapping("/site/apply")
    public String apply(@RequestParam String name, @RequestParam String website, @RequestParam String contact, Model model) {
        model.addAttribute("result", siteService.save(name, website, contact));

        return "site";
    }
}
