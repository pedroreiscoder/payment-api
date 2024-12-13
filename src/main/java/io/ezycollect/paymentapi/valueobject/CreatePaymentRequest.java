package io.ezycollect.paymentapi.valueobject;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePaymentRequest {

    @NotBlank
    @Size(max = 50)
    private String firstName;

    @NotBlank
    @Size(max = 50)
    private String lastName;

    @NotBlank
    @Size(min = 4, max = 4)
    private String zipCode;

    @NotBlank
    @Size(min = 15, max = 19)
    private String cardNumber;
}
