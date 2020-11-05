package com.xuxueli.crawler.filter;

import java.util.HashSet;
import java.util.Set;

/**
 * @Author: CS
 * @Date: 2020/11/4 14:29
 * @Description:
 */
public interface Filter<T> {

    Set mustContains = new HashSet<>();

    Set mustNotContains = new HashSet<>();

    Filter<T> addMustContains(T... t);

    Filter<T> addMustNotContains(T... t);

    default Set<T> getMustContains() {
        return mustContains;
    }


    default Set<T> getMustNotContains() {
        return mustNotContains;
    }


}
