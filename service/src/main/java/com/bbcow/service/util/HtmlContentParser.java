package com.bbcow.service.util;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by adan on 2017/11/24.
 */
public class HtmlContentParser {

    static String[] words = {"小说", "阅读", "章节", "<!--go-->『点击章节报错』","『加入书签，方便阅读』", "笔趣阁www.biqiuge.com，最快更新圣墟最新章节！"};

    public static String get(String url, List<String> secondUrls){
        String content = null;
        try {
            Document doc = Jsoup.connect(url).get();
            content = getContent(doc);
        } catch (IOException e) {

        }

        for (int i = 0; i < secondUrls.size() && content != null; i++) {
            if (!url.equals(secondUrls.get(i))){
                try {
                    Document doc = Jsoup.connect(secondUrls.get(i)).get();
                    content = getContent(doc);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

        return content;
    }

    static String getContent(Document document){
        cleanElement(document);

        Element element = getMaxElement(document);

        String text = text(element);

        return cleanGarbageWord(text, words);
    }

    static void cleanElement(Document document){
        document.getElementsByTag("script").remove();
        document.getElementsByTag("style").remove();
        document.getElementsByTag("link").remove();
        document.getElementsByTag("a").remove();
        document.getElementsByTag("meta").remove();
        document.getElementsByTag("title").remove();
        document.getElementsByTag("footer").remove();
        document.getElementsByTag("header").remove();
        document.getElementsByTag("ul").remove();
        document.getElementsByTag("ol").remove();
        document.getElementsByTag("table").remove();
        document.getAllElements().removeAttr("class");
    }
    static Element getMaxElement(Document document){
        int maxCount = 0;
        String maxTag = "html";
        int maxStartIndex = 0;
        List<Element> maxElements = new LinkedList<>();

        int secondCount = 0;
        String secondTag = "";
        int secondIndex = 0;
        List<Element> secondElements = new LinkedList<>();

        Elements elements = document.getAllElements();
        for (int i = 0; i < elements.size(); i++) {
            Element element = elements.get(i);

            if (maxTag.equals(element.tagName()) && maxStartIndex+maxCount == i){
                // 保证最大连续性
                maxCount += 1;
                maxElements.add(element);
            }else {
                if (secondTag.equals(element.tagName())){
                    secondCount +=1;
                    secondElements.add(element);
                }else {
                    secondTag = element.tagName();
                    secondIndex = i;
                    secondCount = 1;
                    secondElements.clear();
                    secondElements.add(element);
                }

                if (secondCount > maxCount){
                    maxCount = secondCount;
                    maxStartIndex = secondIndex;
                    maxTag = secondTag;

                    maxElements.clear();
                    maxElements.addAll(secondElements);
                    secondElements.clear();

                }
            }
        }

        Element parent = maxElements.get(0).parent();
        Elements children = parent.children();
        for (int i = 0; i < children.size(); i++) {
            if (!children.get(i).tagName().equals(maxTag)){
                children.get(i).remove();
            }
        }
        return parent;
    }
    static String text(Element element) {
        StringBuilder accum = new StringBuilder();
        Iterator var2 = element.childNodes().iterator();
        while(var2.hasNext()) {
            Node node = (Node)var2.next();
            accum.append(node.outerHtml());
        }
        String html = accum.toString();
        html = html.replaceAll("( |　)", "");
        html = html.replaceAll("[\\s]*", "");
        // html实体字符
        html = html.replaceAll("(&?(nbsp|lt|gt|amp|quot|apos|cent|pound|yen|euro|sect|copy|reg|trade|times|divide);?)", "");
        // unicode编码
        html = html.replaceAll("(&[\\S]{2,5};)+", "");
//        html = html.replaceAll("(<?/?(br|p)/?>?)+", "\r\n");
        html = html.replaceAll("(<?/?(br|p)/?>?)+", "<br/>");
        // 特殊html标签处理
//        html = html.replaceAll("(<[^\\u4e00-\\u9fa5]*>)", "");

        return html;
    }
    static String cleanGarbageWord(String text, String[] garbageWords){
        String[] lines = text.split("\r\n");
        String head = lines[0];
        String foot = lines[lines.length-1];

        for (String s: garbageWords) {
            head = StringUtils.removeAll(head , s);
            foot = StringUtils.removeAll(foot , s);
        }

        StringBuilder content = new StringBuilder(head);
        if (StringUtils.isNotBlank(head)){
            content.append("\r\n");
        }
        for (int i = 1; i < lines.length-2; i++) {
           content.append(lines[i]).append("\r\n");
        }
        content.append(foot);

        return content.toString();
    }

    public static void main(String[] args) {
        String html = "&#8481;嗯嗯&#8481;嗯嗯&amp;<divid=\"wrapper\"><divid=\"content\">正在手打中，请稍等片刻，请记住zwda.内容更新后，请重新刷新页面，即可获取最新更新！←→<<!--<script>mark();</script>-->";
        html = html.replaceAll("( |　)", "");
        html = html.replaceAll("[\\s]*", "");
        // html实体字符
        html = html.replaceAll("(&?(nbsp|lt|gt|amp|quot|apos|cent|pound|yen|euro|sect|copy|reg|trade|times|divide);?)", "");
        System.out.println(html+"--");
        // unicode编码
        html = html.replaceAll("(&[\\S]{2,5};)+", "");
        System.out.println(html+"--");
        html = html.replaceAll("(<?/?(br|p)/?>?)+", "\r\n");

        System.out.println(html+"--====");
        // 特殊html标签处理
        html = html.replaceAll("(<[^\\u4e00-\\u9fa5]*>)", "");
        System.out.println(html);
    }
}