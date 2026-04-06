package com.example.ipmanager.service;

import com.example.ipmanager.dto.IpAllocationRequest;
import com.example.ipmanager.dto.IpAddressResponse;
import com.example.ipmanager.entity.IpAddress;
import com.example.ipmanager.entity.IpStatus;
import com.example.ipmanager.entity.Subnet;
import com.example.ipmanager.repository.IpAddressRepository;
import com.example.ipmanager.repository.SubnetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class IpAddressServiceTest {

    @Mock
    private IpAddressRepository ipAddressRepository;

    @Mock
    private SubnetRepository subnetRepository;

    @InjectMocks
    private IpAddressService ipAddressService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @SuppressWarnings("null")
    void testAllocateNextAvailableIp() {
        Subnet subnet = new Subnet();
        subnet.setId("1");
        subnet.setCidr("192.168.1.0/24");
        subnet.setFirstUsableIp("192.168.1.1");
        subnet.setLastUsableIp("192.168.1.254");

        when(subnetRepository.findById("1")).thenReturn(Optional.of(subnet));
        when(ipAddressRepository.findBySubnetId("1")).thenReturn(Collections.emptyList());
        
        IpAddress mockSavedIp = new IpAddress();
        mockSavedIp.setId("10");
        mockSavedIp.setIpAddress("192.168.1.1");
        mockSavedIp.setStatus(IpStatus.ALLOCATED);
        
        when(ipAddressRepository.save(any())).thenReturn(mockSavedIp);

        IpAllocationRequest request = new IpAllocationRequest();
        
        IpAddressResponse response = ipAddressService.allocateIp("1", request);
        
        assertEquals("192.168.1.1", response.getIpAddress());
        verify(ipAddressRepository, times(1)).save(any());
    }
}
