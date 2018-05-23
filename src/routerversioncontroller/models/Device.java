/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package routerversioncontroller.models;

/**
 *
 * @author juan
 */
public class Device {
    
    private String ipAddress;
    private int subnetMask;
    private String name;

    public Device() {
    }

    public Device(String ipAddress, int subnetMask, String name) {
        this.ipAddress = ipAddress;
        this.subnetMask = subnetMask;
        this.name = name;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSubnetMask() {
        return subnetMask;
    }

    public void setSubnetMask(int subnetMask) {
        this.subnetMask = subnetMask;
    }
    
}
