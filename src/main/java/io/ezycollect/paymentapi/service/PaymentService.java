package io.ezycollect.paymentapi.service;

import io.ezycollect.paymentapi.entity.Payment;
import io.ezycollect.paymentapi.exception.AESEncryptionException;
import io.ezycollect.paymentapi.repository.PaymentRepository;
import io.ezycollect.paymentapi.security.CryptographyUtil;
import io.ezycollect.paymentapi.valueobject.CreatePaymentRequest;
import io.ezycollect.paymentapi.valueobject.CreatePaymentResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class PaymentService {

    private static final String CARD_NUMBER_MASK = "**** **** **** ";
    private final PaymentRepository paymentRepository;
    private final WebHookService webHookService;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository,
                          WebHookService webHookService){
        this.paymentRepository = paymentRepository;
        this.webHookService = webHookService;
    }

    @Transactional
    public CreatePaymentResponse createPayment(CreatePaymentRequest request){
        String cardNumber = request.getCardNumber();
        byte[] encryptedCardNumber;

        try{
            encryptedCardNumber = CryptographyUtil.AES256Encrypt(cardNumber);
        }catch(Exception e){
            log.error("Error when encrypting the credit card information: {}", e.getMessage());
            throw new AESEncryptionException("There was an error when encrypting the credit card information");
        }

        Payment payment = Payment.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .zipCode(request.getZipCode())
                .cardNumber(encryptedCardNumber)
                .build();

        Payment createdPayment = paymentRepository.save(payment);
        log.info("New payment created with Id {}", createdPayment.getId());

        String maskedCardNumber = CARD_NUMBER_MASK + cardNumber.substring(cardNumber.length() - 4);
        CreatePaymentResponse response = CreatePaymentResponse.builder()
                .id(createdPayment.getId())
                .firstName(createdPayment.getFirstName())
                .lastName(createdPayment.getLastName())
                .zipCode(createdPayment.getZipCode())
                .cardNumber(maskedCardNumber)
                .build();

        webHookService.sendToWebHooks(response);
        return response;
    }
}
