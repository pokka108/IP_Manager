package com.example.ipmanager.dto;

import com.example.ipmanager.entity.Subnet;
import java.time.LocalDateTime;

public class SubnetResponse {
    private String id;
    private String cidr;
    private String networkAddress;
    private String broadcastAddress;
    private String firstUsableIp;
    private String lastUsableIp;
    private Long totalIps;
    private Long allocatedIps;
    private String description;
    private String tags;
    private LocalDateTime timestampCreated;

    public SubnetResponse() {}

    public SubnetResponse(Subnet subnet) {
        this.id = subnet.getId();
        this.cidr = subnet.getCidr();
        this.networkAddress = subnet.getNetworkAddress();
        this.broadcastAddress = subnet.getBroadcastAddress();
        this.firstUsableIp = subnet.getFirstUsableIp();
        this.lastUsableIp = subnet.getLastUsableIp();
        this.totalIps = subnet.getTotalIps();
        this.description = subnet.getDescription();
        this.tags = subnet.getTags();
        this.timestampCreated = subnet.getTimestampCreated();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCidr() { return cidr; }
    public void setCidr(String cidr) { this.cidr = cidr; }

    public String getNetworkAddress() { return networkAddress; }
    public void setNetworkAddress(String networkAddress) { this.networkAddress = networkAddress; }

    public String getBroadcastAddress() { return broadcastAddress; }
    public void setBroadcastAddress(String broadcastAddress) { this.broadcastAddress = broadcastAddress; }

    public String getFirstUsableIp() { return firstUsableIp; }
    public void setFirstUsableIp(String firstUsableIp) { this.firstUsableIp = firstUsableIp; }

    public String getLastUsableIp() { return lastUsableIp; }
    public void setLastUsableIp(String lastUsableIp) { this.lastUsableIp = lastUsableIp; }

    public Long getTotalIps() { return totalIps; }
    public void setTotalIps(Long totalIps) { this.totalIps = totalIps; }

    public Long getAllocatedIps() { return allocatedIps; }
    public void setAllocatedIps(Long allocatedIps) { this.allocatedIps = allocatedIps; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }

    public LocalDateTime getTimestampCreated() { return timestampCreated; }
    public void setTimestampCreated(LocalDateTime timestampCreated) { this.timestampCreated = timestampCreated; }
}
