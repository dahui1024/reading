package com.bbcow.crawler.site.core;

import com.bbcow.service.util.HtmlChapterParser;
import com.google.common.io.Resources;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * Created by adan on 2017/11/24.
 */
public class ChapterLinkMarker2 {
    public static List<HtmlChapterParser.ChapterUrl> getLinks(String url, Document document){
        List<HtmlChapterParser.ChapterUrl> urls = HtmlChapterParser.getWithOrder(document);
//        if (urls.isEmpty()){
//            System.out.println("------");
//            urls = HtmlChapterParser.getWithOrder(JSParse(url));
//        }
        return urls;
    }

    static Document JSParse(String url){
        URL jsUrl = Resources.getResource("js/book.js");
        String phantomjs = "/Users/adan/Downloads/phantomjs-2.1.1-macosx/bin/phantomjs";
        Runtime runtime = Runtime.getRuntime();
        StringBuilder stringBuilder = new StringBuilder();
        try {
            Process process = runtime.exec(phantomjs+" "+jsUrl.getPath()+" "+url);
            InputStream inputStream = process.getInputStream();
            List<String> lines = IOUtils.readLines(inputStream);
            lines.forEach(line -> stringBuilder.append(line));

        } catch (IOException e) {
            e.printStackTrace();
        }
        Document document = Jsoup.parse(stringBuilder.toString());
        document.setBaseUri(url);
        return document;

    }

    public static void main(String[] args) {
        try {
//            String url = "https://m.qidian.com/book/3602691/catalog";
            String url = "https://book.qidian.com/info/2750457#Catalog";
            Document document = Jsoup.connect(url).get();

            getLinks(url, document).forEach(chapterUrl -> System.out.println(chapterUrl.name));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
