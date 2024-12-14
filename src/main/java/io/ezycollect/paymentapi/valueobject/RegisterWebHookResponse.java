package io.ezycollect.paymentapi.valueobject;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RegisterWebHookResponse {

    private Long id;
    private String url;
}
