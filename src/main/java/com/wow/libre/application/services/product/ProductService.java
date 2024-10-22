package com.wow.libre.application.services.product;

import com.wow.libre.domain.dto.*;
import com.wow.libre.domain.exception.*;
import com.wow.libre.domain.model.*;
import com.wow.libre.domain.port.in.product.*;
import com.wow.libre.domain.port.in.product_details.*;
import com.wow.libre.domain.port.out.product.*;
import com.wow.libre.infrastructure.entities.*;
import org.springframework.stereotype.*;

import java.util.*;
import java.util.stream.*;

@Service
public class ProductService implements ProductPort {
    private final ObtainProducts products;
    private final ProductDetailsPort productDetailsPort;

    public ProductService(ObtainProducts products, ProductDetailsPort productDetailsPort) {
        this.products = products;
        this.productDetailsPort = productDetailsPort;
    }

    @Override
    public Map<String, List<ProductCategoryDto>> products(String transactionId) {

        List<ProductEntity> productsDb = products.findByStatusIsTrue(transactionId);

        return productsDb.stream()
                .collect(Collectors.groupingBy(
                        product -> product.getProductCategoryId().getName(),
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                productList -> {
                                    ProductEntity firstProduct = productList.get(0);
                                    return List.of(new ProductCategoryDto(
                                            firstProduct.getProductCategoryId().getId(),
                                            firstProduct.getProductCategoryId().getName(),
                                            firstProduct.getProductCategoryId().getDescription(),
                                            firstProduct.getProductCategoryId().getDisclaimer(),
                                            productList.stream()
                                                    .map(this::mapToModel)
                                                    .collect(Collectors.toList())
                                    ));
                                }
                        )
                ));
    }

    private ProductsDto mapToModel(ProductEntity product) {
        return ProductsDto.builder()
                .id(product.getId())
                .name(product.getName())
                .disclaimer(product.getDisclaimer())
                .price(product.getPrice())
                .discount(product.getDiscount())
                .gamblingMoney(product.isGamblingMoney())
                .goldPrice(product.getGoldPrice())
                .description(product.getDescription())
                .imgUrl(product.getImageUrl())
                .partner(product.getPartnerId().getName())
                .referenceNumber(product.getReferenceNumber())
                .category(product.getProductCategoryId().getName())
                .build();
    }

    @Override
    public ProductDto product(String referenceCode, String transactionId) {
        Optional<ProductEntity> product = products.findByReferenceNumber(referenceCode, transactionId);

        if (product.isEmpty()) {
            throw new InternalException("Product Not Found", transactionId);
        }

        ProductEntity productModel = product.get();
        List<ProductDetailModel> productDetails = productDetailsPort.findByProductId(productModel, transactionId);

        return ProductDto.builder().id(productModel.getId())
                .name(productModel.getName())
                .disclaimer(productModel.getDisclaimer())
                .price(productModel.getPrice())
                .discount(productModel.getDiscount())
                .gamblingMoney(productModel.isGamblingMoney())
                .goldPrice(productModel.getGoldPrice())
                .description(productModel.getDescription())
                .imgUrl(productModel.getImageUrl())
                .partner(productModel.getPartnerId().getName())
                .referenceNumber(productModel.getReferenceNumber())
                .details(productDetails)
                .category(productModel.getProductCategoryId().getName()).build();

    }

}
