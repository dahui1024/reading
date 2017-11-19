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

import javax.servlet.http.HttpServletRequest;
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
    public String home(Model model, HttpServletRequest request) {

        model.addAttribute("words", searchService.getHotWords());

        model.addAttribute("bbcow_t", "烂白菜-网文世界风向标");
        model.addAttribute("bbcow_d", "烂白菜，网文世界风向标，告诉你关于网文的一切。");
        model.addAttribute("bbcow_k", "烂白菜,白菜,大白菜,网文,网文大全,玄幻排行榜,都市排行榜,总裁排行榜,小说排行榜");
        model.addAttribute("bbcow_mu", "/");

        if (request.getParameter("token") != null){
            model.addAttribute("name", request.getParameter("name"));
            model.addAttribute("token", request.getParameter("token"));
        }

        return "index";
    }
    @RequestMapping("/search")
    public String search(@RequestParam String word, Model model){


        model.addAttribute("results", searchService.search(word));
        model.addAttribute("word", word);

        model.addAttribute("bbcow_t", word + "相关小说-烂白菜");
        model.addAttribute("bbcow_d", word+"相关小说，烂白菜，网文世界风向标，告诉你关于网文的一切。");
        model.addAttribute("bbcow_k", word+",烂白菜");
        model.addAttribute("bbcow_mu", "/");
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

        model.addAttribute("bbcow_t", "【报告】_"+book.getName()+"("+book.getAuthor()+"著)怎么样-烂白菜");
        model.addAttribute("bbcow_d", book.getName()+"是由"+book.getAuthor()+"创作的"+book.getTags()+"类型作品，首发于"+book.getCpName()+"平台，由烂白菜为你深度解析"+book.getName()+"究竟怎样，给你一个满意的答案。");
        model.addAttribute("bbcow_k", book.getName()+","+book.getAuthor()+","+book.getTags()+",烂白菜");
        model.addAttribute("bbcow_mu", "/books/"+book.getId().toString());

        model.addAttribute("labels", labels);
        model.addAttribute("values", values);
        if (!scoreBookLogs.isEmpty()){
            ScoreBookLog yesterdayLog = scoreBookLogs.get(0);
            if (yesterdayLog.getUrls() != null && !yesterdayLog.getUrls().isEmpty()){
                model.addAttribute("log", yesterdayLog.getUrls().subList(0, yesterdayLog.getUrls().size()>10 ? 10 : yesterdayLog.getUrls().size()));
            }
        }

        model.addAttribute("person", bookService.getBookPerson(book.getReferenceKey()));
        model.addAttribute("recommendBooks", bookService.recommend(book.getName(), book.getAuthor()));
        return "books";
    }
    @RequestMapping("/books/rank/page_score")
    public String getRank(Model model){
        model.addAttribute("books", bookService.getTop50());

        model.addAttribute("bbcow_t", "白菜榜_TOP_热门小说推荐榜-烂白菜");
        model.addAttribute("bbcow_d", "白菜榜，一个智能排名榜单，每日由机器自动打分分析，实时反应社会热度。");
        model.addAttribute("bbcow_k", "白菜榜,烂白菜,小说排行榜");
        model.addAttribute("bbcow_mu", "/books/rank/page_score");

        return "ranks";
    }
    @RequestMapping("/books/rank/7kg")
    public String rank7kg(@RequestParam(defaultValue = "1") int page, Model model){
        model.addAttribute("books", bookService.getBooksWithScoreScope(70,  80, page));
        model.addAttribute("next_page", page+1);
        model.addAttribute("last_page", page<=1 ? 1 : page-1);

        model.addAttribute("bbcow_t", "7KG_大全_小说库-烂白菜");
        model.addAttribute("bbcow_d", "7kG，白菜地，高质量网文书库");
        model.addAttribute("bbcow_k", "7KG,白菜地,烂白菜,小说大全,小说书库");
        model.addAttribute("bbcow_mu", "/books/rank/7kg");
        return "stores";
    }
    @RequestMapping("/books/stores")
    public String stores(@RequestParam(defaultValue = "1") int page, Model model){
        model.addAttribute("books", bookService.getBookWithScore(page));
        model.addAttribute("next_page", page+1);
        model.addAttribute("last_page", page<=1 ? 1 : page-1);

        model.addAttribute("bbcow_t", "白菜地_大全_小说库-烂白菜");
        model.addAttribute("bbcow_d", "白菜地，高质量网文书库");
        model.addAttribute("bbcow_k", "白菜地,烂白菜,小说大全,小说书库");
        model.addAttribute("bbcow_mu", "/books/stores?page="+page);
        return "stores";
    }

}
