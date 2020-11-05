package com.xuxueli.crawler.test;

import com.xuxueli.crawler.XxlCrawler;
import com.xuxueli.crawler.filter.UrlFilter;
import com.xuxueli.crawler.parser.strategy.QccParser;

import java.util.HashMap;
import java.util.Map;

/**
 * 爬虫示例01：爬取页面数据并封装VO对象
 *
 * @author xuxueli 2017-10-09 19:48:48
 */
public class XxlCrawlerTest {


    public static void main(String[] args) {

        Map<String, String> cookie = new HashMap<>();
        cookie.put("cookie", "zg_did=%7B%22did%22%3A%20%22174b49ba42f160-068f3438e9764e-3a65420e-1fa400-174b49ba4309e5%22%7D; UM_distinctid=174b49ba48d275-08a488ea7effcd-3a65420e-1fa400-174b49ba48e4a5; _uab_collina=160075793388124444347158; Hm_lvt_78f134d5a9ac3f92524914d0247e70cb=1600999045,1601198166,1601347920,1601368387; QCCSESSID=7k3cjiq9r0khsvc7gt8npbvck2; hasShow=1; acw_tc=7ceef51716044744370792669ed1cdbe723efd9f5505cabf894d7dc1f8; CNZZDATA1254842228=1970960904-1600755179-%7C1604473605; acw_sc__v2=5fa25c69113597054ee46eb04df3df80acb2e99e; zg_de1d1a35bfa24ce29bbf2c7eb17e6c4f=%7B%22sid%22%3A%201604474456617%2C%22updated%22%3A%201604476057895%2C%22info%22%3A%201604368711806%2C%22superProperty%22%3A%20%22%7B%7D%22%2C%22platform%22%3A%20%22%7B%7D%22%2C%22utm%22%3A%20%22%7B%7D%22%2C%22referrerDomain%22%3A%20%22www.qcc.com%22%2C%22zs%22%3A%200%2C%22sc%22%3A%200%2C%22cuid%22%3A%20%226fe8a0fd39f847f229dd59a66a8da53d%22%7D");


        XxlCrawler crawler = new XxlCrawler.Builder()
                .setUrls("https://www.qcc.com/tax_search?key=西安市新城区美及网络工作室")
//                .setWhiteUrlRegexs("https://gitee\\.com/xuxueli0323/projects\\?page=\\d+")
                .setThreadCount(2)
                .setFilter(new UrlFilter().addMustContains("tax").addMustNotContains("#"))
                .setCookieMap(cookie)
                .setPageParser(new QccParser())
                .build();

        System.out.println("start");
        crawler.start(false);
        System.out.println("end");
    }

}
