package io.ezycollect.paymentapi.controller;

import io.ezycollect.paymentapi.service.PaymentService;
import io.ezycollect.paymentapi.valueobject.CreatePaymentRequest;
import io.ezycollect.paymentapi.valueobject.CreatePaymentResponse;
import io.ezycollect.paymentapi.valueobject.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Payments")
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService){
        this.paymentService = paymentService;
    }

    @Operation(summary = "Creates a new payment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Payment was created successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CreatePaymentResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "500", description = "Error when encrypting card number or other server error",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))})
    })
    @PostMapping
    public ResponseEntity<CreatePaymentResponse> createPayment(@RequestBody @Valid CreatePaymentRequest request){

        CreatePaymentResponse response = paymentService.createPayment(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
