package com.bbcow.api.web;

import com.bbcow.service.impl.BookService;
import com.bbcow.service.impl.SearchService;
import com.bbcow.service.mongo.entity.Book;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by adan on 2017/10/17.
 */
@Controller
public class WebIndexController {

    @Autowired
    BookService bookService;
    @Autowired
    SearchService searchService;

    @RequestMapping("/")
    public String home(Model model) {

        model.addAttribute("words", searchService.getHotWords());

        return "index";
    }
    @RequestMapping("/search")
    public String search(@RequestParam String word, Model model){


        model.addAttribute("results", searchService.search(word));
        model.addAttribute("word", word);
        return "result";
    }
    @RequestMapping("/books/{id}")
    public String get(@PathVariable String id, Model model){
        Book book = bookService.getById(new ObjectId(id));
        model.addAttribute("book", book);

        model.addAttribute("recommendBooks", bookService.recommend(book.getAuthor()));
        return "books";
    }
    @RequestMapping("/books/rank/page_score")
    public String getRank(Model model){
        model.addAttribute("books", bookService.getTop50());

        return "ranks";
    }
}
