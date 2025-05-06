package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.InputStream;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try (CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build()) {

            HttpGet request = new HttpGet("https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats");

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                // Проверка успешности запроса
                if (response.getStatusLine().getStatusCode() != 200) {
                    System.err.println("Ошибка при получении данных: " + response.getStatusLine());
                    return;
                }

                // Парсинг JSON
                HttpEntity entity = response.getEntity();
                ObjectMapper mapper = new ObjectMapper();
                InputStream content = entity.getContent();

                List<CatFact> facts = mapper.readValue(content, new TypeReference<List<CatFact>>() {});

                // Фильтрация и вывод
                facts.stream()
                        .filter(fact -> fact.getUpvotes() != null)
                        .forEach(System.out::println);
            }
        } catch (Exception e) {
            System.err.println("Произошла ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
