package com.judicature.databackend.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class HttpClient {
    public static String sendPost(String url, String fileParamName, MultipartFile multipartFile, Map<String, String> map) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        List<NameValuePair> formParams = new ArrayList<>();
        HttpPost httpPost = new HttpPost(url);

        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(60000).setConnectTimeout(60000).setConnectionRequestTimeout(10000).build();
        httpPost.setConfig(requestConfig);

        try {
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setCharset(StandardCharsets.UTF_8);
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

            String filename = multipartFile.getOriginalFilename();
            builder.addBinaryBody(fileParamName, multipartFile.getInputStream(), ContentType.MULTIPART_FORM_DATA, filename);
            for (Map.Entry<String, String> entry : map.entrySet()) {
                builder.addPart(entry.getKey(), new StringBody(entry.getValue(), ContentType.create("text/plain", Consts.UTF_8)));
            }

            HttpEntity entity = builder.build();
            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost);

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return e.getMessage();
        }
        return "";

    }
}
