package com.axreng.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConnectionUtil {

    private static final String DEFAULT_PROTOCOL = "http";
    private static final String DEFAULT_HOST = "hiring.axreng.com";
    private static final String HREF_REG = "href=\"([^\"]*)\"";

    public static final URLConnection connect(String urlLink) throws IOException {

        URL url = new URL(urlLink);

        if (!DEFAULT_PROTOCOL.equals(url.getProtocol())) {
            return null;
        }

        if (!DEFAULT_HOST.equals(url.getHost())) {
            return null;
        }

        return url.openConnection();
    }

    public static final String findNextURL(String line) {

        String urlPage;

        try {

            Pattern pattern = Pattern.compile(HREF_REG, Pattern.DOTALL);
            Matcher matcher = pattern.matcher(line);

            if (!matcher.find()) {
                return null;
            }

            String link = matcher
                    .group(1)
                    .replace("../", "");

            URL url = link.endsWith(".html") && !link.contains("http") ?
                    new URL(String.format("%s://%s/%s", DEFAULT_PROTOCOL, DEFAULT_HOST, link)) :
                    new URL(link);

            if (!DEFAULT_HOST.equals(url.getHost())) {
                return null;
            }

            urlPage = url.toString();

        } catch (MalformedURLException e) {
            urlPage = null;
        }

        return urlPage;
    }
}
