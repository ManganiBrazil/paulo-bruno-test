package com.axreng.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;

public class KeywordUtil {

    public static final boolean validate(String keyword) {

        if (Objects.isNull(keyword)) {
            return false;
        }

        return keyword.length() >= 4 && keyword.length() <= 32;
    }

    public static final String hashcode(String keyword) {

        if (Objects.isNull(keyword)) {
            return null;
        }

        return Base64.getEncoder().encodeToString(keyword.getBytes(StandardCharsets.UTF_8));
    }

    public static final String fromHashcode(String hashcode) {

        if (Objects.isNull(hashcode)) {
            return null;
        }

        return new String(Base64.getDecoder().decode(hashcode));
    }
}
