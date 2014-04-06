package me.filippov.analyzer.config;

import me.filippov.analyzer.processing.IPacketProcessor;
import me.filippov.analyzer.processing.PacketProcessor;

public class ChannelInfo {
    private String address;
    private int port;
    private String name;
    private IPacketProcessor processor;

    public ChannelInfo(String address, int port, String name) {
        this.address = address;
        this.port = port;
        this.name = name;
        processor = new PacketProcessor();
    }

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public String getName() {
        return name;
    }

    public IPacketProcessor getProcessor() {
        return processor;
    }
}
