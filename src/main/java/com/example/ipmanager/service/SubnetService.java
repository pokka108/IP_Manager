package com.example.ipmanager.service;

import com.example.ipmanager.dto.*;
import com.example.ipmanager.entity.*;
import com.example.ipmanager.exception.*;
import com.example.ipmanager.repository.*;
import com.example.ipmanager.util.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SubnetService {
    private final SubnetRepository subnetRepository;
    private final IpAddressRepository ipAddressRepository;

    public SubnetService(SubnetRepository subnetRepository, IpAddressRepository ipAddressRepository) {
        this.subnetRepository = subnetRepository;
        this.ipAddressRepository = ipAddressRepository;
    }

    @Transactional
    public SubnetResponse createSubnet(SubnetRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Subnet request must not be null");
        }
        
        String cidr = request.getCidr();
        if (cidr == null || cidr.isBlank()) {
            throw new IllegalArgumentException("CIDR must not be null or empty");
        }
        if (!CidrUtils.isValidCidr(cidr)) {
            throw new IllegalArgumentException("Invalid CIDR format");
        }
        if (subnetRepository.existsByCidr(cidr)) {
            throw new ConflictException("Subnet with this CIDR already exists");
        }

        NetworkDetails newDetails = CidrUtils.calculateNetworkDetails(cidr);

        List<Subnet> existingSubnets = subnetRepository.findAll();
        for (Subnet sub : existingSubnets) {
            NetworkDetails existingDetails = CidrUtils.calculateNetworkDetails(sub.getCidr());
            if (CidrUtils.isOverlap(newDetails, existingDetails)) {
                throw new ConflictException("Subnet overlaps with existing subnet: " + sub.getCidr());
            }
        }

        Subnet subnet = new Subnet();
        subnet.setCidr(request.getCidr());
        subnet.setNetworkAddress(newDetails.getNetworkAddress());
        subnet.setBroadcastAddress(newDetails.getBroadcastAddress());
        subnet.setFirstUsableIp(newDetails.getFirstUsableIp());
        subnet.setLastUsableIp(newDetails.getLastUsableIp());
        subnet.setTotalIps(newDetails.getTotalIps());
        subnet.setDescription(request.getDescription());
        subnet.setTags(request.getTags());

        subnet = subnetRepository.save(subnet);

        // Reserve Network and Broadcast IPs inherently in the system mapping
        IpAddress networkIp = new IpAddress();
        networkIp.setSubnetId(subnet.getId());
        networkIp.setIpAddress(newDetails.getNetworkAddress());
        networkIp.setStatus(IpStatus.RESERVED);
        networkIp.setDeviceType("Network Address");
        ipAddressRepository.save(networkIp);

        if (!newDetails.getNetworkAddress().equals(newDetails.getBroadcastAddress())) {
            IpAddress broadcastIp = new IpAddress();
            broadcastIp.setSubnetId(subnet.getId());
            broadcastIp.setIpAddress(newDetails.getBroadcastAddress());
            broadcastIp.setStatus(IpStatus.RESERVED);
            broadcastIp.setDeviceType("Broadcast Address");
            ipAddressRepository.save(broadcastIp);
        }

        return new SubnetResponse(subnet);
    }

    public Page<SubnetResponse> listSubnets(Pageable pageable) {
        if (pageable == null) {
            pageable = Pageable.unpaged();
        }
        return subnetRepository.findAll(pageable).map(SubnetResponse::new);
    }

    public SubnetResponse getSubnet(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Subnet ID must not be null");
        }
        Subnet subnet = subnetRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Subnet not found with id: " + id));
        SubnetResponse response = new SubnetResponse(subnet);
        response.setAllocatedIps(ipAddressRepository.countBySubnetIdAndStatus(id, IpStatus.ALLOCATED));
        return response;
    }

    @Transactional
    public SubnetResponse updateSubnet(String id, SubnetUpdateRequest request) {
        if (id == null) {
            throw new IllegalArgumentException("Subnet ID must not be null");
        }
        if (request == null) {
            throw new IllegalArgumentException("Subnet update request must not be null");
        }
        Subnet subnet = subnetRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Subnet not found with id: " + id));
        
        subnet.setDescription(request.getDescription());
        subnet.setTags(request.getTags());
        
        return new SubnetResponse(subnetRepository.save(subnet));
    }

    @Transactional
    public void deleteSubnet(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Subnet ID must not be null");
        }
        Subnet subnet = subnetRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Subnet not found with id: " + id));
            
        long allocatedCount = ipAddressRepository.countBySubnetIdAndStatus(id, IpStatus.ALLOCATED);
        if (allocatedCount > 0) {
            throw new ConflictException("Cannot delete subnet with allocated IP addresses");
        }
        
        List<IpAddress> ips = ipAddressRepository.findBySubnetId(id);
        if (ips != null) {
            ipAddressRepository.deleteAll(ips);
        }
        if (subnet != null) {
            subnetRepository.delete(subnet);
        }
    }
}
