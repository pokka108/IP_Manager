package com.example.ipmanager.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "subnets")
public class Subnet {
    @Id
    private String id;

    @Indexed(unique = true)
    private String cidr;

    private String networkAddress;

    private String broadcastAddress;

    private String firstUsableIp;

    private String lastUsableIp;

    private Long totalIps;

    private String description;

    private String tags;

    private LocalDateTime timestampCreated = LocalDateTime.now();

    public Subnet() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCidr() {
        return cidr;
    }

    public void setCidr(String cidr) {
        this.cidr = cidr;
    }

    public String getNetworkAddress() {
        return networkAddress;
    }

    public void setNetworkAddress(String networkAddress) {
        this.networkAddress = networkAddress;
    }

    public String getBroadcastAddress() {
        return broadcastAddress;
    }

    public void setBroadcastAddress(String broadcastAddress) {
        this.broadcastAddress = broadcastAddress;
    }

    public String getFirstUsableIp() {
        return firstUsableIp;
    }

    public void setFirstUsableIp(String firstUsableIp) {
        this.firstUsableIp = firstUsableIp;
    }

    public String getLastUsableIp() {
        return lastUsableIp;
    }

    public void setLastUsableIp(String lastUsableIp) {
        this.lastUsableIp = lastUsableIp;
    }

    public Long getTotalIps() {
        return totalIps;
    }

    public void setTotalIps(Long totalIps) {
        this.totalIps = totalIps;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public LocalDateTime getTimestampCreated() {
        return timestampCreated;
    }

    public void setTimestampCreated(LocalDateTime timestampCreated) {
        this.timestampCreated = timestampCreated;
    }
}
