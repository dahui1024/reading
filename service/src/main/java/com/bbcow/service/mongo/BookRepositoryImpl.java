package com.bbcow.service.mongo;

import com.bbcow.service.mongo.entity.Book;
import com.bbcow.service.mongo.reporitory.BookRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by adan on 2017/10/17.
 */
@Component
public class BookRepositoryImpl implements BookRepository {
    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public List<Book> find(){
        return mongoTemplate.findAll(Book.class);
    }

    @Override
    public Book find(ObjectId id) {
        return mongoTemplate.findOne(Query.query(Criteria.where("_id").is(id)), Book.class);
    }

    @Override
    public Book find(String name, String author) {
        return mongoTemplate.findOne(Query.query(Criteria.where("name").is(name)).addCriteria(Criteria.where("author").is(author)), Book.class);
    }

    @Override
    public void save(Book book) {
        Book book_record = mongoTemplate.findOne(Query.query(Criteria.where("name").is(book.getName())).addCriteria(Criteria.where("author").is(book.getAuthor())), Book.class);
        if (book_record == null){
            mongoTemplate.save(book);
        } else {
            book_record.setIs_sign(book.getIs_sign());
            if (!StringUtils.isEmpty(book.getDescription()))
                book_record.setDescription(book.getDescription());
            if (book.getTags() != null && !book.getTags().isEmpty())
                book_record.setTags(book.getTags());
            book_record.setUpdate_time(new Date());

            mongoTemplate.save(book_record);
        }
    }
}
