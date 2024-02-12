package com.axreng.service;

import com.axreng.model.KeywordRequestModel;
import com.axreng.model.KeywordResponseModel;
import com.axreng.util.KeywordUtil;

public class KeywordService {

    public KeywordResponseModel decode(KeywordRequestModel request) {

        if (!KeywordUtil.validate(request.getKeyword())) {
            return null;
        }

        KeywordResponseModel response = new KeywordResponseModel();
        response.setId(KeywordUtil.hashcode(request.getKeyword()));

        return response;
    }
}
