package com.example.ipmanager.controller;

import com.example.ipmanager.dto.*;
import com.example.ipmanager.service.IpAddressService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/subnets/{subnetId}/ips")
public class IpAddressController {

    private final IpAddressService ipAddressService;

    public IpAddressController(IpAddressService ipAddressService) {
        this.ipAddressService = ipAddressService;
    }

    @PostMapping("/allocate")
    @ResponseStatus(HttpStatus.CREATED)
    public IpAddressResponse allocateIp(@PathVariable String subnetId, @Valid @RequestBody IpAllocationRequest request) {
        return ipAddressService.allocateIp(subnetId, request);
    }

    @PostMapping("/bulk-allocate")
    @ResponseStatus(HttpStatus.CREATED)
    public List<IpAddressResponse> bulkAllocate(@PathVariable String subnetId, @Valid @RequestBody BulkAllocationRequest request) {
        return ipAddressService.bulkAllocate(subnetId, request);
    }

    @GetMapping
    public List<IpAddressResponse> listIps(@PathVariable String subnetId) {
        return ipAddressService.listIps(subnetId);
    }

    @DeleteMapping("/{ipAddress}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void releaseIp(@PathVariable String subnetId, @PathVariable String ipAddress) {
        ipAddressService.releaseIp(subnetId, ipAddress);
    }
}
