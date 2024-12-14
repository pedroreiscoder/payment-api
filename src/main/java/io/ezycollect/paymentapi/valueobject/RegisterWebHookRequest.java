package io.ezycollect.paymentapi.valueobject;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
public class RegisterWebHookRequest {

    @URL
    @NotBlank
    @Size(max = 250)
    private String url;
}
