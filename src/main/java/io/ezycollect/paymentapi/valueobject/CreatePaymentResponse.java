package io.ezycollect.paymentapi.valueobject;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreatePaymentResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String zipCode;
    private String cardNumber;
}
