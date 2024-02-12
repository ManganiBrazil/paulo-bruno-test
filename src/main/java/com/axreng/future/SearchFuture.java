package com.axreng.future;

import com.axreng.database.SearchDataBase;
import com.axreng.enums.SearchStatus;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SearchFuture {

    private static Logger logger = LoggerFactory.getLogger(SearchFuture.class);
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public Future search(String keyword,
                         LinkedList<String> pagesToSearch,
                         List<String> visitedPages) {

        return this.executor.submit(() -> {

            try {
                while (!pagesToSearch.isEmpty()) {

                    String actualPage = pagesToSearch.poll();

                    visitedPages.add(actualPage);

                    logger.info(String.format("Pages to search: %d.", pagesToSearch.size()));
                    logger.info(String.format("Visited pages: %d.", visitedPages.size()));

                    URLConnection urlConnection = ConnectionUtil.connect(actualPage);

                    if (urlConnection instanceof HttpURLConnection) {

                        HttpURLConnection connection = (HttpURLConnection) urlConnection;
                        BufferedReader bufIn = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        List<String> page = new ArrayList<>();
                        String line;

                        while ((line = bufIn.readLine()) != null) {
                            page.add(line);
                        }

                        SearchUtil.fillLinks(page, visitedPages, pagesToSearch);

                        if (SearchUtil.findKeyword(keyword, page)) {
                            SearchDataBase.put(KeywordUtil.hashcode(keyword), actualPage);
                        }
                    }
                }

                SearchDataBase.changeStatus(KeywordUtil.hashcode(keyword), SearchStatus.DONE);

            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }

            this.executor.shutdown();
        });
    }
}