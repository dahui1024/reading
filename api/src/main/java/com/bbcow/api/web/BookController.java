package com.bbcow.api.web;

import com.bbcow.api.vo.UrlVO;
import com.bbcow.service.impl.BookService;
import com.bbcow.service.impl.BookSiteService;
import com.bbcow.service.impl.ScoreService;
import com.bbcow.service.mongo.entity.Book;
import com.bbcow.service.mongo.entity.ScoreBookLog;
import com.bbcow.service.util.HtmlChapterParser;
import com.bbcow.service.util.HtmlContentParser;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

/**
 * Created by adan on 2017/10/17.
 */
@Controller
public class BookController {

    @Autowired
    BookSiteService bookSiteService;
    @Autowired
    BookService bookService;
    @Autowired
    ScoreService scoreService;

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

        model.addAttribute("bbcow_t", "免费TXT_"+book.getName()+"("+book.getAuthor()+"著)怎么样-烂白菜");
        model.addAttribute("bbcow_d", book.getName()+"是由"+book.getAuthor()+"创作的"+book.getTags()+"类型作品，首发于"+book.getCpName()+"平台，由烂白菜为你深度解析"+book.getName()+"究竟怎样，给你一个满意的答案。");
        model.addAttribute("bbcow_k", book.getName()+","+book.getAuthor()+","+book.getTags()+",烂白菜");
        model.addAttribute("bbcow_mu", "/books/"+book.getId().toString());

        model.addAttribute("labels", labels);
        model.addAttribute("values", values);

        List<UrlVO> urls = new LinkedList<>();


        if (book.getSiteUrls() != null){
            book.getSiteUrls().forEach(siteUrl -> urls.add(new UrlVO("", siteUrl)));
        }

        model.addAttribute("urls", urls);

        model.addAttribute("person", bookService.getBookPerson(book.getReferenceKey()));
        model.addAttribute("recommendBooks", bookService.recommend(book.getName(), book.getAuthor()));
        return "books";
    }
//    @RequestMapping("/books/{rk}/chapters")
//    public String chapters(@PathVariable String rk, Model model) {
//        Book book = bookService.getByReferenceKey(rk);
//
//        model.addAttribute("chapters", bookSiteService.getChapters(rk));
//        model.addAttribute("book", book);
//        return "chapter/chapter";
//    }
    @RequestMapping("/books/chapters/{rk}")
    public String otherChapters(@PathVariable String rk, @RequestParam String url, Model model) {
        Book book = bookService.getByReferenceKey(rk);


        try {
            model.addAttribute("chapters", HtmlChapterParser.get(URLDecoder.decode(url,"UTF-8")));
            model.addAttribute("url", url);
            model.addAttribute("rk", rk);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        model.addAttribute("book", book);
        return "chapter/other_chapter";
    }
    @RequestMapping("/books/content/{rk}")
    public String otherContent(@PathVariable String rk, @RequestParam(required = false) String name, @RequestParam String refer, @RequestParam String url, Model model) {

        try {
            String originUrl = URLDecoder.decode(url,"UTF-8");
            model.addAttribute("content", HtmlContentParser.get(originUrl, Collections.EMPTY_LIST));
            model.addAttribute("url", originUrl);
            model.addAttribute("refer", refer);
            model.addAttribute("rk", rk);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        model.addAttribute("name", name);
        model.addAttribute("rk", rk);

        return "chapter/other_content";
    }
//    @RequestMapping("books/content/{id}")
//    public String content(@RequestParam String url, @PathVariable String id, Model model) {
//
//        BookSiteChapter bookSiteChapter = bookSiteService.getChapter(id);
//
//        if (StringUtils.isBlank(url) && bookSiteChapter != null){
//            model.addAttribute("chapter", bookSiteChapter);
//            if (bookSiteChapter.getSiteUrls() != null) {
//                model.addAttribute("content", HtmlContentParser.get(bookSiteChapter.getSiteUrls().get(0), bookSiteChapter.getSiteUrls()));
//            }
//        }else {
//            model.addAttribute("chapter", bookSiteChapter);
//            if (bookSiteChapter.getSiteUrls() != null) {
//                model.addAttribute("content", HtmlContentParser.get(url, bookSiteChapter.getSiteUrls()));
//            }else {
//                model.addAttribute("content", "");
//            }
//        }
//
//
//        return "chapter/content";
//    }
}
