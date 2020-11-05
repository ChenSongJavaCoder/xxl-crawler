package com.xuxueli.crawler.test.util;

import com.xuxueli.crawler.conf.XxlCrawlerConf;
import com.xuxueli.crawler.model.PageRequest;
import com.xuxueli.crawler.util.JsoupUtil;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * jsoup tool test
 *
 * @author xuxueli 2017-10-09 17:47:13
 */
public class JsoupUtilTest {
    private static Logger logger = LoggerFactory.getLogger(JsoupUtilTest.class);

    /**
     * 加载解析页面
     */
    @Test
    public void loadParseTest() {
        String url = "https://www.qcc.com/tax_search?key=%E6%96%97%E9%B1%BC";

        Map<String, String> cookie = new HashMap<>();
        cookie.put("cookie", "zg_did=%7B%22did%22%3A%20%22174b49ba42f160-068f3438e9764e-3a65420e-1fa400-174b49ba4309e5%22%7D; UM_distinctid=174b49ba48d275-08a488ea7effcd-3a65420e-1fa400-174b49ba48e4a5; _uab_collina=160075793388124444347158; Hm_lvt_78f134d5a9ac3f92524914d0247e70cb=1600999045,1601198166,1601347920,1601368387; QCCSESSID=7k3cjiq9r0khsvc7gt8npbvck2; hasShow=1; acw_tc=7cef9e1c16043704951065420e7a248fbd968b67b99c50aca03e43fc74; CNZZDATA1254842228=1970960904-1600755179-%7C1604371002; acw_sc__v2=5fa0c5f53ed31f4c77312f6309fb0b3c35dd8a2d; zg_de1d1a35bfa24ce29bbf2c7eb17e6c4f=%7B%22sid%22%3A%201604368711804%2C%22updated%22%3A%201604371998770%2C%22info%22%3A%201604368711806%2C%22superProperty%22%3A%20%22%7B%7D%22%2C%22platform%22%3A%20%22%7B%7D%22%2C%22utm%22%3A%20%22%7B%7D%22%2C%22referrerDomain%22%3A%20%22%22%2C%22zs%22%3A%200%2C%22sc%22%3A%200%2C%22cuid%22%3A%20%22e4166f5cbbdfe9e41f27d16ae870ce68%22%7D");

        Document html = JsoupUtil.load(new PageRequest(url, null, cookie, null,
                XxlCrawlerConf.USER_AGENT_CHROME, null, false, XxlCrawlerConf.TIMEOUT_MILLIS_DEFAULT, false, null));
        logger.info(html.html());
    }

    /**
     * 获取页面上所有超链接地址
     */
    @Test
    public void findLinksTest() {
        String url = "https://www.qcc.com/tax_search?key=杭州凡声科技";

        Map<String, String> cookie = new HashMap<>();
        cookie.put("cookie","zg_did=%7B%22did%22%3A%20%22174b49ba42f160-068f3438e9764e-3a65420e-1fa400-174b49ba4309e5%22%7D; UM_distinctid=174b49ba48d275-08a488ea7effcd-3a65420e-1fa400-174b49ba48e4a5; _uab_collina=160075793388124444347158; Hm_lvt_78f134d5a9ac3f92524914d0247e70cb=1600999045,1601198166,1601347920,1601368387; QCCSESSID=7k3cjiq9r0khsvc7gt8npbvck2; hasShow=1; CNZZDATA1254842228=1970960904-1600755179-%7C1604387202; acw_tc=7cef9e1c16043909076186357e43ea5bee693fb237733ff9c4202addee; acw_sc__v2=5fa10ffb665a79493996f7f06a1e0a14a6cd7dd9; zg_de1d1a35bfa24ce29bbf2c7eb17e6c4f=%7B%22sid%22%3A%201604388297809%2C%22updated%22%3A%201604391478861%2C%22info%22%3A%201604368711806%2C%22superProperty%22%3A%20%22%7B%7D%22%2C%22platform%22%3A%20%22%7B%7D%22%2C%22utm%22%3A%20%22%7B%7D%22%2C%22referrerDomain%22%3A%20%22www.qcc.com%22%2C%22zs%22%3A%200%2C%22sc%22%3A%200%2C%22cuid%22%3A%20%22e4166f5cbbdfe9e41f27d16ae870ce68%22%7D");


        Document html = JsoupUtil.load(new PageRequest(url, null, cookie, null,
                XxlCrawlerConf.USER_AGENT_CHROME, null, false, XxlCrawlerConf.TIMEOUT_MILLIS_DEFAULT, false, null));
        Set<String> linkList = JsoupUtil.findLinksByKeyword(html, "tax_view?keyno=");

        logger.info("link num {}", linkList.size());
        if (linkList != null && linkList.size() > 0) {
            for (String link : linkList) {
                logger.info(link);
            }
        }

        String URL = linkList.stream().findFirst().get();

        Document html2 = JsoupUtil.load(new PageRequest(URL, null, cookie, null,
                XxlCrawlerConf.USER_AGENT_CHROME, null, false, XxlCrawlerConf.TIMEOUT_MILLIS_DEFAULT, false, null));

        System.out.println(html2.html());

    }

    /**
     * 获取页面上所有图片地址
     */
    @Test
    public void findImagesTest() {
        String url = "https://www.qcc.com/";

        Document html = JsoupUtil.load(new PageRequest(url, null, null, null,
                XxlCrawlerConf.USER_AGENT_CHROME, null, false, XxlCrawlerConf.TIMEOUT_MILLIS_DEFAULT, false, null));
        Set<String> linkList = JsoupUtil.findImages(html);

        logger.info("images num {}", linkList.size());
        if (linkList != null && linkList.size() > 0) {
            for (String link : linkList) {
                logger.info(link);
            }
        }

    }

}
