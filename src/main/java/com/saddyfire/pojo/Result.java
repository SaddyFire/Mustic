package com.saddyfire.pojo;

import lombok.Data;

import java.util.List;

/**
 * @author SaddyFire
 * @date 2021/12/5
 * @TIME:14:55
 */
@Data
public class Result {
    public Result() {
    }

    public Result(String msg, Integer code) {
        this.msg = msg;
        this.code = code;
    }

    private String msg;
    private Integer code;
    private String id;
    private String name;
    private String author;
    private List<String> songs;
}
