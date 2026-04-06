package com.example.ipmanager.repository;

import com.example.ipmanager.entity.Subnet;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubnetRepository extends MongoRepository<Subnet, String> {
    Optional<Subnet> findByCidr(String cidr);
    boolean existsByCidr(String cidr);
}
