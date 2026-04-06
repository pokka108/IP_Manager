package com.example.ipmanager.dto;
public class IpAllocationRequest {
    private String ipAddress;
    private String hostname;
    private String macAddress;
    private String deviceType;
    private String owner;

    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    public String getHostname() { return hostname; }
    public void setHostname(String hostname) { this.hostname = hostname; }
    public String getMacAddress() { return macAddress; }
    public void setMacAddress(String macAddress) { this.macAddress = macAddress; }
    public String getDeviceType() { return deviceType; }
    public void setDeviceType(String deviceType) { this.deviceType = deviceType; }
    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }
}
