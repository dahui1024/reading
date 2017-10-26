package com.bbcow.crawler.site;

import com.bbcow.crawler.model.ZongHengModel;
import com.bbcow.service.BookService;
import com.bbcow.service.mongo.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.pipeline.PageModelPipeline;

import java.util.Date;

/**
 * Created by adan on 2017/10/18.
 */
@Component
public class ZongHengCrawler implements CommandLineRunner{
    @Autowired
    BookService bookService;

    @Override
    public void run(String... strings) throws Exception {
//        OOSpider.create(Site.me(), new Pipeline(), ZongHengModel.class).addUrl("http://book.zongheng.com").thread(2).start();
    }


    class Pipeline implements PageModelPipeline<ZongHengModel> {

        @Override
        public void process(ZongHengModel model, Task task) {
            Book book = new Book();
            book.setAuthor(model.author);
            book.setCp_host(task.getSite().getDomain());
            book.setCp_image_url(model.image_url);
            book.setCp_name("纵横中文网");
            book.setCp_url(model.original_url);
            book.setDescription(model.description);
            book.setTags(model.tags);

            if ("end".equals(model.status)){
                book.setIs_finish(1);
            }
            if (model.statuses.contains("vip")){
                book.setIs_vip(1);
            }

            if (model.statuses.contains("sign")){
                book.setIs_sign(1);
            }
            book.setName(model.name);
            book.setCreate_time(new Date());
            book.setUpdate_time(book.getCreate_time());

            bookService.save(book);
        }
    }
}
