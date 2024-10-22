package com.wow.libre.infrastructure.controller;

import com.wow.libre.domain.dto.*;
import com.wow.libre.domain.port.in.product.*;
import com.wow.libre.domain.shared.*;
import org.springframework.data.repository.query.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.wow.libre.domain.constant.Constants.HEADER_TRANSACTION_ID;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductPort productPort;

    public ProductController(ProductPort productPort) {
        this.productPort = productPort;
    }


    @GetMapping
    public ResponseEntity<GenericResponse<Map<String, List<ProductCategoryDto>>>> products(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId) {

        Map<String, List<ProductCategoryDto>> accounts = productPort.products(transactionId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new GenericResponseBuilder<>(accounts, transactionId).created().build());
    }

    @GetMapping("/{reference}")
    public ResponseEntity<GenericResponse<ProductDto>> product(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @PathVariable("reference") String referenceNumber) {

        ProductDto product = productPort.product(referenceNumber, transactionId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new GenericResponseBuilder<>(product, transactionId).created().build());
    }
}
