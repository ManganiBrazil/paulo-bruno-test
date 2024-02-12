package com.axreng.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class SearchUtil {

    public static final boolean validate(String keyword) {

        if (Objects.isNull(keyword)) {
            return false;
        }

        return keyword.length() >= 4 && keyword.length() <= 32;
    }

    public static final void addIfNotExist(String url, LinkedList<String> urls) {

        if (Objects.isNull(url) || Objects.isNull(urls)) {
            return;
        }

        if (urls.contains(url)) {
            return;
        }

        urls.offer(url);
    }

    public static final boolean findKeyword(String keyword, List<String> page) {

        for (String line : page) {
            if (line.contains(keyword)) {
                return true;
            }
        }

        return false;
    }

    public static final void fillLinks(List<String> page,
                                       List<String> visitedPages,
                                       LinkedList<String> pagesToSearch) {

        for (String line : page) {

            String nextPage = ConnectionUtil.findNextURL(line);

            if (Objects.isNull(nextPage)) {
                continue;
            }

            if (visitedPages.contains(nextPage)) {
                continue;
            }

            SearchUtil.addIfNotExist(nextPage, pagesToSearch);
        }
    }
}
