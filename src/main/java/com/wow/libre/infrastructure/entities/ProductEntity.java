package com.wow.libre.infrastructure.entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "product")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String name;
    @JoinColumn(
            name = "product_category_id",
            referencedColumnName = "id")
    @ManyToOne(
            optional = false,
            fetch = FetchType.EAGER)
    private ProductCategoryEntity productCategoryId;
    private String disclaimer;
    private Double price;
    private Integer discount;
    @Column(name = "credit_points_enabled")
    private boolean useCreditPoints;
    @Column(name = "credit_points_amount")
    private Long creditPointsValue;

    private String description;
    @Column(name = "image_url")
    private String imageUrl;
    @JoinColumn(
            name = "partner_id",
            referencedColumnName = "id")
    @ManyToOne(
            optional = false,
            fetch = FetchType.EAGER)
    private PartnerEntity partnerId;
    @Column(name = "reference_number")
    private String referenceNumber;
    private Boolean status;
    private String tax;
    @Column(name = "return_tax")
    private String returnTax;
    private String language;
}
