package com.bbcow.crawler.proxy;

import com.bbcow.service.mongo.entity.Book;
import com.bbcow.service.mongo.entity.BookElement;
import org.springframework.util.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Created by adan on 2017/10/18.
 */
public class BookProxy{

    private BookElement bookElement;
    private Html html;
    private Map<String, BookElement> elementMap;

    public BookProxy(Map<String, BookElement> elementMap){
        this.elementMap = elementMap;
    }

    public Book getBook(Page page) {
        try {
            String host = new URL(page.getUrl().get()).getHost();
            this.bookElement = elementMap.get(host);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        this.html = page.getHtml();
        Book book = new Book();
        book.setName(getName());
        book.setAuthor(getAuthor());
        book.setTags(html.xpath(bookElement.getTag()).all());
        book.setDescription(getDescription());
        book.setIsVip(getVip());
        book.setIsFinish(getFinish());
        book.setIsSign(getSign());
        book.setCpUrl(page.getUrl().get());
        book.setCpName(bookElement.getCpName());
        book.setCpImageUrl(html.xpath(bookElement.getImageUrl()).get());
        book.setCpHost(bookElement.getHost());

        System.out.println(book.getName());
        return book;
    }
    String getName(){
        String name = html.xpath(bookElement.getName()).get();
        return name;
    }

    String getAuthor(){
        String author = html.xpath(bookElement.getAuthor()).get();
        return author.replace("作者：", "");
    }
    String getDescription(){
        String description = html.xpath(bookElement.getDescription()).get();
        return description;
    }
    int getVip(){
        List<String> others = html.xpath(bookElement.getOther()).all();
        String vip = html.xpath(bookElement.getVip()).get();
        if (others.contains("VIP") || (!StringUtils.isEmpty(vip) && vip.contains("上架"))){
            return 1;
        }
        return 0;
    }
    int getFinish(){
        List<String> others = html.xpath(bookElement.getOther()).all();
        String finish = html.xpath(bookElement.getStatus()).get();
        if (others.contains("完本") || (!StringUtils.isEmpty(finish) && finish.contains("完"))){
            return 1;
        }
        return 0;
    }
    int getSign(){
        List<String> others = html.xpath(bookElement.getOther()).all();
        String sign = html.xpath(bookElement.getSign()).get();
        if (others.contains("签约") || (!StringUtils.isEmpty(sign) && sign.contains("签约"))){
            return 1;
        }
        return 0;
    }
}
