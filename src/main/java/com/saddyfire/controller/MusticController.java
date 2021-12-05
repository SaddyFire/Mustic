package com.saddyfire.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.saddyfire.pojo.Result;
import com.saddyfire.pojo.Song;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author SaddyFire
 * @date 2021/12/5
 * @TIME:14:41
 */
@RestController
@RequestMapping
public class MusticController {
    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/api/album/{id}")
    public Result reverse(@PathVariable String id){
        //定义网站地址
        String apiUrl = "http://cloud-music.pl-fe.cn/album?id=" + id;
        try {
            //StringBuffer 读取结果
            StringBuffer stringBuffer = new StringBuffer();
            URL url =new URL(apiUrl);
            //打开连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //有关connection的通配设置
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Charset", "utf-8");
            //设置连接方式为GET
            connection.setRequestMethod("GET");
            connection.connect();
            BufferedReader br = null;
            try {
                //读取连接输入: 此处要用StringReader读取文字
                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } catch (IOException e) {
                return new Result("无此专辑",0);
            }
            String read;
            while ((read = br.readLine()) != null) {
                stringBuffer.append(read);
            }

            //拿到数据
            String resultJson = stringBuffer.toString();
            //转为Json对象
            JSONObject jsonObject = JSON.parseObject(resultJson);
            //获取album
            JSONObject album = jsonObject.getJSONObject("album");
            //获取album->中的数据
            album = JSON.parseObject(album.toJSONString());

            String name = album.getString("name");  //获取专辑名
            String id1 = album.getString("id");//获取专辑id
            String author = album.getJSONObject("artist").getString("name");    //获取专辑作者
            //接收歌曲集合
            JSONArray songsJson = jsonObject.getJSONArray("songs");
            List<Song> songs = JSONArray.parseArray(songsJson.toJSONString(), Song.class);
            System.out.println(songs);
            //将歌曲转存至String集合
            List<String> songlist = new ArrayList<>();
            songs.stream().forEach(s->
                songlist.add(s.getName())
            );
            //封装结果集
            Result result = new Result();
            result.setSongs(songlist);
            result.setAuthor(author);
            result.setId(id1);
            result.setName(name);
            result.setCode(1);
            result.setMsg("查询成功");
            System.out.println(result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
