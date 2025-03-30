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
    public Map<String, List<ProductCategoryDto>> products(String language, String transactionId) {

        List<ProductEntity> productsDb = products.findByStatusIsTrueAndLanguage(language, transactionId);

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
        Integer discount = product.getDiscount();
        Double price = product.getPrice();

        return ProductsDto.builder()
                .id(product.getId())
                .name(product.getName())
                .disclaimer(product.getDisclaimer())
                .discountPrice(calculateFinalPrice(price, discount))
                .price(price)
                .discount(discount)
                .usePoints(product.isUseCreditPoints())
                .description(product.getDescription())
                .imgUrl(product.getImageUrl())
                .partner(product.getPartnerId().getName())
                .referenceNumber(product.getReferenceNumber())
                .category(product.getProductCategoryId().getName())
                .build();
    }

    public Double calculateFinalPrice(Double price, Integer discount) {
        Double discountAmount = price * (Optional.of(discount).orElse(0) / 100.0);
        return price - discountAmount;
    }

    public Double calculateGoldPrice(Long price, Integer discount) {
        Double discountAmount = price * (discount / 100.0);
        return price - discountAmount;
    }

    @Override
    public ProductDto product(String referenceCode, String transactionId) {
        ProductEntity productModel = getProduct(referenceCode, transactionId);

        List<ProductDetailModel> productDetails = productDetailsPort.findByProductId(productModel, transactionId);

        return ProductDto.builder().id(productModel.getId())
                .name(productModel.getName())
                .disclaimer(productModel.getDisclaimer())
                .price(productModel.getPrice())
                .discount(productModel.getDiscount())
                .usePoints(productModel.isUseCreditPoints())
                .description(productModel.getDescription())
                .imgUrl(productModel.getImageUrl())
                .partner(productModel.getPartnerId().getName())
                .referenceNumber(productModel.getReferenceNumber())
                .details(productDetails)
                .serverId(productModel.getPartnerId().getServerId())
                .category(productModel.getProductCategoryId().getName()).build();

    }

    @Override
    public ProductEntity getProduct(String referenceCode, String transactionId) {
        Optional<ProductEntity> product = products.findByReferenceNumber(referenceCode, transactionId);

        if (product.isEmpty()) {
            throw new InternalException("Product Not Found", transactionId);
        }

        return product.get();
    }

    @Override
    public List<ProductDiscountsDto> productDiscounts(String language, String transactionId) {


        return products.findByStatusIsTrueAndDiscount(language, transactionId).stream()
                .map(productModel -> ProductDiscountsDto.builder().id(productModel.getId())
                        .name(productModel.getName())
                        .disclaimer(productModel.getDisclaimer())
                        .price(productModel.getPrice())
                        .discountPrice(calculateFinalPrice(productModel.getPrice(),
                                productModel.getDiscount()))
                        .discount(productModel.getDiscount())
                        .usePoints(productModel.isUseCreditPoints())
                        .description(productModel.getDescription())
                        .imgUrl(productModel.getImageUrl())
                        .partner(productModel.getPartnerId().getName())
                        .referenceNumber(productModel.getReferenceNumber())
                        .serverId(productModel.getPartnerId().getServerId())
                        .category(productModel.getProductCategoryId().getName()).build()).toList();
    }

}
