package com.example.ipmanager.dto;
import com.example.ipmanager.entity.IpAddress;

public class IpAddressResponse {
    private String id;
    private String ipAddress;
    private String status;
    private String hostname;
    private String macAddress;
    private String deviceType;
    private String owner;

    public IpAddressResponse() {}

    public IpAddressResponse(IpAddress ip) {
        this.id = ip.getId();
        this.ipAddress = ip.getIpAddress();
        this.status = ip.getStatus().name();
        this.hostname = ip.getHostname();
        this.macAddress = ip.getMacAddress();
        this.deviceType = ip.getDeviceType();
        this.owner = ip.getOwner();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getHostname() { return hostname; }
    public void setHostname(String hostname) { this.hostname = hostname; }
    public String getMacAddress() { return macAddress; }
    public void setMacAddress(String macAddress) { this.macAddress = macAddress; }
    public String getDeviceType() { return deviceType; }
    public void setDeviceType(String deviceType) { this.deviceType = deviceType; }
    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }
}
