package com.axreng.database;

import com.axreng.data.SearchData;
import com.axreng.enums.SearchStatus;

import java.util.HashMap;
import java.util.Map;

import static com.axreng.enums.SearchStatus.ACTIVE;

public class SearchDataBase {

    private static final Map<String, SearchData> database = new HashMap<>();

    public static final synchronized boolean exists(String hashCode) {

        return database.containsKey(hashCode);
    }

    public static final synchronized void put(String hashCode, String url) {

        if (!database.containsKey(hashCode)) {

            SearchData searchData = new SearchData();
            searchData.setId(hashCode);
            searchData.setStatus(ACTIVE.toString());
            searchData.add(url);

            database.put(hashCode, searchData);

        } else {
            database.get(hashCode).add(url);
        }
    }

    public static final synchronized SearchData select(String hashCode) {
        return database.get(hashCode);
    }

    public static final synchronized void changeStatus(String hashCode, SearchStatus status) {

        if (database.containsKey(hashCode)) {
            database.get(hashCode).setStatus(status.toString());

        } else {
            SearchData searchData = new SearchData();
            searchData.setId(hashCode);
            searchData.setStatus(status.toString());

            database.put(hashCode, searchData);
        }
    }
}
