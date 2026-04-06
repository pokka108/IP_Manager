package com.example.ipmanager.dto;
import jakarta.validation.constraints.Min;
public class BulkAllocationRequest {
    @Min(value = 1, message = "Count must be at least 1")
    private int count;
    private String hostnamePrefix;
    private String deviceType;
    private String owner;

    public int getCount() { return count; }
    public void setCount(int count) { this.count = count; }
    public String getHostnamePrefix() { return hostnamePrefix; }
    public void setHostnamePrefix(String hostnamePrefix) { this.hostnamePrefix = hostnamePrefix; }
    public String getDeviceType() { return deviceType; }
    public void setDeviceType(String deviceType) { this.deviceType = deviceType; }
    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }
}
