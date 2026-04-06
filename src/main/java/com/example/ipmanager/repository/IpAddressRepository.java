package com.example.ipmanager.repository;

import com.example.ipmanager.entity.IpAddress;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IpAddressRepository extends MongoRepository<IpAddress, String> {
    List<IpAddress> findBySubnetId(String subnetId);
    Optional<IpAddress> findBySubnetIdAndIpAddress(String subnetId, String ipAddress);
    boolean existsBySubnetIdAndIpAddress(String subnetId, String ipAddress);
    long countBySubnetIdAndStatus(String subnetId, com.example.ipmanager.entity.IpStatus status);
}
