package com.bbcow.service.util;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by adan on 2017/11/24.
 */
public class HtmlChapterParser {
    public static List<ChapterUrl> get(String url){
        try {
            Document document = Jsoup.connect(url).get();

            return getLinks(document);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.EMPTY_LIST;
    }
    public static List<ChapterUrl> getWithOrder(String url){
        try {
            Document document = Jsoup.connect(url).get();

            List<ChapterUrl> chapterUrls = getLinks(document);

            try {
                Collections.sort(chapterUrls, (c1, c2) -> {
                    String ints1 = c1.getUrl().replaceAll("[^\\d]*", "");
                    String ints2 = c2.getUrl().replaceAll("[^\\d]*", "");

                    if (StringUtils.isNotBlank(ints1) && StringUtils.isNotBlank(ints2)){
                        if (Long.parseLong(ints1) - Long.parseLong(ints2) > 0){
                            return -1;
                        }else {
                            return 1;
                        }
                    }
                    return 0;
                });
            }catch (Exception e){

            }
            return chapterUrls;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.EMPTY_LIST;
    }
    public static List<ChapterUrl> getWithOrder(Document document){
        List<ChapterUrl> chapterUrls = getLinks(document);
        try {
            Collections.sort(chapterUrls, (c1, c2) -> {
                String ints1 = c1.getUrl().replaceAll("[^\\d]*", "");
                String ints2 = c2.getUrl().replaceAll("[^\\d]*", "");

                if (StringUtils.isNotBlank(ints1) && StringUtils.isNotBlank(ints2)){
                    if (Long.parseLong(ints1) - Long.parseLong(ints2) > 0){
                        return -1;
                    }else {
                        return 1;
                    }
                }
                return 0;
            });
        }catch (Exception e){

        }
        return chapterUrls;
    }
    static List<ChapterUrl> getLinks(Document document){
        Elements elements = document.getElementsByTag("a");

        List<ChapterUrl> urls = new LinkedList<>();
        List<String> checkRepeatUrls = new ArrayList<>();

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
            if (StringUtils.isNotBlank(url) && StringUtils.isNotBlank(text) && !checkRepeatUrls.contains(url)){
                urls.add(new ChapterUrl(url, text));
                checkRepeatUrls.add(url);
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

        List<ChapterUrl> result = urls.subList(maxIndex, maxIndex+maxCount);

        int illegalCount = 0;
        if (maxCount < 50){
            for (int i = 0; i < result.size(); i++) {
                if (!result.get(i).name.matches("[一二三四五六七八九十百千万亿\\d]")){
                    illegalCount+=1;
                }
            }
        }

        if (illegalCount*100/maxCount > 10){
            return Collections.EMPTY_LIST;
        }

        return result;
    }

    public static class ChapterUrl{
        public String url;
        public String encodeUrl;
        public String name;
        public ChapterUrl(String url ,String name){
            this.url = url;
            this.name = name;
            try {
                this.encodeUrl = URLEncoder.encode(url, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEncodeUrl() {
            return encodeUrl;
        }

        public void setEncodeUrl(String encodeUrl) {
            this.encodeUrl = encodeUrl;
        }
    }

    public static void main(String[] args) {
        getWithOrder("http://www.bequge.com/42_42237/");
    }

}
