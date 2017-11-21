package com.bbcow.crawler;

import okhttp3.*;

import java.io.IOException;

public class Baidu {
    public static final MediaType TEXT = MediaType.parse("text/plain; charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    String post(String url, String text) throws IOException {
        RequestBody body = RequestBody.create(TEXT, text);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public static void main(String[] args) throws IOException {
        Baidu example = new Baidu();
        String response = example.post("http://data.zz.baidu.com/urls?appid=1584272942248118&token=VMF6nZrGqwQ1wwJr&type=realtime", "http://www.bbcow.com/books/59e74844a3168618f172a2d5");
        System.out.println(response);
    }
}
