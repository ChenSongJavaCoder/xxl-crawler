package com.xuxueli.crawler.pojo;

import com.xuxueli.crawler.annotation.PageFieldSelect;
import com.xuxueli.crawler.annotation.PageSelect;
import lombok.Data;
import lombok.ToString;

/**
 * @Author: CS
 * @Date: 2020/11/4 14:15
 * @Description:
 */
@Data
@ToString
@PageSelect(cssQuery = "body > div.container.m-t-lg > div > div.col-sm-9 > section > div.panel-body.m-t.m-b-lg > form")
public class QccVO {

    @PageFieldSelect(cssQuery = "div:nth-child(1) > div > p")
    private String a;

    @PageFieldSelect(cssQuery = "div:nth-child(2) > div > p")
    private String b;

    @PageFieldSelect(cssQuery = "div:nth-child(3) > div > p")
    private String c;

    @PageFieldSelect(cssQuery = "div:nth-child(4) > div > p")
    private String d;

    @PageFieldSelect(cssQuery = "div:nth-child(5) > div > p")
    private String e;

}
