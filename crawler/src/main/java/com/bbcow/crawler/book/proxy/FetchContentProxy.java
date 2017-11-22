package com.bbcow.crawler.book.proxy;

import com.bbcow.service.mongo.entity.BookElement;
import us.codecraft.webmagic.Page;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Created by adan on 2017/10/18.
 */
public class FetchContentProxy {
    private Map<String, BookElement> elementMap;

    public FetchContentProxy(Map<String, BookElement> elementMap){
        this.elementMap = elementMap;
    }

    public String getContent(Page page) {
        try {
            String host = new URL(page.getUrl().get()).getHost();
            BookElement bookElement = elementMap.get(host);

            List<String> texts = page.getHtml().xpath(bookElement.getContent()).all();

            if (texts == null || texts.isEmpty()){
                return null;
            }

            StringBuilder stringBuilder = new StringBuilder();
            texts.forEach(text -> stringBuilder.append(text));

            return stringBuilder.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
