package com.wow.libre.infrastructure.controller;

import com.wow.libre.domain.dto.*;
import com.wow.libre.domain.port.in.product.*;
import com.wow.libre.domain.shared.*;
import jakarta.validation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.wow.libre.domain.constant.Constants.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductPort productPort;

    public ProductController(ProductPort productPort) {
        this.productPort = productPort;
    }


    @GetMapping
    public ResponseEntity<GenericResponse<Map<String, List<ProductCategoryModel>>>> products(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestHeader(name = HEADER_ACCEPT_LANGUAGE) Locale locale) {

        Map<String, List<ProductCategoryModel>> accounts = productPort.products(locale.getLanguage(), transactionId);

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


    @GetMapping("/discount")
    public ResponseEntity<GenericResponse<List<ProductDiscountsDto>>> productDiscounts(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestHeader(name = HEADER_ACCEPT_LANGUAGE) Locale locale) {

        List<ProductDiscountsDto> accounts = productPort.productDiscounts(locale.getLanguage(), transactionId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new GenericResponseBuilder<>(accounts, transactionId).created().build());
    }

    @GetMapping("/offer")
    public ResponseEntity<GenericResponse<ProductDiscountsDto>> offer(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestHeader(name = HEADER_ACCEPT_LANGUAGE) Locale locale) {

        Optional<ProductDiscountsDto> product =
                productPort.productDiscounts(locale.getLanguage(), transactionId).stream().reduce((p1, p2) ->
                        p1.getDiscount() > p2.getDiscount() ? p1 : p2
                ).stream().findFirst();

        return product.map(productDiscountsDto -> ResponseEntity.status(HttpStatus.OK)
                        .body(new GenericResponseBuilder<>(productDiscountsDto, transactionId).created().build()))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(new GenericResponseBuilder<ProductDiscountsDto>(transactionId).created().build()));
    }


    @PostMapping
    public ResponseEntity<GenericResponse<Void>> createProduct(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestBody @Valid CreateProductDto createProductDto) {

        productPort.createProduct(createProductDto, transactionId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new GenericResponseBuilder<Void>(transactionId).ok().build());
    }


    @GetMapping("/all")
    public ResponseEntity<GenericResponse<ProductsDetailsDto>> allProducts(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId) {

        ProductsDetailsDto products = productPort.allProducts(transactionId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new GenericResponseBuilder<>(products, transactionId).ok().build());
    }
}
