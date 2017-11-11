package com.bbcow.crawler;

import com.baidu.aip.nlp.AipNlp;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by adan on 2017/11/7.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class NLPTest {
    @Test
    public void testSDK(){
        // 初始化一个AipNlp
        AipNlp client = new AipNlp("10347588", "Yfm4Gpa4lzPGd8kh989DS8c3", "N2mYTHhK4USBbaI3ccT9zODghPTusNuY");

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);


        // 调用接口
        String text = "秦师瞧得中年男子面庞上的黯淡，也是有些不忍，轻叹一声，道：“殿下本是圣龙之命，当惊艳于世，傲视苍穹，怎料到却遭此劫难...”";
        JSONObject res = client.lexer(text);
        try {
            JSONArray items = res.getJSONArray("items");
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                String word = item.getString("item");
                String ne = item.getString("ne");
                String pos = item.getString("pos");
                System.out.println(word+"--"+ne+"=="+pos);
            }
            System.out.println(res.toString(2));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
