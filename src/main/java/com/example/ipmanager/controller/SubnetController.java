package com.example.ipmanager.controller;

import com.example.ipmanager.dto.*;
import com.example.ipmanager.service.SubnetService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/subnets")
public class SubnetController {

    private final SubnetService subnetService;

    public SubnetController(SubnetService subnetService) {
        this.subnetService = subnetService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SubnetResponse createSubnet(@Valid @RequestBody SubnetRequest request) {
        return subnetService.createSubnet(request);
    }

    @GetMapping
    public Page<SubnetResponse> listSubnets(Pageable pageable) {
        return subnetService.listSubnets(pageable);
    }

    @GetMapping("/{id}")
    public SubnetResponse getSubnet(@PathVariable String id) {
        return subnetService.getSubnet(id);
    }

    @PutMapping("/{id}")
    public SubnetResponse updateSubnet(@PathVariable String id, @RequestBody SubnetUpdateRequest request) {
        return subnetService.updateSubnet(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSubnet(@PathVariable String id) {
        subnetService.deleteSubnet(id);
    }
}
