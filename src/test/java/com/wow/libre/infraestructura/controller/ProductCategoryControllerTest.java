package com.wow.libre.infraestructura.controller;

import com.wow.libre.domain.dto.*;
import com.wow.libre.domain.port.in.product_category.*;
import com.wow.libre.domain.shared.*;
import com.wow.libre.infrastructure.controller.*;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.http.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.*;

public class ProductCategoryControllerTest {

    @Mock
    private ProductCategoryPort productCategoryPort;

    private ProductCategoryController controller;

    @BeforeEach
    void setUp() {
        openMocks(this);
        controller = new ProductCategoryController(productCategoryPort);
    }

    @Test
    void all_returnsListOfProductCategories() {
        String transactionId = "tx-1";
        List<ProductCategoryDto> categories = List.of(new ProductCategoryDto(1L, "Category 1", "Description 1",
                "Disclaimer 1"));
        when(productCategoryPort.findAllProductCategories()).thenReturn(categories);

        ResponseEntity<GenericResponse<List<ProductCategoryDto>>> response = controller.all(transactionId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(categories, response.getBody().getData());
        assertEquals(transactionId, response.getBody().getTransactionId());
        verify(productCategoryPort).findAllProductCategories();
    }

    @Test
    void create_callsPortAndReturnsOk() {
        String transactionId = "tx-2";
        CreateProductCategoryDto dto = new CreateProductCategoryDto();
        dto.setName("cat");
        dto.setDescription("desc");
        dto.setDisclaimer("disc");

        ResponseEntity<GenericResponse<Void>> response = controller.create(transactionId, dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody().getData());
        assertEquals(transactionId, response.getBody().getTransactionId());
        verify(productCategoryPort).createProductCategory("cat", "desc", "disc", transactionId);
    }
}
