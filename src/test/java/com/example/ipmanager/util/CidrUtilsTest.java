package com.example.ipmanager.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CidrUtilsTest {

    @Test
    void testValidCidr() {
        assertTrue(CidrUtils.isValidCidr("192.168.1.0/24"));
        assertTrue(CidrUtils.isValidCidr("10.0.0.0/8"));
        assertFalse(CidrUtils.isValidCidr("192.168.1/24"));
        assertFalse(CidrUtils.isValidCidr("192.168.1.0/33"));
        assertFalse(CidrUtils.isValidCidr("256.0.0.0/8"));
    }

    @Test
    void testCalculateNetworkDetails() {
        NetworkDetails details = CidrUtils.calculateNetworkDetails("192.168.1.0/24");
        assertEquals("192.168.1.0", details.getNetworkAddress());
        assertEquals("192.168.1.255", details.getBroadcastAddress());
        assertEquals("192.168.1.1", details.getFirstUsableIp());
        assertEquals("192.168.1.254", details.getLastUsableIp());
        assertEquals(254, details.getTotalIps());
    }

    @Test
    void testCalculateNetworkDetailsSlash32() {
        NetworkDetails details = CidrUtils.calculateNetworkDetails("10.0.0.5/32");
        assertEquals("10.0.0.5", details.getNetworkAddress());
        assertEquals("10.0.0.5", details.getBroadcastAddress());
        assertEquals("10.0.0.5", details.getFirstUsableIp());
        assertEquals("10.0.0.5", details.getLastUsableIp());
        assertEquals(1, details.getTotalIps());
    }

    @Test
    void testOverlap() {
        NetworkDetails net1 = CidrUtils.calculateNetworkDetails("192.168.1.0/24");
        NetworkDetails net2 = CidrUtils.calculateNetworkDetails("192.168.1.0/25");
        NetworkDetails net3 = CidrUtils.calculateNetworkDetails("192.168.2.0/24");

        assertTrue(CidrUtils.isOverlap(net1, net2)); // net2 is inside net1
        assertFalse(CidrUtils.isOverlap(net1, net3)); // different subnets entirely
    }

    @Test
    void testIpToLongAndBack() {
        String ip = "192.168.1.50";
        long ipLong = CidrUtils.ipToLong(ip);
        String backToString = CidrUtils.longToIp(ipLong);
        assertEquals(ip, backToString);
    }
}
