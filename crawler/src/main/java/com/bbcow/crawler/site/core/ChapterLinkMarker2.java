package com.bbcow.crawler.site.core;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by adan on 2017/11/24.
 */
public class ChapterLinkMarker2 {
    public static List<ChapterUrl> getLinks(Document document){

        Elements elements = document.getElementsByTag("a");

        List<ChapterUrl> urls = new LinkedList<>();

        int maxIndex = 0;
        int maxPart = 0;
        int maxCount = 0;

        int cursorIndex = 0;
        int cursorPart = 0;
        int cursorCount = 0;

        for (int i = 0; i < elements.size(); i++) {
            Element element = elements.get(i);
            String url = element.absUrl("href");
            String text = element.text();
            if (StringUtils.isNotBlank(url) && StringUtils.isNotBlank(text)){
                urls.add(new ChapterUrl(url, text));
            }
        }

        for (int i = 0; i < urls.size(); i++) {
            int part = StringUtils.split(urls.get(i).url, "/").length;
            if (part == maxPart && (maxIndex+maxCount == i)){
                maxCount += 1;
            }else {
                if (part == cursorPart){
                    cursorCount += 1;

                    if (cursorCount >= maxCount){
                        maxIndex = cursorIndex;
                        maxCount = cursorCount;
                        maxPart = cursorPart;
                    }
                }else {
                    cursorIndex = i;
                    cursorPart = part;
                    cursorCount = 1;
                }
            }
        }

        return urls.subList(maxIndex, maxIndex+maxCount);
    }

    public static class ChapterUrl{
        public String url;
        public String name;
        public ChapterUrl(String url ,String name){
            this.url = url;
            this.name = name;
        }
    }

    public static void main(String[] args) {
        try {
            Document document = Jsoup.connect("https://m.qidian.com/book/3602691/catalog").get();

            System.out.println(document.html());
            getLinks(document).forEach(chapterUrl -> System.out.println(chapterUrl.name));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
