package com.bbcow.crawler.site;

import com.bbcow.crawler.model.QiDianModel;
import com.bbcow.crawler.model.QiDianRankModel;
import com.bbcow.service.BookService;
import com.bbcow.service.mongo.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.pipeline.PageModelPipeline;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by adan on 2017/10/18.
 */
@Component
public class QiDianCrawler implements CommandLineRunner{
    @Autowired
    BookService bookService;
    public static int count = 0;

    @Override
    public void run(String... strings) throws Exception {
//        OOSpider.create(Site.me(), new Pipeline(), QiDianModel.class, QiDianRankModel.class).addUrl("https://www.qidian.com").thread(2).start();
        OOSpider ooSpider = OOSpider.create(Site.me(), new PagePipeline(), QiDianRankModel.class);
        ooSpider.addUrl("https://www.qidian.com/all").thread(2).start();
        ooSpider.setExitWhenComplete(true);

        while (count <= 0){
            Thread.sleep(5000);
        }

        OOSpider bookSpider = OOSpider.create(Site.me(), new Pipeline(), QiDianModel.class);
        bookSpider.addUrl("https://www.qidian.com/all?orderId=&page=1&style=1&pageSize=50&siteid=1&pubflag=0&hiddenField=0");
        bookSpider.thread(2).start();
        while (true){
            if (bookSpider.getStatus() == Spider.Status.Stopped){
                count -= 1;
                if (count ==0){

                    break;
                }
                bookSpider.addUrl("https://www.qidian.com/all?orderId=&page="+(count)+"&style=1&pageSize=50&siteid=1&pubflag=0&hiddenField=0");
            }
        }

    }
    class PagePipeline implements PageModelPipeline<QiDianRankModel> {

        @Override
        public void process(QiDianRankModel qiDianRankModel, Task task) {
            count = qiDianRankModel.count;
        }
    }

    class Pipeline implements PageModelPipeline<QiDianModel> {

        @Override
        public void process(QiDianModel qiDianModel, Task task) {
            Book book = new Book();
            book.setAuthor(qiDianModel.author);
            book.setCp_host(task.getSite().getDomain());
            book.setCp_image_url(qiDianModel.image_url);
            book.setCp_name("起点小说网");
            book.setCp_url(qiDianModel.original_url);
            book.setDescription(qiDianModel.description);
            book.setTags(qiDianModel.tags);

            if (qiDianModel.statuses.contains("完本")){
                book.setIs_finish(1);
            }
            if (qiDianModel.statuses.contains("VIP")){
                book.setIs_vip(1);
            }
            if (qiDianModel.statuses.contains("签约")) {
                book.setIs_sign(1);
            }

            book.setName(qiDianModel.name);
            book.setCreate_time(new Date());
            book.setUpdate_time(book.getCreate_time());

            bookService.save(book);

        }
    }
}
