package com.axreng.service;

import com.axreng.data.SearchData;
import com.axreng.database.SearchDataBase;
import com.axreng.enums.SearchStatus;
import com.axreng.future.SearchFuture;
import com.axreng.util.ConnectionUtil;
import com.axreng.util.KeywordUtil;
import com.axreng.util.SearchUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SearchService {

    private static Logger logger = LoggerFactory.getLogger(SearchService.class);
    private final List<String> visitedPages = new ArrayList<>();
    private final LinkedList<String> pagesToSearch = new LinkedList<>();

    public SearchData search(String hashcode, String url) {

        if ((SearchDataBase.exists(hashcode))) {
            return SearchDataBase.select(hashcode);
        }

        SearchUtil.addIfNotExist(url, pagesToSearch);

        SearchData searchData = null;
        String keyword = KeywordUtil.fromHashcode(hashcode);

        try {
            while (!pagesToSearch.isEmpty()) {

                String actualPage = this.pagesToSearch.poll();

                this.visitedPages.add(actualPage);

                URLConnection urlConnection = ConnectionUtil.connect(actualPage);

                if (urlConnection instanceof HttpURLConnection) {


                    HttpURLConnection connection = (HttpURLConnection) urlConnection;
                    BufferedReader bufIn = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    List<String> page = new ArrayList<>();
                    String line;

                    while ((line = bufIn.readLine()) != null) {
                        page.add(line);
                    }

                    SearchUtil.fillLinks(page, this.visitedPages, this.pagesToSearch);

                    if (SearchUtil.findKeyword(keyword, page)) {
                        SearchDataBase.put(hashcode, actualPage);
                        searchData = SearchDataBase.select(hashcode);
                        new SearchFuture().search(keyword, this.pagesToSearch, this.visitedPages);
                        break;
                    }
                }
            }

            if (this.pagesToSearch.isEmpty()) {
                SearchDataBase.changeStatus(hashcode, SearchStatus.DONE);
                searchData = SearchDataBase.select(hashcode);
            }

        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        return searchData;
    }
}
