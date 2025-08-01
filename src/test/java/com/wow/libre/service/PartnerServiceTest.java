package com.wow.libre.service;

import com.wow.libre.application.services.partner.*;
import com.wow.libre.domain.exception.*;
import com.wow.libre.domain.port.out.partner.*;
import com.wow.libre.infrastructure.entities.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class PartnerServiceTest {

    @Mock
    private ObtainPartner obtainPartner;
    @Mock
    private SavePartner savePartner;

    private PartnerService partnerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        partnerService = new PartnerService(obtainPartner, savePartner);
    }

    @Test
    void create_shouldSavePartnerIfNotExists() {
        String name = "Test";
        Long realmId = 1L;
        String tx = "tx-1";
        when(obtainPartner.getByRealmId(realmId, tx)).thenReturn(Optional.empty());

        partnerService.create(name, realmId, tx);

        ArgumentCaptor<PartnerEntity> captor = ArgumentCaptor.forClass(PartnerEntity.class);
        verify(savePartner).save(captor.capture(), eq(tx));
        PartnerEntity saved = captor.getValue();
        assertEquals(name, saved.getName());
        assertEquals(realmId, saved.getRealmId());
        assertTrue(saved.isStatus());
    }

    @Test
    void create_shouldThrowIfPartnerExists() {
        Long realmId = 2L;
        String tx = "tx-2";
        when(obtainPartner.getByRealmId(realmId, tx)).thenReturn(Optional.of(new PartnerEntity()));

        InternalException ex = assertThrows(InternalException.class, () ->
                partnerService.create("Name", realmId, tx)
        );
        assertTrue(ex.getMessage().contains("Partner already exists"));
    }

    @Test
    void exists_shouldReturnTrueIfPresent() {
        when(obtainPartner.getByRealmId(3L, "tx-3")).thenReturn(Optional.of(new PartnerEntity()));
        assertTrue(partnerService.exists(3L, "tx-3"));
    }

    @Test
    void exists_shouldReturnFalseIfNotPresent() {
        when(obtainPartner.getByRealmId(4L, "tx-4")).thenReturn(Optional.empty());
        assertFalse(partnerService.exists(4L, "tx-4"));
    }

    @Test
    void getByRealmId_shouldReturnEntityIfPresent() {
        PartnerEntity entity = new PartnerEntity();
        when(obtainPartner.getByRealmId(5L, "tx-5")).thenReturn(Optional.of(entity));
        assertSame(entity, partnerService.getByRealmId(5L, "tx-5"));
    }

    @Test
    void getByRealmId_shouldThrowIfNotFound() {
        when(obtainPartner.getByRealmId(6L, "tx-6")).thenReturn(Optional.empty());
        NotFoundException ex = assertThrows(NotFoundException.class, () ->
                partnerService.getByRealmId(6L, "tx-6")
        );
        assertTrue(ex.getMessage().contains("Partner not found"));
    }
}
