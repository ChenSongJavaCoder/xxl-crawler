package com.xuxueli.crawler.thread;

import com.xuxueli.crawler.XxlCrawler;
import com.xuxueli.crawler.exception.XxlCrawlerException;
import com.xuxueli.crawler.filter.Filter;
import com.xuxueli.crawler.model.PageRequest;
import com.xuxueli.crawler.parser.strategy.NonPageParser;
import com.xuxueli.crawler.util.JsoupUtil;
import com.xuxueli.crawler.util.UrlUtil;
import org.apache.commons.collections.CollectionUtils;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Proxy;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * crawler thread
 *
 * @author xuxueli 2017-10-10 10:58:19
 */
public class CrawlerThread implements Runnable {
    private static Logger logger = LoggerFactory.getLogger(CrawlerThread.class);

    private XxlCrawler crawler;
    private boolean running;
    private boolean toStop;

    public CrawlerThread(XxlCrawler crawler) {
        this.crawler = crawler;
        this.running = true;
        this.toStop = false;
    }

    public void toStop() {
        this.toStop = true;
    }

    public boolean isRunning() {
        return running;
    }

    @Override
    public void run() {

        while (!toStop) {
            try {

                // ------- url ----------
                running = false;
                crawler.tryFinish();
                String link = crawler.getRunData().getUrl();
                running = true;
                logger.info(">>>>>>>>>>> xxl crawler, process link : {}", link);
                if (!UrlUtil.isUrl(link)) {
                    continue;
                }

                // failover
                for (int i = 0; i < (1 + crawler.getRunConf().getFailRetryCount()); i++) {

                    boolean ret = false;
                    try {
                        // make request
                        PageRequest pageRequest = makePageRequest(link);

                        // pre parse
                        crawler.getRunConf().getPageParser().preParse(pageRequest);

                        // parse
                        if (crawler.getRunConf().getPageParser() instanceof NonPageParser) {
                            ret = processNonPage(pageRequest);
                        } else {
                            ret = processPage(pageRequest);
                        }
                    } catch (Throwable e) {
                        logger.info(">>>>>>>>>>> xxl crawler process error.", e);
                    }

                    if (crawler.getRunConf().getPauseMillis() > 0) {
                        try {
                            TimeUnit.MILLISECONDS.sleep(crawler.getRunConf().getPauseMillis());
                        } catch (InterruptedException e) {
                            logger.info(">>>>>>>>>>> xxl crawler thread is interrupted. 2{}", e.getMessage());
                        }
                    }
                    if (ret) {
                        break;
                    }
                }

            } catch (Throwable e) {
                if (e instanceof InterruptedException) {
                    logger.info(">>>>>>>>>>> xxl crawler thread is interrupted. {}", e.getMessage());
                } else if (e instanceof XxlCrawlerException) {
                    logger.info(">>>>>>>>>>> xxl crawler thread {}", e.getMessage());
                } else {
                    logger.error(e.getMessage(), e);
                }
            }

        }
    }

    /**
     * make page request
     *
     * @param link
     * @return PageRequest
     */
    private PageRequest makePageRequest(String link) {
        String userAgent = crawler.getRunConf().getUserAgentList().size() > 1
                ? crawler.getRunConf().getUserAgentList().get(new Random().nextInt(crawler.getRunConf().getUserAgentList().size()))
                : crawler.getRunConf().getUserAgentList().size() == 1 ? crawler.getRunConf().getUserAgentList().get(0) : null;
        Proxy proxy = null;
        if (crawler.getRunConf().getProxyMaker() != null) {
            proxy = crawler.getRunConf().getProxyMaker().make();
        }

        PageRequest pageRequest = new PageRequest();
        pageRequest.setUrl(link);
        pageRequest.setParamMap(crawler.getRunConf().getParamMap());
        pageRequest.setCookieMap(crawler.getRunConf().getCookieMap());
        pageRequest.setHeaderMap(crawler.getRunConf().getHeaderMap());
        pageRequest.setUserAgent(userAgent);
        pageRequest.setReferrer(crawler.getRunConf().getReferrer());
        pageRequest.setIfPost(crawler.getRunConf().isIfPost());
        pageRequest.setTimeoutMillis(crawler.getRunConf().getTimeoutMillis());
        pageRequest.setProxy(proxy);
        pageRequest.setValidateTLSCertificates(crawler.getRunConf().isValidateTLSCertificates());

        return pageRequest;
    }

    /**
     * process non page
     *
     * @param pageRequest
     * @return boolean
     */
    private boolean processNonPage(PageRequest pageRequest) {
        NonPageParser nonPageParser = (NonPageParser) crawler.getRunConf().getPageParser();

        String pagesource = JsoupUtil.loadPageSource(pageRequest);
        if (pagesource == null) {
            return false;
        }
        nonPageParser.parse(pageRequest.getUrl(), pagesource);
        return true;
    }

    /**
     * process page
     *
     * @param pageRequest
     * @return boolean
     */
    private boolean processPage(PageRequest pageRequest) throws IllegalAccessException, InstantiationException {
        Document html = crawler.getRunConf().getPageLoader().load(pageRequest);

        if (html == null) {
            return false;
        }

        // ------- child link list (FIFO队列,广度优先) ----------
        if (crawler.getRunConf().isAllowSpread()) {
            // limit child spread
            Set<String> links = JsoupUtil.findLinks(html);

            links.stream()
                    .filter(f -> crawler.getRunConf().validWhiteUrl(f))
                    // 新增过滤条件
                    .filter(f -> {
                        Filter<String> filter = crawler.getFilter();
                        if (Objects.nonNull(filter)) {
                            if (CollectionUtils.isNotEmpty(filter.getMustContains()) && filter.getMustContains().stream().anyMatch(m -> !f.contains(m))) {
                                return false;
                            }
                            if (CollectionUtils.isNotEmpty(filter.getMustNotContains()) && filter.getMustNotContains().stream().anyMatch(m -> f.contains(m))) {
                                return false;
                            }
                        }
                        return true;
                    })
                    .forEach(e -> crawler.getRunData().addUrl(e));
        }

        // ------- pagevo ----------
        // limit unvalid-page parse, only allow spread child, finish here
        if (!crawler.getRunConf().validWhiteUrl(pageRequest.getUrl())) {
            return true;
        }

        crawler.getHandler().handler(crawler, html);

        return true;
    }


}