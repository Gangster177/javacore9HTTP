package homework.task2;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static final String KEY = "82NkyFiaISkFJbaClSRagAucezmg3iGdNz2dmYfd";

    public static void main(String[] args) throws IOException {
        HttpGet request = new HttpGet("https://api.nasa.gov/planetary/apod?api_key=" + KEY);

        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)    // максимальное время ожидание подключения к серверу
                        .setSocketTimeout(30000)    // максимальное время ожидания получения данных
                        .setRedirectsEnabled(false) // возможность следовать редиректу в ответе
                        .build())
                .build();

        CloseableHttpResponse response = httpClient.execute(request);//вызываем удаленный сервис
        ObjectMapper mapper = new ObjectMapper();
        ServesNasa servesNasa = mapper.readValue(response.getEntity().getContent(), ServesNasa.class);

        try (InputStream in = new URL(servesNasa.getUrl()).openStream()) {
            Files.copy(in, Paths.get("image.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("The snapshot is uploaded");
    }
}
