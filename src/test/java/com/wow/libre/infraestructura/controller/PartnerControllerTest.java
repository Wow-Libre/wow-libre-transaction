package com.wow.libre.infraestructura.controller;

import com.wow.libre.domain.dto.*;
import com.wow.libre.domain.port.in.partner.*;
import com.wow.libre.domain.shared.*;
import com.wow.libre.infrastructure.controller.*;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PartnerControllerTest {

    @Mock
    private PartnerPort partnerPort;

    private PartnerController partnerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        partnerController = new PartnerController(partnerPort);
    }

    @Test
    void getPartnerByRealmId_returnsExistence() {
        String transactionId = "tx-123";
        Long realmId = 1L;
        when(partnerPort.exists(realmId, transactionId)).thenReturn(true);

        ResponseEntity<GenericResponse<Boolean>> response = partnerController.getPartnerByRealmId(transactionId,
                realmId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().getData());
        assertEquals(transactionId, response.getBody().getTransactionId());
        verify(partnerPort).exists(realmId, transactionId);
    }

    @Test
    void createPartner_callsPortAndReturnsOk() {
        String transactionId = "tx-456";
        PartnerDto dto = new PartnerDto();
        dto.setName("Test Partner");
        dto.setRealmId(2L);

        ResponseEntity<GenericResponse<Void>> response = partnerController.createPartner(transactionId, dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody().getData());
        assertEquals(transactionId, response.getBody().getTransactionId());
        verify(partnerPort).create("Test Partner", 2L, transactionId);
    }
}
