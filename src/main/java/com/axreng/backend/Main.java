package com.axreng.backend;

import com.axreng.data.SearchData;
import com.axreng.model.KeywordRequestModel;
import com.axreng.service.KeywordService;
import com.axreng.service.SearchService;
import com.google.gson.Gson;

import static spark.Spark.get;
import static spark.Spark.post;

public class Main {
    public static void main(String[] args) {

        final KeywordService keywordService = new KeywordService();
        final SearchService searchService = new SearchService();

        get("/crawl/:id", (req, res) -> {
            res.type("application/json");
            SearchData searchData = searchService.search(req.params(":id"), System.getenv("url"));
            return new Gson().toJson(searchData);
        });

        post("/crawl", (req, res) -> {
            res.type("application/json");
            KeywordRequestModel keywordRequest = new Gson().fromJson(req.body(), KeywordRequestModel.class);
            return new Gson().toJson(keywordService.decode(keywordRequest));
        });
    }
}
