package com.bbcow.api.util;

public class SEOUtil {
    private static String book_title_template = "【%s】_%s（%s著）怎么样-%s";
    private static String book_description_template = "%s是%s的一部%s类型新作，首发于青果阅读平台，接下来由***为您全面解析这部力作。";
    private static String book_keywords_suffix = "青果阅读";

    public static String bookTitle(String prefix, String bookName, String authorName, String suffix){
        return String.format(book_title_template, prefix, bookName, authorName, suffix);
    }

    public static String bookDescription(String bookName, String authorName, String type){
        return String.format(book_description_template, bookName, authorName, type);
    }

    public static String bookKeywords(String...params){
        String keywords=  "";
        for (int i = 0; i < params.length; i++) {
            keywords += params[i]+",";
        }
        return keywords + book_keywords_suffix;
    }
}
