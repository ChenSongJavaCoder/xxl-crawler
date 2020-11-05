package com.xuxueli.crawler.filter;

import lombok.Getter;

/**
 * @Author: CS
 * @Date: 2020/11/4 14:32
 * @Description:
 */
@Getter
public class UrlFilter implements Filter<String> {


    @Override
    public UrlFilter addMustContains(String... s) {
        for (String f : s) {
            mustContains.add(f);
        }
        return this;
    }

    @Override
    public UrlFilter addMustNotContains(String... s) {
        for (String f : s) {
            mustNotContains.add(f);
        }
        return this;
    }

}
