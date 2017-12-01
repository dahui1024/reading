package com.bbcow.api.web;

import com.bbcow.service.impl.BookSiteService;
import com.bbcow.service.mongo.entity.BookSiteChapter;
import com.bbcow.service.util.HtmlContentParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;

/**
 * Created by adan on 2017/10/17.
 */
@Controller
public class BookController {

    @Autowired
    BookSiteService bookSiteService;

    @RequestMapping("/content/{id}")
    public String content(@RequestParam String url, @PathVariable String id, Model model) {

        BookSiteChapter bookSiteChapter = bookSiteService.getChapter(id);

        model.addAttribute("chapter", bookSiteChapter);
        model.addAttribute("content", HtmlContentParser.get(url, Collections.emptyList()));

        return null;
    }
}
