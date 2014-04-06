package me.filippov.analyzer.multicast;

import me.filippov.analyzer.processing.IPacketDecoder;
import me.filippov.analyzer.processing.IPacketProcessor;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.MembershipKey;

public class MulticastListener extends Thread {

    private NetworkInterface networkInterface;
    private String groupAddress;
    private int port;
    private IPacketProcessor packetProcessor;
    private IPacketDecoder packetDecoder;

    public MulticastListener(NetworkInterface networkInterface, String groupAddress, int port, IPacketProcessor packetProcessor, IPacketDecoder packetDecoder) {
        this.networkInterface = networkInterface;
        this.groupAddress = groupAddress;
        this.port = port;
        this.packetProcessor = packetProcessor;
        this.packetDecoder = packetDecoder;
    }

    @Override
    public void run() {
        DatagramChannel dc;
        try {
            dc = DatagramChannel.open(StandardProtocolFamily.INET)
                    .setOption(StandardSocketOptions.SO_REUSEADDR, true)
                    .bind(new InetSocketAddress(port))
                    .setOption(StandardSocketOptions.IP_MULTICAST_IF, networkInterface);
            InetAddress group = null;
            try {
                group = InetAddress.getByName(groupAddress);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            MembershipKey key = dc.join(group, networkInterface);
            ByteBuffer buffer = ByteBuffer.allocate(1316);
            while (true) {
                if (key.isValid()) {
                    buffer.clear();
                    dc.receive(buffer);
                    buffer.flip();
                    packetProcessor.process(packetDecoder.decode(buffer));
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
