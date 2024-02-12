package com.axreng.data;

import java.util.LinkedHashSet;
import java.util.Set;

public class SearchData {

    private String id;
    private String status;
    private LinkedHashSet<String> urls;

    public SearchData() {
        this.urls = new LinkedHashSet<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Set<String> getUrls() {

        LinkedHashSet<String> urlsCopy = new LinkedHashSet<>();

        for (String url : this.urls) {
            urlsCopy.add(url);
        }

        return urlsCopy;
    }

    public boolean contains(String url) {
        return this.urls.contains(url);
    }

    public void clearUrls() {
        this.urls.clear();
    }

    public void add(String url) {
        this.urls.add(url);
    }
}