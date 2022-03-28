import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.FileOutputStream;
import java.io.InputStream;

public class Main {

    public static void main(String[] args) {

        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(10000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();

        HttpGet request = new HttpGet(
                "https://api.nasa.gov/planetary/apod?api_key=2rYVA44VppeTdRwgEcCFWnaNtLD0tMBwNxyn8X2k");

        try {
            CloseableHttpResponse response = httpClient.execute(request);

            ObjectMapper mapper = new ObjectMapper();
            Content content = mapper.readValue(response.getEntity().getContent(), new TypeReference<>() {});
            String fileName = content.getUrl().split("/")[content.getUrl().split("/").length - 1];

            InputStream is = httpClient.execute(new HttpGet(content.getUrl())).getEntity().getContent();
            FileOutputStream fos = new FileOutputStream(fileName);

            int inBytes;
            while ((inBytes = is.read()) != -1) {
                fos.write(inBytes);
            }

            is.close();
            fos.close();
            httpClient.close();
        } catch (Exception exc) {
            System.out.println("Ошибка выполнения программы - " + exc.getMessage());
        }

    }

}
