package com.example.ipmanager.util;

public class CidrUtils {

    public static boolean isValidCidr(String cidr) {
        if (cidr == null || !cidr.contains("/")) return false;
        String[] parts = cidr.split("/");
        if (parts.length != 2) return false;
        try {
            int prefix = Integer.parseInt(parts[1]);
            if (prefix < 0 || prefix > 32) return false;
            String[] ipParts = parts[0].split("\\.");
            if (ipParts.length != 4) return false;
            for (String part : ipParts) {
                int octet = Integer.parseInt(part);
                if (octet < 0 || octet > 255) return false;
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static long ipToLong(String ipAddress) {
        String[] addrArray = ipAddress.split("\\.");
        long num = 0;
        for (int i = 0; i < 4; i++) {
            num = (num << 8) | Integer.parseInt(addrArray[i]);
        }
        return num;
    }

    public static String longToIp(long ipAddress) {
        return ((ipAddress >> 24) & 0xFF) + "." +
               ((ipAddress >> 16) & 0xFF) + "." +
               ((ipAddress >> 8) & 0xFF) + "." +
               (ipAddress & 0xFF);
    }

    public static NetworkDetails calculateNetworkDetails(String cidr) {
        if (!isValidCidr(cidr)) {
            throw new IllegalArgumentException("Invalid CIDR format");
        }

        String[] parts = cidr.split("/");
        String ip = parts[0];
        int prefix = Integer.parseInt(parts[1]);

        long mask = (0xFFFFFFFFL << (32 - prefix)) & 0xFFFFFFFFL;
        long ipLong = ipToLong(ip);

        long networkLong = ipLong & mask;
        long broadcastLong = networkLong | (~mask & 0xFFFFFFFFL);

        NetworkDetails details = new NetworkDetails();
        details.setNetworkAddress(longToIp(networkLong));
        details.setBroadcastAddress(longToIp(broadcastLong));
        details.setNetworkLong(networkLong);
        details.setBroadcastLong(broadcastLong);

        if (prefix == 32) {
            details.setFirstUsableIp(details.getNetworkAddress());
            details.setLastUsableIp(details.getBroadcastAddress());
            details.setTotalIps(1);
        } else if (prefix == 31) {
            details.setFirstUsableIp(details.getNetworkAddress());
            details.setLastUsableIp(details.getBroadcastAddress());
            details.setTotalIps(2);
        } else {
            details.setFirstUsableIp(longToIp(networkLong + 1));
            details.setLastUsableIp(longToIp(broadcastLong - 1));
            details.setTotalIps(broadcastLong - networkLong - 1);
        }

        return details;
    }

    public static boolean isOverlap(NetworkDetails net1, NetworkDetails net2) {
        return Math.max(net1.getNetworkLong(), net2.getNetworkLong()) <=
               Math.min(net1.getBroadcastLong(), net2.getBroadcastLong());
    }
}
