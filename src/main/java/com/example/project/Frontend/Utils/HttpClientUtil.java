package com.example.project.Frontend.Utils;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.IOException;

public class HttpClientUtil {

    private static final String BASE_URL = "http://localhost:8080/auth";

    public static String login(String username, String password) throws IOException, ParseException {
        String url = BASE_URL + "/login";
        String json = String.format("{\"username\":\"%s\", \"password\":\"%s\"}", username, password);

        return sendPostRequest(url, json);
    }

    public static String register(String username, String email, String password) throws IOException, ParseException {
        String url = BASE_URL + "/signup";
        String json = String.format("{\"username\":\"%s\", \"email\":\"%s\", \"password\":\"%s\"}", username, email, password);

        return sendPostRequest(url, json);
    }

    private static String sendPostRequest(String url, String json) throws IOException, ParseException {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(url);
            post.setHeader("Content-Type", "application/json");
            post.setEntity(new StringEntity(json));

            try (CloseableHttpResponse response = client.execute(post)) {
                return EntityUtils.toString(response.getEntity());
            }
        }
    }
}