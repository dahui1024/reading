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

        return "site";
    }
    @RequestMapping("/site/apply")
    public String apply(@RequestParam String name, @RequestParam String website, @RequestParam String contact, Model model) {
        model.addAttribute("result", siteService.save(name, website, contact));
        return "site";
    }
}
