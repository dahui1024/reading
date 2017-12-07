package com.bbcow.service.search;

import com.aliyun.opensearch.DocumentClient;
import com.aliyun.opensearch.OpenSearchClient;
import com.aliyun.opensearch.sdk.generated.OpenSearch;
import com.aliyun.opensearch.sdk.generated.commons.OpenSearchClientException;
import com.aliyun.opensearch.sdk.generated.commons.OpenSearchException;
import com.bbcow.service.mongo.entity.Book;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by adan on 2017/10/21.
 */
public class RemoteUpload {

    public static void upload(List<Book> books){
        //创建并构造OpenSearch对象
        OpenSearch openSearch = new OpenSearch(RemoteConstant.ACCESS_KEY, RemoteConstant.SECRET, RemoteConstant.HOST);
        //创建OpenSearchClient对象，并以OpenSearch对象作为构造参数
        OpenSearchClient serviceClient = new OpenSearchClient(openSearch);
        //定义DocumentClient对象添加json格式doc数据批量提交
        DocumentClient documentClient = new DocumentClient(serviceClient);
        String table_name = "books";

        final List<SearchPO> pos = new LinkedList<>();

        books.forEach(book -> {
            SearchPO po = new SearchPO();
            po.getFields().put("name", book.getName());
            po.getFields().put("author", book.getAuthor());
            po.getFields().put("id", book.getId().toString());
            po.getFields().put("cp", book.getCpName());
            po.getFields().put("tags", book.getTags());
            po.getFields().put("page_score", book.getPageScore());
            po.getFields().put("page_count", book.getPageCount());
            po.getFields().put("chapter_status", book.getIsFinish());
            pos.add(po);
            documentClient.add(po.getFields());

        });
        try {
            documentClient.commit(RemoteConstant.APP_NAME, table_name);
        } catch (OpenSearchException e) {
            e.printStackTrace();
        } catch (OpenSearchClientException e) {
            e.printStackTrace();
        }
        System.out.println("over");

    }
}
