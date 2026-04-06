package com.example.ipmanager.util;

public class NetworkDetails {
    private String networkAddress;
    private String broadcastAddress;
    private String firstUsableIp;
    private String lastUsableIp;
    private long totalIps;
    private long networkLong;
    private long broadcastLong;

    public NetworkDetails() {}

    public String getNetworkAddress() { return networkAddress; }
    public void setNetworkAddress(String networkAddress) { this.networkAddress = networkAddress; }

    public String getBroadcastAddress() { return broadcastAddress; }
    public void setBroadcastAddress(String broadcastAddress) { this.broadcastAddress = broadcastAddress; }

    public String getFirstUsableIp() { return firstUsableIp; }
    public void setFirstUsableIp(String firstUsableIp) { this.firstUsableIp = firstUsableIp; }

    public String getLastUsableIp() { return lastUsableIp; }
    public void setLastUsableIp(String lastUsableIp) { this.lastUsableIp = lastUsableIp; }

    public long getTotalIps() { return totalIps; }
    public void setTotalIps(long totalIps) { this.totalIps = totalIps; }

    public long getNetworkLong() { return networkLong; }
    public void setNetworkLong(long networkLong) { this.networkLong = networkLong; }

    public long getBroadcastLong() { return broadcastLong; }
    public void setBroadcastLong(long broadcastLong) { this.broadcastLong = broadcastLong; }
}
