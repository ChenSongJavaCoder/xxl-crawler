package com.xuxueli.crawler.handler;

import com.xuxueli.crawler.XxlCrawler;
import org.jsoup.nodes.Document;

/**
 * @Author: CS
 * @Date: 2020/11/4 13:54
 * @Description:
 */
public interface Handler {

    /**
     * 处理对象的解析
     *
     * @param crawler
     * @param html
     * @throws Exception
     */
    void handler(XxlCrawler crawler, Document html)  throws IllegalAccessException, InstantiationException;
}
