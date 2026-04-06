package com.example.ipmanager.service;

import com.example.ipmanager.dto.*;
import com.example.ipmanager.entity.*;
import com.example.ipmanager.exception.*;
import com.example.ipmanager.repository.*;
import com.example.ipmanager.util.CidrUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class IpAddressService {
    private final IpAddressRepository ipAddressRepository;
    private final SubnetRepository subnetRepository;

    public IpAddressService(IpAddressRepository ipAddressRepository, SubnetRepository subnetRepository) {
        this.ipAddressRepository = ipAddressRepository;
        this.subnetRepository = subnetRepository;
    }

    @Transactional
    public IpAddressResponse allocateIp(String subnetId, IpAllocationRequest request) {
        if (subnetId == null) {
            throw new IllegalArgumentException("Subnet ID must not be null");
        }
        if (request == null) {
            throw new IllegalArgumentException("Allocation request must not be null");
        }

        Subnet subnet = subnetRepository.findById(subnetId)
                .orElseThrow(() -> new ResourceNotFoundException("Subnet not found with id: " + subnetId));

        String ipToAllocate;
        if (request.getIpAddress() != null && !request.getIpAddress().isBlank()) {
            ipToAllocate = request.getIpAddress();
            validateIpInSubnet(subnet, ipToAllocate);
            if (ipAddressRepository.existsBySubnetIdAndIpAddress(subnetId, ipToAllocate)) {
                throw new ConflictException("IP address " + ipToAllocate + " is already allocated or reserved.");
            }
        } else {
            ipToAllocate = findAvailableIps(subnet, 1).get(0);
        }

        IpAddress ip = new IpAddress();
        ip.setSubnetId(subnet.getId());
        ip.setIpAddress(ipToAllocate);
        ip.setStatus(IpStatus.ALLOCATED);
        ip.setHostname(request.getHostname());
        ip.setMacAddress(request.getMacAddress());
        ip.setDeviceType(request.getDeviceType());
        ip.setOwner(request.getOwner());

        return new IpAddressResponse(ipAddressRepository.save(ip));
    }

    @Transactional
    public List<IpAddressResponse> bulkAllocate(String subnetId, BulkAllocationRequest request) {
        if (subnetId == null) {
            throw new IllegalArgumentException("Subnet ID must not be null");
        }
        if (request == null) {
            throw new IllegalArgumentException("Bulk allocation request must not be null");
        }

        Subnet subnet = subnetRepository.findById(subnetId)
                .orElseThrow(() -> new ResourceNotFoundException("Subnet not found with id: " + subnetId));

        List<String> ipsToAllocate = findAvailableIps(subnet, request.getCount());
        List<IpAddressResponse> allocated = new ArrayList<>();
        
        for (int i = 0; i < ipsToAllocate.size(); i++) {
            IpAddress ip = new IpAddress();
            ip.setSubnetId(subnet.getId());
            ip.setIpAddress(ipsToAllocate.get(i));
            ip.setStatus(IpStatus.ALLOCATED);
            if (request.getHostnamePrefix() != null && !request.getHostnamePrefix().isBlank()) {
                ip.setHostname(request.getHostnamePrefix() + "-" + (i + 1));
            }
            ip.setDeviceType(request.getDeviceType());
            ip.setOwner(request.getOwner());
            
            allocated.add(new IpAddressResponse(ipAddressRepository.save(ip)));
        }
        return allocated;
    }

    @Transactional
    public void releaseIp(String subnetId, String ipAddress) {
        if (subnetId == null || ipAddress == null) {
            throw new IllegalArgumentException("Subnet ID and IP Address must not be null");
        }

        IpAddress ip = ipAddressRepository.findBySubnetIdAndIpAddress(subnetId, ipAddress)
                .orElseThrow(() -> new ResourceNotFoundException("IP Address not found in subnet"));
        
        if (ip.getStatus() == IpStatus.RESERVED) {
            throw new ConflictException("Cannot release a reserved IP address");
        }
        ipAddressRepository.delete(ip);
    }

    public List<IpAddressResponse> listIps(String subnetId) {
        if (subnetId == null) {
            throw new IllegalArgumentException("Subnet ID must not be null");
        }
        return ipAddressRepository.findBySubnetId(subnetId).stream()
                .map(IpAddressResponse::new)
                .collect(Collectors.toList());
    }

    private void validateIpInSubnet(Subnet subnet, String ipAddress) {
        if(!CidrUtils.isValidCidr(ipAddress + "/32")) {
            throw new IllegalArgumentException("Invalid IPv4 address format");
        }
        long ipLong = CidrUtils.ipToLong(ipAddress);
        long startIp = CidrUtils.ipToLong(subnet.getFirstUsableIp());
        long endIp = CidrUtils.ipToLong(subnet.getLastUsableIp());
        
        if (ipLong < startIp || ipLong > endIp) {
            throw new IllegalArgumentException("IP Address " + ipAddress + " is not within the usable range of subnet " + subnet.getCidr());
        }
    }

    private List<String> findAvailableIps(Subnet subnet, int count) {
        List<IpAddress> allocatedIps = ipAddressRepository.findBySubnetId(subnet.getId());
        Set<Long> usedIpNumbers = allocatedIps.stream()
                .map(ip -> CidrUtils.ipToLong(ip.getIpAddress()))
                .collect(Collectors.toSet());

        long startIp = CidrUtils.ipToLong(subnet.getFirstUsableIp());
        long endIp = CidrUtils.ipToLong(subnet.getLastUsableIp());

        List<String> available = new ArrayList<>();
        for (long i = startIp; i <= endIp; i++) {
            if (!usedIpNumbers.contains(i)) {
                available.add(CidrUtils.longToIp(i));
                if (available.size() == count) {
                    return available;
                }
            }
        }
        throw new ConflictException("Not enough available IPs. Requested: " + count + ", Available: " + available.size());
    }
}
