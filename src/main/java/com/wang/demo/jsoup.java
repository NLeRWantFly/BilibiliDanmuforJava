package com.wang.demo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class jsoup {
    public static void main(String[] args) throws IOException, InterruptedException {
        ActiveXComponent activeXComponent = new ActiveXComponent("Sapi.SpVoice");
        Dispatch sapo = activeXComponent.getObject();
        activeXComponent.setProperty("Volume", new Variant(100));
        activeXComponent.setProperty("Rate", new Variant(2));
        while(true)
        {
            List<person> list = new ArrayList<>();
            Thread.sleep(3 * 1000);
            Connection.Response document = Jsoup.connect("https://api.live.bilibili.com/xlive/web-room/v1/dM/gethistory?roomid=21652717").timeout(5000).userAgent("Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.15").ignoreContentType(true).execute();
            JSONObject parse = JSON.parseObject(document.body());
            JSONObject data = (JSONObject) parse.get("data");
            JSONArray room = data.getJSONArray("room");
            for (int i = 0; i < room.toArray().length; i++) {
                JSONObject name = (JSONObject) room.get(i);
                String nickname = name.get("nickname").toString();
                String text = name.get("text").toString();
                //如果写的内容一致，就跳过不加入
                for (int j = 0; j < list.size(); j++) {
                    if(list.get(j).getNickname().equals(nickname)&&list.get(j).getText().equals(text))
                    {
                        list.remove(j);
                        continue;
                    }
                }
                list.add(new person(nickname,text));
                System.out.println(nickname+":"+text);
                Dispatch.call(sapo, "Speak", new Variant(text));
                //System.out.print(name.get("nickname").toString()+":");
                //System.out.println(name.get("text").toString());
            }
            //list.clear();
            //for (int i = 0; i < list.size(); i++) {
            //  System.out.println(list.get(i).getNickname()+":"+list.get(i).getText());
            //}
        }
    }
}

class person{
    private String nickname;
    private String text;
    public person(String nickname,String text)
    {
        setNickname(nickname);
        setText(text);
    }
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
