package io.ezycollect.paymentapi.service;

import com.google.gson.Gson;
import io.ezycollect.paymentapi.entity.WebHook;
import io.ezycollect.paymentapi.repository.WebHookRepository;
import io.ezycollect.paymentapi.valueobject.CreatePaymentResponse;
import io.ezycollect.paymentapi.valueobject.RegisterWebHookRequest;
import io.ezycollect.paymentapi.valueobject.RegisterWebHookResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.DefaultHttpRequestRetryStrategy;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.util.TimeValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class WebHookService {

    private final WebHookRepository webHookRepository;
    private final Gson gson;

    @Autowired
    public WebHookService(WebHookRepository webHookRepository,
                          Gson gson){
        this.webHookRepository = webHookRepository;
        this.gson = gson;
    }

    @Transactional
    public RegisterWebHookResponse registerWebHook(RegisterWebHookRequest request){

        WebHook webHook = WebHook.builder()
                .url(request.getUrl())
                .build();

        WebHook createdWebHook = webHookRepository.save(webHook);
        log.info("New webhook registered with Id {}", createdWebHook.getId());

        return RegisterWebHookResponse.builder()
                .id(createdWebHook.getId())
                .url(createdWebHook.getUrl())
                .build();
    }

    @Async
    public void sendToWebHooks(CreatePaymentResponse payload){

        log.info("Getting all webhooks from database");
        List<WebHook> webHooks = webHookRepository.findAll();

        if(webHooks.isEmpty()){
            log.info("No webhooks found");
            return;
        }

        List<HttpPost> requests = new ArrayList<>();
        String json = gson.toJson(payload);
        StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);

        for(WebHook webHook : webHooks){
            HttpPost request = new HttpPost(webHook.getUrl());
            request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.toString());
            request.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());
            request.setEntity(entity);
            requests.add(request);
        }

        try(CloseableHttpClient client = HttpClientBuilder.create()
                .setRetryStrategy(new DefaultHttpRequestRetryStrategy(3, TimeValue.ofSeconds(1)))
                .build()){
            for(HttpPost request : requests){
                try(CloseableHttpResponse response = client.execute(request)){
                    log.info("WebHook - {}, Payment - {}, Status Code - {}", request.getUri(), payload.getId(), response.getCode());
                } catch (Exception e) {
                    log.error("Error when calling webhook {} for payment {}. Error message: {}", request.getUri(), payload.getId(), e.getMessage());
                }
            }
        } catch (Exception e) {
            log.error("Error when calling webhooks for payment {}. Error message: {}", payload.getId(), e.getMessage());
        }
    }
}
