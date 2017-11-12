package com.bbcow.api.web;

import com.bbcow.service.impl.BookService;
import com.bbcow.service.impl.ScoreService;
import com.bbcow.service.impl.SearchService;
import com.bbcow.service.mongo.entity.Book;
import com.bbcow.service.mongo.entity.ScoreBookLog;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Calendar;
import java.util.List;
import java.util.Date;

/**
 * Created by adan on 2017/10/17.
 */
@Controller
public class WebIndexController {

    @Autowired
    ScoreService scoreService;
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

        String[] labels = new String[30];
        String[] values = new String[30];


        List<ScoreBookLog> scoreBookLogs = scoreService.findTop30ByName(book.getName());


        Date today = DateUtils.truncate(new Date(), Calendar.DATE);

        for (int i = 1; i <= 30; i++) {
            int index = 30 - i;
            Date date = DateUtils.addDays(today, -(i-1));
            labels[index] = DateFormatUtils.format(date, "MM-dd");

            for (ScoreBookLog scoreBookLog : scoreBookLogs){
                if (DateUtils.isSameInstant(date, scoreBookLog.getDay())){
                    values[index] = scoreBookLog.getPageScore() / 10.0+"";
                }
            }
            if (values[index] == null) {
                values[index] = "0";
            }
        }

        model.addAttribute("labels", labels);
        model.addAttribute("values", values);
        model.addAttribute("person", bookService.getBookPerson(book.getReferenceKey()));
        model.addAttribute("recommendBooks", bookService.recommend(book.getAuthor()));
        return "books";
    }
    @RequestMapping("/books/rank/page_score")
    public String getRank(Model model){
        model.addAttribute("books", bookService.getTop50());

        return "ranks";
    }
}
