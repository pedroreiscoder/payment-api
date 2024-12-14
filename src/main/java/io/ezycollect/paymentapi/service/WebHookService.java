package io.ezycollect.paymentapi.service;

import io.ezycollect.paymentapi.entity.WebHook;
import io.ezycollect.paymentapi.repository.WebHookRepository;
import io.ezycollect.paymentapi.valueobject.RegisterWebHookRequest;
import io.ezycollect.paymentapi.valueobject.RegisterWebHookResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WebHookService {

    private final WebHookRepository webHookRepository;

    @Autowired
    public WebHookService(WebHookRepository webHookRepository){
        this.webHookRepository = webHookRepository;
    }

    @Transactional
    public RegisterWebHookResponse registerWebHook(RegisterWebHookRequest request){

        WebHook webHook = WebHook.builder()
                .url(request.getUrl())
                .build();

        WebHook createdWebHook = webHookRepository.save(webHook);

        return RegisterWebHookResponse.builder()
                .id(createdWebHook.getId())
                .url(createdWebHook.getUrl())
                .build();
    }
}
