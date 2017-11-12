package com.bbcow.task;

import com.baidu.aip.nlp.AipNlp;
import com.bbcow.service.impl.BookService;
import com.bbcow.service.impl.ScoreService;
import com.bbcow.service.mongo.entity.BookChapter;
import com.bbcow.service.mongo.entity.BookUrl;
import com.bbcow.service.mongo.entity.BookWord;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by adan on 2017/11/5.
 */
@Component
public class WordTask {
    @Autowired
    ScoreService scoreService;
    @Autowired
    BookService bookService;
    boolean isRunning = false;

    @Scheduled(cron = "0 0/10 * * * ?")
    public void pageScoreTask(){
        if (isRunning){
            return;
        }
        isRunning = true;

        List<BookUrl> bookUrls = bookService.getFinishBookChapter();
        bookUrls.forEach(bookUrl -> {
            List<BookChapter> bookChapters = bookService.getBookChapters(bookUrl.getReferenceKey());

            Map<String, BookWord> wordMap = new TreeMap<>();

            // 初始化一个AipNlp
            AipNlp client = new AipNlp("10347588", "Yfm4Gpa4lzPGd8kh989DS8c3", "N2mYTHhK4USBbaI3ccT9zODghPTusNuY");

            // 可选：设置网络连接参数
            client.setConnectionTimeoutInMillis(2000);
            client.setSocketTimeoutInMillis(60000);

            bookChapters.forEach(bookChapter -> {

                if (bookChapter.getContent() == null){
                    return;
                }

                JSONObject res = client.lexer(bookChapter.getContent());
                try {
                    JSONArray items = res.getJSONArray("items");
                    for (int i = 0; i < items.length(); i++) {
                        JSONObject item = items.getJSONObject(i);
                        String word = StringUtils.trim(item.getString("item"));
                        String ne = item.getString("ne");
                        String pos = item.getString("pos");

                        if (StringUtils.isNotBlank(word) && ("PER".equals(ne) || "n".equals(pos))){
                            if (wordMap.containsKey(word)){
                                BookWord bookWord = wordMap.get(word);
                                bookWord.setCount(bookWord.getCount()+1);
                            }else {
                                BookWord bookWord = new BookWord();
                                bookWord.setWord(word);
                                bookWord.setCount(1);
                                bookWord.setReferenceKey(bookChapter.getReferenceKey());
                                if (StringUtils.isNotBlank(ne)){
                                    bookWord.setTag(ne);
                                }
                                if (StringUtils.isNotBlank(pos)){
                                    bookWord.setType(pos);
                                }
                                wordMap.put(word, bookWord);
                            }

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
            bookService.saveWords(wordMap.values());
            bookService.finishChapterWord(bookUrl.getReferenceKey());
        });

        isRunning = false;
    }
}
