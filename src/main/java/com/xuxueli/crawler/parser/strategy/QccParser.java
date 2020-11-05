package com.xuxueli.crawler.parser.strategy;

import com.xuxueli.crawler.parser.PageParser;
import com.xuxueli.crawler.pojo.QccVO;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * @Author: CS
 * @Date: 2020/11/4 14:14
 * @Description:
 */
@Slf4j
public class QccParser extends PageParser<QccVO> {


    @Override
    public void parse(Document html, Element pageVoElement, QccVO pageVo) {

        String uri = html.baseUri();

        log.info("{}:{}", uri, pageVo.toString());

    }
}
