package com.bbcow.service.search;

import com.aliyun.opensearch.OpenSearchClient;
import com.aliyun.opensearch.SearcherClient;
import com.aliyun.opensearch.sdk.dependencies.com.google.common.collect.Lists;
import com.aliyun.opensearch.sdk.dependencies.org.json.JSONArray;
import com.aliyun.opensearch.sdk.dependencies.org.json.JSONObject;
import com.aliyun.opensearch.sdk.generated.OpenSearch;
import com.aliyun.opensearch.sdk.generated.commons.OpenSearchClientException;
import com.aliyun.opensearch.sdk.generated.commons.OpenSearchException;
import com.aliyun.opensearch.sdk.generated.search.Config;
import com.aliyun.opensearch.sdk.generated.search.SearchFormat;
import com.aliyun.opensearch.sdk.generated.search.SearchParams;
import com.aliyun.opensearch.sdk.generated.search.general.SearchResult;
import com.aliyun.opensearch.search.SearchParamsBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adan on 2017/10/21.
 */
public class RemoteSearch {

    public static List<SearchPO> search(String word){

        List<SearchPO> pos = new ArrayList<>();

        //创建并构造OpenSearch对象
        OpenSearch openSearch = new OpenSearch(RemoteConstant.ACCESS_KEY, RemoteConstant.SECRET, RemoteConstant.HOST);
        //创建OpenSearchClient对象，并以OpenSearch对象作为构造参数
        OpenSearchClient serviceClient = new OpenSearchClient(openSearch);
        //创建SearcherClient对象，并以OpenSearchClient对象作为构造参数
        SearcherClient searcherClient = new SearcherClient(serviceClient);
        //定义Config对象，用于设定config子句参数，分页或数据返回格式，指定应用名等等
        Config config = new Config(Lists.newArrayList(RemoteConstant.APP_NAME));
        config.setStart(0);
        config.setHits(10);
        //设置返回格式为FULLJSON，目前支持返回 XML，JSON，FULLJSON 等格式
        config.setSearchFormat(SearchFormat.FULLJSON);
        // 设置搜索结果返回应用中哪些字段
        config.setFetchFields(Lists.newArrayList("id", "name", "author", "cp", "tags"));
        // 创建参数对象
        SearchParams searchParams = new SearchParams(config);
        // 设置查询子句，若需多个索引组合查询，需要setQuery处合并，否则若设置多个setQuery后面的会替换前面查询
        searchParams.setQuery("default:'"+word+"'");
        // SearchParams的工具类，提供了更为便捷的操作
        SearchParamsBuilder paramsBuilder = SearchParamsBuilder.create(searchParams);
        try {
            // 执行返回查询结果
            SearchResult searchResult = searcherClient.execute(paramsBuilder);
            String result = searchResult.getResult();
            JSONObject obj = new JSONObject(result);
            JSONObject resultObj = obj.getJSONObject("result");
            JSONArray items = resultObj.getJSONArray("items");

            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                JSONObject fields = item.getJSONObject("fields");
                SearchPO searchPO = new SearchPO();
                searchPO.getFields().put("author", fields.getString("author"));
                searchPO.getFields().put("name", fields.getString("name").replaceAll("em", "mark"));
                searchPO.getFields().put("id", fields.getString("id"));
                searchPO.getFields().put("cp", fields.getString("cp"));
                searchPO.getFields().put("tags", fields.getString("tags"));

                pos.add(searchPO);
            }

        } catch (OpenSearchException e) {
            e.printStackTrace();
        } catch (OpenSearchClientException e) {
            e.printStackTrace();
        }

        return pos;
    }

    public static void main(String[] args) {
        RemoteSearch.search("一");
    }
}
