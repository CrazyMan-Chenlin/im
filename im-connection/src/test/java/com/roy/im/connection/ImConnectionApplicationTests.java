package com.roy.im.connection;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*@SpringBootTest*/
public class ImConnectionApplicationTests {

    @Test
    void contextLoads() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type",0);
        JSONObject data = new JSONObject();
        data.put("uid",1);
        data.put("timeout",1000);
        jsonObject.put("data",data);
        System.out.println(jsonObject.toJSONString());
    }

    @Test
    public void testPattern(){
        String str = "123-456-1234";
        System.out.println(str.matches("^\\d{3}-\\d{3}-\\d{4}$"));
        //匹配qq号,5-10位，非0开头
        String qq = "10000";
        System.out.println(qq.matches("^[1-9]\\d{4,9}$"));
        //对输入的电话号码进行匹配（匹配要求：匹配成功的电话号码位数为11位的纯数字，
        // 且以1开头，第二位必须是：3、7、8中的一位，即只匹配13*********、
        // 17*********、18*********的电话号码
        String phone = "13247529607";
        System.out.println(phone.matches("^[1][378]\\d{9}$"));
        //对字符串“张三@@@李四@@王五@茅台”进行切割，去掉@符号
        String str1 = "张三@@@李四@@王五@茅台";
        System.out.println(str1.replaceAll("@", ""));
        //获取字符串“Hi ! Don't be moved by yourself Fzz”
        // 中为两个字母的单词。即Hi、be、by。\\是边界的意思
        String str3 = "Hi ! Don't be moved by yourself Fzz";
        Pattern pattern = Pattern.compile("\\b[0-9A-Za-z]{2}\\b");
        Matcher matcher = pattern.matcher(str3);
        if (matcher.find()) {
            System.out.println(matcher.group());
        }
    }

}
